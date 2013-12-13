package com.founder.font.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.founder.font.Debug;
import com.founder.font.FontCoolApplication;
import com.founder.font.FontCoolConstant;
import com.founder.font.util.SQLiteHelper;

public class DbHelper extends SQLiteHelper {

	private static final String TAG = "DbHelper";
	
	private static final String DB_NAME = "fontlifetime.db";
	private static final int DB_VERSION = 4;// 1.5数据库版本为1,1.6数据库版本为2,1.6.1数据库版本为3

	private static String CREATE_TB_FONT;
	private static String DROP_TB_FONT = "DROP TABLE IF EXISTS "
			+ Columns.Tb_Font.TB_NAME;

	private static String CREATE_TB_WEIBO;
	private static String DROP_TB_WEIBO = "DROP TABLE IF EXISTS "
			+ Columns.Tb_Weibo.TB_NAME;

	private static String CREATE_TB_VERSION;
	private static String DROP_TB_VERSION = "DROP TABLE IF EXISTS "
			+ Columns.Tb_Version.TB_NAME;

	public DbHelper() {
		super(FontCoolApplication.getInstance(), getDbName(), null, DB_VERSION);
	}

	private static String getDbName() {
		if (Debug.DB_SAVE_SDCARD) {
			return FontCoolConstant.getFileRootInSdcard() + DB_NAME;
		} else {
			return DB_NAME;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Debug.v(TAG, "onCreate");
		initCreateSql();
		Log.v(TAG, "CREATE_TB_FONT=" + CREATE_TB_FONT);
		Log.v(TAG, "CREATE_TB_WEIBO=" + CREATE_TB_WEIBO);
		Log.v(TAG, "CREATE_TB_VERSION=" + CREATE_TB_VERSION);
		db.execSQL(CREATE_TB_FONT);
		db.execSQL(CREATE_TB_WEIBO);
		db.execSQL(CREATE_TB_VERSION);
		Debug.v(TAG, "onCreate finish");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TB_FONT);
		db.execSQL(DROP_TB_WEIBO);
		db.execSQL(DROP_TB_VERSION);
		onCreate(db);
	}

	private void initCreateSql() {
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Columns.Tb_Font.TB_NAME).append(" (")

		.append(Columns.Tb_Font.ID).append(" INTEGER UNIQUE, ")

		.append(Columns.Tb_Font.BLOCK).append(" TEXT, ")

		.append(Columns.Tb_Font.KEY).append(" TEXT, ")

		.append(Columns.Tb_Font.BUY_TIME).append(" TEXT, ")

		.append(Columns.Tb_Font.LIFE_TIME).append(" TEXT, ")

		.append(Columns.Tb_Font.CPID).append(" TEXT, ")

		.append(Columns.Tb_Font.IS_FREE).append(" text not null, ")

		.append(Columns.Tb_Font.INSTALL_STATE).append(" text not null, ")

		.append(Columns.Tb_Font.NEWFONT).append(" text not null, ")

		.append(Columns.Tb_Font.PATH).append(" text not null, ")

		.append(Columns.Tb_Font.FONT_SET).append(" text, ")

		.append(Columns.Tb_Font.NAME_CODE).append(" text, ")

		.append(Columns.Tb_Font.SORT).append(" integer, ")

		.append(Columns.Tb_Font.DOWNLOAD_SIZE).append(" integer, ")

		.append(Columns.Tb_Font.SIZE).append(" integer, ")

		.append(Columns.Tb_Font.NAME_SHOW).append(" text, ")

		.append(Columns.Tb_Font.USERS).append(" integer);");

		CREATE_TB_FONT = sb.toString();

		// 建立版本信息表
		StringBuffer sb2 = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb2.append(Columns.Tb_Version.TB_NAME).append(" (")

		.append(Columns.Tb_Version.ID_VERSION).append(" INTEGER UNIQUE, ")

		.append(Columns.Tb_Version.CPID).append(" TEXT, ")

		.append(Columns.Tb_Version.VERSION).append(" text not null);");

		CREATE_TB_VERSION = sb2.toString();

		// 建立微博绑定管理表
		StringBuffer sb3 = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb3.append(Columns.Tb_Weibo.TB_NAME).append(" (")

		.append(Columns.Tb_Weibo.PLATFORM).append(" INTEGER UNIQUE, ")

		.append(Columns.Tb_Weibo.USER).append(" TEXT, ")

		.append(Columns.Tb_Weibo.LIFETIME).append(" text);");

		CREATE_TB_WEIBO = sb3.toString();
	}

}
