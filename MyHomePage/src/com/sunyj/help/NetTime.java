package com.sunyj.help;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class NetTime {
	private static NetTime body;
	private Date netDate;
	private String url1 = "http://www.baidu.com";

	public NetTime() {

	}

	public static NetTime getInstance() {

		return body;
	}

	/**
	 * 获取网络时间
	 * 
	 * @param url
	 *            获取网络时间用的地址，常用“"”
	 */
	public Date getNetTime() {

		new Thread() {
			@Override
			public void run() {
				try {
					URL uri = new URL(url1);
					URLConnection uc = uri.openConnection();// 生成连接对象
					uc.connect(); // 发出连接
					long ld = uc.getDate(); // 取得网站日期时间
					netDate = new Date(ld); // 转换为标准时间对象

				} catch (Exception e) {

				}
			}
		}.start();
		return netDate;
	}
}
