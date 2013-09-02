package com.zwh.lockscreen.other;

import java.util.Calendar;

import android.content.Context;

import com.zwh.lockscreen.addition.LunarDate;
import com.zwh.lockscreen.addition.SolarAndLundar;

public class Lunar {
	private volatile static Lunar lunar;
	private SolarAndLundar solarAndLundar;
	private Lunar(Context context){
		solarAndLundar = new SolarAndLundar();
	} 
	public static Lunar getInstance(Context context){
		if(null == lunar){
			synchronized (Lunar.class) {
				lunar = new Lunar(context);
			}
		}
		return lunar;
	}
	
	public String getLundarDate(){
		return getLundarDate(Calendar.getInstance());
	}
	public String getLundarDate(Calendar calendar){
		LunarDate lunarDate = solarAndLundar.sCalendarSolarToLundar(calendar);
		return solarAndLundar.lunarYear(lunarDate.getYear(), lunarDate.getMonth(), lunarDate.getDay());
	}
}
