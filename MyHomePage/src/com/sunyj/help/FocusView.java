package com.sunyj.help;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FocusView extends ImageView {

	/**
	 * direction One of FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT,
	 * FOCUS_FORWARD, or FOCUS_BACKWARD.
	 */
	private int direction;

	private float n;

	public float getN() {
		return n;
	}

	public void setN(float n) {
		this.n = n;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public FocusView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private float[] whxy = new float[4];

	public float[] getWhxy() {
		return whxy;
	}

	/**
	 * width, height, x, y
	 * 
	 * @param whxy
	 */
	public void setWhxy(float[] whxy) {
		this.whxy = whxy;
		this.setX(whxy[2]);
		this.setY(whxy[3]);
		this.setLayoutParams(new RelativeLayout.LayoutParams((int) whxy[0],
				(int) whxy[1]));
	}

	public void setXy(float[] xy) {
		this.setX(xy[0]);
		this.setY(xy[1]);
	}

	@Override
	public String toString() {
		String s = "x:" + getX() + " ,y:" + getY() + ", width:" + getWidth()
				+ ", height:" + getHeight() + ", ScaleX:" + getScaleX()
				+ " , scaleY: " + getScaleY();
		return s + "\r\n" + super.toString();
	}
}