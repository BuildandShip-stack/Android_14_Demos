package com.example.simpleservice;

import android.app.Application;
import android.os.ServiceManager;
import android.util.Log;

public class simpleServiceApp extends Application {
	private static final String LOGTAG = "SimpleService";
	private static final String REMOTE_SERVICE_NAME = "simpleservice";
	private ISimpleServiceImpl serviceImpl;

	public void onCreate() {
		super.onCreate();
		this.serviceImpl = new ISimpleServiceImpl(this);
		ServiceManager.addService(REMOTE_SERVICE_NAME, this.serviceImpl);
		Log.d(LOGTAG, "Registered service"); 
	}

	public void onTerminate() {
		super.onTerminate();
		Log.d(LOGTAG, "Terminated");
	}
}
