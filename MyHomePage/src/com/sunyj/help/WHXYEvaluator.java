package com.sunyj.help;

import android.animation.TypeEvaluator;

public class WHXYEvaluator implements TypeEvaluator<float[]> {

	/**
	 * float array {x, y, width, height}
	 */
	@Override
	public float[] evaluate(float fraction, float[] startValue, float[] endValue) {
		if (null == startValue || endValue == null) {
			return new float[] { 0, 0, 0, 0 };
		}

		float sWidth = startValue[0];
		float sHeight = startValue[1];
		float sX = startValue[2];
		float sY = startValue[3];

		float eWidth = endValue[0];
		float eHeight = endValue[1];
		float eX = endValue[2];
		float eY = endValue[3];

		// result = x0 + t * (v1 - v0),
		return new float[] { sWidth + fraction * (eWidth - sWidth),
				sHeight + fraction * (eHeight - sHeight),
				sX + fraction * (eX - sX), sY + fraction * (eY - sY) };
	}

}
