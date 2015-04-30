package com.sunyj.myhomepage;

import android.util.Log;

public class MyLog {
	public final static String TAG = "MyLog";

	public final static boolean DEBUG = true;

	public static void i(String s) {
		if (DEBUG) {
			Log.i(TAG, s);
		}

	}

	public static void i(String tag, String s) {
		if (DEBUG) {
			Log.i(tag, s);
		}
	}

	public static void d(String s) {
		if (DEBUG) {
			Log.d(TAG, s);
		}
	}

	public static void d(String tag, String s) {
		if (DEBUG) {
			Log.d(tag, s);
		}
	}

	public static void e(String s) {
		if (DEBUG) {
			Log.e(TAG, s);
		}
	}

	public static void e(String tag, String s) {
		if (DEBUG) {
			Log.e(tag, s);
		}
	}

}
