package com.founder.font.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.founder.font.bean.Font;
import com.founder.font.db.DbOpera;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 请求服务器 ，判断网路连接状态、下载
 * 
 * @author $Author:Hao Li
 * @version $Revision: 1.5 $
 * @created $Date: 2012/3/10 $
 * */

public class DownLoadTask implements Callable<String> {
	// 上下文对象
	// private Context mContext;
	// handler 用于发送消息
	private Handler handler;
	// 重连时间
	private long reconnectTime = 5;
	// 字节长度
	private int bufferSize = 1024;
	private CountDownLatch latch;
	private RandomAccessFile file = null;
	private RandomAccessFile tempFile = null;
	// url地址
	private URL url = null;
	private int id;
	// 开始位置，用于断点续传
	@SuppressWarnings("unused")
	private long startPosition;
	// 结束位置
	private long endPosition;
	// 当前位置
	private long currentPosition;
	public boolean flag = true;
	// 文件长度
	long length;
	File file2;
	private int m_nFontID;
	private DbOpera mDbOpera;

	/**
	 * DownLoadTask 有参构造方法
	 * 
	 * @author Hao LI
	 * @since 1.0.0.0
	 * @version 1.0.0.0
	 * @param file
	 *            要读写的文件
	 * @param tempFile
	 *            临时文件
	 * @param url
	 *            下载地址
	 * @param id
	 *            线程数
	 * @param latch
	 *            倒计数锁存
	 * @param reconnectTime
	 *            重连时间
	 * @param buffSize
	 *            字节长度
	 * @param handler
	 *            用于发送即时消息及处理消息
	 * @param length
	 *            文件长度
	 */
	public DownLoadTask(int nFontID, File file, File tempFile, URL url, int id,
			CountDownLatch latch, long reconnectTime, int bufferSize,
			Handler handler, long length, DbOpera opera) {
		this.handler = handler;
		this.mDbOpera = opera;
		this.m_nFontID = nFontID;
		file2 = tempFile;
		this.length = length;
		try {
			this.file = new RandomAccessFile(file, "rw");
			this.tempFile = new RandomAccessFile(tempFile, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.url = url;
		this.id = id;
		this.latch = latch;
	}

	/*
	 * setBoolean 断点续传
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param flag 断点or续传
	 */
	public void setBoolean(Boolean flag) {
		this.flag = flag;
	}

	/*
	 * call 开始下载
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param fileDir 字体文件
	 * 
	 * @return 返回“finish”
	 */
	public String call() {

		try {
			// seek跳转
			tempFile.seek((id - 1) * 28);
			tempFile.readInt();
			this.startPosition = tempFile.readLong();
			this.endPosition = tempFile.readLong();
			this.currentPosition = tempFile.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpURLConnection conn = null;
		InputStream inputStream = null;

		while (true) {
			try {
				// seek设置点
				tempFile.seek(id * 28 - 8);
				conn = (HttpURLConnection) this.url.openConnection();
				setHeader(conn);
				if (currentPosition < endPosition) {

					conn.setRequestProperty("Range", "bytes=" + currentPosition
							+ "-" + endPosition);
					file.seek(currentPosition);
					// 未返回ok，即网络有问题
					// if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
					if (conn.getResponseCode() != HttpURLConnection.HTTP_OK
							&& conn.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {
						file.close();
						conn.disconnect();
						// 发送消息处理
						Message msga = new Message();
						msga.what = FontDownloadManager.MSG_DOWNLOAD_ERROR;
						msga.arg1 = m_nFontID;
						handler.sendMessage(msga);
						break;
					}

					// 获取输入流
					inputStream = conn.getInputStream();
					int len = 0;
					byte[] b = new byte[bufferSize];
					int i = 0;
					// 循环读取
					while ((len = inputStream.read(b)) != -1 && flag) {

						i++;
						file.write(b, 0, len);

						currentPosition += len;
						// set tempFile now position
						tempFile.seek(id * 28 - 8);
						tempFile.writeLong(currentPosition);
						// 每读取50*1024个字节，更新数据库，用handler发送消息更新进度条
						// arg1为字体id,arg2为文件更新大小
						if (i == FontDownloadManager.Download_Block) {
							Message msgb = new Message();
							msgb.what = FontDownloadManager.MSG_DOWNLOAD_UPDATE;
							msgb.arg2 = (int) ((float) currentPosition
									/ (float) endPosition * 100);
							msgb.arg1 = m_nFontID;
							handler.sendMessage(msgb);
							i = 0;
							if(m_nFontID != 0) {
								//TODO 
								
							}
						}
						// liufang,对于字体文件，在下载过程中判断当前用户是否已取消，
						// 判断方式为查询数据库，当字体下载暂停时，数据库中字体状态为暂停
						if (0 != m_nFontID) {
							//更新数据库，存储下载大小
							if (FontDownloadManager.getInstance()
									.getFontInstallState(m_nFontID) == Font.STATE_PAUSE)
								setBoolean(false);
						}
					}
					// 文件，流关闭，节约资源
					file.close();
					tempFile.close();
					inputStream.close();
					conn.disconnect();
				}
				break;
			} catch (IOException e) {
				try {
					TimeUnit.SECONDS.sleep(getReconnectTime());
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				continue;
			}
		}

		if (currentPosition >= endPosition)
			latch.countDown();

		return "finish";
	}

	/*
	 * setHeader 设置头文件
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param conn url连接
	 */
	private void setHeader(URLConnection conn) {
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "aa");
		conn.setRequestProperty("Accept-Charset",
				"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since",
				"Fri, 02 Jan 2009 17:00:05 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-Control", "max-age=0");
		conn.setRequestProperty("Referer",
				"http://www.skycn.com/soft/14857.html");
	}

	/*
	 * getReconnectTime 获取重连时间
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @return 返回重连时间
	 */
	public long getReconnectTime() {
		return reconnectTime;
	}

	/*
	 * setReconnectTime 设置重连时间
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param reconnectTime 重连时间
	 */
	public void setReconnectTime(long reconnectTime) {
		this.reconnectTime = reconnectTime;
	}

	/*
	 * getBufferSize 获取字节长度
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @return 返回字节长度
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/*
	 * setBufferSize 设置字节长度
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param bufferSize 字节长度
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

}