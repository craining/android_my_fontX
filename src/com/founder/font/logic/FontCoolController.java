package com.founder.font.logic;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.founder.font.Debug;
import com.founder.font.FontCoolApplication;
import com.founder.font.FontCoolConstant;
import com.founder.font.bean.Font;
import com.founder.font.db.Columns;
import com.founder.font.db.DbOpera;
import com.founder.font.interfaces.FontCoolObserver;
import com.founder.font.response.CheckNewVersionResponse;
import com.founder.font.response.CheckVersionResponse;
import com.founder.font.response.GetFontLimitStateResponse;
import com.founder.font.response.GetFontUsersResponse;
import com.founder.font.response.UploadFirstStartInfoResponse;
import com.founder.font.util.AppUtil;
import com.founder.font.util.RequestUtil;
import com.founder.font.util.ServiceUtil;
import com.founder.font.util.TimeUtil;
import com.founder.font.util.XmlUtil;

public class FontCoolController {

	private static FontCoolController mController;

	private FontCoolController() {
	}

	public static FontCoolController getInstance() {
		if (mController == null) {
			mController = new FontCoolController();
		}
		return mController;
	}

	/**
	 * 初次安装，解析xml，创建数据库，写入xml里的数据
	 */
	public void checkFirstInstalled(final Context context, final int resSetId,
			final int resSetHffId, final int resSetYffId,
			final FontCoolObserver listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DbOpera db = DbOpera.getInstance();
				String version = db.getVersion();
				try {
					if (!ServiceUtil.isServiceStarted(context,
							FontCoolConstant.SERVICE_NAME)) {
						//TODO 第一次安装启动的检测条件有问题，可判断sharePre里的字段
						
						Debug.v("", "第一次启动，解析预置xml");
						XmlUtil.parseXml(context, resSetId);// 第一次启动，解析xml配置文件，添加到数据库中

						if ("1".equals(version)) {
							Debug.v("", "version == 1");
							XmlUtil.parseXml(context, resSetHffId);
						} else if ("0".equals(version)) {
							Debug.v("", "version == 0");
							XmlUtil.parseXml(context, resSetYffId);
						}

						// 判断是哪种安装 分别为覆盖安装，卸载后安装，新安装

						// 如果存在备份文件，则恢复。
						if (FontCoolConstant.BACKUP_DB_SDC_PATH.exists()) {
							// 恢复备份
							try {
								// 将备份的数据库内容写到新数据库中
								ObjectInputStream ois = new ObjectInputStream(
										new FileInputStream(
												FontCoolConstant.BACKUP_DB_SDC_PATH));
								ArrayList<HashMap<String, Object>> oldData = null;
								oldData = (ArrayList<HashMap<String, Object>>) ois
										.readObject();
								ois.close();
								if (oldData == null) {
									return;
								}
								for (int i = 0; i < oldData.size(); i++) {
									// 更新新数据库
									Font f = new Font();
									f.id = (Integer) oldData.get(i).get(
											Columns.Tb_Font.ID);
									f.key = (String) oldData.get(i).get(
											Columns.Tb_Font.KEY);
									f.buyTime = (String) oldData.get(i).get(
											Columns.Tb_Font.BUY_TIME);
									f.lifeTime = (String) oldData.get(i).get(
											Columns.Tb_Font.LIFE_TIME);
									f.sort = (Integer) oldData.get(i).get(
											Columns.Tb_Font.SORT);
									f.installState = Integer.parseInt((String) oldData.get(i)
											.get(Columns.Tb_Font.INSTALL_STATE));
									db.backupImportUpdateFont(f);
								}
								listener.checkFirstInstalledFinished(true,
										true, false, true, version);
							} catch (Exception e) {
								e.printStackTrace();
								listener.checkFirstInstalledFinished(true,
										true, false, false, version);
							}
						} else {
							// 当前是新安装，且数据未恢复
							listener.checkFirstInstalledFinished(true, true,
									true, false, version);
						}

					} else {
						// 当前是新安装，且数据未恢复
						listener.checkFirstInstalledFinished(true, false,
								false, false, version);
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.checkFirstInstalledFinished(false, false, false,
							false, version);
				}
			}
		}).start();
	}

	/**
	 * 版本验证
	 * 
	 * @return
	 */
	public void checkVersion(final FontCoolObserver listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					CheckVersionResponse response = RequestUtil.getInstance()
							.checkVersion();

					if (response != null) {
						if (response.getStateIn()) {
							listener.checkVersionFinished(true, true);
						} else {
							listener.checkVersionFinished(true, false);
						}
					} else {
						listener.checkVersionFinished(false, false);
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.checkVersionFinished(false, false);
				}
			}
		}).start();

	}

	/**
	 * 获取服务器上字体列表的下载人数
	 * 
	 * @return
	 */
	public void refreshFontUsersCount(final FontCoolObserver listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					GetFontUsersResponse response = RequestUtil.getInstance()
							.getFontUsers();

					if (response != null && response.getResult() != null) {
						// refresh database
						DbOpera db = DbOpera.getInstance();
						ArrayList<Font> fonts = response.getResult();
						for (Font f : fonts) {
							db.updateFontUsers(f);
						}
						// call back
						listener.getFontUsersFinished(true, fonts);
					} else {
						listener.getFontUsersFinished(false, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.getFontUsersFinished(false, null);
				}
			}
		}).start();

	}

	/**
	 * 第一次启动上传装机信息
	 * 
	 * @param listener
	 * @param sys
	 * @param mbd
	 * @param time
	 * @param ptype
	 * @param clientSW
	 */
	public void uploadFirstStartInfo(final FontCoolObserver listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					TelephonyManager telephonyManager = (TelephonyManager) FontCoolApplication
							.getInstance().getSystemService(
									Context.TELEPHONY_SERVICE);// 获取当前手机管理器
					String imei = telephonyManager.getDeviceId();
					imei = imei.substring(0, imei.length() - 1) + "1";

					String release = android.os.Build.VERSION.RELEASE;
					String model = android.os.Build.MODEL;
					String telName = android.os.Build.MANUFACTURER;

					String strCPId = DbOpera.getInstance().getVersionCpid(100);
					// 第一次安装 上行参数上传

					String sys = ("ADR" + release).substring(0, 6);
					String time = TimeUtil.getCurrentTimeStringToUpload();
					String mbd = imei + strCPId;
					String ptype = telName + " " + model;
					String clientSW = AppUtil.getAPKVersion(FontCoolApplication
							.getInstance());

					Debug.v("uploadFirstStartInfo", "time=" + time);
					Debug.v("uploadFirstStartInfo", "sys=" + sys);
					Debug.v("uploadFirstStartInfo", "mbd=" + mbd);
					Debug.v("uploadFirstStartInfo", "ptype=" + ptype);
					Debug.v("uploadFirstStartInfo", "clientSW=" + clientSW);

					UploadFirstStartInfoResponse response = RequestUtil
							.getInstance().uploadFirstStartInfo(sys, mbd, time,
									ptype, clientSW);

					if (response != null && response.getResult()) {
						// call back
						listener.uploadFirstStartInfoFinished(true);
					} else {
						listener.uploadFirstStartInfoFinished(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.uploadFirstStartInfoFinished(false);
				}
			}
		}).start();

	}

	/**
	 * 检测新版本
	 * 
	 * @return
	 */
	public void checkNewVersion(final FontCoolObserver listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					CheckNewVersionResponse response = RequestUtil
							.getInstance().checkNewVersion(
									AppUtil.getAPKVersion(FontCoolApplication
											.getInstance()));

					if (response != null) {
						// call back
						if (response.getHasNew()) {
							listener.checkNewVersionFinished(true, true,
									response.getDownLoadUrl());
						} else {
							listener.checkNewVersionFinished(true, false, null);
						}

					} else {
						listener.checkNewVersionFinished(false, false, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.checkNewVersionFinished(false, false, null);
				}
			}
		}).start();

	}
	
	/**
	 * 获得某个付费字体是否在有效期内
	 * 
	 * @return
	 */
	public void getFontLimitState(final FontCoolObserver listener, final int fontId) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					GetFontLimitStateResponse response = RequestUtil
							.getInstance().getFontLimitState(fontId);

					if (response != null) {
						// call back
						listener.getFontLimitStateFinished(true, response.getState());
					} else {
						listener.getFontLimitStateFinished(false, -1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.getFontLimitStateFinished(false, -1);
				}
			}
		}).start();

	}

	// /////数据库的异步操作//////

	/**
	 * 读取数据库的所有字体
	 * 
	 * @return
	 */
	public void getAllFonts(final FontCoolObserver listener) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ArrayList<Font> fontList = new ArrayList<Font>();
					DbOpera db = DbOpera.getInstance();
					fontList = db.getAllFont();

					listener.getAllFontsFinished(true, fontList);
				} catch (Exception e) {
					e.printStackTrace();
					listener.getAllFontsFinished(false, null);
				}

			}
		}).start();

	}
}
