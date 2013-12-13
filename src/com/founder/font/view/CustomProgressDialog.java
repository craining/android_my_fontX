package com.founder.font.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;

public class CustomProgressDialog extends ProgressDialog
{

	public CustomProgressDialog(Context context)
	{
		super(context);
	}

	public static ProgressDialog createProgressDialog(Context context, String message)
	{
		// 创建进度对话框对象
		ProgressDialog progressDialog = new ProgressDialog(context);

		progressDialog.setMessage(message);

		// 设置进度条风格，风格为圆形，旋转的
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 在屏幕上的显示位置
		progressDialog.getWindow().setGravity(Gravity.CENTER);
		// progressDialog.setCancelable(false);
		// 加载自定义dialog
		// progressDialog.setContentView(R.layout.progressdialogt);
		if (!progressDialog.isShowing())
		{
			progressDialog.show();
		}
		return progressDialog;
	}

	public static ProgressDialog createProgressDialog(Context context, String title, String message)
	{
		// 创建进度对话框对象
		ProgressDialog progressDialog = new ProgressDialog(context);

		progressDialog.setTitle(title);
		progressDialog.setMessage(message);

		// 设置最大值为100
		progressDialog.setMax(100);

		// 设置进度条风格为水平的
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		// 设置ProgressDialog 在屏幕上的显示位置
		progressDialog.getWindow().setGravity(Gravity.CENTER);
		// 加载自定义dialog
		// progressDialog.setContentView(R.layout.progressdialogt);

		return progressDialog;
	}
}
