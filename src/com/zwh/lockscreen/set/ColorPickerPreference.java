package com.zwh.lockscreen.set;

import com.zwh.lockscreen.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
/** 
 * 选择颜色的DialogPreference
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */
public class ColorPickerPreference extends DialogPreference {
    private int mColor;
    private ColorPickerView mColorPickerView;
    private Context context = null;
    
    public interface OnColorChangedListener {
        void colorChanged(int color);
    }

    public ColorPickerPreference(Context context) {
        this(context, null);
        this.context = context;
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ColorPickerPreference(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            mColor = mColorPickerView.getColor();
            setSummaryColor(mColor);
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(getKey(), mColor);
            
            editor.commit();
            callChangeListener(new Integer(mColor));
        }
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
    	// TODO Auto-generated method stub
    	setSummaryColor(getColor());
		return super.onCreateView(parent);
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        OnColorChangedListener listener = new OnColorChangedListener() {
            public void colorChanged(int color) {
                mColor = color;
                onDialogClosed(true);
                getDialog().dismiss();
            }
        }; 
        LinearLayout layout = new LinearLayout(getContext());
        layout.setPadding(20, 20, 20, 20);
        layout.setOrientation(LinearLayout.VERTICAL);
        mColorPickerView = new ColorPickerView(getContext(), listener, mColor);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mColorPickerView.setLayoutParams(params);
        layout.addView(this.mColorPickerView);
        layout.setId(android.R.id.widget_frame);
        builder.setView(layout);
    }
    private void setSummaryColor(int color){
    	String current_color = context.getResources().getString(R.string.current_color);
    	Spannable summary = new SpannableString (current_color);
    	summary.setSpan( new ForegroundColorSpan(color), 0, summary.length(), 0 ); 
    	this.setSummary( summary );
    }
    private int getColor(){
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        mColor = prefs.getInt(getKey(), Color.LTGRAY);
        return mColor;
    }
}

