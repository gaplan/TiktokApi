#! /usr/bin/env python3

from hashlib import md5
import time
import random
import requests
import json
from Protobuf import ProtoBuf
from Sm3 import sm3_hash
from urllib.parse import quote, urlencode

class Api: 
    URL = "http://47.98.221.30:666/aweme_service/result"
    TOKEN = "b2fa2a3688b33c7324217f7c4380aa8e" 
    TIKTOK_APPID = 1233  #appId 根据TikTok的美版或欧版来区分 或者为1180 1233

    @staticmethod
    def send(funcname:str, params:list[str]) -> str:
        resp = requests.post(Api.URL, json={
            'token': Api.TOKEN,
            'appId': Api.TIKTOK_APPID,
            'function': funcname,
            'params': params
        })
        if resp.status_code != 200:
            return None

        jsonObj:dict = json.loads(resp.content)
        if 'error' in jsonObj:
            return jsonObj['error']
            
        return jsonObj['data']


class XLadon:
    @staticmethod
    def encrypt(x_khronos:int, lc_id:str) -> str:
        """
        加密X-Ladon字符串
        """
        return Api.send('XLadon_encrypt', [
            "{}-{}-{}".format(x_khronos, lc_id, Api.TIKTOK_APPID),
            str(Api.TIKTOK_APPID)
        ])

    @staticmethod
    def decrypt(xladon:str) -> str:
        """
        解密X-Ladon字符串
        """
        return Api.send('XLadon_decrypt', [
            xladon,
            str(Api.TIKTOK_APPID)
        ])

class XGorgon:
    @staticmethod
    def build(url_query_md5_hex:str, x_ss_stub:str, sdkver:int, x_khronos:int) -> str:
        default_str = '00000000'
        if url_query_md5_hex == None or len(url_query_md5_hex) == 0:
            url_query_md5_hex = md5('').hexdigest()[0:8]
        else:
            url_query_md5_hex = url_query_md5_hex[0:8]
        
        if x_ss_stub == None or len(x_ss_stub) == 0:
            x_ss_stub = default_str
        else:
            x_ss_stub = x_ss_stub[0:8]

        sdkver_hex = sdkver.to_bytes(4, 'little').hex()
        time_hex = x_khronos.to_bytes(4, 'big').hex()
        buildstr = url_query_md5_hex + x_ss_stub + default_str + sdkver_hex + time_hex
        return XGorgon.encrypt(buildstr)

    @staticmethod
    def encrypt(buildstr:str) -> str:
        return Api.send('XGorgon_encrypt', [
            buildstr
        ])
    
    @staticmethod
    def decrypt(xgorgon:str) -> str:
        return Api.send('XGorgon_decrypt', [
            xgorgon
        ])


class XCylons:
    @staticmethod
    def encrypt(query_md5_hex:str, lc_id:str, timestamp:int) -> str:
        return Api.send('XCylons_encrypt', [
            query_md5_hex,
            lc_id,
            str(timestamp)
        ])

    @staticmethod
    def decrypt(xcylons:str) -> str:
        return Api.send('XCylons_decrypt', [
            xcylons
        ])
        

