package com.zwh.lockscreen.addition;

import java.io.FileNotFoundException;

import com.zwh.lockscreen.R;
import com.zwh.lockscreen.set.PreferenceInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
/** 
 * 设置背景颜色和图片的类
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class BackGround {
	private Context context;
	private int color;
	/** 
	 * 设置参数view的背景
	 * @param
	 * @return 
	 * @exception  
	 * @see        
	 * @since 
	 */
	public void set(Context context,View view){
		
		this.context = context;
		PreferenceInfo pInfo = new PreferenceInfo(context);
		//获取设置的背景颜色
		color = pInfo.getBackgroundColor();
		//获取设置的背景图片的序号
		int bg = pInfo.getBackgroundPicture();
		//根据图片的序号获取资源ID
		switch(bg){
		case 0:
			color = color | 0xff000000;
			setBackgroundColor(color,view);
			break;
		case 1:
			/*color = color | 0xff000000;
			setBackgroundColor(color,view);*/
			setBackgroundDrawable(R.drawable.abg,view);
			break;
		case 2:
			setBackgroundDrawable(R.drawable.bbg,view);
			break;
		case 3:
			setBackgroundDrawable(R.drawable.cbg,view);
			break;
		case 4:
			setBackgroundDrawable(view);
			break;
		default:
			//壁纸
			break;
		}
	} 
	private void setBackgroundDrawable(View view){
		SharedPreferences preference = context.getSharedPreferences("LockScreenPackageList", Context.MODE_WORLD_READABLE);
		String str = preference.getString("background_picture", "null");
		
		ContentResolver cr = context.getContentResolver(); 
		Uri uri = Uri.parse(str);
		
		try {
			Drawable drawable = Drawable.createFromStream(cr.openInputStream(uri), "idol");
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_OVER);
			view.setBackgroundDrawable(drawable);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private void setBackgroundDrawable(int id,View view){
		try {
			//获取图片
			Drawable drawable = context.getResources().getDrawable(id);
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_OVER);
			//设置背景
			view.setBackgroundDrawable(drawable);
		} catch (Resources.NotFoundException e) {
			// TODO: handle exception
		}
	}
	private void setBackgroundColor(int color,View view){
		view.setBackgroundColor(color);
	}
}
