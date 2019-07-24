package com.telcoware.whoareyou;

import android.provider.BaseColumns;

public final class CallLogDb {
	private CallLogDb() {}
	public static final class CallLogs implements BaseColumns {
		private CallLogs() {}
		public static final String TABLE_NAME = "call_logs";
		public static final String NUMBER = "number";                       //TEXT
		public static final String NAME = "name";                           //TEXT 
		public static final String SPAM_LEVEL = "spam_level";               //INTEGER: -1: unknown, 0~9
		public static final String SPAM_KEYWORD = "spam_keyword";           //TEXT: "key1:4,key2:2,key3:1"
		public static final String SPAM_REGI = "spam_regi";                 //INTEGER: 0(UnRegi), 1(Regi)
		public static final String SPAM_REGI_KEYWORD = "spam_regi_keyword";  //TEXT
		public static final String DATE = "date";                           //INTEGER: epoch msec
		public static final String TYPE = "type";                           //INTEGER: 0(Incoming), 1(Missed), 2(SMS)
		public static final String CONTENT = "sms_content";                 //TEXT: SMS Content
		public static final String DEFAULT_SORT_ORDER = "date DESC";
	}
}

