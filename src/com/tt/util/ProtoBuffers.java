package com.tt.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;

import okio.Buffer;
import okio.ByteString;

public class ProtoBuffers {
	
	private static <T> Field getFieldByTag(Class<T> clazz, int tag){
		for (Field field : clazz.getDeclaredFields()) {
			WireField wireField = field.getAnnotation(WireField.class);
			if(wireField.tag() == tag){
				return field;
			}
		}
		return null;
	}
	
	public static String toString(Object obj){
		StringBuilder sb = new StringBuilder();
		
		Class<?> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			WireField wireField = field.getAnnotation(WireField.class);
			Object value = null;
			try {
				value = field.get(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.append(String.format("%d(%s)(%s)::%s\n", wireField.tag(), field.getName(), 
					wireField.adapter().substring(wireField.adapter().lastIndexOf('#')+1),
					value));
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Buffer buffer, Class<T> clazz){
		try {
			T t = clazz.newInstance();
			ProtoReader reader = new ProtoReader(buffer);
			long l = reader.beginMessage();
			while (true) {
				int tag = reader.nextTag();
				if (tag < 0)
					break;
				
				Field field = getFieldByTag(clazz, tag);
				if(field == null){
					System.err.println("toObject() tag("+tag+") is not exist");
					reader.skip();
					continue;
				}
				
				WireField wireField = field.getAnnotation(WireField.class);
				ProtoAdapter<?> adapter = ProtoAdapterUtils.get(wireField.adapter());
				if(adapter != null){
					Object decode = adapter.decode(reader);
					if(List.class.isAssignableFrom(field.getType())){
						List<Object> list = (List<Object>)field.get(t);
						if(list == null){
							list = new ArrayList<>();
							field.set(t, list);
						}
						list.add(decode);
					}else{
						field.set(t, decode);
					}
					continue;
				}
				
				if(wireField.adapter().endsWith("#MESSAGE")){
					ByteString decode = ProtoAdapter.BYTES.decode(reader);
					Buffer buf = new Buffer();
					buf.write(decode);
					Object decodeObj = toObject(buf, field.getType());
					if(List.class.isAssignableFrom(field.getType())){
						List<Object> list = (List<Object>)field.get(t);
						if(list == null){
							list = new ArrayList<>();
							field.set(t, list);
						}
						list.add(decodeObj);
					}else{
						field.set(t, decodeObj);
					}
					continue;
				}
				
				throw new RuntimeException("unknow adapter: "+wireField.adapter());
			}
			reader.endMessage(l);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Buffer toBuf(Object obj){
		try {
			Class<?> clazz = obj.getClass();
			Buffer buffer = new Buffer();
			ProtoWriter writer = new ProtoWriter(buffer);
			
			for (Field field : clazz.getDeclaredFields()) {
				Object value = field.get(obj);
				if(value == null) continue;
				
				WireField wireField = field.getAnnotation(WireField.class);
				ProtoAdapter<Object> adapter = (ProtoAdapter<Object>)ProtoAdapterUtils.get(wireField.adapter());
				if(adapter != null){
					if(List.class.isAssignableFrom(field.getType())){
						List<Object> list = (List<Object>)value;
						for (Object object : list) {
							adapter.encodeWithTag(writer, wireField.tag(), object);
						}
					}else{
						adapter.encodeWithTag(writer, wireField.tag(), value);
					}
					continue;
				}
				
				if(wireField.adapter().endsWith("#MESSAGE")){
					if(List.class.isAssignableFrom(field.getType())){
						List<Object> list = (List<Object>)value;
						for (Object object : list) {
							ProtoAdapter.BYTES.encodeWithTag(writer, wireField.tag(), toBuf(object).readByteString());
						}
					}else{
						ProtoAdapter.BYTES.encodeWithTag(writer, wireField.tag(), toBuf(value).readByteString());
					}
					continue;
				}
				
				throw new RuntimeException("unknow adapter: "+wireField.adapter());
			}
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Buffer toBuf(TreeMap<Integer, Object> map) {
		if(map == null) return null;
		try {
			Buffer buffer = new Buffer();
			ProtoWriter writer = new ProtoWriter(buffer);
			for (Entry<Integer, Object> item : map.entrySet()) {
				Integer key = item.getKey();
				Object value = item.getValue();
				if(value == null) continue;
				
				if(value instanceof List){
					for (Object v2 : ((List<Object>)value)) {
						if(v2 == null) continue;
						ProtoAdapter<Object> adapter = (ProtoAdapter<Object>) ProtoAdapterUtils.get(v2.getClass());
						if(adapter == null){
							throw new RuntimeException("unknow field type: "+v2.getClass());
						}
						adapter.encodeWithTag(writer, key, v2);
					}
					continue;
				}
				
				ProtoAdapter<Object> adapter = (ProtoAdapter<Object>) ProtoAdapterUtils.get(value.getClass());
				if(adapter == null){
					throw new RuntimeException("unknow field type: "+value.getClass());
				}
				adapter.encodeWithTag(writer, key, value);
			}
			return buffer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static TreeMap<Integer, Object> toMap(Buffer buffer){
		if(buffer == null) return null;
		try {
			TreeMap<Integer, Object> map = new TreeMap<Integer, Object>();
			ProtoReader reader = new ProtoReader(buffer);
			
			long l = reader.beginMessage();
			while (true) {
				int tag = reader.nextTag();
				if (tag < 0) break;
				
				FieldEncoding encoding = reader.peekFieldEncoding();
				ProtoAdapter<?> adapter = ProtoAdapterUtils.get(encoding);
				if(adapter == null){
					throw new RuntimeException("unknow field type: "+encoding);
				}
				Object value = adapter.decode(reader);
				Object value2 = map.get(tag);
				if(value2 == null){
					map.put(tag, value);
					continue;
				}
				
				if(value2 instanceof List){
					((List<Object>)value2).add(value);
				}else{
					List<Object> list = new ArrayList<Object>();
					list.add(value2);
					list.add(value);
					map.put(tag, list);
				}
			}
			reader.endMessage(l);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static class ProtoAdapterUtils {
		public static ProtoAdapter<?> get(FieldEncoding encoding){
			switch(encoding){
			case FIXED32: return ProtoAdapter.FLOAT;
			case FIXED64: return ProtoAdapter.DOUBLE;
			case LENGTH_DELIMITED: return ProtoAdapter.BYTES;
			case VARINT: return ProtoAdapter.INT64;
			default: return null;
			}
		}
		
		public static ProtoAdapter<?> get(Class<?> clazz){
			if(clazz == Integer.class) return ProtoAdapter.INT32;
			if(clazz == Long.class) return ProtoAdapter.INT64;
			if(clazz == Float.class) return ProtoAdapter.FLOAT;
			if(clazz == Double.class) return ProtoAdapter.DOUBLE;
			if(clazz == ByteString.class) return ProtoAdapter.BYTES;
			if(clazz == String.class) return ProtoAdapter.STRING;
			return null;
		}
		
		public static ProtoAdapter<?> get(String adapterString){
			try {
				int hash = adapterString.indexOf('#');
				String className = adapterString.substring(0, hash);
				String fieldName = adapterString.substring(hash + 1);
				return (ProtoAdapter<?>)Class.forName(className).getField(fieldName).get(null);
			} catch (IllegalAccessException|NoSuchFieldException|ClassNotFoundException e) {
				return null;
			}
		}
	}
	
}
