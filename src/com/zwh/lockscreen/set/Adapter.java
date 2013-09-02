package com.zwh.lockscreen.set;

import java.util.List;
import java.util.Map;

import com.zwh.lockscreen.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleAdapter;
/** 
 * 继承SimpleAdapter
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */
public class Adapter extends SimpleAdapter{
	boolean[] select;
	public Adapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to,boolean []select) {
		super(context, data, resource, from, to);
		//listview中每一项的是否选中
		this.select = select;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.if_open);
		//设置checkbox的状态
		checkbox.setChecked(select[position]);
		return view;
	}
}

