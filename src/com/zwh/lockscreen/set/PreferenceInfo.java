package com.zwh.lockscreen.set;
/** 
 * 程序设置信息的处理
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class PreferenceInfo {
	private SharedPreferences settings;
	static final String SHOW_CHOICE_DEFAULT_VALUE = "1/2/3/4/5/6";
	private Context context;
	private String choice;

	public PreferenceInfo(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		settings = PreferenceManager.getDefaultSharedPreferences(context); 
	}

	//得到泡泡设置
	@SuppressLint("WorldReadableFiles")
	public String[] getLockSet(){
		String lockSet = settings.getString("lock_set", null);
		
		SharedPreferences preference = context.getSharedPreferences("LockScreenPackageList", Context.MODE_WORLD_READABLE);
		String packageList = preference.getString("packageList", "null");
		
		if(null==lockSet){
			LockSetMultiSelectListPreference l = new LockSetMultiSelectListPreference(context);
			return l.getDefaultString();
		}
		else{
			return new String[]{lockSet,packageList};
		}
	}

	//得到背景图片
	public int getBackgroundPicture(){
		String picture = settings.getString("background_picture", "1");
		// zhengwenhui note
		int pic = Integer.valueOf(picture);
		return pic;
	}
	
	//得到背景图片
	public int getBubble(){
		int pic = settings.getInt("lock_screen_method", 0);
		//int pic = Integer.valueOf(bubble);
		return pic;
	}

	//得到背景颜色
	public int getBackgroundColor(){
		int color = settings.getInt("colorpiker", 0);
		int alpha = settings.getInt("background_color_alpha", 100);
		alpha = (0xff * (100 - alpha)) / 100;
		color = (color & 0xffffff) | (alpha<<24);
		return color;
	}

	//是否显示状态栏
	public boolean showStatusBar(){
		return getChoiceItem("0");
	}

	//是否显示时间
	public boolean showTime(){
		return getChoiceItem("1");
	}
	//是否显示日期
	public boolean showDate(){
		return getChoiceItem("2");
	}
	//是否显示农历日期
	public boolean showLunarDate(){
		return getChoiceItem("3");
	}
	//是否显示星期
	public boolean showWeek(){
		return getChoiceItem("4");
	}
	
	public boolean aboutTime(){
		return showTime()|showDate()|showLunarDate()|showWeek();
	}
	
	//是否显示电池信息（电量、充电时显示"充电中"、过热、电压过高等）
	public boolean showBattery(){
		return getChoiceItem("5");
	}
	//是否显示闹钟
	public boolean showAlarm(){
		return getChoiceItem("6");
	}
	
	private boolean getChoiceItem(String item){
		if(null == choice){
			choice = settings.getString("show_choice", SHOW_CHOICE_DEFAULT_VALUE);	
		}
		return choice.contains(item);
	}

	//显示位置设置
	public int getShowSet(){
		int set = settings.getInt("show_set", 0xffffffff);
		return set;
	}	

	//是否打开了振动
	public boolean isVibrateOpen(){
		boolean open = settings.getBoolean("vibrate", true);
		return open;
	}

	//是否打开了音效
	public boolean isVoiceOpen(){
		boolean open = settings.getBoolean("voice", true);
		return open;
	}
	
	//是否自定义解屏方式
	public boolean isDefine(){
		return settings.getBoolean("set_lock_screen", false);
	}
	
}
