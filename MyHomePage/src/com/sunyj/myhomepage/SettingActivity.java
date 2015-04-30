package com.sunyj.myhomepage;

import com.sunyj.help.MySharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.app.Activity;
import android.content.Intent;

public class SettingActivity extends Activity {
	private Boolean isLog = true;// 是否需要打印log
	private String TAG = "sunyj";// log文件的TAG

	/**
	 * 根據需要控制是否打印log
	 * 
	 * @msg 需要輸出的信息
	 */
	private void mLog(String msg) {
		if (isLog)
			Log.d(TAG, msg);
	}

	private CheckBox weather, time, remote;
	private Button appManager;
	private boolean isWeather, isTime, isRemote;
	private MySharedPreferences mySharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLog("onCreate()---------------------------------------");
		setContentView(R.layout.activity_setting);

		mySharedPreferences = MySharedPreferences.getInstance(this);
		findViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLog("onResume()---------------------------------------");

		isWeather = mySharedPreferences.GetBoolean(
				MySharedPreferences.IsWeather, false);
		isTime = mySharedPreferences.GetBoolean(MySharedPreferences.IsTime,
				false);
		isRemote = mySharedPreferences.GetBoolean(MySharedPreferences.IsRemote,
				false);

		weather.setChecked(isWeather);
		time.setChecked(isTime);
		remote.setChecked(isRemote);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLog("onPause()---------------------------------------");

	}

	private void findViews() {
		weather = (CheckBox) findViewById(R.id.weathercheck);
		time = (CheckBox) findViewById(R.id.timecheck);
		remote = (CheckBox) findViewById(R.id.remotecheck);
		appManager = (Button) findViewById(R.id.appmanager);
		appManager.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,
						MyAppManager.class));
			}
		});

		weather.setOnCheckedChangeListener(new myOnCheckedChangeListener());
		time.setOnCheckedChangeListener(new myOnCheckedChangeListener());
		remote.setOnCheckedChangeListener(new myOnCheckedChangeListener());

	}

	private class myOnCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.weathercheck:
				mySharedPreferences.SetBoolean(MySharedPreferences.IsWeather,
						isChecked);

				break;
			case R.id.timecheck:
				mySharedPreferences.SetBoolean(MySharedPreferences.IsTime,
						isChecked);

				break;
			case R.id.remotecheck:
				mySharedPreferences.SetBoolean(MySharedPreferences.IsRemote,
						isChecked);

				break;
			}
		}
	}

}
