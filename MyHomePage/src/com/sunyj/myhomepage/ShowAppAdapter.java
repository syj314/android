package com.sunyj.myhomepage;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowAppAdapter extends BaseAdapter {
	private String TAG = "ShowAppAdapter";// log文件的TAG

	private Context mContext;

	/** 原始app列表 */
	private List<AppInfo> mList;

	public ShowAppAdapter(Context c) {
		mContext = c;
	}

	public ShowAppAdapter(Context c, List<AppInfo> list) {
		mContext = c;
		mList = list;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfo appInfo = mList.get(position);
		AppItem mAppItem;
		if (convertView == null) {
			mAppItem = new AppItem();
			View mView = LayoutInflater.from(mContext).inflate(R.layout.my_app,
					null);

			mAppItem.mAppIcon = (ImageView) mView.findViewById(R.id.appicon);
			mAppItem.mAppTitle = (TextView) mView.findViewById(R.id.apptitle);

			mView.setTag(mAppItem);

			convertView = mView;
		} else {
			mAppItem = (AppItem) convertView.getTag();
		}
		mAppItem.mAppIcon.setBackgroundDrawable(appInfo.Icon);
		mAppItem.mAppTitle.setText(appInfo.AppName);
		return convertView;

	}

	// App have icon and title
	private class AppItem {
		ImageView mAppIcon;
		TextView mAppTitle;
	}

}
