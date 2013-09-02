package com.zwh.lockscreen.addition;

import com.zwh.lockscreen.set.PreferenceInfo;
import android.content.Context;
import android.os.Vibrator;
/** 
 * 振动
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class Vibrate{
	private Context context;
	private boolean openVibrate = true;//开关振动
	public Vibrate(Context context){ 
		this.context = context;
		//判断是否已开启振动
		openVibrate = new PreferenceInfo(context).isVibrateOpen();
	}
	/** 
	 * 振动
	 * @param      
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	public void vibrate(){
		if(openVibrate){
			@SuppressWarnings("static-access")
			Vibrator vibrator=(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
			vibrator.vibrate(new long[]{0,40}, -1); 
		}
	}
}
