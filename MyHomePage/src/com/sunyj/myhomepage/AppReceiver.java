package com.sunyj.myhomepage;

import com.sunyj.help.MySharedPreferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AppReceiver extends BroadcastReceiver {
	private static final String TAG = "AppReceiver";
	private boolean isLog = false;

	private void mLog(String msg) {
		if (isLog)
			Log.i(TAG, msg);
	}

	private MySqliteManager sqlManager;
	private MySharedPreferences mySharedPreferences;

	@Override
	public void onReceive(Context context, Intent intent) {
		sqlManager = new MySqliteManager(context);
		mySharedPreferences = MySharedPreferences.getInstance(context);

		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString();
			mLog("--------add package:-------" + packageName);
			// sqlManager.insertDB(packageName);
			mySharedPreferences.SetBoolean(MySharedPreferences.IsAppChange,
					true);
		} else if (intent.getAction().equals(
				"android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
			mLog("--------remove package-------" + packageName);
			sqlManager.deleteDB(packageName);
			mySharedPreferences.SetBoolean(MySharedPreferences.IsAppChange,
					true);
		}

	}

}
