package com.founder.font.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.founder.font.FontCoolConstant;
import com.founder.font.R;
import com.founder.font.bean.Font;
import com.founder.font.config.FontCoolConfig;
import com.founder.font.factory.BuyFactory;
import com.founder.font.interfaces.FontCoolObserver;
import com.founder.font.interfaces.FontDownloadObserver;
import com.founder.font.logic.FontCoolController;
import com.founder.font.response.GetFontLimitStateResponse;
import com.founder.font.util.FontDownloadManager;
import com.founder.font.util.SoundManager;
import com.founder.font.util.TimeUtil;

public class PopViewMain {
	// private DbOpera dao;
	// 字体id
	public int nFontID = 0;
	// 是否免费
//	private int isFree;
	// 上下文
	public Activity ctx;
	// button
	private ImageButton mStatusBut = null;
	// buyfatory对象
	public BuyFactory mBuyFactory = null;

	public int buy_ResourceID = 0;// drawable下购买图片的资源Id
	public int download_ResourceID = 0;// drawable下下载图片的资源Id
	public int use_ResourceID = 0;// drawable下使用图片的资源Id
	public int using_ResourceID = 0;// drawable下使用中图片的资源Id
	public int pause_ResourceID = 0;// drawable下暂停图片的资源id

	private ProgressBar mProgressBar = null;
	private FontDownloadManager mFontDownloadManager;
	private Font mFont;

	/**
	 * @brief PopView的构造函数
	 */
	public PopViewMain(final Activity context, final ImageButton imageBut, ProgressBar progressBar, FontDownloadManager manager) {
		ctx = context;
		mStatusBut = imageBut;
		mProgressBar = progressBar;
		mFontDownloadManager = manager;
		
		mStatusBut.setOnClickListener(mOnClickListener);
		// 加载数据资源
		loadDataResource();
	}

	/**
	 * @brief 加载资源数据
	 */
	private void loadDataResource() {
		// 主界面
		buy_ResourceID = R.drawable.main_buy;
		download_ResourceID = R.drawable.main_download;
		use_ResourceID = R.drawable.main_use;
		using_ResourceID = R.drawable.main_using;
		pause_ResourceID = R.drawable.main_pause;

		// 设置图片按钮的背景图片资源，实现按钮点击特殊效果
		mStatusBut.setBackgroundResource(R.drawable.main_imagebut_selector);
	}

