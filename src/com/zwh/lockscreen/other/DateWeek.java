package com.zwh.lockscreen.other;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import com.zwh.lockscreen.R;

public class DateWeek {
	private volatile static DateWeek showDateLunarWeek;
	
	private SimpleDateFormat mDateFormat;
	
	private DateWeek(Context context){
		mDateFormat = new SimpleDateFormat(context.getString(R.string.wday_month_day_no_year));
	} 
	
	public static DateWeek getInstance(Context context){
		if(null == showDateLunarWeek){
			synchronized (DateWeek.class) {
				showDateLunarWeek = new DateWeek(context);
			}
		}
		return showDateLunarWeek;
	}
	
	public String getDateWeek() {
		// TODO Auto-generated method stub
		return mDateFormat.format(new Date());
		
		
	}
	
	public void setDateFormat(String dateFormat){
		mDateFormat = new SimpleDateFormat(dateFormat);
	}
}
