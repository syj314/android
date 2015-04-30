package com.sunyj.myhomepage;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;
import com.sunyj.help.MyFocusView;
import com.sunyj.help.MySharedPreferences;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class MainActivity extends Activity {
	private Boolean isLog = true;// �Ƿ���Ҫ��ӡlog
	private String TAG = "myhome";// log�ļ���TAG

	/**
	 * ������Ҫ�����Ƿ��ӡlog
	 * 
	 * @msg ��Ҫݔ������Ϣ
	 */
	private void mLog(String msg) {
		if (isLog)
			Log.d(TAG, msg);
	}

	private MySqliteManager sqlManager;

	private List<AppInfo> sysList;
	private List<AppInfo> visibileAppList;

	private GridView homePage;
	private PackageManager mPackageManager;
	/** δ�����Ӧ���б� */
	// private List<ResolveInfo> resolveInfos;
	private List<AppInfo> appInfos;
	/** ������Ӧ���б� */
	// private List<ResolveInfo> orderResolveInfos;
	private ShowAppAdapter mAdapter;

	private TextView textCity, textTemp, dataTime, textWeather;
	private ImageView image1, image2;
	private Time localTime = new Time();
	private JSONObject weather;
	/** ��־���Ƿ����°�װpak */
	private boolean hasNewApk = false;
	/** ��־�����ݿ����Ƿ�����ɾ��pak */
	private boolean hasDeletedApk = false;

	private Intent mainIntent;
	private MyFocusView myFocusView;

	private MySharedPreferences mySharedPreferences;

	private boolean isWeather, isTime, isRemote, isAppChange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLog("onCreate()---------------------------------------");
		setContentView(R.layout.activity_main);

		mySharedPreferences = MySharedPreferences.getInstance(this);

		mPackageManager = this.getPackageManager();
		sqlManager = new MySqliteManager(MainActivity.this);

		mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		findViews();

		bindAdapter();

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
		isAppChange = mySharedPreferences.GetBoolean(
				MySharedPreferences.IsAppChange, false);

		if (isNetworkConnected(this)) {
			if (isTime) {
				handler.removeCallbacks(runnable);
				handler.postDelayed(runnable, 1);
			} else {
				handler.removeCallbacks(runnable);
				dataTime.setText("");
			}
			if (isWeather) {
				getWeatherInfo();
			} else {
				textCity.setText("");
				textTemp.setText("");
				textWeather.setText("");

				image1.setImageDrawable(null);
				image2.setImageDrawable(null);
			}
			Intent i = new Intent(MainActivity.this, RemoteReceiver.class);
			if (isRemote) {
				startService(i);
			} else {
				stopService(i);
			}
		}

		if (isAppChange) {
			bindAdapter();

			mySharedPreferences.SetBoolean(MySharedPreferences.IsAppChange,
					false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLog("onPause()---------------------------------------");
		// ֹͣ��ʱ
		handler.removeCallbacks(runnable);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)
				&& event.getRepeatCount() == 0) {
			mLog("onKeyDown()");
			return true;
		}
		// else if (keyCode == KeyEvent.KEYCODE_MENU
		// && event.getAction() == KeyEvent.ACTION_DOWN) {
		//
		// startActivity(new Intent(MainActivity.this, SettingActivity.class));
		// }

		return super.onKeyDown(keyCode, event);
	}

	private void findViews() {
		homePage = (GridView) findViewById(R.id.homepage);

		homePage.setOnItemSelectedListener(new myOnItemSelectedListener());
		homePage.setOnItemClickListener(new myOnItemClickListener());
		homePage.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					int index = homePage.getSelectedItemPosition() + 1;

					if (index == homePage.getCount()) {
						homePage.setSelection(0);
					} else if (index % 6 == 0) {
						homePage.setSelection(index);
					}

				} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					int index = homePage.getSelectedItemPosition() + 1;

					if (index == 1) {
						homePage.setSelection(homePage.getCount() - 1);
					} else if (index % 6 == 1 && index > 1) {
						homePage.setSelection(index - 2);
					}
				} else if (keyCode == KeyEvent.KEYCODE_MENU
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					PopupMenu pop = new PopupMenu(MainActivity.this, homePage
							.getSelectedView());
					pop.inflate(R.menu.menu);
					pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							switch (item.getItemId()) {
							case R.id.information:
								startActivity(new Intent(MainActivity.this,
										AppInformation.class));
								return true;
							case R.id.setting:
								startActivity(new Intent(MainActivity.this,
										SettingActivity.class));
								return true;
							default:
								return false;
							}
						}
					});
					pop.show();
					return true;
				}
				return false;
			}
		});

		textCity = (TextView) findViewById(R.id.city);
		textTemp = (TextView) findViewById(R.id.wendu);
		dataTime = (TextView) findViewById(R.id.datatime);
		textWeather = (TextView) findViewById(R.id.weather);
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		myFocusView = (MyFocusView) findViewById(R.id.myfocusview);
	}

	private void bindAdapter() {
		MyLog.i(TAG, "bindAdapter() ");

		UpdateData();

		// MyLog.i(TAG, "visibileAppList��size() = " + visibileAppList.size());
		mAdapter = new ShowAppAdapter(MainActivity.this, visibileAppList);
		homePage.setAdapter(mAdapter);

		// handler.sendEmptyMessageDelayed(2, 1000);
		// myFocusView.beginScroll(mAdapter.getView(0, null, homePage));
	}

	/** ��ȡ���п���ʾapp����ת��ΪAppInfo�б� */
	private List<AppInfo> getViewApps() {
		MyLog.i(TAG, "getViewApps() ");
		sysList = new ArrayList<AppInfo>();
		List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(
				mainIntent, PackageManager.GET_ACTIVITIES);
		for (ResolveInfo info : resolveInfos) {
			AppInfo ai = new AppInfo();
			ai.AppName = info.loadLabel(mPackageManager).toString();
			ai.PackageName = info.activityInfo.packageName;
			ai.Icon = info.loadIcon(mPackageManager);
			ai.ClickNum = 0;
			ai.Visibility = true;
			sysList.add(ai);
		}
		return sysList;
	}

	/** ��һ�� ����ʱ���������ݿ� */
	private void UpdateData() {
		visibileAppList = new ArrayList<AppInfo>();
		getViewApps();
		List<AppInfo> dbList = sqlManager.queryAllApps();
		/* ��ʱ�洢���ݿ� ���ڶԱ�dbList����DB�Ƿ����� */
		List<AppInfo> tempDBList = new ArrayList<AppInfo>();
		// �������ݿ�
		if (sysList.size() > 0) {
			for (int i = 0; i < sysList.size(); i++) {
				boolean isInDB = false;
				for (int j = 0; j < dbList.size(); j++) {
					// MyLog.i(TAG, "list.get(i).PackageName ="
					// + list.get(i).PackageName);
					if (sysList.get(i).PackageName
							.equals(dbList.get(j).PackageName)) {

						sysList.get(i).ClickNum = dbList.get(j).ClickNum;
						sysList.get(i).Visibility = dbList.get(j).Visibility;

						addToList(sysList.get(i));
						tempDBList.add(dbList.get(j));
						isInDB = true;
						break;
					}
				}

				if (isInDB == false) {
					MyLog.i(TAG, "isInDB == false");

					sqlManager.insertDB(sysList.get(i));
					visibileAppList.add(sysList.get(i));
				}
			}

			if (tempDBList.size() < dbList.size()) {
				// ����DB����
				for (AppInfo ai : dbList) {
					if (tempDBList.indexOf(ai) < 0) {
						sqlManager.updateClickNum(ai);
					}
				}

			}
		}
	}

	/** �ж� list ���Ƿ������ͬ�� */
	private void addToList(AppInfo ai) {
		// MyLog.i(TAG, "addToList");
		if (ai.Visibility == false) {
			// MyLog.i(TAG, "Visibility= false");
			return;
		}

		// MyLog.i(TAG, "ai.AppName = " + ai.AppName);
		if (visibileAppList.size() == 0) {
			visibileAppList.add(ai);
			// MyLog.i(TAG, "size() == 0 AppName = " + ai.AppName);
		} else {
			for (int i = 0; i < visibileAppList.size(); i++) {
				if (visibileAppList.get(i).ClickNum <= ai.ClickNum) {
					visibileAppList.add(i, ai);
					// MyLog.i(TAG, "if AppName = " + ai.AppName);
					break;
				} else {
					if (i == visibileAppList.size() - 1) {
						visibileAppList.add(ai);
						// MyLog.i(TAG, "else AppName = " + ai.AppName);
						break;
					}
				}
			}
		}
	}

	/** ���app�¼� */
	private class myOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			AppInfo info = mAdapter.getItem(position);
			// ȡ������İ���
			Intent i = mPackageManager
					.getLaunchIntentForPackage(info.PackageName);
			// ����ó��򲻿���������ϵͳ�Դ��İ����кܶ���û����ڵģ��᷵��NULL
			if (i != null) {
				MainActivity.this.startActivity(i);
				// Intent i1 = mAppInfo.getIntent();
				// mContext.startActivity(i1);

				sqlManager.updateClickNum(info);
			} else
				Toast.makeText(MainActivity.this, "�����޷�����", Toast.LENGTH_SHORT)
						.show();
		}
	}

	private class myOnItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// requestFocus(arg1);
			myFocusView.beginScroll(arg1);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	}

	public ImageView focusImage = null;

	// public void requestFocus(View v) {
	// mLog("requestFocus()");
	// if (focusImage == null) {
	// focusImage = (ImageView) this.findViewById(R.id.focusview);
	// }
	// FocusSettings fs = new FocusSettings(focusImage, v);
	// try {
	// fs.onAnimationFocusChange(true);
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }

	// ����Handler����
	private Handler handler = new Handler() {
		@Override
		// ������Ϣ���ͳ�����ʱ���ִ��Handler���������
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (localTime != null) {
					dataTime.setText("" + localTime.year + "-"
							+ (localTime.month + 1) + "-" + localTime.monthDay
							+ " " + localTime.hour + ":" + localTime.minute);
				}
				break;
			case 1:
				jsonToUI();
				break;
			case 2:
				myFocusView.beginScroll(mAdapter.getView(0, null, homePage));
				break;
			}
		}
	};

	private Runnable runnable = new Runnable() {
		public void run() {
			// getNetTime();
			getLocalTime();

			handler.postDelayed(this, 10000);
		}
	};

	/** ��ȡ����ʱ�� */
	private void getLocalTime() {
		// mLog("getLocalTime()");
		localTime.setToNow();

		handler.sendEmptyMessage(0);

		if (localTime.year < 2014) {
			getNetTime();
		}
	}

	/** ��ȡ����ʱ��,��ͬ��ϵͳʱ�� */
	private void getNetTime() {
		mLog("getNetTime()");
		new Thread() {
			@Override
			public void run() {
				URL url;
				try {
					url = new URL("http://www.baidu.com");
					URLConnection uc = url.openConnection();// �������Ӷ���
					uc.connect(); // ��������
					long ld = uc.getDate(); // ȡ����վ����ʱ��

					boolean bool = SystemClock.setCurrentTimeMillis(ld);
					mLog("set system clock bool= " + bool);
				} catch (Exception e) {
					mLog(e.toString());
				}
				// handler.sendEmptyMessage(0);
			}
		}.start();
	}

	/** ��ȡ����ʱ�� */
	private void getWeatherInfo() {
		new Thread() {
			@Override
			public void run() {
				try {
					/* ��������Ҫ���ʵĵ�ַurl */
					URL uri = new URL(
							"http://www.weather.com.cn/data/cityinfo/101120201.html");

					/* �����url���� */
					URLConnection ucon = uri.openConnection();

					/* �������������ȡ��InputStream */
					InputStream inputStream = ucon.getInputStream();
					BufferedInputStream bufferedInputStream = new BufferedInputStream(
							inputStream);
					ByteArrayBuffer baf = new ByteArrayBuffer(100);
					int current = 0;
					/* һֱ�����ļ����� */
					while ((current = bufferedInputStream.read()) != -1) {
						baf.append((byte) current);
					}

					String string = new String(baf.toByteArray());
					// mLog("string = " + string);
					JSONObject json = new JSONObject(string);

					weather = json.getJSONObject("weatherinfo");
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					Log.d("sunyj", "Exception = " + e.toString());
				}
			}
		}.start();
	}

	/** ����JSON���ݣ�չʾ��UI�� */
	private void jsonToUI() {
		mLog("jsonToUI()");
		try {
			textCity.setText("" + weather.getString("city"));
			textTemp.setText("" + weather.getString("temp1") + "~"
					+ weather.getString("temp2"));
			textWeather.setText("" + weather.getString("weather"));

			String img1 = weather.getString("img1").replace(".gif", "");
			String img2 = weather.getString("img2").replace(".gif", "");
			mLog("img1 = " + img1 + ",img2 = " + img2);

			try {
				Class<com.sunyj.myhomepage.R.drawable> cls = R.drawable.class;
				int value1 = cls.getDeclaredField(img1).getInt(null);
				int value2 = cls.getDeclaredField(img2).getInt(null);

				// int value3 = getResources().getIdentifier(img1, "drawable",
				// "com.sunyj.myhomepage");

				image1.setImageDrawable(getResources().getDrawable(value1));
				image2.setImageDrawable(getResources().getDrawable(value2));
			} catch (Exception e) {
				mLog(e.toString());
			}
		} catch (JSONException e) {
			mLog(e.toString());
		}
	}

	/** ��ȡ��ǰ��������״̬ */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

}
