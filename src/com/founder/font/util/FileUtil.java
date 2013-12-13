package com.founder.font.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class FileUtil {
	/**
	 * @brief 读取assets下的txt文件
	 * @param String
	 *            fileName 文件的名字
	 * @return String 文件的内容
	 * */
	public static String getStringFromAssets(Context context, String fileName) {
		String string = null;
		try {
			InputStream inputStream = context.getAssets().open(fileName);
			int size = inputStream.available();
			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();
			string = new String(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}

	/**
	 * 初始化文件目录
	 */
	private static void initFilePath() {
		// File file = new File(sdcsardPath);
		// if (!file.exists()) {
		// file.mkdir();
		// }
		// File fileStore = new File(file, "/Fstore");
		// // 创建文件
		// if (!fileStore.exists()) {
		// fileStore.mkdir();
		// }
		// // 文件路径
		// File fileDownload = new File(Const.fontUnZip + "/");
		// // 创建文件
		// if (!fileDownload.exists()) {
		// fileDownload.mkdir();
		// }
		// // 创建图片文件夹
		// File imageFile = new File(file, "/image");
		// if (!imageFile.exists()) {
		// imageFile.mkdir();
		// }
		//
		// // 创建系统目录下字体文件夹
		// File fontFile = new File(Const.SYSTEM_PATH);
		// if (!fontFile.exists()) {
		// fontFile.mkdir();
		// }
	}

	/**
	 * 
	 * @Description:用浏览器打开Url
	 */
	public static void viewUrl(Context context, String url) {
		try {// 默认系统浏览器
			Uri u = Uri.parse(url);
			Intent it = new Intent();
			it.setAction(Intent.ACTION_VIEW);
			it.setDataAndType(u, "text/html");
			it.setClassName("com.android.browser",
					"com.android.browser.BrowserActivity");
			context.startActivity(it);
		} catch (Exception e) {
			try {// 选择列表打开
				Uri u = Uri.parse(url);
				Intent it = new Intent();
				it.setAction(Intent.ACTION_VIEW);
				it.setDataAndType(u, "text/html");
				context.startActivity(it);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
