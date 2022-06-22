package com.tt.tiktok;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.tt.http.HttpClientFactory;
import com.tt.tiktok.Api.XArgus;
import com.tt.tiktok.Api.XGorgon;
import com.tt.tiktok.Api.XLadon;
import com.tt.tiktok.bean.TokenReqBean;
import com.tt.tiktok.bean.XArgusBean;
import com.tt.tiktok.bean.XArgusBean.ActionRecord;
import com.tt.util.GZipUtil;
import com.tt.util.HexUtil;
import com.tt.util.MDUtil;
import com.tt.util.ProtoBuffers;
import com.tt.util.SM3;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;

public class TokenRequest {
	
	public static void main(String[] args) throws IOException {
		get_token();
	}
	
	public static byte[] buildTokenReqBody(){
		Map<Integer, Object> pbmap = new TreeMap<>();
		pbmap.put(1, 1077938244); //请求序列号 
		pbmap.put(2, 2);
		pbmap.put(3, 2);
		pbmap.put(4, ProtoBuffers.toBuf(new TokenReqBean()));//填充这个TokenReqBean对象就行
		pbmap.put(5, System.currentTimeMillis() << 1);
		return ProtoBuffers.toBuf(pbmap).readByteArray();
	}
	
	public static void get_token() throws IOException {
		String url = "https://mssdk16-platform-useast5.us.tiktokv.com/sdi/get_token";
		String query = ""
				+ "lc_id=1225625952&"
				+ "platform=iOS&"
				+ "device_platform=ios&"
				+ "sdk_ver=v04.03.08-ov-iOS&"
				+ "sdk_ver_code=67307553&"
				+ "app_ver=23.3.0&"
				+ "version_code=233020&"
				+ "aid=1180&"
				+ "sdkid=&"
				+ "subaid=&"
				+ "iid=7068482260869383983&"
				+ "did=7068481272823793198&"
				+ "bd_did=&"
				+ "client_type=inhouse&"
				+ "region_type=ov&"
				+ "mode=2";
		
		byte[] bodybytes = buildTokenReqBody();
		
		final String lc_id = "466012054";
		final int sdkver = 0x4030921;
		
		long x_khronos = System.currentTimeMillis()/1000;
		String query_md5_hex = HexUtil.toString(MDUtil.md5(query.getBytes(StandardCharsets.UTF_8)));
		String x_ss_stub = HexUtil.toString(MDUtil.md5(bodybytes)).toUpperCase();
		
		String x_ladon = XLadon.encrypt(x_khronos, lc_id);
		String x_gorgon = XGorgon.build(query_md5_hex, x_ss_stub, sdkver, (int)x_khronos);
		
		XArgusBean xArgus = new XArgusBean();
		xArgus.magic = 0x20200929 << 1; //固定值
		xArgus.version = 2;
		xArgus.rand = Math.abs(new Random().nextInt()); 
		xArgus.msAppID = "1233";
		xArgus.deviceID = "7074501187519448622";
		xArgus.licenseID = lc_id;
		xArgus.appVersion = "24.4.0";
		xArgus.sdkVersionStr = "v04.03.09-ov-iOS";
		xArgus.sdkVersion = sdkver << 1;
		xArgus.envcode = ByteString.decodeHex("0000000000000000"); //越狱检测
		xArgus.platform = 1;
		xArgus.createTime = x_khronos << 1;
		xArgus.bodyHash = ByteString.of(SM3.hash(HexUtil.toBytes(x_ss_stub)));
		xArgus.queryHash = ByteString.of(SM3.hash(query.getBytes()));
		xArgus.actionRecord = new ActionRecord();
		xArgus.actionRecord.reportCount = 4;
		xArgus.actionRecord.settingCount = 1388734;
		xArgus.actionRecord.signCount = 492;
		xArgus.secDeviceToken = "AJ3T-R05Bj1-8RF4XsdAX6HOk";
		xArgus.isAppLicense = x_khronos << 1;
		xArgus.pskVersion = "0";
		xArgus.callType = 738; //固定值
		
		String xArgusStr = XArgus.encrypt(xArgus);
		
		Headers.Builder headers = new Headers.Builder();
		headers.add("x-tt-token", "04a0a5d6f5f98572df4678ca26d703f98b05967de3eed9786865a36ffba95019231a76f94fa4d7069f95cc572827588b55210c2b57c37ecafb92d2eb999248aae9c2b194c827304a3f1344618ebfeb99b617b46debe21b43ee7e48a98d873c921011c-1.0.1");
		headers.add("x-tt-dm-status", "login=1;ct=1;rt=1");
		headers.add("x-vc-bdturing-sdk-version", "2.2.0");
		headers.add("content-type", "application/x-www-form-urlencoded");
		headers.add("user-agent", "TikTok 24.4.0 rv:244024 (iPhone; iOS 13.3; en_US) Cronet");
		headers.add("x-tt-cmpl-token", "AgQQAPNSF-RPsLJx5wJVIR0i-Ew0aqqyP6zZYMfGEA");
		headers.add("sdk-version", "2");
		headers.add("passport-sdk-version", "5.12.1");
		headers.add("x-ss-stub", x_ss_stub);
		headers.add("x-tt-store-idc", "useast5");
		headers.add("x-tt-store-region", "us");
		headers.add("x-tt-store-region-src", "uid");
		headers.add("x-bd-kmsv", "0");
		headers.add("x-ss-dp", "1233");
		headers.add("x-tt-trace-id", "00-dc64c7fc10622dad59052d062e5804d1-dc64c7fc10622dad-01");
		headers.add("accept-encoding", "gzip, deflate, br");
		headers.add("cookie", "passport_csrf_token=11c5f4255a69d99d8987f045eced0b93");
		headers.add("cookie", "passport_csrf_token_default=11c5f4255a69d99d8987f045eced0b93");
		headers.add("cookie", "cmpl_token=AgQQAPNSF-RPsLJx5wJVIR0i-Ew0aqqyP6zZYMfGgg");
		headers.add("cookie", "multi_sids=7083891346860393499%3Aa0a5d6f5f98572df4678ca26d703f98b");
		headers.add("cookie", "odin_tt=db9af362a4738efb350f4ad0737e32eab4a0584b4250ddc910f541cde772f6229b0cfff7c63de70cb5162ac589701779173356e03e8324e4bc7e3cacf3a77481d3ea2915af2052ddd9abfb6948170cd6");
		headers.add("cookie", "sessionid=a0a5d6f5f98572df4678ca26d703f98b");
		headers.add("cookie", "sessionid_ss=a0a5d6f5f98572df4678ca26d703f98b");
		headers.add("cookie", "sid_guard=a0a5d6f5f98572df4678ca26d703f98b%7C1652958175%7C5184000%7CMon%2C+18-Jul-2022+11%3A02%3A55+GMT");
		headers.add("cookie", "sid_tt=a0a5d6f5f98572df4678ca26d703f98b");
		headers.add("cookie", "uid_tt=cad870c5f8970ab4266b76415f1c3cefc1b2de89b9b1570c7eee5065282eacd5");
		headers.add("cookie", "uid_tt_ss=cad870c5f8970ab4266b76415f1c3cefc1b2de89b9b1570c7eee5065282eacd5");
		headers.add("cookie", "install_id=7099400845528074030");
		headers.add("cookie", "ttreq=1$cef6647f77990679e41e74b31d2a772feb0fdb1e");
		headers.add("cookie", "store-idc=useast5");
		headers.add("cookie", "store-country-code=us");
		headers.add("cookie", "tt-target-idc=useast5");
		headers.add("cookie", "msToken=w40IXnAg1GfeBH_TZfNL5g3QzpKo31AlBmFvvnVBwNsFe1YHuS02vA0hsjXM-wsB18g5VY6W_SpUatMRN4F5jDnLXqh_SfaVqe39xZEzJQ==");
		headers.add("x-argus", xArgusStr);
		headers.add("x-gorgon", x_gorgon);
		headers.add("x-khronos", ""+x_khronos);
		headers.add("x-ladon", x_ladon);
		
		Request request = new Request.Builder()
				.url(url + "?" + query)
				.headers(headers.build())
				.post(RequestBody.create(null, bodybytes))
				.build();
				
		OkHttpClient client = HttpClientFactory.newHttpClient();
		Response response = client.newCall(request).execute();
		byte[] data = response.body().bytes();
		if(GZipUtil.isGZIPBuff(data)){
			data = GZipUtil.decompress(data);
		}
		System.out.println(new String(data));
	}
}
