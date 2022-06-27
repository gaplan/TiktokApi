package com.tt.util;

import java.nio.charset.StandardCharsets;

public class HexUtil {
	
	public static String toString(short[] arr){
		if(arr == null)
			return "";
		
		StringBuilder sb = new StringBuilder(arr.length*4);
		for (int i = 0; i < arr.length; i++) {
			sb.append(String.format("%04x ", arr[i]));
		}
		return sb.toString();
	}
	
	public static String toString(byte[] arr) {
		if (arr == null)
			return "";
		
		StringBuffer sb = new StringBuffer(arr.length*2);
		for (int i = 0; i < arr.length; ++i) {
			sb.append(String.format("%02x", arr[i]));
		}
		return sb.toString();
	}
	
	private static String formatStr(String str, int line) {
		StringBuilder builder = new StringBuilder();
		char[] c = str.toCharArray();
		for (int i = 0,x = 1; i < c.length/2; i++,x++) {
			builder.append(c[i*2]);
			builder.append(c[i*2+1]);
			if(x%4 == 0)
				builder.append(' ');
			if(x%line == 0)
				builder.append('\n');
		}
		return builder.toString();
	}
	
	public static String toFormatString(byte []arr){
		return formatStr(toString(arr), 16);
	}
	
	public static byte[] toBytes(String string) {
		if(string == null || string.length() == 0)
			return null;
		
		byte[] arr = string.getBytes(StandardCharsets.UTF_8);
		if(arr.length%2 != 0)
			return null;
		
		byte[] result = new byte[arr.length / 2];
		for (int i = 0; i < arr.length; i += 2) {
			result[i / 2] = ((byte) Integer.parseInt(new String(arr, i, 2), 16));
		}
		return result;
	}
	
}
