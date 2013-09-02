package com.zwh.lockscreen.other;

import static android.os.BatteryManager.BATTERY_STATUS_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_FULL;

import com.zwh.lockscreen.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;


public class Battery {
	private volatile static Battery battery;
	private Context context;
	
	private int mBatteryStatus = BATTERY_STATUS_FULL;
	private int mBatteryLevel = 100;
	
	private static final int LOW_BATTERY_THRESHOLD = 20;
	
//	private boolean mPluggedIn;
//	private boolean mShowingBatteryInfo;
	
	private Drawable mChargingIcon;
	private String mCharging;

	private Battery(Context context){
		this.context = context;
	} 
	public static Battery getInstance(Context context){
		if(null == battery){
			synchronized (Battery.class) {
				battery = new Battery(context);
			}
		}
		return battery;
	}
	
	public String getCharging(){
		return mCharging;
	}
	
	public Drawable getChargingIcon(){
		return mChargingIcon;
	}
	
	public void refreshBatteryDisplay(int batteryStatus, int batteryLevel) {
		
		if (isBatteryUpdateInteresting(batteryStatus, batteryLevel)) {
			mBatteryStatus = batteryStatus;
			mBatteryLevel = batteryLevel;
			refreshBatteryStringAndIcon();
		}
	}

	private boolean isBatteryUpdateInteresting(int batteryStatus, int batteryLevel) {
		// change in plug is always interesting
		final boolean isPluggedIn = isPluggedIn(batteryStatus);
		final boolean wasPluggedIn = isPluggedIn(mBatteryStatus);
		final boolean stateChangedWhilePluggedIn =
			wasPluggedIn == true && isPluggedIn == true && (mBatteryStatus != batteryStatus);
		if (wasPluggedIn != isPluggedIn || stateChangedWhilePluggedIn) {
			return true;
		}

		// change in battery level while plugged in
		if (isPluggedIn && mBatteryLevel != batteryLevel) {
			return true;
		}

		if (!isPluggedIn) {
			// not plugged in and below threshold
			if (isBatteryLow(batteryLevel) && batteryLevel != mBatteryLevel) {
				return true;
			}
		}
		return false;
	}
	
	//更新
	private void refreshBatteryStringAndIcon() {
		if (!shouldShowBatteryInfo()) {
			mCharging = null;
			return;
		}

		if (mChargingIcon == null) {
			mChargingIcon =
				context.getResources().getDrawable(R.drawable.ic_lock_idle_charging);
		}

		if (isPluggedIn(mBatteryStatus)) {
			if (isDeviceCharged()) {
				mCharging = context.getString(R.string.lockscreen_charged);
			} else {
				mCharging = context.getString(R.string.lockscreen_plugged_in)+mBatteryLevel+'%';

			}
		} else {
			mCharging = context.getString(R.string.lockscreen_low_battery);
		}
	}
	
	//已充满
	public boolean isDeviceCharged() {
		return mBatteryStatus == BatteryManager.BATTERY_STATUS_FULL
		|| mBatteryLevel >= 100; // in case a particular device doesn't flag it
	}

	
	//当在充电中或者电量低的时候才显示
	public boolean shouldShowBatteryInfo() {
		return isPluggedIn(mBatteryStatus) || isBatteryLow(mBatteryLevel);
	}
	
	//是否在充电中
	private boolean isPluggedIn(int status) {
		return status == BATTERY_STATUS_CHARGING || status == BATTERY_STATUS_FULL;
	}
	
	//是否电量低
	private boolean isBatteryLow(int batteryLevel) {
		return batteryLevel < LOW_BATTERY_THRESHOLD;
	}
}
