package com.tt.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MDUtil {
	
	private static byte[] digest(String algorithm, byte[] content){
		if(content == null)
			return null;
	
		try {
			return MessageDigest.getInstance(algorithm).digest(content);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] md5(byte []content){
		return digest("MD5", content);
	}
	
	public static byte[] sha1(byte []content){
		return digest("SHA1", content);
	}
	
	public static byte[] sha256(byte []content){
		return digest("SHA-256", content);
	}
	
	public static byte[] sha512(byte []content){
		return digest("SHA-512", content);
	}
	
	
	
	private static byte[] hmac_digest(String algorithm, byte[] content, byte[] keybytes){
		try {
			SecretKeySpec key = new SecretKeySpec(keybytes, algorithm);
			Mac mac = Mac.getInstance(algorithm);
			mac.init(key);
			return mac.doFinal(content);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] hmac_md5(byte[] content, byte[] keybytes){
		return hmac_digest("HmacMD5", content, keybytes);
	}
	
	public static byte[] hmac_sha1(byte[] content, byte[] keybytes){
		return hmac_digest("HmacSHA1", content, keybytes);
	}
	
	public static byte[] hmac_sha256(byte[] content, byte[] keybytes){
		return hmac_digest("HmacSHA256", content, keybytes);
	}
	
	public static byte[] hmac_sha512(byte[] content, byte[] keybytes){
		return hmac_digest("HmacSHA512", content, keybytes);
	}
}
