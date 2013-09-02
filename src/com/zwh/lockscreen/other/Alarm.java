package com.zwh.lockscreen.other;

import com.zwh.lockscreen.R;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextUtils;


public class Alarm {
	private volatile static Alarm alarm;
	private Context context;
	private ContentResolver mContentResolver;
	
	private Drawable mAlarmIcon;
	private String nextAlarm;

	private Alarm(Context context){
		this.context = context;
		mContentResolver = context.getContentResolver();
		refreshAlarmDisplay();
	} 
	public static Alarm getInstance(Context context){
		if(null == alarm){
			synchronized (Alarm.class) {
				alarm = new Alarm(context);
			}
		}
		return alarm;
	}
	
    public void refreshAlarmDisplay() {
		nextAlarm = Settings.System.getString(mContentResolver,
				Settings.System.NEXT_ALARM_FORMATTED);
		if (nextAlarm == null || TextUtils.isEmpty(nextAlarm)) {
			nextAlarm = null;
			mAlarmIcon = null;
		}
		else{
			mAlarmIcon = context.getResources().getDrawable(R.drawable.ic_lock_idle_alarm);
		}
    }
    
    public Drawable getAlarmIcon(){
    	return mAlarmIcon;
    }
    
	public String getNextAlarm() {
		return nextAlarm;
	}
}
