package com.zwh.lockscreen.lock;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.telephony.TelephonyManager;
/** 
 * @param  解屏的广播处理的服务
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class LockService extends Service {
	//use to lock and unlock the keyboard
	private KeyguardManager mKeyguard;
	private KeyguardLock mKeylock;
	private ScreenReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		registerIntentReceivers();
		mKeyguard = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		mKeylock = mKeyguard.newKeyguardLock("Charge");
		mKeylock.disableKeyguard();//屏蔽Keyguard
		super.onCreate();
	}
	class AppIntentReceiver extends BroadcastReceiver { 
		@Override 
		public void onReceive(Context context, Intent intent) { 
		}
	}

	class ScreenReceiver extends BroadcastReceiver {
		@SuppressLint("WorldReadableFiles")
		@Override 
		public void onReceive(Context context, Intent intent) { 
			//摘机状态下不锁屏
			if(!isCalling()){
				SharedPreferences preference = context.getSharedPreferences("LockScreenPackageList", Context.MODE_WORLD_READABLE);
				if(!preference.getBoolean("openLockScreen", false)){
					Intent intentLock=new Intent(context,LockActivity.class);
					intentLock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intentLock); 
					
					Editor editor = preference.edit();
					editor.putBoolean("openLockScreen", false);
					editor.commit();
				}
			}
		}
	}
	/** 
	 * 判断手机当前是否在摘机状态（CALL_STATE_OFFHOOK）
	 * @param      
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */ 
	boolean isCalling(){
		boolean result = false;
		TelephonyManager telephonymanager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonymanager.getCallState()){
		case TelephonyManager.CALL_STATE_OFFHOOK:
			result = true;
			break;
		default:
			result = false;
		}
		return result;
	}
	/** 
	 * 注册监听ACTION_SCREEN_ON ACTION_SCREEN_OFF的广播接收器
	 * @param      
	 * @return    
	 * @exception  
	 * @see        
	 * @since      
	 */
	private void registerIntentReceivers() { 
			receiver = new ScreenReceiver();
//			IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF); 
			IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON); 
			registerReceiver(receiver, filter);
	}  
}
