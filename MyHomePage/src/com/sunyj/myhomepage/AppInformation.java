package com.sunyj.myhomepage;

import com.sunyj.help.MySharedPreferences;

import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class AppInformation extends Activity {
	private Boolean isLog = true;// �Ƿ���Ҫ��ӡlog
	private String TAG = "sunyj";// log�ļ���TAG

	/**
	 * ������Ҫ�����Ƿ��ӡlog
	 * 
	 * @msg ��Ҫݔ������Ϣ
	 */
	private void mLog(String msg) {
		if (isLog)
			Log.d(TAG, msg);
	}

	private MySharedPreferences mySharedPreferences;

	private Context mContext;
	// Ӧ��ͼ��
	protected ImageView app_icon_iv;
	// ��������
	protected TextView app_name_tv;
	// ����汾
	protected TextView app_version;
	// ������������ܺ�
	protected TextView app_total;
	// �����С
	protected TextView app_size;
	// �������ݴ�С
	protected TextView app_data;
	// ���򻺴��С
	protected TextView app_cache;
	// ��Ӧ�ó���Ĳ���
	protected ListView mAppOperation;
	// ǿ��ֹͣ
	protected Button force_stop_btn;
	// ж��
	protected Button uninstall_btn;
	// �������
	protected Button clear_data_btn;

	// ת��app
	protected Button mMoveApp;
	// ��ʾ֪ͨ 2013-05-10 add by ������ version 1.0
	protected CompoundButton mNotificationSwitch;

	protected RelativeLayout notification_switch_layout;
	// ж�ؽ�����
	protected ProgressBar mProgressBar;
	// ж����ʾ
	protected TextView uninstall;

	protected LinearLayout totalLayout;
	protected LinearLayout appLayout;
	protected LinearLayout dataLayout;
	protected LinearLayout cacheLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLog("onCreate()---------------------------------------");
		setContentView(R.layout.activity_information);

		findViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLog("onResume()---------------------------------------");

	}

	@Override
	protected void onPause() {
		super.onPause();
		mLog("onPause()---------------------------------------");

	}

	private void findViews() {

		// = LayoutInflater.from(TvMainServcie.context)
		// .inflate(R.layout.application_information, null);
		// mAppOperation = (ListView) findViewById(R.id.app_operation_select);

		app_icon_iv = (ImageView) findViewById(R.id.app_icon);
		app_name_tv = (TextView) findViewById(R.id.app_name);
		app_version = (TextView) findViewById(R.id.app_version_tv);
		app_total = (TextView) findViewById(R.id.app_total_size);
		app_size = (TextView) findViewById(R.id.app_size_tv);
		app_data = (TextView) findViewById(R.id.app_data_tv);
		app_cache = (TextView) findViewById(R.id.app_cache_tv);

		force_stop_btn = (Button) findViewById(R.id.force_stop_btn);
		uninstall_btn = (Button) findViewById(R.id.uninstall_btn);
		clear_data_btn = (Button) findViewById(R.id.clear_data_btn);
		mMoveApp = (Button) findViewById(R.id.move_app_btn);
		// mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mProgressBar.setIndeterminate(true);

		uninstall = (TextView) findViewById(R.id.uninstall_progress);

		totalLayout = (LinearLayout) findViewById(R.id.total_ll);
		appLayout = (LinearLayout) findViewById(R.id.app_ll);
		dataLayout = (LinearLayout) findViewById(R.id.data_ll);
		cacheLayout = (LinearLayout) findViewById(R.id.cache_ll);
		// ��ʾ֪ͨ
		mNotificationSwitch = (CompoundButton) findViewById(R.id.notification_switch);
		notification_switch_layout = (RelativeLayout) findViewById(R.id.notification_switch_layout);
	}
	
	private void BindData(){
//		app_name_tv.setText(mAppInfor.getAppName());
//		app_version.setText(mAppInfor.getVersionName());
//		app_total.setText(formateFileSize(mAppInfor.getTotalSize()));
//		app_size.setText(formateFileSize(mAppInfor.getAppSize()));
//		app_data.setText(formateFileSize(mAppInfor.getDataSize()));
//		app_cache.setText(formateFileSize(mAppInfor.getCacheSize()));
	}

	/**
	 * ϵͳ�������ַ���ת�� long -String (kb)
	 * 
	 * @param size
	 * @return
	 */
	private String formateFileSize(long size) {
		if (size > 0) {
			return Formatter.formatFileSize(mContext, size);
		} else {
			return "0KB";
		}
	}

}
