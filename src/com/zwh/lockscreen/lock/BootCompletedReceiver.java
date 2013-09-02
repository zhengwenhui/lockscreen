package com.zwh.lockscreen.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/** 
 * 开机的BroadcastReceiver的类
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class BootCompletedReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context,LockService.class);
		//开启服务
		context.startService(i);
		Intent b=new Intent(context,LockActivity.class);
		b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//开启Activity
		context.startActivity(b); 
	}
}
