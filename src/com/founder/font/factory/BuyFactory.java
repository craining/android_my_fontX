package com.founder.font.factory;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.view.KeyEvent;
import android.widget.Toast;

import com.founder.font.bean.Font;
import com.founder.font.db.DbOpera;
import com.founder.font.util.TimeUtil;
import com.founder.font.view.CustomProgressDialog;

public class BuyFactory {
//	private CustomDialog currDialog = null;
	
	Activity activity;
	DbOpera dao = null; 
	boolean hasNext = true;
	public static HashMap<String, String> hmapOrder = null;
	final static String m_strPrice="2";//价格
	static ProgressDialog payChannelDialog = null;
//	private ProgressDialog curProcessDialog = null;
	private int fontId;
	
	//银联支付
	private static final String TRADE_COMMAND = "1001";
	private static final String SERVER_URL = "http://202.104.148.76/splugin/interface";
	
   //private static final String SERVER_URL = "http://222.66.233.198:8080/gateway/merchant/trade";

	
	private Font mFontToBuy;
	
	public BuyFactory(Activity context, Font font){
		this.activity = context;
		this.mFontToBuy = font;
		dao = DbOpera.getInstance();
	}
	
	// 获取支付通道handler
	public static Handler handler = new Handler()
	{

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			hmapOrder = (HashMap<String, String>) msg.obj;
			if(payChannelDialog != null)
				payChannelDialog.dismiss();
		}

	};
	
//	// 获取索引
//	public CustomDialog getCurrDialog()
//	{
//		return currDialog;
//	}
//
//	// dialog 窗口控制
//	public void closeCurrDialog()
//	{
//		if (currDialog != null && currDialog.isShowing())
//		{
//			currDialog.dismiss();
//		}
//	}
//
//	public void closeProgreeDialog()
//	{
//		if (curProcessDialog != null && curProcessDialog.isShowing())
//		{
//			curProcessDialog.dismiss();
//		}
//	}
	
	
	public void buy(final int nFontID){

		hasNext = true;

		// final ProgressDialog payChannelDialog = ProgressDialog.show(ctx,
		// getExt(), getCpid());
		// // 设置进度条风格，风格为圆形，旋转的
		// payChannelDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// // 设置ProgressDialog 在屏幕上的显示位置
		// payChannelDialog.getWindow().setGravity(Gravity.CENTER);
		// // 加载自定义dialog
		// payChannelDialog.setContentView(R.layout.buyprogress);

		// 创建等待提示对话框
		fontId = nFontID;
		final String fontName = mFontToBuy.fontSet;
		String getPayMessage = "正在获取支付通道...";
		payChannelDialog = CustomProgressDialog.createProgressDialog(activity,
				getPayMessage);


		// 取消获取支付通道
		payChannelDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
			{
				switch (keyCode)
				{
				case KeyEvent.KEYCODE_BACK:
					// 窗口取消
					dialog.dismiss();
					hasNext = false;
					break;
				}
				return false;
			}
		});
		// 调用执行支付的方法
		payChannelDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
		{
			public void onDismiss(DialogInterface dialog)
			{
				if (hasNext)
					// 支付通道获取并获得下一步提示
					if(null != hmapOrder && !hmapOrder.isEmpty()){
						//调用支付接口进行支付
						final String strOrderid = hmapOrder.get("orderid");
						//TODO 
//				            if(strOrderid!=null)
//						    UPPayAssistEx.startPayByJAR(activity,
//				            PayActivity.class, null, null, strOrderid, "00");
				
						//IydpayIns.PayEntry(activity, nFontID, strOrderid, m_strPrice);
					}
			}
		});

		//TODO 
