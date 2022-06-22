package com.tt.tiktok;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.tt.http.Https;
import com.tt.tiktok.bean.XArgusBean;
import com.tt.util.Bytes;
import com.tt.util.HexUtil;
import com.tt.util.MDUtil;
import com.tt.util.ProtoBuffers;
import com.tt.util.Utils;

import okio.Buffer;

public class Api {
	public static String url = "http://47.98.221.30:666/aweme_service/result";
	/**
	 * 联系qq获取token: 76752951
	 */
	public static String token = "02a5df908cb98f34760b44227d906288";
	/**
	 * appId 根据TikTok的美版或欧版来区分 
	 */
	public static int appId = 1180;  //或者为1180 1233
	
	
	public static class TikTokReq {
		public String token = Api.token;
		public int appId = Api.appId;
		public String function;
		public String params[];
	}
	
	public static class TikTokResp {
		public String data;
		public String error;
	}
	
	public static TikTokResp send(TikTokReq req) {
		String resp = Https.post(url, JSON.toJSONString(req).getBytes(Charset.forName("UTF-8")), false);
		TikTokResp respObj = JSON.parseObject(resp, TikTokResp.class);
		if(respObj.error != null){
			System.out.println(resp);
		}
		return respObj;
	}
	
	public static class XLadon {
		/**
		 * 加密X-Ladon字符串
		 * @param x_khronos 请求头里面的时间戳
		 * @param lc_id
		 * @param aid
		 * @return
		 */
		public static String encrypt(long x_khronos, String lc_id) {
			TikTokReq req = new TikTokReq();
			req.function = "XLadon_encrypt";
			req.params = new String[] {
				String.format("%d-%s-%s", x_khronos, lc_id, Api.appId),
				String.valueOf(Api.appId)
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
		
		/**
		 * 解密X-Ladon字符串
		 * @param xladon
		 * @param aid
		 * @return
		 */
		public static String decrypt(String xladon) {
			TikTokReq req = new TikTokReq();
			req.function = "XLadon_decrypt";
			req.params = new String[] {
				xladon,
				String.valueOf(Api.appId)
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
	}
	
	public static class XGorgon {
		/**
		 * 拼接X-Gorgon字符串
		 * 
		 * @param url_query_md5_hex
		 * @param x_ss_stub
		 * @param sdkver
		 * @param x_khronos
		 * @return
		 */
		public static String build(String url_query_md5_hex, String x_ss_stub, int sdkver, int x_khronos) {
			final String defaultStr = "00000000";
			if(Utils.isBlank(url_query_md5_hex)){
				url_query_md5_hex = HexUtil.toString(MDUtil.md5("".getBytes()));
			}
			if(Utils.isBlank(x_ss_stub)){
				x_ss_stub = defaultStr;
			}
			String sdkver_hex = HexUtil.toString(Bytes.toBytes(new int[]{sdkver}, true));
			String time_hex = HexUtil.toString(Bytes.toBytes(new int[]{x_khronos}, true));
			return String.format("%s%s%s%s%s", url_query_md5_hex, x_ss_stub, defaultStr, sdkver_hex, time_hex);
		}
		
		/**
		 * 加密X-Gorgon字符串
		 * 
		 * @param buildstr
		 * @return
		 */
		public static String encrypt(String buildstr) {
			TikTokReq req = new TikTokReq();
			req.function = "XGorgon_encrypt";
			req.params = new String[] {
				buildstr,
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
		
		/**
		 * 解密X-Gorgon字符串
		 * 
		 * @param xgorgon
		 * @return
		 */
		public static String decrypt(String xgorgon) {
			TikTokReq req = new TikTokReq();
			req.function = "XGorgon_decrypt";
			req.params = new String[] {
				xgorgon,
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
	}
	
	public static class XCylons {
		/**
		 * 加密X-Cylons字符串
		 * 
		 * @param query_md5_hex 
		 * @param lc_id
		 * @param timestamp
		 * @return
		 */
		public static String encrypt(String query_md5_hex, String lc_id, long timestamp) {
			TikTokReq req = new TikTokReq();
			req.function = "XCylons_encrypt";
			req.params = new String[] {
				query_md5_hex,
				lc_id,
				String.valueOf(timestamp)
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
		
		/**
		 * 解密X-Cylons字符串
		 * 
		 * @param xcylons
		 * @return
		 */
		public static String decrypt(String xcylons) {
			TikTokReq req = new TikTokReq();
			req.function = "XCylons_decrypt";
			req.params = new String[] {
				xcylons,
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
	}
	
	public static class XArgus {
		/**
		 * X-Argus字符串的加密方法, 参数为protobuf的hex字符串
		 * 
		 * @param argus
		 * @return
		 */
		public static String encrypt(XArgusBean argus) {
			byte[] data = ProtoBuffers.toBuf(argus).readByteArray();
			
			TikTokReq req = new TikTokReq();
			req.function = "XArgus_encrypt";
			req.params = new String[] {
				HexUtil.toString(data),
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
		
		/**
		 * 解密X-Argus字符串
		 * 
		 * @param xargus
		 * @return
		 */
		public static XArgusBean decrypt(String xargus) {
			TikTokReq req = new TikTokReq();
			req.function = "XArgus_decrypt";
			req.params = new String[] {
					xargus,
			};
			TikTokResp resp = send(req);
			
			Buffer buffer = new Buffer();
			buffer.write(HexUtil.toBytes(resp.data));
			return ProtoBuffers.toObject(buffer, XArgusBean.class);
		}
	}
	
	public static class TokenReq {
		/**
		 * 加密/sdi/get_token请求body中的部分数据
		 * 
		 * @param hex
		 * @return
		 */
		public static String encrypt(String hex) {
			TikTokReq req = new TikTokReq();
			req.function = "TokenReq_encrypt";
			req.params = new String[] {
				hex,
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
		
		/**
		 * 解密/sdi/get_token请求body中的部分数据
		 * 
		 * @param hex
		 * @return
		 */
		public static String decrypt(String hex) {
			TikTokReq req = new TikTokReq();
			req.function = "TokenReq_decrypt";
			req.params = new String[] {
				hex,
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
	}
	
	public static class TCCryptor {
		/**
		 * 加密/service/2/device_register/请求body
		 * 
		 * @param hex
		 * @return
		 */
		public static String encrypt(String hex) {
			TikTokReq req = new TikTokReq();
			req.function = "TCCryptor_encrypt";
			req.params = new String[] {
				hex,
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
		
		/**
		 * 解密/service/2/device_register/请求body
		 * 
		 * @param hex
		 * @return
		 */
		public static String decrypt(String hex) {
			TikTokReq req = new TikTokReq();
			req.function = "TCCryptor_decrypt";
			req.params = new String[] {
				hex,
			};
			TikTokResp resp = send(req);
			return resp.data;
		}
	}
}
