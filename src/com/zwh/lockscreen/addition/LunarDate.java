package com.zwh.lockscreen.addition;
/** 
 * 农历日期的类
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class LunarDate {
	private int year;
	private int month;
	private int day;
	private int leapMonth;
	
	
	/** 
	 * 设置农历日期
	 * @param
	 * @return
	 * @exception  
	 * @see        
	 * @since 
	 */
	public void setDate(int year,int month,int day){
		this.year = year;
		this.month = month;
		this.day = day;
	}
	public void setYear(int year){
		this.year = year;
	}
	public void setMonth(int month){
		this.month = month;
	}
	public void setDay(int day){
		this.day = day;
	}
	
	/** 
	 * 设置闰月
	 * @param
	 * @return
	 * @exception  
	 * @see        
	 * @since 
	 */
	public void setLeapMonth(int leapMonth){
		this.leapMonth = leapMonth;
	}
	
	public int getYear(){
		return year;
	}
	public int getMonth(){
		return month;
	}
	public int getDay(){
		return day;
	}
	public int leapMonth(){
		return leapMonth;
	}
}
