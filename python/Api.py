#! /usr/bin/env python3

from hashlib import md5
import time
import random
import requests
import json
from Protobuf import ProtoBuf
from Sm3 import sm3_hash

class Api: 
    URL = "http://47.98.221.30:666/aweme_service/result"
    TOKEN = "7d3249fc8cec65559b6b54836a24be4c" 
    TIKTOK_APPID = 1180  #appId 根据TikTok的美版或欧版来区分 或者为1180 1233

    @staticmethod
    def send(funcname, params):
        resp = requests.post(Api.URL, json={
            'token': Api.TOKEN,
            'appId': Api.TIKTOK_APPID,
            'function': funcname,
            'params': params
        })
        if resp.status_code != 200:
            return None

        jsonObj = json.loads(resp.content)
        if 'error' in jsonObj:
            return jsonObj['error']
            
        return jsonObj['data']


class XLadon:
    @staticmethod
    def encrypt(x_khronos, lc_id):
        """
        加密X-Ladon字符串
        """
        return Api.send('XLadon_encrypt', [
            "{}-{}-{}".format(x_khronos, lc_id, Api.TIKTOK_APPID),
            str(Api.TIKTOK_APPID)
        ])

    @staticmethod
    def decrypt(xladon):
        """
        解密X-Ladon字符串
        """
        return Api.send('XLadon_decrypt', [
            xladon,
            str(Api.TIKTOK_APPID)
        ])

class XGorgon:
    @staticmethod
    def build(url_query_md5_hex, x_ss_stub, sdkver, x_khronos):
        default_str = '00000000'
        if url_query_md5_hex == None or len(url_query_md5_hex) == 0:
            url_query_md5_hex = md5('').hexdigest()[0:8]
        
        if x_ss_stub == None or len(x_ss_stub) == 0:
            x_ss_stub = default_str
        else:
            x_ss_stub = x_ss_stub[0:8]

        sdkver_hex = sdkver.to_bytes(8, 'big').hex()
        time_hex = x_khronos.to_bytes(8, 'big').hex()
        buildstr = url_query_md5_hex + x_ss_stub + default_str + sdkver_hex + time_hex
        return XGorgon.encrypt(buildstr)

    @staticmethod
    def encrypt(buildstr):
        return Api.send('XGorgon_encrypt', [
            buildstr
        ])
	
    @staticmethod
    def decrypt(xgorgon):
        return Api.send('XGorgon_decrypt', [
            xgorgon
        ])


class XCylons:
    @staticmethod
    def encrypt(query_md5_hex, lc_id, timestamp):
        return Api.send('XCylons_encrypt', [
            query_md5_hex,
			lc_id,
            str(timestamp)
        ])

    @staticmethod
    def decrypt(xcylons):
        return Api.send('XCylons_decrypt', [
            xcylons
        ])
		

class XArgus:
    @staticmethod
    def get_argus():
        timestamp = int(time.time()) << 1
        return {
            1:0x20200929 << 1,   #magic
            2:2,            #version
            3:random.randint(0, 0x7FFFFFFF),   #rand
            4:'1180',       #msAppID
            5:'7068481272823793198', #deviceID
            6:'1225625952', #licenseID
            7:'23.3.0',     #appVersion
            8:'v04.03.08-ov-iOS', #sdkVersionStr
            9:0x4030921 << 1,    #sdkVersion
            10:bytes(8),    #envcode  越狱检测
            11:1,           #platform
            12:timestamp,   #createTime
            13:b'',         #bodyHash
            14:b'',         #queryHash
            15: {
                1: 0,       #signCount
                2: 0,       #reportCount
                3: 0,       #settingCount
            },
            16:'AnPPIveUCQlIiFroHGG17nXK6', #secDeviceToken
            17:timestamp,   #isAppLicense
            20:'none',      #pskVersion
            21:738,         #callType
        }

    @staticmethod
    def encrypt(xargus_bean):
        return Api.send('XArgus_encrypt', [
            ProtoBuf(xargus_bean).toBuf().hex()
        ])

    @staticmethod
    def decrypt(xargus):
        resp = Api.send('XArgus_decrypt', [ xargus ])
        pb = ProtoBuf(bytes.fromhex(resp))
        return pb.toDict(XArgus.get_argus())

