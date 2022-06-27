package com.tt.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Files {
	
	public static String readUtf8(File file){
		byte[] bytes = readFile(file);
		if(bytes !=null)
			return new String(bytes, StandardCharsets.UTF_8);
		return null;
	}
	
	public static byte[] readFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			return Inputs.readAllAndClose(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static byte[] readFileHeader(File file, int length) {
		try {
			byte[] result = new byte[length];
			FileInputStream fis = new FileInputStream(file);
			fis.read(result);
			fis.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static List<String> readFileLines(File file) {
		try {
			List<String> result = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null; 
			while((line = br.readLine()) != null){
				result.add(line);
			}
			br.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static void writeFileLines(File file, List<String> lines) {
		try {
			PrintWriter pw = new PrintWriter(file);
			for (String line : lines) {
				pw.println(line);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static boolean writeFile(File file, InputStream input) {
		if(input == null || file == null)
			return false;
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			Inputs.readToAndClose(input, fos);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			
		return false;
	}
	
	public static boolean writeFile(File file, byte[] buf) {
		if(buf == null || file == null)
			return false;
		
		return writeFile(file, new ByteArrayInputStream(buf));
	}

	public static boolean copyFile(File fromFile, File toFile) {
		if(fromFile == null || toFile == null || !fromFile.isFile())
			return false;
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if(toFile.exists())
				deleteFile(toFile);
			
			if(!toFile.getParentFile().exists()){
				toFile.getParentFile().mkdirs();
			}
			
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
	
			Inputs.readTo(fis, fos);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) fis.close();
				if(fos != null) fos.close();
			} catch (IOException e) {
			}
		}
		return false;
	}
	
	public static boolean copyDir(File fromDir, File toDir){
		if(fromDir == null || toDir == null || !fromDir.isDirectory())
			return false;
		
		if(toDir.exists() && toDir.isFile()){
			toDir.delete();
		}
		if(!toDir.exists()){
			toDir.mkdirs();
		}
		
		List<File> items = new ArrayList<File>();
		listFiles(fromDir, items, true);
		
		for (File file : items) {
			String relativePath = file.getAbsolutePath().substring(fromDir.getAbsolutePath().length());
			File newFile = new File(toDir, relativePath);
			if (file.isDirectory()) {
				newFile.mkdir();
			}else{
				copyFile(file, newFile);
			}
		}
		return true;
	}
	
	public static boolean copy(File from, File to){
		if(from == null || !from.exists())
			return false;
		
		if(from.isFile()){
			return copyFile(from, to);
		}else{
			return copyDir(from, to);
		}
	}
	
	public static void listFiles(File dir, List<File> result, boolean addDir) {
		if (dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				listFiles(f, result, addDir);
			}
			if(addDir){
				result.add(dir);
			}
		} else {
			result.add(dir);
		}
	}

	public static List<File> listFiles(File dir) {
		List<File> list = new ArrayList<File>();
		listFiles(dir, list, false);
		return list;
	}
	
	public static Object readObject(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();

			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return null;
	}

	public static void writeObject(File file, Object obj) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void deleteFile(File file) {
		if(file == null || !file.exists()){
			return;
		}
		
		if(file.isFile()){
			file.delete();
		}else if(file.isDirectory()){
			for (File item : file.listFiles()) {
				deleteFile(item);
			}
			file.delete();
		}
	}
	
	public static boolean splitFile(File file, File[] splits){
		Objects.requireNonNull(file);
		Objects.requireNonNull(splits);
		
		long len = file.length();
		long one = len/splits.length;
		long more = len%splits.length;
		byte[] buf = new byte[0x4000];
		
		try {
			RandomAccessFile target = new RandomAccessFile(file, "r");

			for (int i = 0; i < splits.length; i++) {
				if(splits[i] == null){
					splits[i] = new File(file.getParentFile(), file.getName()+"."+i);
				}
				
				RandomAccessFile singleFp = new RandomAccessFile(splits[i], "rw");
				singleFp.setLength(0);
				
				int maxRead = (int) one;
				if(i == 0) maxRead += more;
				
				while(maxRead > 0){
					int needRead = Math.min(maxRead, buf.length);
					
					int read = target.read(buf, 0, needRead);
					if(read != needRead){
						System.err.printf("读取警告: read(%d) != needRead(%d)\n", read, needRead);
					}
					
					singleFp.write(buf, 0, read);
					maxRead -= read;
				}
				singleFp.close();
			}
			
			target.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return false;
	}
	
	public static boolean mergeFile(File file, File[] splits){
		Objects.requireNonNull(file);
		Objects.requireNonNull(splits);
		
		byte[] buf = new byte[0x4000];
		
		try {
			RandomAccessFile target = new RandomAccessFile(file, "rw");
			target.setLength(0);
			
			for (int i = 0; i < splits.length; i++) {
				Objects.requireNonNull(splits[i]);
				
				RandomAccessFile singleFp = new RandomAccessFile(splits[i], "r");
				int maxRead = (int) splits[i].length();
				
				while(maxRead > 0){
					int needRead = Math.min(maxRead, buf.length);
					
					int read = singleFp.read(buf, 0, needRead);
					if(read != needRead){
						System.err.printf("读取警告: read(%d) != needRead(%d)\n", read, needRead);
					}
					
					target.write(buf, 0, read);
					maxRead -= read;
				}
				singleFp.close();
			}
			
			target.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return false;
	}
}
