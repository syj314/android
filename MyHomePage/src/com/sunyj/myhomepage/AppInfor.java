package com.sunyj.myhomepage;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * ���ڼ�¼��ϵͳӦ�õ���Ϣ
 * 
 * @author
 * @date
 * @since 1.0
 */
public class AppInfor implements Parcelable {
	// Ӧ�ó����ͼ��
	private Bitmap iconAppDrawable;

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

	// ͼƬ���ֽ�����
	private byte[] bitmap;

	// kernel user-ID
	private int uid;

	public AppInfor() {

	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Bitmap getIconAppDrawable() {
		return iconAppDrawable;
	}

	public void setIconAppDrawable(Bitmap iconAppDrawable) {
		this.iconAppDrawable = iconAppDrawable;
	}

	public byte[] getBitmap() {
		return bitmap;
	}

	public void setBitmap(byte[] bitmap) {
		this.bitmap = bitmap;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public long getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(long cacheSize) {
		this.cacheSize = cacheSize;
	}

	public long getDataSize() {
		return dataSize;
	}

	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}

	public long getAppSize() {
		return appSize;
	}

	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}

	public static final Parcelable.Creator<AppInfor> CREATOR = new Creator<AppInfor>() {
		public AppInfor createFromParcel(Parcel source) {
			AppInfor mAppInfor = new AppInfor();
			mAppInfor.appName = source.readString();
			mAppInfor.packageName = source.readString();
			mAppInfor.versionName = source.readString();
			mAppInfor.totalSize = source.readLong();
			mAppInfor.appSize = source.readLong();
			mAppInfor.dataSize = source.readLong();
			mAppInfor.cacheSize = source.readLong();
			mAppInfor.versionCode = source.readInt();
			mAppInfor.uid = source.readInt();
			mAppInfor.bitmap = source.createByteArray();
			return mAppInfor;
		}

		public AppInfor[] newArray(int size) {
			return new AppInfor[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(appName);
		dest.writeString(packageName);
		dest.writeString(versionName);
		dest.writeLong(totalSize);
		dest.writeLong(appSize);
		dest.writeLong(dataSize);
		dest.writeLong(cacheSize);
		dest.writeInt(versionCode);
		dest.writeInt(uid);
		dest.writeByteArray(bitmap);
	}
}