class TokenReqCryptor:
    def encrypt(hex):
        """
        加密/sdi/get_token请求body中的部分数据
        """
        return Api.send('TokenReq_encrypt', [hex])

    def decrypt(hex):
        """
        解密/sdi/get_token请求body中的部分数据
        """
        return Api.send('TokenReq_decrypt', [hex])


class TCCryptor:
    def encrypt(hex):
        """
        加密/service/2/device_register/请求body
        """
        return Api.send('TCCryptor_encrypt', [hex])

    def decrypt(hex):
        """
        解密/service/2/device_register/请求body
        """
        return Api.send('TCCryptor_decrypt', [hex])


def testXLadon():
    ss1 = XLadon.decrypt("9E97UNYCpmxRHH1PeBJW1rGr855NuRid2W0vsJE1H5HhWW1h")
    print(ss1)
    
    ss2 = XLadon.encrypt(1646098215, "1225625952")
    print(ss2)
    
    ss3 = XLadon.decrypt(ss2)
    print(ss3 == ss1)
	
	
def testXGorgon() :
    ss1 = XGorgon.decrypt("8404008900006d2495919861ae80fbdfc51b0161d0ded28ac70e")
    print(ss1)
    
    ss2 = XGorgon.encrypt(ss1)
    print(ss2)
    
    ss3 = XGorgon.decrypt(ss2)
    print(ss3 == (ss1))


def testXArgus() :
    ss1 = XArgus.decrypt("UIeySaYiasr9z2T/ZmDoPO7I2YnjAco0HSXCNqoyNBafMIQoiI3nuFu5Y5+qb/R/riOgoQx4hrcJ8MKpnnXUedR1Lai4jDI775lb/lL3OnYHy284QgzvHyDUbqYkdXldX1DqSLe2cp57uPUrfYmEA6B46U1tFxKsl60VvX73nPVZl7MoofJ3xS4ES/BYfZArd32mLKDOyRCEU2sp8Yh+Qe0pstEScE7bKWvkvw+Y57Ja5kadUjQDh5rnlrcoOutRt/DU7E1kWgSV8O65Za3ZhJtW")
    print(ss1)
    
    ss2 = XArgus.encrypt(ss1)
    print(ss2)
    
    ss3 = XArgus.decrypt(ss2)
    print(ss3)
    print(ss3 == ss1)


def testXCylons() :
    xcylons = "vCzcLbH1humC6lstWfdp4Cfl"
    ss1 = XCylons.decrypt(xcylons)
    print(ss1)

    l = ss1.split(',')
    
    ss2 = XCylons.encrypt(l[1].strip(' '), l[0].strip(' '), l[2].strip(' '))
    print(ss2)
    print(ss2 == (xcylons))

def read_file(filename) -> bytes:
    with open(filename, 'rb') as f:
        return f.read()
    return None

def testTCCryptor():
    #读取/service/2/device_register/请求内容
    data = read_file("~/Desktop/device_register.req")
    ss1 = TCCryptor.decrypt(data.hex())
    print(bytes.fromhex(ss1).decode('utf-8'))
    
    ss2 = TCCryptor.encrypt(ss1)
    print(ss2)
    
    ss3 = TCCryptor.decrypt(ss2)
    print(ss3 == ss1)

def testTokenRequestDecrypt():
    #读取/sdi/get_token请求内容
    filedata = read_file('~/Desktop/get_token.req')
    endata = ProtoBuf(filedata).getBytes(4)
    dedata = TokenReqCryptor.decrypt(endata.hex())
    ProtoBuf(dedata).dump()

	
def testTokenResponseDecrypt():
    #读取/sdi/get_token返回内容
    filedata = read_file('~/Desktop/get_token.resp')
    endata = ProtoBuf(filedata).getBytes(6)
    dedata = TokenReqCryptor.decrypt(endata.hex())
    ProtoBuf(dedata).dump()


if __name__ == '__main__':
    testXArgus()
