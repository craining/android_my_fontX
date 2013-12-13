package com.founder.font.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.founder.font.Debug;
import com.founder.font.bean.Font;

public class DbOpera extends DbHelper {

	private static final String TAG = "DbOpera";

	private DbOpera() {
		super();
	}

	private static String mSyn = "";

	private static DbOpera instances;

	public static DbOpera getInstance() {

		synchronized (mSyn) {
			if (instances == null) {
				instances = new DbOpera();
			}
			return instances;
		}
	}

	/**
	 * 插入一条字体数据
	 * 
	 * @Description:
	 * @param font
	 * @see:
	 */
	public boolean insertFont(Font font, boolean existUpdate) {

		Cursor cursor = null;
		boolean contain = false;
		try {
			// if (exitNotInsertButUpdate) {
			// cursor = query(Columns.Tb_Font.TB_NAME, null, null, null);
			// if (cursor != null && cursor.getCount() > 0) {
			// cursor.moveToFirst();
			//
			// do {
			// int id = cursor.getInt(cursor
			// .getColumnIndex(Columns.Tb_Font.ID));
			// if (id == font.id) {
			// contain = true;
			// Debug.e("insertFont", "already exist");
			// break;
			// }
			// } while (cursor.moveToNext());
			// }
			// }

			ContentValues value = new ContentValues();
			value.put(Columns.Tb_Font.ID, font.id);
			value.put(Columns.Tb_Font.BLOCK, font.block);
			value.put(Columns.Tb_Font.KEY, font.key);
			value.put(Columns.Tb_Font.BUY_TIME, font.buyTime);
			value.put(Columns.Tb_Font.LIFE_TIME, font.lifeTime);
			value.put(Columns.Tb_Font.CPID, font.cpid);
			value.put(Columns.Tb_Font.IS_FREE, font.isFree);
			value.put(Columns.Tb_Font.INSTALL_STATE, font.installState);
			value.put(Columns.Tb_Font.NEWFONT, font.newFont);
			value.put(Columns.Tb_Font.PATH, font.path);
			value.put(Columns.Tb_Font.FONT_SET, font.fontSet);
			value.put(Columns.Tb_Font.NAME_CODE, font.nameCode);
			value.put(Columns.Tb_Font.SORT, font.sort);
			value.put(Columns.Tb_Font.DOWNLOAD_SIZE, font.downloadSize);
			value.put(Columns.Tb_Font.SIZE, font.size);
			value.put(Columns.Tb_Font.NAME_SHOW, font.nameshow);
			value.put(Columns.Tb_Font.USERS, font.users);

			if (existUpdate) {
				Debug.v(TAG, "insertFont-->" + "id=" + font.id + "name="
						+ font.fontSet + "    existUpdate Mode");
				insertOrReplace(Columns.Tb_Font.TB_NAME, value);// 有则替换，可用于数据更新
			} else {
				Debug.v(TAG, "insertFont-->" + "id=" + font.id + "name="
						+ font.fontSet + "    existIgnore Mode");
				insertOrIgnore(Columns.Tb_Font.TB_NAME, value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return false;
	}

	/**
	 * 恢复备份时的更新
	 * 
	 * @Description:
	 * @param font
	 * @see:
	 */
	public boolean backupImportUpdateFont(Font font) {

		Cursor cursor = null;
		try {
			ContentValues value = new ContentValues();
			value.put(Columns.Tb_Font.ID, font.id);
			value.put(Columns.Tb_Font.KEY, font.key);
			value.put(Columns.Tb_Font.BUY_TIME, font.buyTime);
			value.put(Columns.Tb_Font.LIFE_TIME, font.lifeTime);
			value.put(Columns.Tb_Font.INSTALL_STATE, font.installState);
			value.put(Columns.Tb_Font.SORT, font.sort);
			Debug.v(TAG, "backupImportUpdateFont-->" + "id=" + font.id);
			update(Columns.Tb_Font.TB_NAME, value, Columns.Tb_Font.ID + "=?",
					new String[] { font.id + "" });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return false;
	}

	/**
	 * 下载人数更新
	 * 
	 * @Description:
	 * @param font
	 * @see:
	 */
	public boolean updateFontUsers(Font font) {

		Cursor cursor = null;
		try {
			ContentValues value = new ContentValues();
			value.put(Columns.Tb_Font.ID, font.id);
			value.put(Columns.Tb_Font.USERS, font.users);
			Debug.v(TAG, "updateFontUsers-->" + "id=" + font.id
					+ " users count=" + font.users);
			update(Columns.Tb_Font.TB_NAME, value, Columns.Tb_Font.ID + "=?",
					new String[] { font.id + "" });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return false;
	}

	/**
	 * 
	 * TODO 没有where？
	 **/
	public int updateFontnew() {
		ContentValues values = new ContentValues();
		values.put(Columns.Tb_Font.NEWFONT, 0);
		return update(Columns.Tb_Font.TB_NAME, values, null, null);
	}

	/**
	 * 读取所有font
	 * @return
	 */
	public ArrayList<Font> getAllFont() {
		ArrayList<Font> result = new ArrayList<Font>();
		Cursor cursor = null;
		Font f = null;
		try {
			cursor = query(Columns.Tb_Font.TB_NAME, null, null, null,
					Columns.Tb_Font.SORT, null);

			if (cursor != null && cursor.getCount() > 0) {

				cursor.moveToFirst();
				do {
					f = new Font();
					f.id = cursor.getInt(cursor
							.getColumnIndex(Columns.Tb_Font.ID));
					f.block = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.BLOCK));
					f.key = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.KEY));
					f.buyTime = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.BUY_TIME));
					f.lifeTime = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.LIFE_TIME));
					f.cpid = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.CPID));
					f.isFree = cursor.getInt(cursor
							.getColumnIndex(Columns.Tb_Font.IS_FREE));
					f.installState = Integer.parseInt(cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.INSTALL_STATE)));
					f.newFont = cursor.getInt(cursor
							.getColumnIndex(Columns.Tb_Font.NEWFONT));
					f.path = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.PATH));
					f.fontSet = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.FONT_SET));
					f.nameCode = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.NAME_CODE));
					f.sort = cursor.getInt(cursor
							.getColumnIndex(Columns.Tb_Font.SORT));
					f.downloadSize = cursor.getInt(cursor
							.getColumnIndex(Columns.Tb_Font.DOWNLOAD_SIZE));
					f.nameshow = cursor.getString(cursor
							.getColumnIndex(Columns.Tb_Font.NAME_SHOW));
					f.size = cursor.getInt(cursor
							.getColumnIndex(Columns.Tb_Font.SIZE));
					f.users = cursor.getInt(cursor
							.getColumnIndex(Columns.Tb_Font.USERS));
					result.add(f);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}

	/**
	 * @brief 获取版本的合作伙伴id
	 * @author $Author: LiuFang
	 * @since 1.0.0.0
	 * @version 1.0.0.0
	 * @param id
	 *            version id
	 * @return value ：版本的合作伙伴id
	 */
	public String getVersionCpid(int id) {
		String cpid = null;
		Cursor c = null;
		try {

			c = query(Columns.Tb_Version.TB_NAME, null,
					Columns.Tb_Version.ID_VERSION + " = " + id, null, null,
					null);

			// 移动光标到下一行
			if (c.moveToNext()) {
				int index = c.getColumnIndex(Columns.Tb_Version.ID_VERSION);
				cpid = c.getString(index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return cpid;
	}

	/*
	 * @brief 初始化版本表
	 * 
	 * @author $Author: Xiao Fan
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param id 版本id
	 * 
	 * @param version 版本号
	 */
	public boolean insertVersion(int id, String version) {

		// 有则替换，无则存入

		Cursor cursor = null;
		try {

			ContentValues value = new ContentValues();
			value.put(Columns.Tb_Version.CPID, "");
			value.put(Columns.Tb_Version.ID_VERSION, id);
			value.put(Columns.Tb_Version.VERSION, version);

			insertOrReplace(Columns.Tb_Version.TB_NAME, value);
			Debug.v(TAG, "insertVersion-->" + "id=" + id + "version=" + version);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return false;

	}

	/**
	 * updateVersionCpid 更新软件cpid
	 * 
	 * @author Hao LI
	 * @since 1.0.0.0
	 * @version 1.0.0.0
	 * @param version
	 *            表Id
	 */
	public void updateVersionCpid(String cpid) {

		try {
			// 存放键值对
			ContentValues values = new ContentValues();
			// 放入激活码
			values.put(Columns.Tb_Version.CPID, cpid);
			update(Columns.Tb_Version.TB_NAME, values,
					Columns.Tb_Version.ID_VERSION + " = " + 100, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @brief 获取版本的version属性
	 * @author $Author: LiuFang
	 * @since 1.0.0.0
	 * @version 1.0.0.0
	 * @param id
	 *            版本id
	 */
	public String getVersion() {
		String version = null;
		Cursor c = null;
		try {
			c = query(Columns.Tb_Version.TB_NAME, null,
					Columns.Tb_Version.ID_VERSION + " =?",
					new String[] { "100" }, null, null);
			if (c.moveToNext()) {
				int index = c.getColumnIndex(Columns.Tb_Version.VERSION);
				version = c.getString(index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return version;
	}

	// /**
	// * ��ѯ��Ϣ����
	// *
	// * @Description:
	// * @return
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-9-30
	// */
	// public int getPushMessageTotalCount() {
	// int count = 0;
	// try {
	// SQLiteDatabase db = getWritableDatabase();
	// // TODO Ŀǰֻ�д������ӣ� �����ղؼ�
	// // ����
	// StringBuffer sql = new StringBuffer();
	// sql.append("select count(").append(Columns.Tb_PushMessage.ID).append(")");
	// sql.append(" from ").append(Columns.Tb_PushMessage.TB_NAME);
	// // sql.append(" where ").append(Columns.Tb_PushMessage.ID).append("=?");
	// count = (int) DatabaseUtils.longForQuery(db, sql.toString(), null);
	// sql = null;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return count;
	// }

	// /**
	// * ��ȡ��Ϣ�б�
	// *
	// * @Description:
	// * @param limit
	// * @return
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-9-30
	// */
	// public List<PushMessage> getPushMessageList(String limit) {
	// Cursor cursor = null;
	// List<PushMessage> pushMessages = new ArrayList<PushMessage>();
	// PushMessage msg = null;
	// try {
	// cursor = query(Columns.Tb_PushMessage.TB_NAME, null, null, null,
	// Columns.Tb_PushMessage.RECEIVE_TIME + " desc", limit);
	//
	// if (cursor != null && cursor.getCount() > 0) {
	//
	// cursor.moveToFirst();
	// do {
	// msg = new PushMessage();
	// msg.setId(cursor.getInt(cursor.getColumnIndex(Columns.Tb_PushMessage.ID)));
	// msg.setTitle(cursor.getString(cursor.getColumnIndex(Columns.Tb_PushMessage.TITLE)));
	// msg.setContent(cursor.getString(cursor.getColumnIndex(Columns.Tb_PushMessage.CONTENT)));
	// msg.setTag(cursor.getString(cursor.getColumnIndex(Columns.Tb_PushMessage.TAG)));
	// msg.setReceiveTime(cursor.getLong(cursor.getColumnIndex(Columns.Tb_PushMessage.RECEIVE_TIME)));
	// msg.setReadStatue(cursor.getInt(cursor.getColumnIndex(Columns.Tb_PushMessage.READ_STATUE)));
	// msg.setSharedTimes(cursor.getInt(cursor.getColumnIndex(Columns.Tb_PushMessage.SHARED_TIMES)));
	// pushMessages.add(msg);
	// } while (cursor.moveToNext());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (cursor != null) {
	// cursor.close();
	// }
	// }
	// return pushMessages;
	// }

	// /**
	// * ���һ����Ϣ���Ѷ�δ��״̬
	// *
	// * @Description:
	// * @param id
	// * @param flag
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-9-30
	// */
	// public void setPushMessageReadFalg(long receiveTime, int flag) {
	// StringBuffer sql = new StringBuffer();
	// sql.append("update ").append(Columns.Tb_PushMessage.TB_NAME).append(" set ").append(Columns.Tb_PushMessage.READ_STATUE).append("=? where ").append(Columns.Tb_PushMessage.RECEIVE_TIME).append("=?");
	// getWritableDatabase().execSQL(sql.toString(), new Object[] { flag,
	// receiveTime });
	// }

	// /**
	// * ��������1
	// *
	// * @Description:
	// * @param id
	// * @param flag
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-9-30
	// */
	// public void addPushMessageSharedTime(long receiveTime) {
	//
	// StringBuffer sql = new StringBuffer();
	// sql.append("UPDATE ").append(Columns.Tb_PushMessage.TB_NAME).append(" set ").append(Columns.Tb_PushMessage.SHARED_TIMES).append("CONVERT (CHAR,(CONVERT(").append(Columns.Tb_PushMessage.SHARED_TIMES).append(",INT)+1)) where ").append(Columns.Tb_PushMessage.RECEIVE_TIME).append("=?");
	// getWritableDatabase().execSQL(sql.toString(), new Object[] { receiveTime
	// });
	// }

	// /**
	// * ɾ����Ϣ
	// *
	// * @Description:
	// * @param uids
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2013-10-8
	// */
	// public boolean deletePushMessage(int[] ids) {
	// SQLiteDatabase db = getWritableDatabase();
	// db.beginTransaction();
	// try {
	// for (int id : ids) {
	// db.delete(Tb_PushMessage.TB_NAME, Tb_PushMessage.ID + "=?", new String[]
	// { id + "" });
	// }
	// db.setTransactionSuccessful();
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// } finally {
	// db.endTransaction();
	// }
	// return true;
	// }
}
