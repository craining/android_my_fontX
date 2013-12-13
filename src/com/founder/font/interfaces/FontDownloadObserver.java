package com.founder.font.interfaces;

/**
 * 字体下载过程中的回调
 * @author zhuanggy
 *
 */
public class FontDownloadObserver {

	public void onDownloadStart(int fontId) {

	}
	public void onDownloadPause(int fontId) {

	}

	public void onDownloading(int fontId, int progress) {

	}

	public void onDownloadFinished(int fontId) {

	}

	public void onDownloadFailed(int fontId) {

	}
}
