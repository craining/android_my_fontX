package com.founder.font.interfaces;

import java.util.ArrayList;

import com.founder.font.bean.Font;

public class FontCoolObserver {

	public void checkFirstInstalledFinished(boolean result,
			boolean isFirstInstalled, boolean noDataBackup,
			boolean dataBackupResult, String version) {

	}

	public void checkVersionFinished(boolean rseult, boolean stateIn) {

	}

	public void getFontUsersFinished(boolean result, ArrayList<Font> fonts) {

	}

	public void uploadFirstStartInfoFinished(boolean result) {

	}

	public void checkNewVersionFinished(boolean result, boolean hasNewVersion, String downloadUrl) {

	}
	
	public void getAllFontsFinished(boolean result, ArrayList<Font> fonts) {
		
	}
	
	public void getFontLimitStateFinished(boolean result, int state) {
		
	}
}
