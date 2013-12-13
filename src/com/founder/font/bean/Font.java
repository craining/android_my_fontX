package com.founder.font.bean;

public class Font {

	// 购买
	public final static int STATE_BUY = 0;
	// 安装
	public final static int STATE_INSTALL = 1;
	// 换字
	public final static int STATE_CHANGE = 2;
	// 使用中
	public final static int STATE_USING = 3;
	// 下载中
	public final static int STATE_DOWNLOADING = 4;
	// 暂停
	public final static int STATE_PAUSE = 5;
	
	public static final int FONT_IS_NEW = 1;// 是新字体
	public static final int FONT_ISNOT_NEW = 0;// 不是新字体

	// 字体id
	public int id;
	// 激活码
	public String key;
	public String block;
	public String fontSet;
	// 购买时间
	public String buyTime;
	// 合作伙伴ID
	public String cpid;
	// 使用期限
	public String lifeTime;
	// 是否是特色字体（更多字体）
	public int newFont;
	// 免费标志位：（0:免费 1:收费 2:期免费）
	public int isFree;
	// 安装状态（0:购买 1:下载 2:使用 3:使用中 4:下载中 5:暂停中（4,5不会作为初始配置））
	public int installState;
	// 安装路径
	public String path;
	// 排序位置
	public int sort;
	// 名字使用的映射字符
	public String nameCode;
	// 字体文件已下载大小
	public int downloadSize;
	// 字体文件总大小
	public int size;
	// 字体使用人数
	public int users;
	// 字体展示名
	public String nameshow;

	// 图片资源的id
	public int imgId;

	@Override
	public boolean equals(Object o) {

		Font a = (Font) o;

		if (a.id == id) {
			if (a.nameCode == null && nameCode == null) {
				return true;
			} else if (a.nameCode != null && nameCode != null) {
				if (a.nameCode.equals(nameCode)) {
					return true;
				}
			}
		}
		return false;
	}

}
