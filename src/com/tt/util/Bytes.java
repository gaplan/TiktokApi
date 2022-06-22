package com.tt.util;

public class Bytes {

	public static short swap(short x) {
		return Short.reverseBytes(x);
	}

	public static char swap(char x) {
		return Character.reverseBytes(x);
	}

	public static int swap(int x) {
		return Integer.reverseBytes(x);
	}

	public static long swap(long x) {
		return Long.reverseBytes(x);
	}

	// -- get/put char --

	static private char makeChar(byte b1, byte b0) {
		return (char) ((b1 << 8) | (b0 & 0xff));
	}

	public static char getCharL(byte[] bb, int bi) {
		return makeChar(bb[bi + 1], bb[bi]);
	}

	public static char getCharB(byte[] bb, int bi) {
		return makeChar(bb[bi], bb[bi + 1]);
	}

	public static char getChar(byte[] bb, int bi, boolean bigEndian) {
		return bigEndian ? getCharB(bb, bi) : getCharL(bb, bi);
	}

	private static byte char1(char x) {
		return (byte) (x >> 8);
	}

	private static byte char0(char x) {
		return (byte) (x);
	}

	public static void putCharL(byte[] bb, int bi, char x) {
		bb[bi] = char0(x);
		bb[bi+1] = char1(x);
	}

	public static void putCharB(byte[] bb, int bi, char x) {
		bb[bi] = char1(x);
		bb[bi+1] = char0(x);
	}

	public static void putChar(byte[] bb, int bi, char x, boolean bigEndian) {
		if (bigEndian)
			putCharB(bb, bi, x);
		else
			putCharL(bb, bi, x);
	}

	// -- get/put short --

	static private short makeShort(byte b1, byte b0) {
		return (short) ((b1 << 8) | (b0 & 0xff));
	}

	public static short getShortL(byte[] bb, int bi) {
		return makeShort(bb[bi+1], bb[bi]);
	}

	public static short getShortB(byte[] bb, int bi) {
		return makeShort(bb[bi], bb[bi+1]);
	}

	public static short getShort(byte[] bb, int bi, boolean bigEndian) {
		return bigEndian ? getShortB(bb, bi) : getShortL(bb, bi);
	}

	private static byte short1(short x) {
		return (byte) (x >> 8);
	}

	private static byte short0(short x) {
		return (byte) (x);
	}

	public static void putShortL(byte[] bb, int bi, short x) {
		bb[bi] = short0(x);
		bb[bi+1] = short1(x);
	}

	public static void putShortB(byte[] bb, int bi, short x) {
		bb[bi] = short1(x);
		bb[bi+1] = short0(x);
	}

	public static void putShort(byte[] bb, int bi, short x, boolean bigEndian) {
		if (bigEndian)
			putShortB(bb, bi, x);
		else
			putShortL(bb, bi, x);
	}

	// -- get/put int --

	static private int makeInt(byte b3, byte b2, byte b1, byte b0) {
		return (((b3) << 24) | ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | ((b0 & 0xff)));
	}

	public static int getIntL(byte[] bb, int bi) {
		return makeInt(bb[bi+3], bb[bi+2], bb[bi+1], bb[bi]);
	}

	public static int getIntB(byte[] bb, int bi) {
		return makeInt(bb[bi], bb[bi+1], bb[bi+2], bb[bi+3]);
	}

	public static int getInt(byte[] bb, int bi, boolean bigEndian) {
		return bigEndian ? getIntB(bb, bi) : getIntL(bb, bi);
	}

	private static byte int3(int x) {
		return (byte) (x >> 24);
	}

	private static byte int2(int x) {
		return (byte) (x >> 16);
	}

	private static byte int1(int x) {
		return (byte) (x >> 8);
	}

	private static byte int0(int x) {
		return (byte) (x);
	}

	public static void putIntL(byte[] bb, int bi, int x) {
		bb[bi+3] = int3(x);
		bb[bi+2] = int2(x);
		bb[bi+1] = int1(x);
		bb[bi+0] = int0(x);
	}

