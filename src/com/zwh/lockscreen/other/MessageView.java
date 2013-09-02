package com.zwh.lockscreen.other;

import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwh.lockscreen.R;
import com.zwh.lockscreen.set.PreferenceInfo;

public class MessageView extends RelativeLayout{
	private TextView mDate;
	private TextView mStatus1;
	private TextView mStatus2;
	private TextView mTimeTextViwe;
	private TextView mAmPm;
	private TextView mCarrier;

	private Battery battery;
	private Alarm alarm;
	private DateWeek dateweek;
	private Lunar lunar;
	private Time time;

	private Drawable mChargingIcon;
	private String mCharging;
	private String nextAlarm; 
	private Drawable mAlarmIcon;

	private boolean mAttached;
	private Context mContext;
	private Handler handler = new RefreshHandlerLock();
	private static final int MSG_TIME_UPDATE = 301;
	private static final int MSG_BATTERY_UPDATE = 302;

	private PreferenceInfo info;

	public MessageView(Context context) {
		super(context);
		mContext = context; 
	}
	public MessageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context; 
	}
	public MessageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context; 
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		info = new PreferenceInfo(mContext);

		mCarrier = (TextView) findViewById(R.id.carrier);
		mTimeTextViwe = (TextView) findViewById(R.id.timeDisplay);
		mAmPm = (TextView) findViewById(R.id.am_pm);
		mDate = (TextView) findViewById(R.id.date);
		mStatus1 = (TextView)findViewById(R.id.status1);
		mStatus2 = (TextView)findViewById(R.id.status2);

		battery = Battery.getInstance(mContext);
		alarm = Alarm.getInstance(mContext);
		dateweek = DateWeek.getInstance(mContext);
		setDateFormat();
		
		lunar = Lunar.getInstance(mContext);
		time = Time.getInstance(mContext);
		RefreshAlarm();
		time.setShowAmPm(mAmPm);
		setTextColor();
		handleTimeUpdate();
	}

	private void setDateFormat(){
		int id = 0;
		if(info.showDate()&&info.showWeek()){
			id = R.string.wday_month_day_no_year;

		}else if(info.showDate()){
			id = R.string.month_day_no_year;
		}
		else if(info.showWeek()){
			id = R.string.wday;
		}
		if(id!=0){
			dateweek.setDateFormat(mContext.getResources().getString(id));
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mAttached) return;
		mAttached = true;
		registerReceiver();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (!mAttached) return;
		mAttached = false;
		mContext.unregisterReceiver(receiver);
	}

	private void registerReceiver(){
		final IntentFilter filter = new IntentFilter();

		if(info.aboutTime()){
			filter.addAction(Intent.ACTION_TIME_TICK);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);	
		}
		if(info.showBattery()){
			filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		}

		/*filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);*/

		mContext.registerReceiver(receiver, filter);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (Intent.ACTION_TIME_TICK.equals(action)
					|| Intent.ACTION_TIME_CHANGED.equals(action)
					|| Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
				handler.sendMessage(handler.obtainMessage(MSG_TIME_UPDATE));
			}else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				final int pluggedInStatus = intent.getIntExtra("status", BATTERY_STATUS_UNKNOWN);
				int batteryLevel = intent.getIntExtra("level", 0);
				final Message msg = handler.obtainMessage(
						MSG_BATTERY_UPDATE,
						pluggedInStatus,
						batteryLevel);
				handler.sendMessage(msg);
			}
			/*else if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(action)) {
				handler.sendMessage(handler.obtainMessage(MSG_RINGER_MODE_CHANGED,
                        intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1), 0));
            }else if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                handler.sendMessage(handler.obtainMessage(MSG_PHONE_STATE_CHANGED, state));
            }*/
		}
	};
	@SuppressLint("HandlerLeak")
	class RefreshHandlerLock extends Handler {
		@Override
		public void handleMessage(Message message){
			switch (message.what) {
			case MSG_BATTERY_UPDATE:
				battery.refreshBatteryDisplay(message.arg1, message.arg2);
				if(null!=battery.getCharging()){
					mCharging = battery.getCharging();
					mChargingIcon = battery.getChargingIcon();
					updateStatusLines();
				}
				break;
			case MSG_TIME_UPDATE:
				handleTimeUpdate();
				break;
				/*case MSG_PHONE_STATE_CHANGED:
				handleTimeUpdate();
				break;
			case MSG_RINGER_MODE_CHANGED:
				handleTimeUpdate();
				break;*/
			}

			super.handleMessage(message);
		}
	}

	private void RefreshAlarm(){
		if(info.showAlarm()){	
			alarm.refreshAlarmDisplay();
			nextAlarm = alarm.getNextAlarm();
			mAlarmIcon = alarm.getAlarmIcon();
			updateStatusLines();
		}
	}
	
	private void updateStatusLines() {
		if ((mCharging == null && nextAlarm == null)) {
			mStatus1.setVisibility(View.INVISIBLE);
			mStatus2.setVisibility(View.INVISIBLE);
		} else if (mCharging != null && nextAlarm == null) {
			// charging only
			mStatus1.setVisibility(View.VISIBLE);
			mStatus2.setVisibility(View.INVISIBLE);

			mStatus1.setText(mCharging);
			mStatus1.setCompoundDrawablesWithIntrinsicBounds(mChargingIcon, null, null, null);
		} else if (nextAlarm != null && mCharging == null) {
			// next alarm only
			mStatus1.setVisibility(View.VISIBLE);
			mStatus2.setVisibility(View.INVISIBLE);

			mStatus1.setText(nextAlarm);
			mStatus1.setCompoundDrawablesWithIntrinsicBounds(mAlarmIcon, null, null, null);
		} else if (mCharging != null && nextAlarm != null) {
			// both charging and next alarm
			mStatus1.setVisibility(View.VISIBLE);
			mStatus2.setVisibility(View.VISIBLE);
			mStatus1.setText(mCharging);
			mStatus1.setCompoundDrawablesWithIntrinsicBounds(mChargingIcon, null, null, null);
			mStatus2.setText(nextAlarm);
			mStatus2.setCompoundDrawablesWithIntrinsicBounds(mAlarmIcon, null, null, null);
		}
	}
	private void handleTimeUpdate() {
		// TODO Auto-generated method stub
		
		String str = "";
		if(info.showDate()||info.showWeek()){
			str += dateweek.getDateWeek();
			if(info.showLunarDate()){
				str += ',';
				str += lunar.getLundarDate();
			}
		}
		else if(info.showLunarDate()){
			str = lunar.getLundarDate();
		}
		mDate.setText(str);

		if(info.showTime()){
			mTimeTextViwe.setText(time.getTime());
			mAmPm.setText(time.getAmPm());
		}
	}
	
	private void setTextColor(){
		int color = info.getShowSet();
		
		mCarrier.setTextColor(color); 
		mTimeTextViwe.setTextColor(color);  
		mAmPm.setTextColor(color); 
		mDate.setTextColor(color); 
		mStatus1.setTextColor(color); 
		mStatus2.setTextColor(color); 
	}
}
