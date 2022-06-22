package com.tt.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtil {
	private final static int GZIP_MAGIC = 0x8b1f;
	private static final int DEFLATED = 8;
	private final static int GZIP_HEADER_SIZE = 10;
	
	
	public static boolean isGZIPBuff(byte []data) {
		if(data == null || data.length <= GZIP_HEADER_SIZE) {
			return false;
		}
		return (data[0] == (byte)GZIP_MAGIC) && 
				(data[1] == (byte)(GZIP_MAGIC >> 8)) && 
				(data[2] == DEFLATED);
	}

	public static byte[] compress(byte data[]) {
		if (data == null) {
			return null;
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			GZIPOutputStream gzipOut = new GZIPOutputStream(output);
			gzipOut.write(data);
			gzipOut.close();
			return output.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decompress(byte data[]) {
		if (data == null) {
			return null;
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		try {
			GZIPInputStream gzipInput = new GZIPInputStream(input);
			while( (len = gzipInput.read(buff)) > 0 ){
				output.write(buff, 0, len);
			}
			return output.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
