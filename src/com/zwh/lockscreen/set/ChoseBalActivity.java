package com.zwh.lockscreen.set;

import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;

import com.zwh.lockscreen.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class ChoseBalActivity extends Activity implements PointsChangeNotify{

	private int current;
	private int[] ids={
			R.id.b_layout,
			R.id.e_layout,
			R.id.g_layout,
			R.id.j_layout,
			R.id.f_layout,
			R.id.h_layout,
			R.id.i_layout,
	};

	private View[] iconViews = new View[7];
	private boolean[] openIt = new boolean[7];
	private TextView offersWallTV;
	private SharedPreferences mSharedPreferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chose_ball);

		PointsManager.getInstance(this).registerNotify(this);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		if( mSharedPreferences.getBoolean("first", true) ){
			Editor editor = mSharedPreferences.edit();
			editor.putBoolean("first", false);
			editor.putBoolean(String.valueOf(ids[0]), true);
			editor.commit();
			PointsManager.getInstance(this).awardPoints(30);
		}
		else{
			PointsManager.getInstance(this).awardPoints(1);
		}

		for(int i = 0; i < ids.length; i++){
			iconViews[i] = findViewById(ids[i]).findViewById(R.id.radio);
			openIt[i] = mSharedPreferences.getBoolean(String.valueOf(ids[i]), false);
			if( !openIt[i] ){
				iconViews[i].setBackgroundResource(R.drawable.lock);
			}
		}

		current = mSharedPreferences.getInt("lock_screen_method", 0);
		iconViews[current].setBackgroundResource(R.drawable.select);

		offersWallTV = (TextView)findViewById(R.id.offers_wall_textview);
		int myPointBalance = PointsManager.getInstance(this).queryPoints();
		onPointBalanceChange(myPointBalance);
	}

	public void onClickItem(View view){
		int id = view.getId();

		if ( id == R.id.offers_wall){
			//调用showOffersWall显示全屏的积分墙界面
			OffersManager.getInstance(this).showOffersWall();
			return;
		}

		int i;
		for( i = 0; i < ids.length; i++){
			if( id == ids[i]){
				break;
			}
		}

		if( i >= ids.length ){
			return;
		}
		
		if( !openIt[i] ){
			if(PointsManager.getInstance(this).spendPoints(30)){           
				Editor editor = mSharedPreferences.edit();
				editor.putInt("lock_screen_method", i);
				editor.putBoolean(String.valueOf(ids[i]), true);
				editor.commit();
				
				iconViews[current].setBackgroundResource(0);
				iconViews[i].setBackgroundResource(R.drawable.select);
				current = i;
				
			}else{
				//Toast.makeText(this, R.string.lostfen, Toast.LENGTH_SHORT).show();
				OffersManager.getInstance(this).showOffersWall();
			}
		}
		else{
			iconViews[current].setBackgroundResource(0);
			iconViews[i].setBackgroundResource(R.drawable.select);
			current = i;

			Editor editor = mSharedPreferences.edit();
			editor.putInt("lock_screen_method", i);
			editor.commit();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		PointsManager.getInstance(this).unRegisterNotify(this);
		super.onDestroy();
	}

	@Override
	public void onPointBalanceChange(int pointsBalance) {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getString(R.string.lost));
		stringBuilder.append(pointsBalance);
		stringBuilder.append(getString(R.string.fen));

		offersWallTV.setText(stringBuilder.toString());
	}
}