class XArgus:
    @staticmethod
    def get_bodyhash(x_ss_stub=None):
        if(x_ss_stub == None or len(x_ss_stub) == 0):
            return sm3_hash(bytes(16))[0:6]
        return sm3_hash(bytes.fromhex(x_ss_stub))[0:6]
    
    @staticmethod
    def get_queryhash(query:str):
        if(query == None or len(query) == 0):
            return sm3_hash(bytes(16))[0:6]
        return sm3_hash(query.encode('utf-8'))[0:6]

    @staticmethod
    def get_argus() -> dict:
        timestamp = int(time.time()) << 1
        return {
            1:0x20200929 << 1,      #magic
            2:2,                    #version
            3:random.randint(0, 0x7FFFFFFF),   #rand
            4:str(Api.TIKTOK_APPID),               #msAppID
            5:'7068481272823793198', #deviceID
            6:'1225625952',         #licenseID
            7:'23.3.0',             #appVersion
            8:'v04.03.08-ov-iOS',   #sdkVersionStr
            9:0x04030821 << 1,       #sdkVersion
            10:bytes(8),            #envcode  越狱检测
            11:1,                   #platform
            12:timestamp,           #createTime
            13:XArgus.get_bodyhash(None),         #bodyHash
            14:XArgus.get_queryhash(None),         #queryHash
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
    def encrypt(xargus_bean:dict) -> str:
        return Api.send('XArgus_encrypt', [
            ProtoBuf(xargus_bean).toBuf().hex()
        ])

    @staticmethod
    def decrypt(xargus:str) -> dict:
        resp = Api.send('XArgus_decrypt', [ xargus ])
        pb = ProtoBuf(bytes.fromhex(resp))
        return pb.toDict(XArgus.get_argus())

class TokenReqCryptor:
    def encrypt(hex:str) -> str:
        """
        加密/sdi/get_token请求body中的部分数据
        """
        return Api.send('TokenReq_encrypt', [hex])

    def decrypt(hex:str) -> str:
        """
        解密/sdi/get_token请求body中的部分数据
        """
        return Api.send('TokenReq_decrypt', [hex])


class TCCryptor:
    def encrypt(hex:str) -> str:
        """
        加密/service/2/device_register/请求body
        """
        return Api.send('TCCryptor_encrypt', [hex])

    def decrypt(hex:str) -> str:
        """
        解密/service/2/device_register/请求body
        """
        return Api.send('TCCryptor_decrypt', [hex])


def testXLadon():
    ss1 = XLadon.decrypt("ltCyalMN4I88MKaornPKU+LSy5Tl6jDZcJFrMF3eokqTucfp")
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
    ss1 = XArgus.decrypt("vJD5fL7pD9FjIAcvqRcdUppx7WMUrMYE+nZei85Ax6AWKc7CPzAtqx0H2N8FjZbEujResKXhHiahPcFD0hXL37rvZVrMmSSFanIs709vQczszNEzCB3IckOW3/sU/lsnVCOvjKdPpeA1ftVosroyHeNYDyavkgjTWCKzwER6yohr9b4axDUHDOvDAOJKAWUkWQJ21i4EA+FUGBHo7zc9MqqnGwVMYkrvNanT8smw6MedRSa7T9+zGHYT6vb1myTkJzp+7qzCUlmtU/bVhLssNu+z")
    print(ss1)

    print('bodyhash:', ss1[13].hex())
    print('queryhash:', ss1[14].hex())
    
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

def read_file(filename:str) -> bytes:
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


def query_user_profile(user_id:str, sec_user_id:str):
    lc_id = '466012054'
    device_id = '7111580899770353153'
    sdk_ver = 0x04030821

    path = 'https://api16-core-useast5.us.tiktokv.com/aweme/v1/user/profile/other/'
    query = {
        'residence':'VN',
        'device_id':device_id,
        'os_version':'14.2',
        'iid':'7111582539991844610',
        'app_name':'musical_ly',
        'locale':'zh-Hant-TW',
        'ac':'WIFI',
        'sys_region':'VN',
        'js_sdk_version':'',
        'version_code':'23.3.0',
        'channel':'App Store',
        'op_region':'US',
        'tma_jssdk_version':'',
        'os_api':'18',
        'idfa':'991B3FE3-4122-D51B-ABA6-6732AFDB7E27',
        'idfv':'991B3FE3-4122-D51B-ABA6-6732AFDB7E27',
        'device_platform':'iphone',
        'device_type':'iPhone9,1',
        'openudid':'18be2553a01e63fa5f92f4310e8bf261309edf77',
        'account_region':'us',
        'tz_name':'US/Pacific',
        'tz_offset':'-25200',
        'app_language':'zh-Hant',
        'current_region':'VN',
        'build_number':'233017',
        'aid':'1233',
        'mcc_mnc':'',
        'screen_width':'750',
        'uoo':'1',
        'content_language':'',
        'language':'zh-Hant',
        'cdid':'F3BCE558-FFAB-4819-9087-48EA98D17CC7',
        'app_version':'23.3.0',
        'user_id':'7111000370362532910',
        'address_book_access':'2',
        'sec_user_id':'MS4wLjABAAAA8aP_LGWvN1gB7o932qiS0d4KWlJfdRSZPOcBqwRbezRLth-QL4ABKujvZ3mkKJ7Q',
        'user_avatar_shrink':'188_188',
    }

    query_str = urlencode(query, safe='/,', quote_via=quote)

    query_md5_hex = md5(query_str.encode('utf-8')).hexdigest()

    x_khronos = int(time.time())
    x_ladon = XLadon.encrypt(x_khronos, lc_id)
    x_gorgon = XGorgon.build(query_md5_hex, None, sdk_ver, x_khronos)
    
    argus = XArgus.get_argus()
    argus[5] = device_id
    argus[6] = lc_id
    argus[13] = XArgus.get_bodyhash(None)
    argus[14] = XArgus.get_queryhash(query_str)
    argus[16] = 'A9EbgRR2qEaNO5OPtE48sPJbX'
    x_argus = XArgus.encrypt(argus)

    headers = {
        'passport-sdk-version':'5.12.1',
        'x-vc-bdturing-sdk-version':'2.2.0',
        'user-agent':'TikTok 23.3.0 rv:233017 (iPhone; iOS 14.2; zh-Hant_VN) Cronet',
        'sdk-version':'2',
        'x-tt-dm-status':'login=1;ct=1;rt=6',
        'x-tt-store-idc':'useast5',
        'x-tt-store-region':'us',
        'x-tt-store-region-src':'uid',
        'x-ss-dp':'1233',
        'accept-encoding':'gzip, deflate, br',
        'x-argus':x_argus,
        'x-gorgon':x_gorgon,
        'x-khronos':str(x_khronos),
        'x-ladon':x_ladon,
    }
    print('request.url:', path + '?' + query_str)
    print('request.headers:', headers)

    resp = requests.get(path + '?' + query_str, headers=headers)
    print(resp.status_code, resp.content)

if __name__ == '__main__':
    query_user_profile(None, None)