	public static void putIntB(byte[] bb, int bi, int x) {
		bb[bi+0] = int3(x);
		bb[bi+1] = int2(x);
		bb[bi+2] = int1(x);
		bb[bi+3] = int0(x);
	}

	public static void putInt(byte[] bb, int bi, int x, boolean bigEndian) {
		if (bigEndian)
			putIntB(bb, bi, x);
		else
			putIntL(bb, bi, x);
	}

	// -- get/put long --

	static private long makeLong(byte b7, byte b6, byte b5, byte b4, byte b3, byte b2, byte b1, byte b0) {
		return ((((long) b7) << 56) | (((long) b6 & 0xff) << 48) | (((long) b5 & 0xff) << 40)
				| (((long) b4 & 0xff) << 32) | (((long) b3 & 0xff) << 24) | (((long) b2 & 0xff) << 16)
				| (((long) b1 & 0xff) << 8) | (((long) b0 & 0xff)));
	}

	public static long getLongL(byte[] bb, int bi) {
		return makeLong(bb[bi+7], bb[bi+6], bb[bi+5], bb[bi+4], bb[bi+3],
				bb[bi+2], bb[bi+1], bb[bi]);
	}

	public static long getLongB(byte[] bb, int bi) {
		return makeLong(bb[bi], bb[bi+1], bb[bi+2], bb[bi+3], bb[bi+4],
				bb[bi+5], bb[bi+6], bb[bi+7]);
	}

	public static long getLong(byte[] bb, int bi, boolean bigEndian) {
		return bigEndian ? getLongB(bb, bi) : getLongL(bb, bi);
	}

	private static byte long7(long x) {
		return (byte) (x >> 56);
	}

	private static byte long6(long x) {
		return (byte) (x >> 48);
	}

	private static byte long5(long x) {
		return (byte) (x >> 40);
	}

	private static byte long4(long x) {
		return (byte) (x >> 32);
	}

	private static byte long3(long x) {
		return (byte) (x >> 24);
	}

	private static byte long2(long x) {
		return (byte) (x >> 16);
	}

	private static byte long1(long x) {
		return (byte) (x >> 8);
	}

	private static byte long0(long x) {
		return (byte) (x);
	}

	public static void putLongL(byte[] bb, int bi, long x) {
		bb[bi+7] = long7(x);
		bb[bi+6] = long6(x);
		bb[bi+5] = long5(x);
		bb[bi+4] = long4(x);
		bb[bi+3] = long3(x);
		bb[bi+2] = long2(x);
		bb[bi+1] = long1(x);
		bb[bi+0] = long0(x);
	}

	public static void putLongB(byte[] bb, int bi, long x) {
		bb[bi+0] = long7(x);
		bb[bi+1] = long6(x);
		bb[bi+2] = long5(x);
		bb[bi+3] = long4(x);
		bb[bi+4] = long3(x);
		bb[bi+5] = long2(x);
		bb[bi+6] = long1(x);
		bb[bi+7] = long0(x);
	}

	public static void putLong(byte[] bb, int bi, long x, boolean bigEndian) {
		if (bigEndian)
			putLongB(bb, bi, x);
		else
			putLongL(bb, bi, x);
	}

	// -- get/put float --

	public static float getFloatL(byte[] bb, int bi) {
		return Float.intBitsToFloat(getIntL(bb, bi));
	}

	public static float getFloatB(byte[] bb, int bi) {
		return Float.intBitsToFloat(getIntB(bb, bi));
	}

	public static float getFloat(byte[] bb, int bi, boolean bigEndian) {
		return bigEndian ? getFloatB(bb, bi) : getFloatL(bb, bi);
	}

	public static void putFloatL(byte[] bb, int bi, float x) {
		putIntL(bb, bi, Float.floatToRawIntBits(x));
	}

	public static void putFloatB(byte[] bb, int bi, float x) {
		putIntB(bb, bi, Float.floatToRawIntBits(x));
	}

	public static void putFloat(byte[] bb, int bi, float x, boolean bigEndian) {
		if (bigEndian)
			putFloatB(bb, bi, x);
		else
			putFloatL(bb, bi, x);
	}

