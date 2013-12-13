package com.founder.font.util;

import junit.framework.Assert;
import android.content.Context;

public class ResourceUtil {
	/**
	 ************************************************************* 
	 * @brief: 根据资源名字动态获取资源对应的ID
	 * @author: $Author: an
	 * @param Context
	 * @param resName
	 * @param resType
	 *            :(layout、drawable、string)
	 * @return 资源对应的ID
	 ************************************************************* 
	 */
	public static int getResourceId(Context context, String resName,
			String resType) {
		Assert.assertNotNull(context);
		Assert.assertNotNull(resName);
		return context.getResources().getIdentifier(resName, resType,
				context.getPackageName());
	}
	
	
}
