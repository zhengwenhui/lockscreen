package com.zwh.lockscreen.set;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
/** 
 * 多选显示信息项的ListPreference
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */
public class ShowChoiceMultiSelectListPreference extends ListPreference {

	private static final String SEPARATOR = "/";
	private boolean[] itsSelectedItems;
	/**
	 * @param context
	 */
	public ShowChoiceMultiSelectListPreference(Context context)
	{
		this(context, null);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public ShowChoiceMultiSelectListPreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		int len = 0;
		if (getEntries() != null) {
			len = getEntries().length;
		}
		itsSelectedItems = new boolean[len];
	}
	/* (non-Javadoc)
	 * @see android.preference.ListPreference#setEntries(java.lang.CharSequence[])
	 */
	@Override
	public void setEntries(CharSequence[] entries)
	{
		super.setEntries(entries);
		int len = 0;
		if (entries != null) {
			len = entries.length;
		}
		itsSelectedItems = new boolean[len];
	}
	/* (non-Javadoc)
	 * @see android.preference.ListPreference#onPrepareDialogBuilder(android.app.AlertDialog.Builder)
	 */
	@Override
	protected void onPrepareDialogBuilder(Builder builder)
	{
		CharSequence[] entries = getEntries();
		CharSequence[] entryValues = getEntryValues();

		if ((entries == null) || (entryValues == null) ||
				(entries.length != entryValues.length)) {
			throw new IllegalStateException(
					"ListPreference requires an entries array and an entryValues " +
			"array which are both the same length");
		}
		
		String value = getValue(); 
		if (null == value) {
			value = PreferenceInfo.SHOW_CHOICE_DEFAULT_VALUE;
		}
		for (int i = 0; i < itsSelectedItems.length; ++i) {
			itsSelectedItems[i] = value.contains(String.valueOf(i)) ? true:false;
		}

		builder.setMultiChoiceItems( 
				entries, itsSelectedItems,
				new DialogInterface.OnMultiChoiceClickListener()
				{
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked)
					{
						itsSelectedItems[which] = isChecked;
					}
				});
	}
	@Override
	protected void onDialogClosed(boolean positiveResult)
	{
		CharSequence[] entryValues = getEntryValues();
		if (positiveResult && (entryValues != null)) {
			boolean first = true;
			StringBuilder newValue = new StringBuilder();
			for (int i = 0; i < entryValues.length; ++i) {
				if (itsSelectedItems[i]) { 
					if (first) {
						first = false;
					} else {
						newValue.append(SEPARATOR);
					}
					newValue.append(entryValues[i]);
				} 
			}
			String newStr = newValue.toString();
			setValue(newStr);
			callChangeListener(newStr);
		}
	}
}
