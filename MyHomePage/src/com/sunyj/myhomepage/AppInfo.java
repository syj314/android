package com.sunyj.myhomepage;

import android.graphics.drawable.Drawable;

public class AppInfo {
	String AppName;
	String PackageName;
	String ClaseName;
	Drawable Icon;

	  // Ӧ�ó���İ���
    private String packageName;

    // Ӧ�ó��������
    private String appName;

    // Ӧ�ó���İ汾����
    private String versionName = "";

    // Ӧ�ó���İ汾��
    private int versionCode = 0;

    // �����С
    private long cacheSize;

    // ���ݴ�С
    private long dataSize;

    // Ӧ�ó����С
    private long appSize;

    // Ӧ�ó����ܵĴ�С
    private long totalSize;
	
	int ClickNum = 0;
	boolean Visibility = true;
}
