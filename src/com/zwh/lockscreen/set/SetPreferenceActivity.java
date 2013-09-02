package com.zwh.lockscreen.set;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.zwh.lockscreen.R;
import com.zwh.lockscreen.lock.LockService;
/** 
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */
public class SetPreferenceActivity extends PreferenceActivity 
implements OnPreferenceClickListener,OnPreferenceChangeListener 
{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//所的的值将会自动保存到SharePreferences
		addPreferencesFromResource(R.xml.set_preference);

		Preference restoreDefaults = (Preference)findPreference("restore_defaults");
		restoreDefaults.setOnPreferenceClickListener(this);

		ListPreference background_picture = (ListPreference)findPreference("background_picture");
		background_picture.setOnPreferenceChangeListener(this);
		
		Preference lock_screen_method = (Preference)findPreference("lock_screen_method");
		lock_screen_method.setOnPreferenceClickListener(this);

		//YoumiPush.startYoumiPush(this, "09bc7b784fd44589", "f7f08e66cf3c39c8", false);
		// 初始化应用ID和密钥，以及设置测试模式
        AdManager.getInstance(this).init("09bc7b784fd44589","f7f08e66cf3c39c8", false); 
        // 请务必调用以下代码，告诉SDK应用启动，可以让SDK进行一些初始化操作。
        OffersManager.getInstance(this).onAppLaunch(); 
		
		Intent intent=new Intent(this,LockService.class);
		startService(intent);
	}

	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		if(preference.getKey().equals("restore_defaults")){
			getAlertDialog().show();
		}
		else if (preference.getKey().equals("lock_screen_method")){
			//start a activity
			startActivity(new Intent(this, ChoseBalActivity.class));
		}
		return false;
	}
	
	private AlertDialog getAlertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setMessage(R.string.are_you_sure)
		.setNegativeButton(android.R.string.cancel, null)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@SuppressLint("WorldReadableFiles")
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor mPreEditor = 
					getPreferenceManager().getSharedPreferences().edit(); 
				mPreEditor.clear();
				//恢复默认设置
				mPreEditor.commit();
				
				SharedPreferences pre = getSharedPreferences("LockScreenPackageList", Context.MODE_WORLD_READABLE);
				Editor editor = pre.edit();
				editor.putBoolean("openLockScreen", false);
				editor.commit();
				
				SetPreferenceActivity.this.finish();
			}
		});
		
		return builder.create();
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		if(newValue.equals("4")){
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 1);
		}
		return true;
	}

	@SuppressLint("WorldReadableFiles")
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if (resultCode == RESULT_OK) { 
			Uri uri = data.getData(); 

			SharedPreferences preference = this.getSharedPreferences("LockScreenPackageList", Context.MODE_WORLD_READABLE);
			Editor editor = preference.edit();
			editor.putString("background_picture", uri.toString());
			editor.commit();
		} 
		super.onActivityResult(requestCode, resultCode, data); 
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		OffersManager.getInstance(this).onAppExit(); 
	}
}
