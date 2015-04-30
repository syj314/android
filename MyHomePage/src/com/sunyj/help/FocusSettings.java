package com.sunyj.help;

import com.sunyj.myhomepage.MyLog;

import android.animation.ObjectAnimator;
import android.view.View;

public class FocusSettings {
	private View focusView;
	private View currentView;
	private int mFocusOffSet = 10;
	private int duration = 300;

	public FocusSettings(View focusView, View currentView) {
		this.focusView = focusView;
		this.currentView = currentView;
	}

	public void onAnimationFocusChange(boolean isanimation) {

		MyLog.d("===========onAnimationFocusChange==============" + true);
		int[] currentLocation = new int[2];
		currentView.getLocationOnScreen(currentLocation);
		int x = currentLocation[0];
		int y = currentLocation[1];
		int[] focusViewLocation = new int[2];
		focusView.getLocationOnScreen(focusViewLocation);
		int fx = focusViewLocation[0];
		int fy = focusViewLocation[1];

		int w = currentView.getWidth();
		int h = currentView.getHeight();
		float x1 = focusView.getX() - (fx - x);
		float y1 = focusView.getY() - (fy - y);
		MyLog.d("=======x1======" + x1 + "=============y1========" + y1
				+ "======w=======" + w + "=====h========" + h);
		if (!isanimation) {
			ObjectAnimator.ofObject(focusView, "whxy", new WHXYEvaluator(),
					new float[] { w, h, x1, y1 }).start();
		}

		if (focusView.getWidth() == 0) {
			MyLog.d("===========onAnimationFocusChange==============" + 1);
			ObjectAnimator
					.ofObject(focusView, "whxy", new WHXYEvaluator(),
							new float[] { w, h, x1, y1 }).setDuration(5)
					.start();
			new WHXYEvaluator().toString();
		} else {
			MyLog.d("===========onAnimationFocusChange==============" + 2);
			ObjectAnimator
					.ofObject(focusView, "whxy", new WHXYEvaluator(),
							new float[] { w, h, x1, y1 })
					.setDuration(duration / 2).start();
		}
	}

}
