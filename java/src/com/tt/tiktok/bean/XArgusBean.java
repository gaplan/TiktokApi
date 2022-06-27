package com.tt.tiktok.bean;

import com.squareup.wire.WireField;

import okio.ByteString;

public class XArgusBean {
	@WireField(tag=1, adapter="com.squareup.wire.ProtoAdapter#INT64")
	public long magic;
	@WireField(tag=2, adapter="com.squareup.wire.ProtoAdapter#INT32")
	public int version;
	@WireField(tag=3, adapter="com.squareup.wire.ProtoAdapter#INT64")
	public long rand;
	@WireField(tag=4, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String msAppID;
	@WireField(tag=5, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String deviceID;
	@WireField(tag=6, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String licenseID;
	@WireField(tag=7, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String appVersion;
	@WireField(tag=8, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String sdkVersionStr;
	@WireField(tag=9, adapter="com.squareup.wire.ProtoAdapter#INT64")
	public long sdkVersion;
	@WireField(tag=10, adapter="com.squareup.wire.ProtoAdapter#BYTES")
	public ByteString envcode;
	@WireField(tag=11, adapter="com.squareup.wire.ProtoAdapter#INT32")
	public int platform;
	@WireField(tag=12, adapter="com.squareup.wire.ProtoAdapter#INT64")
	public long createTime;
	@WireField(tag=13, adapter="com.squareup.wire.ProtoAdapter#BYTES")
	public ByteString bodyHash;
	@WireField(tag=14, adapter="com.squareup.wire.ProtoAdapter#BYTES")
	public ByteString queryHash;
	@WireField(tag=15, adapter="com.squareup.wire.ProtoAdapter#MESSAGE")
	public ActionRecord actionRecord;
	@WireField(tag=16, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String secDeviceToken;
	@WireField(tag=17, adapter="com.squareup.wire.ProtoAdapter#INT64")
	public long isAppLicense;
	@WireField(tag=20, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String pskVersion;
	@WireField(tag=21, adapter="com.squareup.wire.ProtoAdapter#INT32")
	public int callType;
	
	public static class ActionRecord {
		@WireField(tag=1, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int signCount;
		@WireField(tag=2, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int reportCount;
		@WireField(tag=3, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int settingCount;
	}
	
}

