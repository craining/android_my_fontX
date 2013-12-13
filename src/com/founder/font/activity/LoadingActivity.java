package com.founder.font.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.founder.font.Debug;
import com.founder.font.FontCoolApplication;
import com.founder.font.R;
import com.founder.font.bean.Font;
import com.founder.font.interfaces.FontCoolObserver;
import com.founder.font.logic.FontCoolController;
import com.founder.font.util.NetworkUtil;

/**
 * loading页逻辑：
 * 
 * 1、每次启动时的例行检测：是否初次安装，是否需要恢复备份
 * 
 * 2、联网检测版本：程序是否到期
 * 
 * 3、联网更新字体下载人数
 * 
 * @author zhuanggy
 * 
 */
public class LoadingActivity extends Activity {

	private static final String TAG = "LoadingActivity";
	private FontCoolController mController;
	private TextView mTextTip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loading);
		mTextTip = (TextView) findViewById(R.id.text_tip);

		mController = FontCoolController.getInstance();
		checkFirstInstallState();
	}

	/**
	 * 启动时的必要检测，是否是初次安装，若初次启动，则判断是否需要恢复数据，恢复数据等操作
	 */
	private void checkFirstInstallState() {
		mController.checkFirstInstalled(LoadingActivity.this, R.raw.set,
				R.raw.set_hff, R.raw.set_yff, mObserver);
		Debug.v(TAG, "check FirstInstalled");
		mTextTip.setText("check first install ...");
	}

	/**
	 * 版本检测，是否到期等
	 * 
	 */
	private void checkVersion(String version) {
		mTextTip.setText("check version ...");
		if ("0".equals(version)) {
			mController.checkVersion(mObserver);
			Debug.v(TAG, "checkVersion start!");
		} else {
			// 无需检测版本，更新下载人数
			updateUsersCount();
		}
	}

	/**
	 * 更新下载人数
	 */
	private void updateUsersCount() {
		mController.refreshFontUsersCount(mObserver);
		Debug.v(TAG, "load users start!");
		mTextTip.setText("update users count...");
	}

	/**
	 * 进入主页
	 */
	private void jumpToMainActivity() {
		startActivity(new Intent(LoadingActivity.this, MainActivity.class));
		finish();
	}

	/**
	 * 异步操作的回调，注意此回调回来仍是在异步线程里，若操作与UI相关，需要在UI线程里执行runOnUiThread或发handler消息
	 */
	private FontCoolObserver mObserver = new FontCoolObserver() {

		@Override
		public void checkFirstInstalledFinished(final boolean result,
				final boolean isFirstInstalled, final boolean noDataBackup,
				final boolean dataBackupResult, final String version) {
			super.checkFirstInstalledFinished(result, isFirstInstalled,
					noDataBackup, dataBackupResult, version);
			// 启动时检测结束，进而联网检测版本

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (result) {
						if (noDataBackup) {
							// 没有恢复备份
						} else {
							if (dataBackupResult) {
								// 恢复备份成功
							} else {
								// 恢复备份失败
							}
						}
						// 检测网络
						if (NetworkUtil.isNetworkAvailable(FontCoolApplication
								.getInstance())) {
							checkVersion(version);// 检测版本是否过期
						} else {
							jumpToMainActivity();
							Toast.makeText(LoadingActivity.this, "无网络",
									Toast.LENGTH_LONG).show();
						}

					} else {
						// 初始化失败
						mTextTip.setText("check first install failed");
					}

				}
			});

		}

		@Override
		public void checkVersionFinished(final boolean result,
				final boolean stateIn) {
			super.checkVersionFinished(result, stateIn);
			// 版本检测完成，进而更新users count

			Debug.v(TAG, "checkVersion finished!");
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (result) {
						if (!stateIn) {
							// TODO
							String title = "系统提示";
							String message = "当前客户端已过期，请重新购买！";

							return;
						}
					}

					updateUsersCount();// 检测成功，程序未到期，进而更新users count
				}
			});

		}

		@Override
		public void getFontUsersFinished(boolean result, ArrayList<Font> fonts) {
			super.getFontUsersFinished(result, fonts);
			// 更新users count 完成，进而跳转主页面

			if (result) {
				Debug.v(TAG, "下载人数更新成功！");
			} else {
				Debug.v(TAG, "下载人数更新失败！");
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					jumpToMainActivity();// 跳转主页
				}
			});
		}

	};
}
