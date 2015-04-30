package com.sunyj.help;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPreferences {
	private static MySharedPreferences body = null;
	private static Context mContext;

	private SharedPreferences preferences;
	private Editor editor;

	public static String IsWeather = "isWeather", IsTime = "isTime",
			IsRemote = "isRemote", IsAppChange = "isAppChange";

	private MySharedPreferences() {
		preferences = mContext.getSharedPreferences("settings", 0);
		editor = preferences.edit();
	}

	public static MySharedPreferences getInstance(Context context) {
		mContext = context;
		
		if (body == null)
			body = new MySharedPreferences();

		return body;
	}

	public void SetString(String name, String value) {
		editor.putString(name, value).commit();
	}

	public String GetString(String name, String defaultValue) {
		return preferences.getString(name, defaultValue);
	}

	public void SetInt(String name, int value) {
		editor.putInt(name, value).commit();
	}

	public int GetInt(String name, int defaultValue) {
		return preferences.getInt(name, defaultValue);
	}

	public void SetBoolean(String name, Boolean value) {
		editor.putBoolean(name, value).commit();
	}

	public Boolean GetBoolean(String name, boolean defaultValue) {
		return preferences.getBoolean(name, defaultValue);
	}

}
