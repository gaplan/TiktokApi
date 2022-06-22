package com.tt.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Inputs {
	
	public static void readTo(InputStream in, OutputStream out){
		try {
			int len = 0;
			byte buff[] = new byte[1024 * 4];
			while ((len = in.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void readToAndClose(InputStream in, OutputStream out){
		try {
			readTo(in, out);
		} finally {
			try {
				if(in != null) in.close();
				if(out != null) out.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static byte[] readAll(InputStream in) {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	readTo(in, bos);
        return bos.toByteArray();
    }

    public static byte[] readAllAndClose(InputStream in) {
        try {
            return readAll(in);
        } finally {
            try {
            	if(in != null) in.close();
			} catch (Exception e) {
			}
        }
    }
    
    
}
