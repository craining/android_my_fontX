package com.founder.font;

import java.io.File;

import com.founder.font.db.DbOpera;

import android.app.Application;
import android.graphics.Typeface;

public class FontCoolApplication extends Application {

	private static final String TAG = "FontCoolApplication";
	private static FontCoolApplication application;

	// 字体样式对象
	private Typeface mCustomTypeface;

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

	public Typeface getmCustomTypeface() {
		if (mCustomTypeface == null) {
			// 初始化字体
			if (new File(FontCoolConstant.DEF_FONT_FILE_PATH_INSDACRD).exists()) {
				mCustomTypeface = Typeface.createFromFile(FontCoolConstant.DEF_FONT_FILE_PATH_INSDACRD);
			} else {
				mCustomTypeface = Typeface.createFromAsset(this.getAssets(), FontCoolConstant.FILE_NAME_FONT_DEF);
			}
		}
		return mCustomTypeface;
	}
}
