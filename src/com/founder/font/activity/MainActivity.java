package com.founder.font.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.founder.font.Debug;
import com.founder.font.FontCoolConstant;
import com.founder.font.R;
import com.founder.font.adapter.StyleImageAdapter;
import com.founder.font.bean.Font;
import com.founder.font.config.FontCoolConfig;
import com.founder.font.interfaces.FontCoolObserver;
import com.founder.font.interfaces.FontDownloadObserver;
import com.founder.font.logic.FontCoolController;
import com.founder.font.util.ActivityManager;
import com.founder.font.util.FileUtil;
import com.founder.font.util.FontDownloadManager;
import com.founder.font.util.ResourceUtil;
import com.founder.font.util.SoundManager;
import com.founder.font.view.PopViewMain;
import com.founder.font.view.StyleGallery;

public class MainActivity extends Activity implements OnClickListener,
		OnItemSelectedListener, OnItemClickListener, OnTouchListener {

	private static final String TAG = "MainActivity";
	private FontCoolController mController;
	private FontDownloadManager mFontDownloadManager;
	private SoundManager mSoundManager;

	// 字体样式对象
	private Typeface mCustomTypeface;
	private Typeface mSystemTypeface;

	private int mMainFontCount;// 主界面展示数

	private StyleImageAdapter mStyleImageAdapter;
	private ArrayList<Font> mArrayListFonts;

	private StyleGallery mStyleGallery;
	private TextView mTextViewFontName = null;

	private LinearLayout mFontInfoLayout = null;
	private TextView mFontSizeView = null;
	private TextView mFontUsersView = null;
	private TextView mContent;
	private ProgressBar mProgressBar = null;
	private ImageButton mSettingBut = null;
	private ImageButton mFontPickerBut = null;

	private PopViewMain mBtnRightTop;

	private int mShowingPosition = 0;// 当前展示的index

	private View mViewLine;
	private static final int MSG_ONITEM_SELECTED = 0x100;// 某个item选中时，延迟发消息刷新UI，防止多次刷新
	private static final int MSG_LINE_HIDE = 0x101;// 延时隐藏刻度
	private static final int LINE_HIDE_DELY = 1000;// 隐藏刻度延时

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ActivityManager.push(MainActivity.this);

		mController = FontCoolController.getInstance();
		mFontDownloadManager = FontDownloadManager.getInstance();
		mFontDownloadManager.addDownloadObserver(mFontDownloadObserver);

		initView();
		init();
	}

	@Override
	protected void onResume() {
		if (mArrayListFonts != null) {
			// 已经读取过数据库了
			mStyleGallery.setSelection(mShowingPosition);
			showSelectedItem();
		}

		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		checkUploadFirstStartInfo();
		checkAgreeULA();

		readDbFonts();

		// 获取要展示的数目
		mMainFontCount = FontCoolConfig.getInstance().getMainFontShowCount();
		// 初始化字体
		mSystemTypeface = Typeface
				.createFromFile(FontCoolConstant.FILE_NAME_SYSTEM_FONT);
		if (new File(FontCoolConstant.DEF_FONT_FILE_PATH_INSDACRD).exists()) {
			mCustomTypeface = Typeface
					.createFromFile(FontCoolConstant.DEF_FONT_FILE_PATH_INSDACRD);
		} else {
			mCustomTypeface = Typeface.createFromAsset(this.getAssets(),
					FontCoolConstant.FILE_NAME_FONT_DEF);
		}
		// 加载声音文件
		soundLoad();
	}

	private void initView() {
		mTextViewFontName = (TextView) findViewById(R.id.main_founder_name_title);
		mFontSizeView = (TextView) findViewById(R.id.main_font_size);
		mFontUsersView = (TextView) findViewById(R.id.main_font_users);
		mFontInfoLayout = (LinearLayout) findViewById(R.id.main_font_info_layout);
		mProgressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
		mSettingBut = (ImageButton) findViewById(R.id.main_setting_but);
		mFontPickerBut = (ImageButton) findViewById(R.id.main_picker_but);
		mStyleGallery = (StyleGallery) findViewById(R.id.main_style_gallery);
		mContent = (TextView) findViewById(R.id.text_mainfont_content);
		mViewLine = (View) findViewById(R.id.style_gallery_item_line_selected);

		mSettingBut.setOnClickListener(this);
		mFontPickerBut.setOnClickListener(this);

		mBtnRightTop = new PopViewMain(MainActivity.this, mFontPickerBut,
				mProgressBar, mFontDownloadManager);

	}

	private void refreshFontList() {
		// 初始化主界面字体列表
		mStyleImageAdapter = new StyleImageAdapter(MainActivity.this,
				mArrayListFonts, mCustomTypeface, mShowingPosition);
		mStyleGallery.setAdapter(mStyleImageAdapter);
		mStyleGallery.setOnTouchListener(this);

		// 设置Gallery在flinging时，避免出现选择的情况
		mStyleGallery.setCallbackDuringFling(false);
		/* 设定一个itemclickListener事件 */
		mStyleGallery.setOnItemSelectedListener(this);
		mStyleGallery.setOnItemClickListener(this);

		// 设置gallery位置
		mStyleGallery.setSelection(mShowingPosition);
		showSelectedItem();

		// 如果服务未启动，则在启动软件时启动服务
		// if (!Tools.isServiceRunning(this, Const.SERVICE_NAME)) {
		// Intent intent_mainView = new Intent("Broadcast_mainView");
		// intent_mainView.putExtra("mainStart", true);
		// sendBroadcast(intent_mainView);
		// }
		// // 软件已启动
		// Tools.saveData(mContext, "mainsave", "isStart", true);

		// Intent intent = getIntent();
		// // 正常启动主界面
		// if (intent == null) {
		// return;
		// }
		// boolean flag = intent.getBooleanExtra("xuFei", false);
		// int i = intent.getIntExtra("fontId", 0);
		// from = intent.getBooleanExtra("fromActToMain", false);
		// isShow = intent.getBooleanExtra("dialogShow", true);

		// // 启动线程，检查是否有字体正在使用，正在使用字体是否到期
		// new Thread() {
		// public void run() {
		// checkDateLine();
		// }
		// }.start();
		//
		// // 如果由续费界面跳转到此，获取要续费的字体id ,并且使其gallery位置被选中
		// if (flag) {
		// for (int j = 0; j < nTotalFontNum; j++) {
		// if ((Integer) this.arrFontList.get(j).get(FontLifeTimeDAO.F_ID) == i)
		// {
		// mStyleGallery.setSelection(j + 1);
		// break;
		// }
		// }
		// // TODO:
		// // buyFactory.showDialog(this.popView, Const.BuyFactory_GetPay,
		// // nCurFontID);
		// intent.removeExtra("xuFei");
		// }

		// // 如果服务未启动，则启动卸载监听的服务（用于有些手机换字之后不能重启，而服务处于未启动状态）
		// if (!Tools.isServiceRunning(this, Const.SERVICE_NAME)) {
		// Intent serverIntent = new Intent(Const.SERVICE_ACT);
		// mContext.startService(serverIntent);
		// }
	}

	/**
	 * 是否需要弹出许可协议
	 */
	private void checkAgreeULA() {
		if (!FontCoolConfig.getInstance().isAgreeUla()) {
			// 弹出许可协议
			String fileContent = FileUtil.getStringFromAssets(
					MainActivity.this, FontCoolConstant.FILE_NAME_ULA);

			AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
			dlg.setTitle(R.string.dialog_ula_title);
			dlg.setMessage(fileContent);
			dlg.setPositiveButton(R.string.agree,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							FontCoolConfig.getInstance().setIsAgreeUla(true);
							// 检测新版本
							checkNewVersion();
						}
					});
			dlg.setNegativeButton(R.string.exit,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							ActivityManager.popAll();
						}
					});
			dlg.create().show();
		} else {
			// 检测新版本
			checkNewVersion();
		}
	}

	/**
	 * 上传装机量信息与否
	 */
	private void checkUploadFirstStartInfo() {
		if (!FontCoolConfig.getInstance().isUploadFirstStartInfo()) {
			mController.uploadFirstStartInfo(mListener);
			Debug.v(TAG, "checkUploadFirstStartInfo start");
		} else {
			Debug.v(TAG, "checkUploadFirstStartInfo , no need");
		}
	}

	/**
	 * 异步读取数据库数据
	 */
	private void readDbFonts() {
		mController.getAllFonts(mListener);
		Debug.v(TAG, "readDbFonts start");
	}

	/**
	 * 检测新版本
	 */
	private void checkNewVersion() {
		mController.checkNewVersion(mListener);
		Debug.v(TAG, "checkNewVersion start");
	}

	/**
	 * 检测到新版本提示
	 */
	private void showNewVersionAlert(final String downloadUrl) {

		AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
		dlg.setTitle(R.string.dialog_newversion_title);
		dlg.setMessage(R.string.dialog_newversion_content);
		dlg.setPositiveButton(R.string.dialog_newversion_btn_p,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 立即更新
						FileUtil.viewUrl(MainActivity.this, downloadUrl);

					}
				});
		dlg.setNegativeButton(R.string.dialog_newversion_btn_n,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dlg.create().show();
	}

	/**
	 * 初始化音频
	 */
	private void soundLoad() {
		// 添加音频特效
		mSoundManager = SoundManager.getInstance();
		mSoundManager.initSounds(this);
		mSoundManager.loadSounds();
	}

	private void showSelectedItem() {

		if (mShowingPosition == mArrayListFonts.size() - 1) {
			// TODO 进入列表页

			return;
		} else {
			mStyleImageAdapter.notifyDataSetChanged(mShowingPosition);
			mBtnRightTop.show(mArrayListFonts.get(mShowingPosition));
			if (mShowingPosition == 0) {
				// 展示系统默认
				mFontInfoLayout.setVisibility(View.GONE);
				mTextViewFontName.setText(R.string.system_def_font);
				mContent.setText(ResourceUtil.getResourceId(MainActivity.this,
						"d000", "string"));
				mContent.setTypeface(mSystemTypeface);
			} else {
				// 展示方正字体
				mFontInfoLayout.setVisibility(View.VISIBLE);
				mTextViewFontName
						.setText(mArrayListFonts.get(mShowingPosition).fontSet);

				mFontSizeView.setText(String.valueOf(mArrayListFonts
						.get(mShowingPosition).size / 10000 / 100.0) + "MB");
				mFontUsersView
						.setText(mArrayListFonts.get(mShowingPosition).users
								+ "");

				mContent.setText(ResourceUtil.getResourceId(MainActivity.this,
						"d" + mArrayListFonts.get(mShowingPosition).id,
						"string"));
				mContent.setTypeface(mCustomTypeface);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_picker_but:
			// 右上角按钮

			break;
		case R.id.main_setting_but:
			// 设置按钮

			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		Debug.v(TAG, "onItemSelected  position=" + position);
		mShowingPosition = position;
		if (mShowingPosition == (mArrayListFonts.size() - 1)) {
			mShowingPosition -= 1;
			// 设置更多字体的前一项为Gallery的焦点
			mStyleGallery.setSelection(mShowingPosition);
		}
		Message msg = new Message();
		msg.what = MSG_ONITEM_SELECTED;
		msg.arg1 = mShowingPosition;
		mHandler.sendMessageDelayed(msg, 140);// 延迟140毫秒
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Debug.v(TAG, "onNothingSelected");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Debug.v(TAG, "onItemClick  position=" + position);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_ONITEM_SELECTED:
				// 延迟后，保证只刷新一次
				if (mShowingPosition == msg.arg1) {
					showSelectedItem();
					mSoundManager.playSound(1, 1);
				}
				break;
			case MSG_LINE_HIDE:
				Debug.v(TAG, " handle MSG_LINE_HIDE");
				mViewLine.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (v.getId() == R.id.main_style_gallery) {
				mViewLine.setVisibility(View.VISIBLE);
				mHandler.removeMessages(MSG_LINE_HIDE);
				Debug.v(TAG, "onTouch main_style_gallery DOWN");
			}
			break;
		case MotionEvent.ACTION_UP:
			if (v.getId() == R.id.main_style_gallery) {
				mHandler.sendEmptyMessageDelayed(MSG_LINE_HIDE, LINE_HIDE_DELY);
				Debug.v(TAG, "onTouch main_style_gallery UP");
			}
			break;

		default:
			break;
		}

		return false;
	}

	/**
	 * 异步操作的回调
	 */
	private FontCoolObserver mListener = new FontCoolObserver() {

		@Override
		public void uploadFirstStartInfoFinished(boolean result) {
			super.uploadFirstStartInfoFinished(result);
			if (result) {
				FontCoolConfig.getInstance().setIsUploadFirstStartInfo(true);
				Debug.v(TAG, "upload info when first start, success!");
			} else {
				Debug.v(TAG, "upload info when first start, failed!");
			}
		}

		@Override
		public void checkNewVersionFinished(boolean result,
				boolean hasNewVersion, final String downloadUrl) {
			super.checkNewVersionFinished(result, hasNewVersion, downloadUrl);
			if (result) {
				if (hasNewVersion) {
					Debug.v(TAG, "checkNewVersionFinished has new version");
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// 检测到新版本
							showNewVersionAlert(downloadUrl);
						}
					});
				} else {
					Debug.v(TAG, "checkNewVersionFinished has no new version");
				}
			} else {
				Debug.v(TAG, "checkNewVersionFinished failed");
			}
		}

		@Override
		public void getAllFontsFinished(boolean result, ArrayList<Font> fonts) {
			super.getAllFontsFinished(result, fonts);

			if (result) {
				// 读取所有字体结束
				mArrayListFonts = fonts;
				mArrayListFonts = new ArrayList<Font>();

				Font system = new Font();
				system.nameCode = getString(R.string.system_font);
				system.installState = Font.STATE_CHANGE;
				mArrayListFonts.add(system);
				mArrayListFonts.addAll(fonts.subList(0, mMainFontCount));
				Font more = new Font();
				more.nameCode = getString(R.string.more);
				mArrayListFonts.add(more);

				Debug.v(TAG, "getAllFontsFinished success");

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						refreshFontList();
					}
				});

			} else {
				Debug.v(TAG, "getAllFontsFinished failed");
			}
		}

	};

	private FontDownloadObserver mFontDownloadObserver = new FontDownloadObserver() {

		@Override
		public void onDownloadStart(int fontId) {
			// TODO Auto-generated method stub
			super.onDownloadStart(fontId);
		}

		@Override
		public void onDownloadPause(int fontId) {
			// TODO Auto-generated method stub
			super.onDownloadPause(fontId);
		}

		@Override
		public void onDownloading(int fontId, final int progress) {
			super.onDownloading(fontId, progress);
			if(fontId == mShowingPosition) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mProgressBar.setProgress(progress);
					}
				});
			}
		}

		@Override
		public void onDownloadFinished(int fontId) {
			// TODO Auto-generated method stub
			super.onDownloadFinished(fontId);
		}

		@Override
		public void onDownloadFailed(int fontId) {
			// TODO Auto-generated method stub
			super.onDownloadFailed(fontId);
		}

	};
}
