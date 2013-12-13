package com.founder.font.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.content.SharedPreferences;

import com.founder.font.FontCoolConstant;
import com.founder.font.bean.Font;
import com.founder.font.db.DbOpera;

/**
 * 配置文件xml解析
 * 
 * @author $Author: Yaobin Zhang
 * @version $Revision: 1.5 $
 * @created $Date: 2011/10/31 $
 * */
public class UnicodeVersionApp extends DefaultHandler {
	// 字体 ID
	public static final int FONT_ID = 0;
	private String suoId = null;
	// 字体 counts
	public static final int FONT_COUNTS = 1;
	private String block = null;
	// 字体Name
	public static final int FONT_NAME = 2;
	private String name = null;
	// 表名称
	@SuppressWarnings("unused")
	private static final String TABLE_NAME = "tb_font";
	// 字体id
	public static final String F_ID = "_id";
	// 激活码
	public static final String F_KEY = "key";
	// 购买时间
	public static final String F_BUYTIME = "buytime";
	// 使用期限
	public static final String F_LIFETIME = "lifetime";
	// 免费标志位： 0为免费 1为收费
	public static final String F_ISFREE = "isfree";
	// 更多字体
	public static final String F_ISNEWFONT = "isnewfont";
	// 安装状态： 0为未安装，1为已购买，2为已安装， 3为已换字
	public static final String F_INSTALLSTATE = "installstate";
	// 安装路径
	public static final String F_PATH = "path";
	// 字体文件总大小
	public static final String F_TotalSize = "size";
	// 字体使用人数
	public static final String F_Users = "users";
	// 上下文
	// private MainView mainView;
	// 所有数据
	private ArrayList<Font> allData;
	// 版本
	private String version;
	// 软件版本
	private String apkVersion;
	// 字符串
	private StringBuffer sb = new StringBuffer();
	// 字体信息类
	private Font mFont;
	private String[] a = null;
	final String[] al = null;
	static int i = 0;
	private Font ui = new Font();
	private StringBuffer buffer;
	// 上下文
	private Context ctx;
	private DbOpera mDbOprea;
	private String id = null;
	int count = 0;
	private SharedPreferences preferences = null;
	private ArrayList<Integer> builder = new ArrayList<Integer>();

	public ArrayList<Integer> getBuilder() {
		return builder;
	}

	public int getCount() {
		return count;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		super.characters(ch, start, length);
		buffer.append(ch, start, length);
	}

	/*
	 * UnicodeVersionApp 构造函数
	 * 
	 * @author Hao LI
	 * 
	 * @since 1.0.0.0
	 * 
	 * @version 1.0.0.0
	 * 
	 * @param mContext 上下文
	 */
	public UnicodeVersionApp(Context ctx) {
		this.ctx = ctx;
		mDbOprea = DbOpera.getInstance();
		buffer = new StringBuffer();
		preferences = ctx.getSharedPreferences("newfont", ctx.MODE_PRIVATE);
	}

	@Override
	public void endDocument() throws SAXException {

		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);

		if ("id".equals(localName)) { // id标签获取到的内容设置到信息类中
			mFont.id = Integer.parseInt(buffer.toString().trim());
			id = buffer.toString().trim();
		} else if ("isfree".equals(localName)) {// isfree标签获取到的内容设置到信息类中
			if (mFont != null) {
				mFont.isFree = Integer.parseInt(buffer.toString().trim());
			}
		} else if ("install".equals(localName)) {// install标签获取到的内容设置到信息类中
			mFont.installState = Integer.parseInt(buffer.toString().trim());
		} else if ("lifetime".equals(localName)) {// lifetime标签获取到的内容设置到信息类中
			mFont.lifeTime = buffer.toString().trim();
		}

