package com.founder.font.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Message;

import com.founder.font.FontCoolConstant;
import com.founder.font.bean.Font;
import com.founder.font.db.DbOpera;
import com.founder.font.interfaces.FontDownloadObserver;

public class FontDownloadManager {

	
	private static final String DOWN_LOAD_URL = "http://diy.foundertype.com/eFont/fontAndroid0/";
	
	// 下载中通知更新的文件块数,下载时是按每块1024读取的
	public final static int Download_Block = 50;
	
	
	private ArrayList<FontDownloadObserver> mObservers;// 所有监听
	private HashMap<Integer, Font> mFontsToDownload;// 所有要下载的或暂停下载的字体
	private static FontDownloadManager instance;
	private DbOpera mDbOpera;// 数据库操作实例

	private FontDownloadManager() {
		mFontsToDownload = new HashMap<Integer, Font>();
		mDbOpera = DbOpera.getInstance();
	}

	public static FontDownloadManager getInstance() {
		if (instance == null) {
			instance = new FontDownloadManager();
		}
		return instance;
	}

	/**
	 * 设置下载状态监听
	 * 
	 * @param observer
	 */
	public void addDownloadObserver(FontDownloadObserver observer) {
		if (mObservers == null) {
			mObservers = new ArrayList<FontDownloadObserver>();
		}
		mObservers.add(observer);
	}

	/**
	 * 移除下载状态监听
	 * 
	 * @param observer
	 */
	public void removeDownloadObserver(FontDownloadObserver observer) {
		if (mObservers != null) {
			mObservers.remove(observer);
		}
	}

	private void removeDownloadFont(Font f) {
		mFontsToDownload.remove(f.id);
	}

	public void startDownloadFont(final Font f) {
		if (!mFontsToDownload.containsKey(f.id)) {
			mFontsToDownload.put(f.id, f);
			// 出触发下载
			final DownLoad dl = new DownLoad(mHandler, mDbOpera);
			
			dl.setFileDir(FontCoolConstant.FONT_FILE_PATH_INSDACRD_UNZIP + "/");
			dl.setFileName(f.path);
			// 设置线程数
			dl.setPoolSize(1);
			// 下载线程启动
			new Thread()
			{
				public void run()
				{
					dl.downLoad(DOWN_LOAD_URL + f.path, f.id);
				}
			}.start();
			
			
			
			
			// 回调
			onDownloadStart(f.id);
		}
	
	}

	public void pauseDownloadFont(Font f) {
		
		if(mFontsToDownload.containsKey(f.id)) {
			mFontsToDownload.get(f.id).installState = Font.STATE_PAUSE;
			// 回调
			onDownloadPause(f.id);
		}
	}

	/**
	 * 读取缓存里的下载进度
	 * 
	 * @param f
	 * @return
	 */
	public int getFontDownloadProgress(Font f) {
		if (mFontsToDownload.containsKey(f.id)) {
			Font font = mFontsToDownload.get(f.id);
			if (0 != font.size) {
				return font.downloadSize * 100 / font.size;
			}
		}
		return 0;
	}
	
	/**
	 * 获得下载状态
	 * @param fontId
	 * @return
	 */
	public int getFontInstallState(int fontId) {
		if(mFontsToDownload.containsKey(fontId)) {
			return mFontsToDownload.get(fontId).installState;
		} else {
			return Font.STATE_PAUSE;
		}
	}

	/**
	 * 下载过程中更新进度
	 * 
	 * @param fontId
	 * @param downloadSize
	 */
	private void updateFontDownloadSize(int fontId, int downloadedSize,
			int allSize) {
		if (mFontsToDownload.containsKey(fontId)) {
			mFontsToDownload.get(fontId).downloadSize = downloadedSize;
			onDownloading(fontId, downloadedSize * 100 / allSize);// 通知更新
			// TODO update db
			// mDbOpera.

		}
	}

	public static final int MSG_DOWNLOAD_UPDATE = 0x200;
	public static final int MSG_DOWNLOAD_ERROR = 0x201;
	public static final int MSG_DOWNLOAD_FINISHED = 0x202;
	public static final int MSG_DOWNLOAD_START = 0x203;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_DOWNLOAD_START:
//				TODOmsg.arg1为新的字体包的大小，更新
				
				break;
			case MSG_DOWNLOAD_UPDATE:
//				updateFontDownloadSize();
				
				
				
				
				
			 break;
			case MSG_DOWNLOAD_ERROR:

				break;
			case MSG_DOWNLOAD_FINISHED:

				break;
			default:
				break;
			}

		}
	};

	/******* 回调 *****/
	private void onDownloadStart(int fontId) {
		if (mObservers != null) {
			for (FontDownloadObserver observer : mObservers) {
				observer.onDownloadStart(fontId);
			}
		}
	}

	private void onDownloadPause(int fontId) {
		if (mObservers != null) {
			for (FontDownloadObserver observer : mObservers) {
				observer.onDownloadPause(fontId);
			}
		}
	}

	private void onDownloading(int fontId, int progress) {
		if (mObservers != null) {
			for (FontDownloadObserver observer : mObservers) {
				observer.onDownloading(fontId, progress);
			}
		}
	}

	private void onDownloadFinished(int fontId) {
		if (mObservers != null) {
			for (FontDownloadObserver observer : mObservers) {
				observer.onDownloadFinished(fontId);
			}
		}
	}

	private void onDownloadFailed(int fontId) {
		if (mObservers != null) {
			for (FontDownloadObserver observer : mObservers) {
				observer.onDownloadFailed(fontId);
			}
		}
	}

}
