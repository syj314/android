package com.sunyj.myhomepage;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ManageAppAdapter extends BaseAdapter {
	private String TAG = "ManageAppAdapter";// log文件的TAG

	private Context mContext;

	/** 数据库app列表 */
	private List<AppInfo> mList;

	public ManageAppAdapter(Context c, List<AppInfo> appInfos) {
		mContext = c;

		mList = appInfos;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public AppInfo getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// @Override
	// public void notifyDataSetChanged() {
	// super.notifyDataSetChanged();
	// }
	//
	// @Override
	// public void registerDataSetObserver(DataSetObserver observer) {
	// super.registerDataSetObserver(observer);
	// }

	ListItem mAppItem;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfo appInfo = mList.get(position);

		if (convertView == null) {
			mAppItem = new ListItem();
			View mView = LayoutInflater.from(mContext).inflate(
					R.layout.app_manager, null);

			mAppItem.appName = (TextView) mView.findViewById(R.id.appname);
			mAppItem.packageName = (TextView) mView
					.findViewById(R.id.package_name);
			mAppItem.isView = (CheckBox) mView.findViewById(R.id.isview);

			mView.setTag(mAppItem);

			convertView = mView;
		} else {
			mAppItem = (ListItem) convertView.getTag();
		}

		mAppItem.appName.setText(appInfo.AppName);
		mAppItem.packageName.setText(appInfo.PackageName);
		mAppItem.isView.setChecked(appInfo.Visibility);

		return convertView;

	}

	// public class AppItem {
	// TextView appName;
	// TextView packageName;
	// CheckBox isView;
	//
	//
	// }

	public void setMyVisibility(boolean bool) {
		mAppItem.isView.setChecked(bool);
	}
}
