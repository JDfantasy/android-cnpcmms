package com.cnpc.zhibo.app.entity;

//վ���˵Ĳ鿴���޵���ʵ����
public class Centre_page_approve {
	public String number;// ���
	public String time;// ʱ��
	public String title;// ���
	public String state;// ״̬
	public String statetime;// ״̬ʱ��
	public String id;// ���޵���id
	public String icon;// ͼƬ

	public Centre_page_approve(String number, String time, String title, String state, String statetime, String id,
			String icon) {
		super();
		this.number = number;
		this.time = time;
		this.title = title;
		this.state = state;
		this.statetime = statetime;
		this.id = id;
		this.icon = icon;
	}

	public Centre_page_approve() {
		super();
		// TODO Auto-generated constructor stub
	}

}