//		new Thread()
//		{
//			@Override
//			public void run()   
//			{
//				//连接服务器获取订单号
//				HashMap<String, String> hmapOrderid = new HashMap<String, String>();
//				final TelephonyManager telephonyManager = (TelephonyManager) activity
//				.getSystemService(Context.TELEPHONY_SERVICE);
//				hmapOrderid = Tools.getOrderid(telephonyManager, nFontID, getExt(), getCpid(nFontID));
//				Message msg = new Message();
//				msg.obj = hmapOrderid;
//				handler.sendMessage(msg);
//			}
//		}.start();
		
		
	}
	
	public void dealBuyResult(final int requestCode, int resultCode, Intent data){
		String msg = "";
		String receipt = "";
		String status = null;
//		String strFontName = dao.getFontName(fontId);
		String strMsg = "支付未成功，我们已经帮您将其排在首位，请尝试重新支付！";
		final String strMNC = receipt;
		//TODO:
		 //这里是什么情况？
			if (fontId >= 100 && fontId < 999)
			{
				
				if (null != data)
				{
					msg = data.getStringExtra("msg");
					receipt = data.getStringExtra("receipt");
				}
				
				
				
			}
				if(data == null){
			return;
		}
		String str = data.getExtras().getString("pay_result");
        
	 if(str!=null){
		if (str.equalsIgnoreCase("success")) {
			status = "success";
			// 更新数据库中字体isfree属性为免费
//			dao.updateFreeStatus(fontId, "0");
			// 更新数据库中字体installstate为下载
//			dao.updateFontState(fontId, "1");

			strMsg = "支付成功啦，请下载使用!";
//			new Thread()
//			{
//				@Override
//				public void run()
//				{
//					final TelephonyManager telephonyManager = (TelephonyManager) activity
//					.getSystemService(Context.TELEPHONY_SERVICE);
//					HashMap<String, String> hmapUpdate = Tools.updateOrderIYD(telephonyManager, fontId, getExt(), getCpid(fontId), strMNC);
//					if(hmapUpdate != null && !hmapUpdate.isEmpty()){
//						String strPwd = hmapUpdate.get("pwd");
//						dao.updateFontKey(strPwd, fontId);
//					}
//				}
//			}.start();
		
		} else if (str.equalsIgnoreCase("fail")) {
           // msg = "支付失败！";
         } else if (str.equalsIgnoreCase("cancel")) {
           // msg = "用户取消了支付";
        }
		}
		
	
		//	switch (resultCode)
//			{
//			case Iydpay.PAY_SUCCESS:
//				status = "success";
//				// 更新数据库中字体isfree属性为免费
//				dao.updateFreeStatus(nFontID, "0");
//				// 更新数据库中字体installstate为下载
//				dao.updateFontState(nFontID, "1");
//
//				strMsg = "支付成功啦，请下载使用!";
//				new Thread()
//				{
//					@Override
//					public void run()
//					{
//						final TelephonyManager telephonyManager = (TelephonyManager) activity
//						.getSystemService(Context.TELEPHONY_SERVICE);
//						HashMap<String, String> hmapUpdate = Tools.updateOrderIYD(telephonyManager, nFontID, getExt(), getCpid(nFontID), strMNC);
//						if(hmapUpdate != null && !hmapUpdate.isEmpty()){
//							String strPwd = hmapUpdate.get("pwd");
//							dao.updateFontKey(strPwd, nFontID);
//						}
//					}
//				}.start();
//				break;
//			case Iydpay.PAY_FAIL:
//				status = "fail";
//
//				
//
//				break;
//			case Iydpay.PAY_UNKNOWN:
//				status = "unknow";
//				break;
//			case Iydpay.PAY_CANCELED:
//				status = "cancel";
//				break;
//			default:
//				break;
//			}
			
			Toast.makeText(activity, "【" + mFontToBuy.fontSet + "】 " + strMsg,
					Toast.LENGTH_LONG).show();
	}
	/**
	 * @brief 获得当前系统时间，并转化为需求文档所规定的特殊表示方式
	 * @author $Author: liufang
	 * @since 1.0.0.0
	 * @version 1.0.0.0
	 * @return String 返回当前系统时间年月日的字符串
	 */
	private static String getExt()
	{
		String year = "";
		String mouth = "";
		String day = "";
		Time time = new Time();
		time.setToNow();
		int yearId = time.year;
		int mouthId = time.month;
		int dayId = time.monthDay;
		year = TimeUtil.yearMap[yearId - 2010];
		if (dayId < 27)
		{
			mouth = TimeUtil.mouthMap[mouthId];
			day = TimeUtil.dayMap[dayId - 1];
		} else
		{
			mouth = TimeUtil.mouthMap[12 + mouthId];
			day = TimeUtil.dayMap[dayId - 27];
		}
		// 返回年月日的字符串
		return year + mouth + day;
	}
	
//	/**
//	 * 
//	 * @brief获得合作伙伴Id
//	 * @author $Author: liufang
//	 * @since 1.0.0.0
//	 * @version 1.0.0.0
//	 * @param MainView
//	 *            mainView
//	 * @return String 返回当前字体的合作伙伴id
//	 */
//	public String getCpid(int nFontid)
//	{
//		String cpid = null;
//
//		cpid = dao.getCPID(nFontid);
//
//		return cpid;
//	}
}