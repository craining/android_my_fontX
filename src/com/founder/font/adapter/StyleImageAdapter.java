package com.founder.font.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.founder.font.R;
import com.founder.font.bean.Font;

/**
 * ****************************************************************
 * 
 * @brief: 描述图像信息和设置图片的倒影效果
 * @author: $Author: an
 * @since: 1.0.0.0
 * @version: 1.0.0.0
 ***************************************************************** 
 */
public class StyleImageAdapter extends BaseAdapter {
	// static int mGalleryItemBackground = 0; // 设置背景属性
	private Context mContext = null; // 上下文对象
	private ArrayList<Font> arrFontList = null; // 字体展示图片的id
	private LayoutInflater mInflater;

	public int mSelectPosition = 0; // 当前被选中项的位置
	// private int mMainShowFontCount;// main里展示的数量

	/**
	 ******************************************************** 
	 * @brief: StyleImageAdapter的构造函数
	 * @author: $Author: an
	 * @since: 1.0.0.0
	 * @version: 1.0.0.0
	 ********************************************************* 
	 */
	public StyleImageAdapter(Context context, ArrayList<Font> list, int selectedPosition) {
		mContext = context;
		this.arrFontList = list;
		mInflater = LayoutInflater.from(mContext);
		// this.mMainShowFontCount = mainShowCount;
		this.mSelectPosition = selectedPosition;
	}

	@Override
	public int getCount() {
		return arrFontList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrFontList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 记录当前选中的位置
	public void notifyDataSetChanged(int albumId) {
		mSelectPosition = albumId;
		super.notifyDataSetChanged();
	}

	// 返回具体位置的ImageView对象
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		view = mInflater.inflate(R.layout.mainview_style_gallery_item, null);

		TextView textView = (TextView) view
				.findViewById(R.id.style_gallery_item_image);

		textView.setBackgroundColor(Color.alpha(1)); // 设置背景色为1

		textView.setText(arrFontList.get(position).nameCode);

		// 当前选中的字体颜色突显
		if (position == mSelectPosition) {
			textView.setTextColor(mContext.getResources().getColor(
					R.color.main_text));
		} else {
			textView.setTextColor(mContext.getResources().getColor(
					R.color.black));
		}

		if (position == 0) {
			// 系统默认
			textView.setBackgroundResource(R.drawable.main_scale_system);
		} else if (position == arrFontList.size() - 1) {
			// 更多
			textView.setBackgroundResource(R.drawable.main_scale_normal);
		} else {
			textView.setTypeface( );
			textView.setBackgroundResource(R.drawable.main_scale_normal);
		}

		// if (position != 0)
		// {
		// textView.setBackgroundResource(R.drawable.a000);
		// }

		return view;

		// return createReflectedImages(imageList.get(position));// 实现图片的倒影效果
	}

	 
}
