package com.cnpc.zhibo.app.entity;

/**
 * ��Ӧ�̵Ĺ�����Ϣ��ʵ����
 */
public class Supplier_notice {
	public String id;// ����ID
	public String noticeTitle;// ����
	public String content;// ����
	public String unreadcount;// δ������
	public String time;// ʱ��

	public Supplier_notice() {
		super();
	}

	public Supplier_notice(String id, String noticeTitle, String content, String unreadcount, String time) {
		super();
		this.id = id;
		this.noticeTitle = noticeTitle;
		this.content = content;
		this.unreadcount = unreadcount;
		this.time = time;
	}

	

}
