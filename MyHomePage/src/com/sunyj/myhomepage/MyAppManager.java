package com.sunyj.myhomepage;

import java.util.List;

import com.sunyj.help.MySharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.app.Activity;

public class MyAppManager extends Activity {
	private String TAG = "sunyj";// log文件的TAG

	private MySqliteManager sqlManager;

	private GridView all_apps;
	/** 排序后的应用列表 */
	private List<AppInfo> appInfos;
	private ManageAppAdapter mAdapter;

	private MySharedPreferences mySharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.i(TAG, "onCreate()---------------------------------------");
		setContentView(R.layout.activity_manager);

		sqlManager = new MySqliteManager(this);
		appInfos = sqlManager.queryAllApps();

		mySharedPreferences = MySharedPreferences.getInstance(this);

		findViews();

		bindAdapter();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyLog.i(TAG, "onResume()---------------------------------------");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyLog.i(TAG, "onPause()---------------------------------------");
	}

	private void findViews() {
		all_apps = (GridView) findViewById(R.id.all_apps);
		all_apps.setOnItemClickListener(new myOnItemClickListener());
	}

	private void bindAdapter() {
		mAdapter = new ManageAppAdapter(MyAppManager.this, appInfos);
		all_apps.setAdapter(mAdapter);
	}

	/** 点击app事件 */
	private class myOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			ListItem item = (ListItem) arg1.getTag();

			AppInfo ai = appInfos.get(arg2);
			ai.Visibility = !ai.Visibility;

			item.isView.setChecked(ai.Visibility);
			sqlManager.updateVisibility(ai);

			mySharedPreferences.SetBoolean(MySharedPreferences.IsAppChange,
					true);
		}
	}

}
