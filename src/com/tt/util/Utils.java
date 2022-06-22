package com.tt.util;

import java.util.Collection;

public class Utils {

	public static boolean isAscii(byte[] bytes) {
		if (bytes == null)
			return false;

		for (int i = 0; i < bytes.length; i++) {
			int num = (bytes[i] & 0xFF);
			if (num < 0x20 || num >= 0x7F)
				return false;
		}
		return true;
	}

	public static boolean isBlank(String str) {
		return (str == null) || (str.trim().length() == 0);
	}

	public static boolean isBlank(Collection<?> list) {
		return (list == null) || list.isEmpty();
	}

	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
}
