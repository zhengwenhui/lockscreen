package com.zwh.lockscreen.set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zwh.lockscreen.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
/** 
 * 多选ListPreference
 * @param
 * @author 郑文辉
 * @Date   2012年1月9日
 */
public class LockSetMultiSelectListPreference extends ListPreference {
	private static final String SEPARATOR = "/";
	private boolean[] selectedItems;
	private String[] labelArray;
	private List<ResolveInfo> apps;
	private List<String> nameList;
	private List<String> pkgList;
	private final int MAX_COUNT = 5;
	private final int MAX_RECENT_TASKS = 5;
	private final int MAX_DEFAULT_COUNT = 5;
	private Context context;
	private int selectedCount = 0;

	/**
	 * @param context
	 */
	public LockSetMultiSelectListPreference(Context context)
	{
		this(context, null);
		this.context = context;
		//getLabelArray(context);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public LockSetMultiSelectListPreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		//etLabelArray(context);
	}

	public String[] getDefaultString(){
		final PackageManager packageManager = context.getPackageManager();
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// get all apps 
		List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
		ResolveInfo app;
		StringBuilder nameBuilder = new StringBuilder();
		StringBuilder pkgNameBuilder = new StringBuilder();
		nameBuilder.append("main");
		pkgNameBuilder.append("main");
		for (int i = 0; i<apps.size() && i<MAX_DEFAULT_COUNT-1; ++i) {
			app = apps.get(i);
			nameBuilder.append(SEPARATOR);
			pkgNameBuilder.append(SEPARATOR);
			nameBuilder.append(app.activityInfo.name);
			pkgNameBuilder.append(app.activityInfo.packageName);
		}

		String name = nameBuilder.toString();
		String pkgName = pkgNameBuilder.toString();
		return new String[]{name,pkgName}; 
	}

	public ComponentName[] getRecentComponents(){
		final ActivityManager am = 
			(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		
		final List<RecentTaskInfo> recentTasks =
			am.getRecentTasks(11, ActivityManager.RECENT_WITH_EXCLUDED );

		getLabelArray(context);
		
		final ArrayList<ComponentName> cnList = new ArrayList<ComponentName>();
		
		cnList.add(new ComponentName(context, "main"));
		
		for(RecentTaskInfo info : recentTasks){
			if(null!=info.baseIntent&&null!=info.baseIntent.getComponent()){
				if(nameList.contains(info.baseIntent.getComponent().getClassName())){
					cnList.add(info.baseIntent.getComponent());
					if(cnList.size()>=5){
						break;
					}
				}
			}
		}
		ComponentName[] cn = new ComponentName[cnList.size()];
		return cnList.toArray(cn);
	}

	private void getLabelArray(Context context){
		final PackageManager packageManager = context.getPackageManager();
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// get all apps 
		apps = packageManager.queryIntentActivities(mainIntent, 0);
		labelArray = new String[apps.size()+1];
		selectedItems = new boolean[apps.size()+1];
		nameList = new ArrayList<String>(); 
		pkgList = new ArrayList<String>(); 
		
		ResolveInfo app;
		String label;
		String name;

		nameList.add("main");
		pkgList.add("main");
		labelArray[0] = context.getResources().getString(com.zwh.lockscreen.R.string.main_screen);

		for(int i = 1; i<labelArray.length; i++){
			app = apps.get(i-1);
			label = (String) app.loadLabel(packageManager);
			labelArray[i] = label;

			name = app.activityInfo.name;
			nameList.add(name);
			name = app.activityInfo.packageName;
			pkgList.add(name);
		}
		//将 recent tasks 调换到前面
		aheadRecentTasks();
	}

	private void aheadRecentTasks(){
		final ActivityManager am = 
			(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RecentTaskInfo> recentTasks =
			am.getRecentTasks(MAX_RECENT_TASKS+1, ActivityManager.RECENT_WITH_EXCLUDED);

		int to = 1,from = 0;
		for(ActivityManager.RecentTaskInfo info : recentTasks){
			if(null!=info.baseIntent){
				from = nameList.indexOf(info.baseIntent.getComponent().getClassName());
				if(from>=0){
					exchange(from, to++);
				}

				if(to>=MAX_RECENT_TASKS){
					break;
				}
			}
		}
	}

	private void exchange(int from, int to){
		String m = nameList.remove(from);
		nameList.add(to, m);
		
		m = pkgList.remove(from);
		pkgList.add(to, m);

		m = labelArray[from];
		for(int i = from; i>to;){
			labelArray[i] = labelArray[--i];
		}
		labelArray[to] = m;
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder)
	{
		getLabelArray(context);
		String value = getValue();
		String[] values = null;
		int index = 0;

		if(null == value){
			value = getDefaultString()[0];
		}

		values = value.split(SEPARATOR);
		selectedCount = 0;
		for(String v:values){
			index = nameList.indexOf(v);
			if(index>=0){
				selectedItems[index] = true;
				selectedCount++;
			}
		}
		builder.setView(getListView());
	}

	private ListView getListView(){
		ListView view = new ListView(context);
		view.setCacheColorHint(Color.WHITE);

		ArrayList<HashMap<String, Object>> users = new ArrayList<HashMap<String, Object>>();

		for(int i = 0;i<selectedItems.length;i++){
			HashMap<String, Object> user = new HashMap<String, Object>();
			user.put("apps_name", labelArray[i]);
			user.put("if_open", selectedItems[i]);
			users.add(user);
		}

		SimpleAdapter saImageItems = new Adapter(context,
				users,
				R.layout.list_view_item,
				new String[] { "apps_name", "if_open" },
				new int[] {  R.id.apps_name, R.id.if_open }, 
				selectedItems);
		view.setAdapter(saImageItems);
		view.setBackgroundColor(Color.WHITE);
		view.setOnItemClickListener(onItemClickListener);
		return view;
	} 

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			CheckBox ifOpen = (CheckBox)arg1.findViewById(R.id.if_open);
			boolean current = !ifOpen.isChecked();
			if(true==current){
				//				if(0==position){
				//					
				//				}
				//				else 
				if(selectedCount>=MAX_COUNT){
					String text = context.getString(R.string.toast_warning);
					Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
				}
				else{
					selectedCount++;
					ifOpen.setChecked(true);
					selectedItems[position]=true;
				}
			}
			else{
				//				if(0==position){
				//					
				//				}
				//				else
				{
					selectedCount--;
					ifOpen.setChecked(false);
					selectedItems[position]=false;
				}
			}
		}
	};

	@Override
	protected void onDialogClosed(boolean positiveResult)
	{
		if(positiveResult){
			int count = 0;
			boolean first = true;
			StringBuilder itemsBuilder = new StringBuilder();
			StringBuilder pkgitemsBuilder = new StringBuilder();
			for (int i = 0; i < selectedItems.length; ++i) {
				if (selectedItems[i]) { 
					if (first) {
						first = false;
					}
					else {
						itemsBuilder.append(SEPARATOR);
						pkgitemsBuilder.append(SEPARATOR);
					}
					itemsBuilder.append(nameList.get(i));
					pkgitemsBuilder.append(pkgList.get(i));
					count++;
					if(count>=MAX_COUNT){
						break;
					}
				}
			}

			SharedPreferences preference = context.getSharedPreferences("LockScreenPackageList", Context.MODE_WORLD_READABLE);
			Editor editor = preference.edit();
			editor.putString("packageList", pkgitemsBuilder.toString());
			editor.commit();

			String items = itemsBuilder.toString();
			setValue(items);
			callChangeListener(items);
		}
	}
}

