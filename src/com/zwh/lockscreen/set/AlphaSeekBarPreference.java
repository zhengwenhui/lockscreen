package com.zwh.lockscreen.set;

import com.zwh.lockscreen.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
/** 
 * 设置透明度的SeekBar DialogPreference
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */
public class AlphaSeekBarPreference extends DialogPreference {
	private SeekBar seekBar;
	private TextView valueTextView;
	//处理OnSeekBarChangeListener
	private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			String percentStr = String.valueOf(progress)+"%";
			valueTextView.setText(percentStr);
		}
	};
	public AlphaSeekBarPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AlphaSeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.onCreateView(parent);
		SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
		
		int percent = prefs.getInt(getKey(), 100);
		setSummary(String.valueOf(percent)+"%");
		return view;
	}
	
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
        	setSummary(String.valueOf(seekBar.getProgress())+"%");
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(getKey(),seekBar.getProgress() );
            editor.commit();
            callChangeListener(new Integer(seekBar.getProgress()));
        }
    }
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		super.onPrepareDialogBuilder(builder);

		LinearLayout layout = new LinearLayout(getContext());
		layout.setPadding(20, 20, 20, 20);
		layout.setOrientation(LinearLayout.VERTICAL);

		SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
		int percent = prefs.getInt(getKey(), 100);
		
		String percentStr = String.valueOf(percent)+"%";
		
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.seekbar_preference, null);
		
		seekBar = (SeekBar)view.findViewById(R.id.alphaBar);
		seekBar.setProgress(percent);
		seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

		valueTextView = (TextView)view.findViewById(R.id.value);
		valueTextView.setText(percentStr);
		
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params1.gravity = Gravity.CENTER;
		view.setLayoutParams(params1);
		layout.addView(view);
		layout.setId(android.R.id.widget_frame);
		builder.setView(layout);
	}
}
