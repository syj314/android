package com.sunyj.myhomepage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import android.app.Instrumentation;
import android.inputmethodservice.InputMethodService;
import android.os.Looper;
import android.util.Log;

public class RemoteReceiver extends InputMethodService {
	private String TAG = "remote";
	private static final int PORT = 1720;
	private DatagramSocket mobileInputSocket = null;
	private boolean isMobileInputSocketReceive = false;
	private LooperThread looperThread = null;

	// @Override
	// public IBinder onBind(Intent arg0) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate()");

		looperThread = new LooperThread();
		looperThread.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy()");

		stopMobileInputSocket();
	}

	class LooperThread extends Thread {
		public void run() {
			Looper.prepare();
			startMobileInputSocket();
			Looper.loop();
		}
	}

	/**
	 * start MobileInput Socket
	 * 
	 * @author sunyj
	 */
	public void startMobileInputSocket() {
		Log.i(TAG, "startMobileInputSocket()");
		try {
			mobileInputSocket = new DatagramSocket(PORT);
			// Log.i(TAG, "startMobileInputSocket()1");
			byte data[] = new byte[100];

			isMobileInputSocketReceive = true;
			while (isMobileInputSocketReceive) {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				Log.i(TAG, "startMobileInputSocket()2");
				mobileInputSocket.receive(packet);
				// Log.i(TAG, "startMobileInputSocket()3");
				String result = new String(packet.getData(),
						packet.getOffset(), packet.getLength());
				if (result == null || result.equals("")) {
					Log.i(TAG, "startMobileInputSocket() result = null"
							+ result);
				} else {
					// Log.i(TAG, "startMobileInputSocket() result = " +
					// result);
					sendKeyCode(Integer.parseInt(result));
				}
			}

		} catch (Exception e) {
			Log.i(TAG, "startMobileInputSocket() Exception = " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * stop MobileInput Socket
	 * 
	 * @author sunyj
	 */
	public void stopMobileInputSocket() {
		// Log.v(TAG, "stopMobileInputSocket() ");
		try {
			isMobileInputSocketReceive = false;
			mobileInputSocket.close();
			mobileInputSocket = null;
			looperThread.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 模拟按键 。传入需要的键值即可
	 * 
	 * @param keyCode
	 */
	private void sendKeyCode(final int keycode) {
		// Log.v(TAG, "sendKeyCode() keycode = " + keycode);
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(keycode);

				} catch (Exception e) {
					Log.e("Exception when sendPointerSync", e.toString());
				}
				/* 上边的方式反应快一些，下方的方法有一点延迟 */
//				try {
//					String keyCommand = "input keyevent " + keycode;
//					Runtime runtime = Runtime.getRuntime();
//					Process proc = runtime.exec(keyCommand);
//				} catch (IOException e) {
//					Log.i(TAG, "sendKeyCode() error = " + e.toString());
//					e.printStackTrace();
//				}
			}
		}.start();

		// simulateKeyEvent(KeyEvent.ACTION_DOWN, keycode, 0);
	}

	// public boolean simulateKeyEvent(int in_aiton, int keyCode, int
	// repeatCount) {
	// Log.v(TAG, "simulateKeyEvent() ");
	//
	// KeyEvent key = new KeyEvent(in_aiton, keyCode);
	//
	// if (in_aiton == KeyEvent.ACTION_DOWN) {
	// return onKeyDown(key.getKeyCode(), key);
	// } else if (in_aiton == KeyEvent.ACTION_UP) {
	// return onKeyUp(key.getKeyCode(), key);
	// } else
	// return onKeyMultiple(key.getKeyCode(), repeatCount, key);
	// }

}
