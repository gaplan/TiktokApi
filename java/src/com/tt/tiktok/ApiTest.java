package com.tt.tiktok;

import java.io.File;
import java.util.TreeMap;

import com.tt.tiktok.Api.TCCryptor;
import com.tt.tiktok.Api.TokenReq;
import com.tt.tiktok.Api.XArgus;
import com.tt.tiktok.Api.XCylons;
import com.tt.tiktok.Api.XGorgon;
import com.tt.tiktok.Api.XLadon;
import com.tt.tiktok.bean.TokenReqBean;
import com.tt.tiktok.bean.XArgusBean;
import com.tt.util.Files;
import com.tt.util.HexUtil;
import com.tt.util.ProtoBuffers;

import okio.Buffer;
import okio.ByteString;

/**
 * 由于算法加密后会添加随机数, 故每次加密结果不一样
 */
public class ApiTest {
	
	public static void testXLadon() {
		

		String ss1 = XLadon.decrypt("9E97UNYCpmxRHH1PeBJW1rGr855NuRid2W0vsJE1H5HhWW1h");
		System.out.println(ss1);
		
		String ss2 = XLadon.encrypt(1646098215, "1225625952");
		System.out.println(ss2);
		
		String ss3 = XLadon.decrypt(ss2);
		System.out.println(ss3.equals(ss1));
	}
	
	public static void testXGorgon() {
		
		
		String ss1 = XGorgon.decrypt("8404008900006d2495919861ae80fbdfc51b0161d0ded28ac70e");
		System.out.println(ss1);
		
		String ss2 = XGorgon.encrypt(ss1);
		System.out.println(ss2);
		
		String ss3 = XGorgon.decrypt(ss2);
		System.out.println(ss3.equals(ss1));
	}
	
	public static void testXArgus() {
		
		
		XArgusBean ss1 = XArgus.decrypt("UIeySaYiasr9z2T/ZmDoPO7I2YnjAco0HSXCNqoyNBafMIQoiI3nuFu5Y5+qb/R/riOgoQx4hrcJ8MKpnnXUedR1Lai4jDI775lb/lL3OnYHy284QgzvHyDUbqYkdXldX1DqSLe2cp57uPUrfYmEA6B46U1tFxKsl60VvX73nPVZl7MoofJ3xS4ES/BYfZArd32mLKDOyRCEU2sp8Yh+Qe0pstEScE7bKWvkvw+Y57Ja5kadUjQDh5rnlrcoOutRt/DU7E1kWgSV8O65Za3ZhJtW");
		System.out.println(ProtoBuffers.toString(ss1));
		
		String ss2 = XArgus.encrypt(ss1);
		System.out.println(ss2);
		
		XArgusBean ss3 = XArgus.decrypt(ss2);
		System.out.println(ProtoBuffers.toString(ss3));
	}
	
	public static void testXCylons() {
		
		
		String xcylons = "vCzcLbH1humC6lstWfdp4Cfl";
		
		String ss1 = XCylons.decrypt(xcylons);
		System.out.println(ss1);
		
		String ss2 = XCylons.encrypt("174e", "1225625952", 1649240979);
		System.out.println(ss2);
		System.out.println(ss2.equals(xcylons));
	}
	
	public static void testTCCryptor() {
		//读取/service/2/device_register/请求内容
		byte[] data = Files.readFile(new File("~/Desktop/device_register.req"));
		String ss1 = TCCryptor.decrypt(HexUtil.toString(data));
		System.out.println(new String(HexUtil.toBytes(ss1)));
		
		String ss2 = TCCryptor.encrypt(ss1);
		System.out.println(ss2);
		
		String ss3 = TCCryptor.decrypt(ss2);
		System.out.println(ss3.equals(ss1));
	}
	
	public static void testTokenRequestDecrypt(){
		//读取/sdi/get_token请求内容
		byte[] data = Files.readFile(new File("~/Desktop/get_token.req"));
		Buffer buffer = new Buffer();
		buffer.write(data);
		TreeMap<Integer, Object> map = ProtoBuffers.toMap(buffer);
		ByteString bs = (ByteString) map.get(4);  //第4个字段解密
		String string = TokenReq.decrypt(bs.hex());
		System.out.println(string);
		
		Buffer buffer2 = new Buffer();
		buffer2.write(HexUtil.toBytes(string));
		TokenReqBean bean = ProtoBuffers.toObject(buffer2, TokenReqBean.class);
		System.out.println(ProtoBuffers.toString(bean));
		System.out.println();
		System.out.println(ProtoBuffers.toString(bean.device));
	}
	
	public static void testTokenResponseDecrypt(){
		//读取/sdi/get_token返回内容
		byte[] data = Files.readFile(new File("~/Desktop/get_token.resp"));
		Buffer buffer = new Buffer();
		buffer.write(data);
		TreeMap<Integer, Object> map = ProtoBuffers.toMap(buffer);
		ByteString bs = (ByteString) map.get(6);  //第6个字段解密
		String string = TokenReq.decrypt(bs.hex());
		System.out.println(string);
		
		Buffer buffer2 = new Buffer();
		buffer2.write(HexUtil.toBytes(string));
		TreeMap<Integer, Object> mm = ProtoBuffers.toMap(buffer2);
		System.out.println(mm);
	}

	public static void main(String[] args) {
		testTokenRequestDecrypt();
	}
}
