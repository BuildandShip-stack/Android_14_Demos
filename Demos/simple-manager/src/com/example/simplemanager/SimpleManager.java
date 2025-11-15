package com.example.simplemanager;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class SimpleManager {
	private static final String LOGTAG = "SimpleManager";
	private static final String REMOTE_SERVICE_NAME = "simpleservice";
	private final ISimpleManager service;

	public static SimpleManager getInstance() {
		return new SimpleManager();
	}

	public SimpleManager() {
		Log.d(LOGTAG, "Connecting to service" + REMOTE_SERVICE_NAME);
		this.service = ISimpleManager.Stub.asInterface(ServiceManager.getService(REMOTE_SERVICE_NAME));
		if (this.service == null) {
			throw new IllegalStateException("Service not found");
		}
	}

	public int addInts(int a, int b) {
		try {
			return this.service.addInts(a, b);
		} catch (RemoteException e) {
			throw new RuntimeException("addInts", e);
		}
	}

	public String echoString(String s) {
		try {
			return this.service.echoString(s);
		} catch (RemoteException e) {
			throw new RuntimeException("echoString", e);
		}
	}
}

