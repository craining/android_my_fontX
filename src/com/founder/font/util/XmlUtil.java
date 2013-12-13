package com.founder.font.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.util.EncodingUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;

import com.founder.font.bean.Font;
import com.founder.font.xml.UnicodeVersionApp;

public class XmlUtil {
	/**
	 * @brief 解析xml文件
	 * @author $Author:Hao Li
	 * @since 1.0.0.0
	 * @version 1.5.0.0
	 * @param xmlName
	 *            xml文件
	 */
	public static ArrayList<Font> parseXml(Context context, int xmlId) {
		String reqStr = getXml(context, xmlId).toString();
		ArrayList<Font> result = new ArrayList<Font>();
		try {
			// XML解析 DOM SAX
			// 1 获取SAX解析器工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 2 通过工厂获取SAX解析器对象
			SAXParser sp = spf.newSAXParser();
			// 3 通过SAX解析器对象获取XMLReader对象
			XMLReader xr = sp.getXMLReader();
			// 4 关键一步 给XMLReader对象设置一个XMLHandler对象
			UnicodeVersionApp unicodeApp = new UnicodeVersionApp(context);
			xr.setContentHandler(unicodeApp);
			StringReader sr = new StringReader(reqStr.trim());
			InputSource is = new InputSource(sr);
			result = unicodeApp.getAllData();
			try {
				xr.parse(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	// 解析xml文件
	private static String getXml(Context context, int xmlId) {
		InputStream i = null;
		String s = null;

		try {
			i = context.getResources().openRawResource(xmlId);
			int len = i.available();
			byte a[] = new byte[len];
			i.read(a);
			String res = EncodingUtils.getString(a, "UTF-8");

			s = res.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
