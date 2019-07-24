package com.telcoware.whoareyou;

import android.provider.BaseColumns;

public class PrivateContactDb {
	private PrivateContactDb() {}
	public static final class PrivateContacts implements BaseColumns {
		private PrivateContacts() {}
		public static final String TABLE_NAME = "private_contacts";
		public static final String NUMBER = "number";                       //TEXT
		public static final String NAME = "name";                           //TEXT
	}
}
