package com.founder.font.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.founder.font.FontCoolApplication;

import android.os.Environment;

public class BackUpUtil {

	/**
	 * 备份操作，
	 * @param export 导出备份，否则 恢复备份
	 * @return
	 */
	public static boolean dataBackUp(boolean export) {
		File dbFile = FontCoolApplication.getInstance().getDatabasePath("fontlifetime.db"); // 获得数据库路径，默认是/data/data/(包名)org.dlion/databases/
		File exportDir = new File(
				Environment.getExternalStorageDirectory(), "dbBackup");
		if (!exportDir.exists()) {
			exportDir.mkdirs();
		}
		File backup = new File(exportDir, dbFile.getName());
		if (export) {
			try {
				backup.createNewFile();
				fileCopy(dbFile, backup);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try {
				fileCopy(backup, dbFile);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	private static void fileCopy(File dbFile, File backup) throws IOException {
		FileChannel inChannel = new FileInputStream(dbFile).getChannel();
		FileChannel outChannel = new FileOutputStream(backup).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}
}
