package com.sunyj.myhomepage;

import android.graphics.drawable.Drawable;

public class AppInfo {
	String AppName;
	String PackageName;
	String ClaseName;
	Drawable Icon;

	  // 应用程序的包名
    private String packageName;

    // 应用程序的名字
    private String appName;

    // 应用程序的版本名字
    private String versionName = "";

    // 应用程序的版本号
    private int versionCode = 0;

    // 缓存大小
    private long cacheSize;

    // 数据大小
    private long dataSize;

    // 应用程序大小
    private long appSize;

    // 应用程序总的大小
    private long totalSize;
	
	int ClickNum = 0;
	boolean Visibility = true;
}
