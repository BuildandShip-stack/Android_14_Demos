package com.example.simpleservice;

import android.content.Context;
import android.util.Log;
import com.example.simplemanager.ISimpleManager;

class ISimpleServiceImpl extends ISimpleManager.Stub {
	private static final String LOGTAG = "SimpleService";
	private Context mContext;

	ISimpleServiceImpl(Context context) {
		mContext = context;
	}

	public int addInts(int a, int b) {
		Log.d(LOGTAG, "addInts");
		return a + b;
	}

	public String echoString(String s) {
		Log.d(LOGTAG, "echoString");
		return s;
	}
}

