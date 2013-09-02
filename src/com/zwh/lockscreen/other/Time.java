package com.zwh.lockscreen.other;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;


public class Time {
	private volatile static Time time;
	private Context context;
	private final static String M12 = "h:mm";
	private final static String M24 = "kk:mm";
	private String mAmString, mPmString;
	private Calendar mCalendar;
	private String mFormat;
	//private Context mContext = null;

	private Time(Context context){
		this.context = context;
		
        String[] ampm = new DateFormatSymbols().getAmPmStrings();
        mAmString = ampm[0];
        mPmString = ampm[1];
        
        mCalendar = Calendar.getInstance();
        setDateFormat();
	} 
	public static Time getInstance(Context context){
		if(null == time){
			synchronized (Time.class) {
				time = new Time(context);
			}
		}
		return time;
	}
	
	public CharSequence getTime() {
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		return DateFormat.format(mFormat, mCalendar);
	}
	public String getAmPm() {
		boolean isMorning = mCalendar.get(Calendar.AM_PM) == 0;
		return isMorning ? mAmString : mPmString;
	}

	public void setDateFormat() {
		mFormat = android.text.format.DateFormat.is24HourFormat(context) 
		? M24 : M12;
	}
	
    public void setShowAmPm(View mAmPm) {
    	boolean show = mFormat.equals(M12);
        mAmPm.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