	// -- get/put double --

	public static double getDoubleL(byte[] bb, int bi) {
		return Double.longBitsToDouble(getLongL(bb, bi));
	}

	public static double getDoubleB(byte[] bb, int bi) {
		return Double.longBitsToDouble(getLongB(bb, bi));
	}

	public static double getDouble(byte[] bb, int bi, boolean bigEndian) {
		return bigEndian ? getDoubleB(bb, bi) : getDoubleL(bb, bi);
	}

	public static void putDoubleL(byte[] bb, int bi, double x) {
		putLongL(bb, bi, Double.doubleToRawLongBits(x));
	}

	public static void putDoubleB(byte[] bb, int bi, double x) {
		putLongB(bb, bi, Double.doubleToRawLongBits(x));
	}

	public static void putDouble(byte[] bb, int bi, double x, boolean bigEndian) {
		if (bigEndian)
			putDoubleB(bb, bi, x);
		else
			putDoubleL(bb, bi, x);
	}
	
	/** array */
	public static byte[] toBytes(boolean[] bools){
		if(bools == null) return null;
		byte[] bytes = new byte[bools.length * Byte.BYTES];
		for (int i = 0; i < bools.length; i++) {
			bytes[i] = (byte) (bools[i]?1:0);
		}
		return bytes;
	}
	
	public static byte[] toBytes(short[] shorts, boolean bigEndian){
		if(shorts == null) return null;
		byte[] bytes = new byte[shorts.length * Short.BYTES];
		for (int i = 0; i < shorts.length; i++) {
			putShort(bytes, i * Short.BYTES, shorts[i], bigEndian);
		}
		return bytes;
	}
	
	public static byte[] toBytes(char[] chars, boolean bigEndian){
		if(chars == null) return null;
		byte[] bytes = new byte[chars.length * Character.BYTES];
		for (int i = 0; i < chars.length; i++) {
			putChar(bytes, i * Character.BYTES, chars[i], bigEndian);
		}
		return bytes;
	}
	
	public static byte[] toBytes(int[] ints, boolean bigEndian){
		if(ints == null) return null; 
		byte[] bytes = new byte[ints.length * Integer.BYTES];
		for (int i = 0; i < ints.length; i++) {
			putInt(bytes, i * Integer.BYTES, ints[i], bigEndian);
		}
		return bytes;
	}
	
	public static byte[] toBytes(long[] longs, boolean bigEndian){
		if(longs == null) return null;
		byte[] bytes = new byte[longs.length * Long.BYTES];
		for (int i = 0; i < longs.length; i++) {
			putLong(bytes, i * Long.BYTES, longs[i], bigEndian);
		}
		return bytes;
	}
	
	public static byte[] toBytes(float[] floats, boolean bigEndian){
		if(floats == null) return null;
		byte[] bytes = new byte[floats.length * Float.BYTES];
		for (int i = 0; i < floats.length; i++) {
			putFloat(bytes, i * Float.BYTES, floats[i], bigEndian);
		}
		return bytes;
	}
	
	public static byte[] toBytes(double[] doubles, boolean bigEndian){
		if(doubles == null) return null;
		byte[] bytes = new byte[doubles.length * Double.BYTES];
		for (int i = 0; i < doubles.length; i++) {
			putDouble(bytes, i * Double.BYTES, doubles[i], bigEndian);
		}
		return bytes;
	}
	
	public static byte[] toBytesL(boolean[] bools){
		return toBytes(bools);
	}
	
	public static byte[] toBytesL(short[] shorts){
		return toBytes(shorts, false);
	}
	
	public static byte[] toBytesL(char[] chars){
		return toBytes(chars, false);
	}
	
	public static byte[] toBytesL(int[] ints){
		return toBytes(ints, false);
	}
	
	public static byte[] toBytesL(long[] longs){
		return toBytes(longs, false);
	}
	
	public static byte[] toBytesL(float[] floats){
		return toBytes(floats, false);
	}
	
	public static byte[] toBytesL(double[] doubles){
		return toBytes(doubles, false);
	}
}
