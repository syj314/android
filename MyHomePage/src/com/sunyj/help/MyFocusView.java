package com.sunyj.help;

import com.sunyj.myhomepage.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class MyFocusView extends LinearLayout {
	private boolean isLog = true;
	private String TAG = "MyFocusView";

	private void mLog(String msg) {
		if (isLog) {
			Log.i(TAG, msg);
		}
	}

	/** �����ƶ�����ģ�� */
	private Scroller moveScroller = null;
	/** ����ͼƬ��������ģ�� */
	private Scroller sizeScroller = null;
	/** ��������ʱ�� */
	private int duration = 1000;
	/** ����ͼƬ */
	private TextView tv;
	Bitmap img, img_fan;
	/** ����ͼƬ��ʼ�ƶ�λ�� */
	private int startX = 0, startY = 0;
	/** ����ͼƬ��ʼ��С */
	private int startWidth = 0, startHeight = 0;

	public MyFocusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		moveScroller = new Scroller(context);
		sizeScroller = new Scroller(context);

		tv = new TextView(context);

		Resources res = this.getContext().getResources();
		img = BitmapFactory.decodeResource(res, R.drawable.niao);
		img_fan = BitmapFactory.decodeResource(res, R.drawable.niao_fan);

		tv.setBackgroundDrawable(new BitmapDrawable(img));
		LayoutParams lp = new LayoutParams(130, 130);// 213, 130
		 
		this.addView(tv, lp);
	}

	@Override
	public void computeScroll() {
		if (moveScroller.computeScrollOffset()) {
			scrollTo(moveScroller.getCurrX(), moveScroller.getCurrY());
			if (sizeScroller.computeScrollOffset()) {
				LayoutParams lp = new LayoutParams(sizeScroller.getCurrX(),
						sizeScroller.getCurrY());
				tv.setLayoutParams(lp);
			}

			postInvalidate();
		}
	}

	public void beginScroll(View v) {
		if (v == null)
			return;

		int currentWidth = v.getWidth();
		int currentHeight = v.getHeight();
		mLog("currentWidth = " + currentWidth + ";currentHeight = "
				+ currentHeight);
//		if (startWidth + startHeight == 0) {
//			LayoutParams lp = new LayoutParams(currentWidth, currentHeight);
//			tv.setLayoutParams(lp);
//
//			startWidth = currentWidth;
//			startHeight = currentHeight;
//		} else if (currentWidth != startWidth || currentHeight != startHeight) {
//			int x = currentWidth - startWidth;
//			int y = currentHeight - startHeight;
//
//			sizeScroller.startScroll(startWidth, startHeight, x, y, duration);
//
//			startWidth = currentWidth;
//			startHeight = currentHeight;
//		}

		// -------------------------------------------------------------------
		int[] currentLocation = new int[2];
		v.getLocationOnScreen(currentLocation);
		int currentX = -currentLocation[0];
		int currentY = -currentLocation[1];// +

		mLog("currentX = " + currentX + ";currentY = " + currentY);

		int dx = currentX - startX;
		int dy = currentY - startY + 38;
		// 38//Ī�������Щ�����һ�пؼ���ȡ��Y���겻��0.����38.

		moveScroller.startScroll(startX, startY, dx, dy, duration);

		// ����X���ƶ����򣬸�����ͬ����ͼƬ
		if (dx < 0) {
			tv.setBackgroundDrawable(new BitmapDrawable(img));
		} else {
			tv.setBackgroundDrawable(new BitmapDrawable(img_fan));
		}

		startX = currentX;
		startY = currentY;

		invalidate();
	}
}
