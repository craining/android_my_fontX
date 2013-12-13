package com.founder.font;

import com.founder.font.db.DbOpera;

import android.app.Application;

public class FontCoolApplication extends Application {

	private static final String TAG = "FontCoolApplication";
	private static FontCoolApplication application;
	
	
	@Override
	public void onCreate() {
		Debug.e(TAG, "onCreate");
		application = this;
		
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	
	public static FontCoolApplication getInstance() {
		return application;
	}
	
}
