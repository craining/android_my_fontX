package com.founder.font.config;

import android.content.Context;

import com.founder.font.FontCoolApplication;
import com.founder.font.FontCoolConstant;
import com.founder.font.util.PreferenceUtil;

/**
 * 配置文件<br/>
 * 通过{@link #getInstance()}获取实例
 * 
 */
public class FontCoolConfig extends PreferenceUtil {

	public static final int EFFECTIVE_SYSTEM = 0;// 应用于系统
	public static final int EFFECTIVE_ANYVIEW = 1;// 应用于anyview

	// 文件名
	private static final String PREFERENCE_NAME = "fontcoll_config";
	private static FontCoolConfig instance;
	// 各个配置参数
	private static final String IS_AGREE_ULA = "agree_ula";// 许可协议是否接受
	private static final String IS_UPLOAD_FIRSTSTART_INFO = "upload_firststart_info";// 装机信息是否上传
	private static final String MAIN_FONT_SHOW_COUNT = "main_fount_showcount";// 主界面展示数
	private static final String FONT_EFFECTIVE_TYPE = "font_effective_type";// 字体应用类型：系统或anyview

	// 购买字体时存储的数据，暂不明白
	private static final String FONT_BUY_SUCCESS = "buy_success";
	private static final String FONT_BUY_CURRENT_ID = "buy_font_id";
	private static final String FONT_BUY_TIME = "buy_time";

	private FontCoolConfig() {
		super(PREFERENCE_NAME);
	}

	public static FontCoolConfig getInstance() {
		if (instance == null) {
			instance = new FontCoolConfig();
		}
		return instance;
	}

	@Override
	protected Context getContext() {
		return FontCoolApplication.getInstance();
	}

	public boolean isAgreeUla() {
		return getBool(IS_AGREE_ULA, false);
	}

	public void setIsAgreeUla(boolean state) {
		putBool(IS_AGREE_ULA, state);
	}

	public boolean isUploadFirstStartInfo() {
		return getBool(IS_UPLOAD_FIRSTSTART_INFO, false);
	}

	public void setIsUploadFirstStartInfo(boolean state) {
		putBool(IS_UPLOAD_FIRSTSTART_INFO, state);
	}

	public int getMainFontShowCount() {
		return getInt(MAIN_FONT_SHOW_COUNT,
				FontCoolConstant.FREE_FONT_DEF_COUNT);
	}

	public void setMainFontShowCount(int count) {
		putInt(MAIN_FONT_SHOW_COUNT, count);
	}

	public int getFontEffectiveType() {
		return getInt(FONT_EFFECTIVE_TYPE, EFFECTIVE_SYSTEM);
	}

	public void setFontEffectiveType(int type) {
		putInt(FONT_EFFECTIVE_TYPE, type);
	}

	public boolean isFontBuySuccess() {
		return getBool(FONT_BUY_SUCCESS, false);
	}

	public void setIsFontBuySuccess(boolean result) {
		putBool(FONT_BUY_SUCCESS, result);
	}

	public int getFontBuyId() {
		return getInt(FONT_BUY_CURRENT_ID, 0);
	}

	public void setFontBuyId(int id) {
		putInt(FONT_BUY_CURRENT_ID, id);
	}

	public long getFontBuyTime() {
		return getLong(FONT_BUY_TIME, 0);
	}

	public void setFontBuyTime(long time) {
		putLong(FONT_BUY_TIME, time);
	}

}
