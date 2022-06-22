package com.tt.tiktok.bean;

import com.squareup.wire.WireField;

public class TokenReqBean {
	@WireField(tag=1, adapter="com.squareup.wire.ProtoAdapter#MESSAGE")
	public Device device;
	@WireField(tag=2, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String secDeviceToken;
	@WireField(tag=3, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String platform;
	@WireField(tag=4, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String sdkVersionStr;
	@WireField(tag=5, adapter="com.squareup.wire.ProtoAdapter#INT32")
	public int sdkVersion;
	@WireField(tag=6, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String msAppID;
	@WireField(tag=7, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String appVersion;
	@WireField(tag=8, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String deviceID;
	@WireField(tag=9, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String envcode;
	@WireField(tag=10, adapter="com.squareup.wire.ProtoAdapter#INT32")
	public int tag10;
	@WireField(tag=11, adapter="com.squareup.wire.ProtoAdapter#INT32")
	public int tag11;
	@WireField(tag=12, adapter="com.squareup.wire.ProtoAdapter#STRING")
	public String tag12;
	@WireField(tag=16, adapter="com.squareup.wire.ProtoAdapter#INT32")
	public int tag16;
	
	public static class Device {
		@WireField(tag=1, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag1;
		@WireField(tag=2, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String deviceType;
		@WireField(tag=3, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag3;
		@WireField(tag=4, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag4;
		@WireField(tag=5, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String devicePlatform;
		@WireField(tag=6, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String deviceVersion;
		@WireField(tag=7, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String deviceDpi;
		@WireField(tag=8, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag8;
		@WireField(tag=9, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag9;
		@WireField(tag=10, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag10;
		@WireField(tag=11, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag11;
		@WireField(tag=12, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String timezone;
		@WireField(tag=13, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag13;
		@WireField(tag=14, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag14;
		@WireField(tag=15, adapter="com.squareup.wire.ProtoAdapter#INT64")
		public long tag15;
		@WireField(tag=16, adapter="com.squareup.wire.ProtoAdapter#INT64")
		public long tag16;
		@WireField(tag=17, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag17;
		@WireField(tag=18, adapter="com.squareup.wire.ProtoAdapter#INT64")
		public long tag18;
		@WireField(tag=20, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag20;
		@WireField(tag=21, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag21;
		@WireField(tag=22, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag22;
		@WireField(tag=23, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag23;
		@WireField(tag=24, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag24;
		@WireField(tag=25, adapter="com.squareup.wire.ProtoAdapter#INT64")
		public long deviceBuildTime1;  //时间戳左移1位
		@WireField(tag=26, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag26;
		@WireField(tag=27, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag27;
		@WireField(tag=28, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag28;
		@WireField(tag=29, adapter="com.squareup.wire.ProtoAdapter#INT64")
		public long deviceBuildTime2; //时间戳左移1位
		@WireField(tag=30, adapter="com.squareup.wire.ProtoAdapter#INT64")
		public long deviceBuildTime3; //时间戳左移1位
		@WireField(tag=31, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag31;
		@WireField(tag=32, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String ip;
		@WireField(tag=33, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String dnsIp;
		@WireField(tag=34, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag34;
		@WireField(tag=35, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag35;
		@WireField(tag=38, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag38;
		@WireField(tag=39, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag39;
		@WireField(tag=40, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag40;
		@WireField(tag=42, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag42;
		@WireField(tag=43, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag43;
		@WireField(tag=44, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag44;
		@WireField(tag=45, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag45;
		@WireField(tag=46, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag46;
		@WireField(tag=47, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag47;
		@WireField(tag=48, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag48;
		@WireField(tag=49, adapter="com.squareup.wire.ProtoAdapter#INT32")
		public int tag49;
		@WireField(tag=50, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag50;
		@WireField(tag=51, adapter="com.squareup.wire.ProtoAdapter#INT64")
		public long tag51;
		@WireField(tag=52, adapter="com.squareup.wire.ProtoAdapter#INT64")
		public long tag52;
		@WireField(tag=53, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag53;
		@WireField(tag=54, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag54;
		@WireField(tag=55, adapter="com.squareup.wire.ProtoAdapter#STRING")
		public String tag55;
//		@WireField(tag=56, adapter="com.squareup.wire.ProtoAdapter#INT32")
//		public int tag56;
	}
}
