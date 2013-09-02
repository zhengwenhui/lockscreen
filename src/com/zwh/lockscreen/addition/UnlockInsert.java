package com.zwh.lockscreen.addition;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.zwh.lockscreen.R;
import com.zwh.lockscreen.set.LockSetMultiSelectListPreference;
import com.zwh.lockscreen.set.PreferenceInfo;
/** 
 * 解锁屏并打开指定应用界面的类
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class UnlockInsert{
	private Context context;
	private ComponentName[] componentNameArray;

	public UnlockInsert(Context context){
		this.context = context;
		getLockSet();
	}
	/** 
	 * 得到应用的图标
	 * @param      
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	public Bitmap getBitmap(int index){
		Drawable icon = null;
		
		try {
			icon = context.getPackageManager().getActivityIcon(componentNameArray[index]);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			icon = context.getResources().getDrawable(R.drawable.home);
			e.printStackTrace();
		}
		return ((BitmapDrawable)icon).getBitmap();
	}

	/** 
	 * 根据给定的name开启Activity
	 * @param      
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	@SuppressLint("WorldReadableFiles")
	public void startActivity(int index){
		
		SharedPreferences preference = context.getSharedPreferences("LockScreenPackageList", Context.MODE_WORLD_READABLE);
		Editor editor = preference.edit();
		editor.putBoolean("openLockScreen", false);
		editor.commit();
		
		if(null==componentNameArray || index>=componentNameArray.length || index<0){
			return;
		}
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(componentNameArray[index]);
		//Log.i("zheng", ".."+intent.toURI());
		try {
			//Log.i("zheng", "..try");
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException noFound) {
			//Log.i("zheng", "..catch");
		}
	}
	/** 
	 * 已设置的应用的个数
	 * @param      
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	public int getCount(){
		int count;
		if(null==componentNameArray ){
			count = 0;
		}
		else{
			count = componentNameArray.length;
		}
		return count;
	}
	/** 
	 * 得到已设置的应用名称
	 * @param      
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	private void getLockSet(){
		PreferenceInfo info = new PreferenceInfo(context);
		boolean isDefine = info.isDefine();
		
		if(!isDefine){
			//add by zhengwenhui at 15:15
			componentNameArray = new LockSetMultiSelectListPreference(context).getRecentComponents();
			//Log.i("zheng", "if(!isDefine)  ["+componentNameArray.length);
		}
		else{
			String lockSet[] = info.getLockSet();
			String[] nameArray = lockSet[0].split("/");
			String[] packageArray = lockSet[1].split("/");
			
			componentNameArray = new ComponentName[nameArray.length];
			ComponentName componentName;
			
			for(int i=0;i<nameArray.length;i++){
				componentName = new ComponentName(packageArray[i], nameArray[i]);
				componentNameArray[i] = componentName;
			}
		}
	}
}