		else if ("isnewfont".equals(localName)) {// isnewfont标签获取到的内容设置到信息类中
			mFont.newFont = Integer.parseInt(buffer.toString().trim());
		} else if ("path".equals(localName)) {// path标签获取到的内容设置到信息类中
			mFont.path = buffer.toString().trim();
		} else if ("suoId".equals(localName)) {// suoId标签获取到的内容设置到信息类中
			suoId = buffer.toString().trim();
		} else if ("block".equals(localName)) {// block标签获取到的内容设置到信息类中
			mFont.block = buffer.toString().trim();
			block = buffer.toString().trim();
		} else if ("name".equals(localName)) {// name标签获取到的内容设置到信息类中
			name = buffer.toString().trim();
		} else if ("fontset".equals(localName)) {// fontset标签获取到的内容设置到信息类中
			mFont.fontSet = buffer.toString().trim();
		} else if ("namecode".equals(localName)) {// namecode标签获取到的内容设置到信息类中
			mFont.nameCode = buffer.toString().trim();
		} else if ("sort".equals(localName)) {// sort标签获取到的内容设置到信息类中
			mFont.sort = Integer.parseInt(buffer.toString().trim());
		} else if ("size".equals(localName)) {// size标签获取到的内容设置到信息类中
			int nSize = (int) (Float.parseFloat(buffer.toString().trim()) * 1000 * 1000);
			mFont.size = nSize;
		} else if ("users".equals(localName)) {// users标签获取到的内容设置到信息类中
			mFont.users = Integer.parseInt(buffer.toString().trim());
		} else if ("nameshow".equals(localName)) {// nameshow标签获取到的内容设置到信息类中
			mFont.nameshow = buffer.toString().trim();
		} else if ("content".equals(localName)) {
			if (count == 0)
				mDbOprea.updateFontnew();
			count++;
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("d" + id + "new_font_content", buffer.toString()
					.trim()); // value to store
			editor.putLong("d" + id + "time", new Date().getTime());
			editor.commit();
			builder.add(Integer.valueOf(id));

		} else if ("font".equals(localName) || "newfont".equals(localName)) {// font标签获取到的内容设置到信息类中
			allData.add(mFont);

			FontCoolConstant.font[Integer.parseInt(suoId) - 1][0] = suoId;
			FontCoolConstant.font[Integer.parseInt(suoId) - 1][1] = block;
			FontCoolConstant.font[Integer.parseInt(suoId) - 1][2] = name;

			// int c = dao.queryFontExists(mFont.getF_ID());
			// dao.getFontName(mFont.getF_ID());
			// if (c != mFont.getF_ID() || c == 0) {
			// 如果不存在，将版本cpid附值给字体cpid，并初始化数据库
			// mFont.setF_CPID(dao.getVersionCpid(100));
			mFont.cpid = mDbOprea.getVersionCpid(100);
			// dao.init(mFont.getF_ID(), mFont.getF_BLOCK(),
			// mFont.getF_ISFREE(),
			// mFont.getF_INSTALLSTATE(),
			// mFont.getF_LIFETIME(), mFont.getF_CPID(),
			// mFont.getF_ISNEWFONT(), mFont.getF_PATH(),
			// mFont.getF_FONTSET(),
			// mFont.getF_NAMECODE(), mFont.getF_SORT(),
			// mFont.getF_TotalSize(), mFont.getF_Users(),
			// mFont.getF_NameShow());

			mDbOprea.insertFont(mFont, false);
			// }
			// liufang,为自动更新需要，不更新cpid，20121119
			// if
			// (!dao.getCPID(unicodeInfo.getF_ID()).equals(dao.getVersionCpid(100)))
			// {
			// //在删除或者覆盖安装后，将字体的cpid更新为当前软件客户端的cpid
			// dao.updateFontCpid(unicodeInfo.getF_ID(),
			// dao.getVersionCpid(100));
			//
			// }

		} else if ("version".equals(localName)) {// version标签获取到的内容设置到信息类中
			version = buffer.toString().trim();

			mDbOprea.insertVersion(100, version);
			// // 获取version表
			// String version1 = dao.getVersion();
			// if (version1 == null) { // 如果不存在，则初始化
			// dao.initVersion(100, version);
			// } else if (version1 != null && !("".equals(version1))
			// && version1 != version) {
			// // 如果存在，则更新
			// dao.updateVersion(version);
			// }
		} else if ("cpid".equals(localName)) { // cpid标签获取到的内容设置到信息类中
			String cpid = buffer.toString().trim();
			// 更新客户端cpid
			// if (!cpid.equals(dao.getVersionCpid(100))) {
			// //liufang,为自动更新需要，不更新cpid，20121119
			// dao.updateVersionCpid(cpid);
			// Tools.saveData(ctx, "isRequest", "blean", true);
			// }
			String localCpid = mDbOprea.getVersionCpid(100);
			if (localCpid == null || localCpid.length() == 0) {
//				dao.updateVersionCpid(cpid);
				mDbOprea.updateVersionCpid(cpid);
//				Tools.saveData(ctx, "isRequest", "blean", true);TODO 
			}
		}
		// 设置字符串长度归零
		buffer.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		allData = new ArrayList<Font>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if ("font".equals(localName) || "newfont".equals(localName)) {// font标签，创建信息类
			mFont = new Font();
		}
		if ("version".equals(localName)) {

		}
	}

	public ArrayList<Font> getAllData() {
		return allData;
	}

	public void setAllData(ArrayList<Font> allData) {
		this.allData = allData;
	}
}