	/**
	 * @brief 根据字体状态，显示不同view,并设置监听事件
	 * @author $Author:LiuFang
	 * @since 1.0.0.0
	 * @version 1.6.0.0
	 * @param nCurFontID
	 *            字体ID
	 */
	public void show(Font f) {
		this.mFont = f;
		this.mBuyFactory = new BuyFactory(ctx, f);
		
		int nProgress = mFontDownloadManager.getFontDownloadProgress(mFont);
		if (nProgress <= 50) {
			nProgress = (int) Math.sqrt(nProgress * (100 - nProgress));
		}
		mProgressBar.setProgress(nProgress);

		int state = mFont.installState;
		switch (state) {
		// 状态： 购买
		case Font.STATE_BUY:

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss",
					Locale.CHINA);
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String currentTime = formatter.format(curDate);
			long longstr1 = Long.valueOf(currentTime);
			boolean isSu = FontCoolConfig.getInstance().isFontBuySuccess();
			int fontID = FontCoolConfig.getInstance().getFontBuyId();
			long saveTime = FontCoolConfig.getInstance().getFontBuyTime();
			// 如果用户在购买之后因为短信延迟缘故未收到短信，则在十分钟内购买状态暂时变为安装
			if ((longstr1 - saveTime < 500) && !isSu && (nFontID == fontID)) {
				mStatusBut.setImageDrawable(ctx.getResources().getDrawable(
						download_ResourceID));
				// mStatusBut.setImageResource(download_ResourceID);
			} else {
				mStatusBut.setImageDrawable(ctx.getResources().getDrawable(
						buy_ResourceID));
				// mStatusBut.setImageResource(buy_ResourceID);
			}

			break;
		// 状态：下载
		case Font.STATE_INSTALL:
			// 如果状态为“下载”，查找当前是否已有字体文件，如有则不下载，直接状态改为“使用”
			// 查找字体文件对应ttf文件名
			String strFontPath = mFont.path;
			strFontPath = strFontPath.replace(".zip", ".ttf");
			strFontPath = FontCoolConstant.FONT_FILE_PATH_INSDACRD + "/"
					+ strFontPath;
			File fileCurFont = new File(strFontPath);
			if (fileCurFont.exists()) {
				// TODO 改为使用
				mFont.installState = Font.STATE_CHANGE;
				// dao.updateInstall(nCurFontID);更新数据库
				show(mFont);
			} else {
				mStatusBut.setImageResource(download_ResourceID);
			}
			break;
		// 状态：使用，下载完成
		case Font.STATE_CHANGE:

			// 主界面处理流程
			String fontSize = String.valueOf(mFont.size / 10000 / 100.0) + "MB";
			String fontUsers = mFont.users + "人下载";
			// MainView.setFontExhibitTitle(fontSize, fontUsers);
			// TODO 更新主页UI
			mProgressBar.setProgress(100);

			mStatusBut.setImageResource(use_ResourceID);

			break;
		// 状态：使用中
		case Font.STATE_USING:

			mProgressBar.setProgress(100);
			// fontType：1:AnyView:换字;否则：系统换字
			if (FontCoolConfig.getInstance().getFontEffectiveType() == FontCoolConfig.EFFECTIVE_ANYVIEW) {
				mStatusBut.setImageResource(use_ResourceID);

			} else {
				mStatusBut.setImageResource(using_ResourceID);
				// “使用中”状态时，去除背景资源
				mStatusBut.setBackgroundResource(0);

			}

			break;
		// 状态：下载中
		case Font.STATE_DOWNLOADING:

			// 设置进度信息
			String downloadProgress = null;
			if (nProgress <= 100) {
				downloadProgress = nProgress + "%";
			} else {
				downloadProgress = "100%";
			}
			String downloadState = "下载中";

			// 主界面处理流程
			// MainView.setFontExhibitTitle(downloadState, downloadProgress);
			//
			mStatusBut.setImageResource(pause_ResourceID);

			break;
		// 状态：下载暂停
		case Font.STATE_PAUSE:

			// 设置进度信息
			String pauseProgress = null;
			if (nProgress <= 100) {
				pauseProgress = nProgress + "%";
			} else {
				pauseProgress = "100%";
			}
			String pauseState = "暂停中";

			// 主界面处理流程
			// MainView.setFontExhibitTitle(pauseState, pauseProgress);

			mStatusBut.setImageResource(download_ResourceID);

			break;
		}
	}

	/**
	 * @brief: 显示错误提示对话框
	 * @param: ctx 上下文对象
	 * @param: strContent 提示内容
	 */
	private void ShowErrDialog(Context ctx, String strContent) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("系统提示");
		builder.setMessage(strContent);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});

		// 显示对话框
		builder.create().show();
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SoundManager.getInstance().playSound(1, 1);
			switch (mFont.installState) {
			case Font.STATE_BUY:
				// liufang，点击购买后，将当前字体调整为排序首位
				// dao.updateFirstFont(nCurFontID);
				// Const.FREE_FONT_DEF_NUM++;
				// SharedPreferences preferences = ctx.getSharedPreferences(
				// "fontnumupdate", ctx.MODE_PRIVATE);
				// SharedPreferences.Editor editor = preferences.edit();
				// editor.putInt("num", Const.FREE_FONT_DEF_NUM);
				// editor.commit();
				// Iydpay 2013-4-12
				mBuyFactory.buy(mFont.id);
				break;
			case Font.STATE_INSTALL:
				// liufang,20120628,增加限免字体的处理
				// 在安装流程开始前，查询数据库中当前字体isfree属性，如果为限免字体，进入限免字体逻辑
//				if (mFont.isFree == FontCoolConstant.Limit_Tag) {
//					// 属于付费字体
//					// TODO 弹出等待验证
//
//					FontCoolController.getInstance().getFontLimitState(
//							mObserver, mFont.id);
//				} else {
					// 字酷安装流程
					// InstallFactory.showDialog(nFontID, PopViewMain.this,
					// Const.InstallFactory_BeginInstall);
					
					//TODO 检查网络，文件等
					mFontDownloadManager.startDownloadFont(mFont);
//				}

				break;

			case Font.STATE_CHANGE:
				// 换字
				// 点击换字
				// ChangeFactory.showDialog(PopViewMain.this,
				// Const.ChangeFactory_YesOrNo);
				break;
			case Font.STATE_USING:
				if (FontCoolConfig.getInstance().getFontEffectiveType() == FontCoolConfig.EFFECTIVE_SYSTEM) {
					Toast.makeText(ctx, "当前字体正在使用中！", Toast.LENGTH_SHORT)
							.show();
				}

				break;
			case Font.STATE_PAUSE:
				// 检查当前是否处于飞行模式
				int modeIdx = Settings.System.getInt(ctx.getContentResolver(),
						Settings.System.AIRPLANE_MODE_ON, 0);
				// 此处检查手机是否处于飞行状态
				if (modeIdx == 1) {
					ShowErrDialog(ctx, "您的手机正处于飞行模式，无法获取网络数据。");
					return;
				}

				// InstallFactory.showDialog(nFontID, PopViewMain.this,
				// Const.InstallFactory_BeginInstall);
				break;

			case Font.STATE_DOWNLOADING:
				// // 更新数据库中字体状态为暂停
				mFont.installState = Font.STATE_PAUSE;
				// TODO
				// dao.updateFontState(nCurFontID,
				// String.valueOf(Const.PopView_Pause));
				show(mFont);

				break;
			default:
				break;
			}
		}
	};

	private FontCoolObserver mObserver = new FontCoolObserver() {

		@Override
		public void getFontLimitStateFinished(boolean result, int state) {
			super.getFontLimitStateFinished(result, state);

			if (!result) {
				state = TimeUtil.getFontLimitState();
			}
			switch (state) {
			case GetFontLimitStateResponse.FONT_LIMIT_STATE_LIMIT:
				// TODO 如果限免字体超过限免期的，更改按钮状态，不进入安装流程
				mFont.installState = Font.STATE_BUY;
				// TODO 刷新数据库

				// // 更新数据库中字体isfree属性为收费
				// tempDao.updateFreeStatus(nFontID, "1");
				// // 更新数据库中字体installstate为购买
				// tempDao.initFont(nFontID);

				ctx.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						show(mFont);
						ShowErrDialog(ctx, "很抱歉，当前字体已过限免期，请购买后使用！");
					}
				});
				break;
			case GetFontLimitStateResponse.FONT_LIMIT_STATE_OK:
				// 更新数据库中字体installstate为免费
				// tempDao.initFont(nFontID);

				break;
			default:
				break;
			}
		}

	};
}