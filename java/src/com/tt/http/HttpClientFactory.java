package com.tt.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.tt.util.Utils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClientFactory {
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int WRITE_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 5000;
	private static final int RETRY_COUNT = 3;
	private static final int RETRY_INTERVAL = 500;
	
	private static X509TrustManager tm = new X509TrustManager() {
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
		}
		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	};
	
	private static SSLSocketFactory getDefaultSSLSocketFactory() {
		SSLSocketFactory socketFactory = null;
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());
			socketFactory = sslContext.getSocketFactory();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return socketFactory;
	}
	
	/**
	 * 重试拦截器
	 */
	private static class RetryIntercepter implements Interceptor {
	    public int retryLimit;//最大重试次数
	    private int retryNum;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
	    private int retryInterval;
	    
	    public RetryIntercepter(int retryLimit, int retryInterval) {
	    	this.retryNum = 0;
	    	this.retryLimit = retryLimit;
	    	this.retryInterval = retryInterval;
	    }

	    @Override
	    public Response intercept(Chain chain) throws IOException {
	        Request request = chain.request();
	        Response response = chain.proceed(request);
	        
	        while ((response == null || !response.isSuccessful()) && retryNum < retryLimit) {
	            retryNum++;
	            response.close();
	            
	            Utils.sleep(retryInterval);
	            
	            System.err.println("重试"+retryNum+": "+request);
	            response = chain.proceed(request);
	        }
	        return response;
	    }
	}
	
	public static OkHttpClient newHttpClient() {
		return newHttpClient(null, 0);
	}
	
	public static OkHttpClient newHttpClient(String proxyIp, int proxyPort) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.sslSocketFactory(getDefaultSSLSocketFactory(), (X509TrustManager) tm);
		if(!Utils.isBlank(proxyIp) && proxyPort > 0) {
			builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort)));
		}
		builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
		builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
		builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
//		builder.pingInterval(30, TimeUnit.SECONDS);
		builder.addInterceptor(new RetryIntercepter(RETRY_COUNT, RETRY_INTERVAL));
		builder.retryOnConnectionFailure(true);
		builder.protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1));
		
		return builder.build();
	}
	
	public static void closeHttpClient(OkHttpClient client) {
		if(client != null)
			client.dispatcher().executorService().shutdown();
	}
	
	public static boolean isFinish(OkHttpClient client) {
		return client.dispatcher().runningCallsCount() == 0;
	}
}
