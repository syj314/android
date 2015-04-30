package com.sunyj.myhomepage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MySqliteManager {
	private String TAG = "database";// log文件的TAG

	private SQLiteDatabase db = null;
	private Context mContext;
	private MySQLiteHelper myHelper;

	public MySqliteManager(Context c) {
		mContext = c;
		myHelper = new MySQLiteHelper(mContext, "myapps.db", null, 1);

	}

	/** 打开或创建数据库/表 */
	private void OpenDB() {
		MyLog.i(TAG, "OpenDB()");
		// 打开或创建test.db数据库
		db = mContext.openOrCreateDatabase("myapps.db", Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE TABLE IF NOT EXISTS launcher_apps(_id INTEGER PRIMARY KEY AUTOINCREMENT,appName TEXT, packageName TEXT,clickNum INTEGER,visibility INTEGER);");
	}

	/** 判断数据库是否存在此apk */
	public boolean isInDB(String _packageName) {
		OpenDB();
		boolean bool = true;
		int hasThisApp = 0;
		try {
			Cursor cursor = db
					.rawQuery(
							"SELECT count(*) from launcher_apps WHERE packageName = ?;",
							new String[] { _packageName });

			while (cursor.moveToNext()) {
				hasThisApp = cursor.getInt(0);
			}
			if (hasThisApp == 0) {
				/* 数据库不包含该apk */
				bool = false;
			} else {
				/* 数据库包含该apk */
				bool = true;
			}

		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
		return bool;
	}

	/** 新apk包名 写入数据库 */
	public void insertDB(AppInfo ri) {
		MyLog.i(TAG, "insertDB()");
		OpenDB();
		String sqlString = "INSERT INTO launcher_apps(packageName,appName,clickNum,visibility) values(?,?,?,?)";

		try {
			db.execSQL(sqlString, new Object[] { ri.PackageName, ri.AppName,
					ri.ClickNum, ri.Visibility });
		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
	}

	/** 新apk包名 写入数据库 */
	public void insertDB(String packageName) {
		MyLog.i(TAG, "insertDB() packageName");
		OpenDB();
		String sqlString = "INSERT INTO launcher_apps(packageName,appName,clickNum,visibility) values(?,?,?)";

		try {
			db.execSQL(sqlString, new Object[] { packageName, 0, 1 });
		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
	}

	/** 查询数据库，按照点击次数排序 */
	public List<String> queryDB() {
		MyLog.i(TAG, "queryDB()");
		List<String> _packageNames = new ArrayList<String>();
		OpenDB();
		String sqlString = "SELECT packageName FROM launcher_apps ORDER BY clickNum DESC";

		String packageName = null;
		try {
			Cursor cursor = db.rawQuery(sqlString, null);

			while (cursor.moveToNext()) {
				packageName = cursor.getString(0);
				if (packageName != null) {
					_packageNames.add(packageName);
				} else {
					/* 数据库包含该apk */
				}
			}

		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
		return _packageNames;
	}

	/** 查询数据库，按照点击次数排序 */
	public List<AppInfo> queryAllApps() {
		MyLog.i(TAG, "queryAllApps()");

		List<AppInfo> appInfos = new ArrayList<AppInfo>();

		OpenDB();
		String sqlString = "SELECT appName,packageName,clickNum,visibility FROM launcher_apps ORDER BY clickNum DESC";

		try {
			Cursor cursor = db.rawQuery(sqlString, null);
			while (cursor.moveToNext()) {
				AppInfo appInfo = new AppInfo();
				appInfo.AppName = cursor.getString(0);
				appInfo.PackageName = cursor.getString(1);
				appInfo.ClickNum = cursor.getInt(2);
				appInfo.Visibility = cursor.getInt(3) == 0 ? false : true;
				appInfos.add(appInfo);
			}

		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
		return appInfos;
	}

	/** 根据包名，删除数据库记录 */
	public void deleteDB(AppInfo ai) {
		MyLog.i(TAG, "deleteDB(): ai.PackageName = " + ai.PackageName);
		OpenDB();
		String sqlString = "DELETE FROM launcher_apps WHERE packageName = ?";

		try {
			db.execSQL(sqlString, new String[] { ai.PackageName });
		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
	}

	public void deleteDB(String packageName) {
		MyLog.i(TAG, "deleteDB(): packageName = " + packageName);
		OpenDB();
		String sqlString = "DELETE FROM launcher_apps WHERE packageName = ?";

		try {
			db.execSQL(sqlString, new String[] { packageName });
		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
	}

	/** 更新 该apk打开次数 */
	public void updateClickNum(AppInfo info) {
		MyLog.i(TAG, "updateClickNum()");
		OpenDB();

		int clickNum = 0;
		try {
			Cursor cursor = db
					.rawQuery(
							"SELECT clickNum from launcher_apps WHERE packageName = ?;",
							new String[] { info.PackageName });

			while (cursor.moveToNext()) {
				clickNum = cursor.getInt(0);
			}
			clickNum++;
			MyLog.i(TAG, "clickNum = " + clickNum);
			db.execSQL(
					"update launcher_apps set clickNum =? where packageName = ?;",
					new Object[] { clickNum, info.PackageName });

		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
	}

	/** 更新 该apk打开次数 */
	public void updateVisibility(AppInfo info) {
		MyLog.i(TAG, "updateVisibility()");
		OpenDB();
		try {
			MyLog.i(TAG, "mAppInfo.getPackageName() =" + info.PackageName);

			db.execSQL(
					"update launcher_apps set visibility =? where packageName = ?;",
					new Object[] { info.Visibility == true ? 1 : 0,
							info.PackageName });

		} catch (Exception e) {
			MyLog.i(TAG, "Exception=" + e.toString());
		}

		db.close();
	}
}
