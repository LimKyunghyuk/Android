package com.telcoware.taxicalluser;

import android.os.*;
import android.preference.*;

public class PActivity extends PreferenceActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
