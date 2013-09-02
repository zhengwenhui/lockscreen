package com.zwh.lockscreen.addition;

import com.zwh.lockscreen.R;
import com.zwh.lockscreen.set.PreferenceInfo;
import android.content.Context;
import android.media.MediaPlayer;
/** 
 * 播放音频的类
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */ 
public class PlaySound {
	private Context context;
	private boolean isOpen = true;     //音效是否打开
	public PlaySound(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		//判断音效是否打开
		isOpen = new PreferenceInfo(context).isVoiceOpen();     
	}
	/** 
	 * 开始播放
	 * @param
	 * @return
	 * @exception  
	 * @see        
	 * @since 
	 */
	public void startPlayer(){
		if(isOpen){
			MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.open);
			mediaPlayer.start();
		}
	}
}
