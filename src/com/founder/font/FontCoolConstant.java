package com.founder.font;

import java.io.File;

import android.os.Environment;

public class FontCoolConstant {

	// 限免期限
	public static final String Limit_Date = "20121231";
	// 限免标识
	public static final int Limit_Tag = 2;

	public static final String SERVICE_NAME = "com.founder.font.service.TestFontService";

	private static final String FILE_PATH_IN_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/founderfont/";

	// 数据库备份文件
	public static final File BACKUP_DB_SDC_PATH = new File(FILE_PATH_IN_SDCARD + "/dataBackup.dat");
	// 许可协议文件
	public static final String FILE_NAME_ULA = "fonts/ULA.txt";
	public static final String FILE_NAME_FONT_DEF = "fonts/fzcool.mp3";
	// 字体索引
	public static String font[][] = new String[95][3];
	// 默认主界面展示数
	public static int FREE_FONT_DEF_COUNT = 5;// 39

	// 系统默认字体文件
	public static final String FILE_NAME_SYSTEM_FONT_ZH = "/system/fonts/DroidSansFallback.ttf";//中文
	public static final String FILE_NAME_SYSTEM_FONT_EN = "/system/fonts/DroidSans.ttf";//英文
	public static final String FILE_NAME_SYSTEM_FONT_ = "/system/fonts/Roboto-Regular.ttf";
	

	// sd卡下ttf等文件路径
	public static String FONT_FILE_PATH_INSDACRD = FILE_PATH_IN_SDCARD + "/Fstore";
	// sd卡下ttf等文件路径
	public static String FONT_FILE_PATH_INSDACRD_UNZIP = FILE_PATH_IN_SDCARD + "/FstoreUnZip";

	// sd卡下ttf等文件路径
	public static String DEF_FONT_FILE_PATH_INSDACRD = FILE_PATH_IN_SDCARD + "/Fstore/fzcool.mp3";

	public static String getFileRootInSdcard() {
		File file = new File(FILE_PATH_IN_SDCARD);
		if (!file.exists()) {
			file.mkdirs();
		}
		return FILE_PATH_IN_SDCARD;
	}
}
