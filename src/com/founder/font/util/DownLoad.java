package com.founder.font.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.founder.font.db.DbOpera;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class DownLoad
{
//	private Context mContext;
	// 文件目录、文件名
	public String fileDir = "";
	public String fileName;
	public ProgressBar bar;
	public Handler handler;
	// 超时重连时间
	public long reconnectTime = 5;
	// 线程数
	private int poolSize = 5;
	// 每个线程的缓冲区大小
	public int bufferSize = 1024;
	public static DownLoadTask downLoadTask;
	private DbOpera mDbOpera;

	/**
	 * DownLoad 构造方法
	 * 
	 * @author Hao LI
	 * @since 1.0.0.0
	 * @version 1.0.0.0
	 * @param handler
	 *            构造handler 用于发送消息
	 */
	public DownLoad(Handler handler, DbOpera opera)
	{
		this.handler = handler;
		this.mDbOpera = opera;
	}

	/*
	 * downLoad 开始下载
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param urlLocation url地址字符串
	 */
	public void downLoad(String urlLocation, int nFontID)
	{
		File file = null;
		File tempFile = null;
		CountDownLatch latch;
		URL url = null;

		ExecutorService pool = Executors.newCachedThreadPool();
		int contentLength = 0;
		int threadLength = 0;
		try
		{
			// 如果未指定名称，则从url中获得下载的文件格式与名字
			if (fileName == null || "".equals(fileName))
			{
				this.fileName = urlLocation
						.substring(urlLocation.lastIndexOf("/") + 1,
								urlLocation.lastIndexOf("?") > 0 ? urlLocation.lastIndexOf("?")
										: urlLocation.length());
				if ("".equalsIgnoreCase(this.fileName))
				{
					this.fileName = UUID.randomUUID().toString();
				}
			}
			// 文件创建
			new File(fileDir).mkdirs();
			// 创建子文件
			file = new File(fileDir + File.separator + fileName);
			// 创建临时文件
			tempFile = new File(fileDir + File.separator + fileName + "_temp");
			url = new URL(urlLocation);
			// 接口打开
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			setHeader(conn);
			// 得到content的长度
			contentLength = conn.getContentLength();
			if (contentLength == -1)
			{
				// 如果长度获取为-1 则网络出现问题 用handler发送消息致主线程
				Message msgf = new Message();
				msgf.what = FontDownloadManager.MSG_DOWNLOAD_ERROR;
				handler.sendMessage(msgf);
				return;
			}
			// liufang，更新数据库中字体文件的总大小
			// 所有非字体文件的下载，传入的fontid都为0
			if (0 != nFontID)
			{
				Message msgf = new Message();
				msgf.what = FontDownloadManager.MSG_DOWNLOAD_START;
				msgf.arg1 = nFontID;
				handler.sendMessage(msgf);
				//TODO 数据库更新字体文件大小
				
				
				
			}
			// 把context分为poolSize段，计算每段的长度。
			BigDecimal b1 = new BigDecimal(Double.toString(contentLength));
			BigDecimal b2 = new BigDecimal(Double.toString(this.poolSize));
			threadLength = b1.divide(b2, 0, BigDecimal.ROUND_HALF_UP).intValue();
			if (file.exists() && tempFile.exists())
			{
				// 如果文件已存在，根据临时文件中记载的线程数量，继续上次的任务
				latch = new CountDownLatch((int) tempFile.length() / 28);
				for (int i = 0; i < tempFile.length() / 28; i++)
				{
					// 下载线程任务开启
					downLoadTask = new DownLoadTask(nFontID, file, tempFile, url, i + 1,
							latch, reconnectTime, bufferSize, handler, contentLength, mDbOpera);
					pool.submit(downLoadTask);
				}
			} else
			{
				// 如果下载的目标文件不存在，则创建新文件

				// 如果下载的是字体文件，将数据库中下载大小初始化为0
				// 所有非字体文件的下载，传入的fontid都为0
				if (0 != nFontID)
				{
					//TODO  已下载更新为0，数据库
				}
				latch = new CountDownLatch(poolSize);
				file.createNewFile();
				tempFile.createNewFile();
				DataOutputStream os = new DataOutputStream(new FileOutputStream(tempFile));
				for (int i = 0; i < this.poolSize; i++)
				{
					os.writeInt(i + 1);
					os.writeLong(i * threadLength);
					if (i == this.poolSize - 1)
					{
						// 最后一个线程的结束位置应为文件末端
						os.writeLong(contentLength);
					} else
					{
						os.writeLong((i + 1) * threadLength);
					}
					os.writeLong(i * threadLength);
					// 多线程下载任务
					downLoadTask = new DownLoadTask(nFontID, file, tempFile, url, i + 1,
							latch, reconnectTime, bufferSize, handler, contentLength, mDbOpera);
					pool.submit(downLoadTask);
				}
				os.close();
			}

			// 等待下载任务完成
			latch.await();

			// 下载完成，利用handler发送消息到主线程。 删除临时文件
			Message msgd = new Message();
			msgd.what = FontDownloadManager.MSG_DOWNLOAD_FINISHED;
			msgd.arg1 = nFontID;
			handler.sendMessage(msgd);
			tempFile.delete();

			if(0!= nFontID) {
				//TODO 更新数据库下载状态
				
				
				
				
			}
			
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} finally
		{
			pool.shutdown();
		}
	}

	/*
	 * getDownLoadTask 获取下载任务实例
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @return 返回下载线程任务的实例
	 */
	public DownLoadTask getDownLoadTask()
	{
		return downLoadTask;
	}

	/*
	 * setHeader 设置文件头
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param conn url连接
	 */
	private void setHeader(URLConnection conn)
	{
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "aa");
		conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since", "Fri, 02 Jan 2009 17:00:05 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-Control", "max-age=0");
		conn.setRequestProperty("Referer", "http://www.skycn.com/soft/14857.html");
	}

	/*
	 * getFileDir 获取文件路径
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @return 返回文件路径
	 */
	public String getFileDir()
	{
		return fileDir;
	}

	/*
	 * setFileDir 设置文件路径
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param fileDir 字体文件路径
	 */
	public void setFileDir(String fileDir)
	{
		this.fileDir = fileDir;
	}

	/*
	 * @brief 获取文件名字
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @return 返回文件名字（字符串）
	 */
	public String getFileName()
	{
		return fileName;
	}

	// 设置文件名字
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	// 获取重连时间
	public long getReconnectTime()
	{
		return reconnectTime;
	}

	// 设置重连时间
	public void setReconnectTime(long reconnectTime)
	{
		this.reconnectTime = reconnectTime;
	}

	// 获取线程数
	public int getPoolSize()
	{
		return poolSize;
	}

	// 设置线程数
	public void setPoolSize(int poolSize)
	{
		this.poolSize = poolSize;
	}

	// 获取字节长度
	public int getBufferSize()
	{
		return bufferSize;
	}

	// 设置字节长度
	public void setBufferSize(int bufferSize)
	{
		this.bufferSize = bufferSize;
	}
}