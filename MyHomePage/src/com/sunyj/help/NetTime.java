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
	 * ��ȡ����ʱ��
	 * 
	 * @param url
	 *            ��ȡ����ʱ���õĵ�ַ�����á�"��
	 */
	public Date getNetTime() {

		new Thread() {
			@Override
			public void run() {
				try {
					URL uri = new URL(url1);
					URLConnection uc = uri.openConnection();// �������Ӷ���
					uc.connect(); // ��������
					long ld = uc.getDate(); // ȡ����վ����ʱ��
					netDate = new Date(ld); // ת��Ϊ��׼ʱ�����

				} catch (Exception e) {

				}
			}
		}.start();
		return netDate;
	}
}
