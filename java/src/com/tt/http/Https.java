package com.tt.http;

import java.io.File;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.tt.util.Files;
import com.tt.util.GZipUtil;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Https {
	public static OkHttpClient http = HttpClientFactory.newHttpClient("127.0.0.1", 0);
	
	public static boolean download(String url, File file){
		byte[] jarBytes = Https.getBytes(url);
		if(jarBytes == null){
			return false;
		}
		
		return Files.writeFile(file, jarBytes);
	}
	
	public static boolean isResponseOk(String response){
		if(response == null)
			return false;
		
		try {
			return JSON.parseObject(response).getIntValue("code") == 200;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static byte[] getBytes(String url) {
		Response response = null;
		try {
			Request builder = new Request.Builder().get().url(url).build();
			Call call = http.newCall(builder);
			response = call.execute();
			if(response != null && response.isSuccessful()){
				return response.body().bytes();
			}
		} catch (IOException e) {
			System.err.println("GetBytes异常: "+e.getMessage());
		} finally {
			if(response != null)
				response.close();
		}
		return null;
	}
	
	public static String get(String url) {
		Response response = null;
		try {
			Request builder = new Request.Builder().get().url(url).build();
			Call call = http.newCall(builder);
			response = call.execute();
			if(response != null && response.isSuccessful()){
				return new String(response.body().bytes());
			}
		} catch (IOException e) {
			System.err.println("Get异常: "+e.getMessage());
		} finally {
			if(response != null){
				response.close();
			}
		} 
		return null;
	}
	public static String post(String url, byte[] body, boolean isGzip){
		Response response = null;
		try {
			Request.Builder builder = new Request.Builder();
			builder.url(url);
			builder.header("Content-Type", "application/json;charset=utf-8");
			
			if(isGzip){
				builder.header("Content-Encoding", "gzip");
				body = GZipUtil.compress(body);
			}
			builder.post(RequestBody.create(null, body));
			
			Call call = http.newCall(builder.build());
			response = call.execute();
			if(response != null && response.isSuccessful()){
				return new String(response.body().bytes());
			}
		} catch (IOException e) {
			System.err.println("Post异常: "+e.getMessage());
		} finally {
			if(response != null){
				response.close();
			}
		} 
		return null;
	}
}
