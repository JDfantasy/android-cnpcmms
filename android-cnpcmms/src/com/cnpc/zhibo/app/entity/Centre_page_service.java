package com.cnpc.zhibo.app.entity;

//վ���˵ĵײ��˵��е�ά���е�ά�޵���ʵ����
public class Centre_page_service {
	public String id;// ά�޵���id
	public String number;// ���
	public String time;// ʱ��
	public String title;// ���
	public String state;// ״̬
	public String WorkStatus;// ��ǰ״̬�����ж�ά�޵���ǰ��״̬
	public String statetime;// ״̬ʱ��
	public String userid;// ά�޹��Ĺ���
	public String icon;// ͼƬ
	public String score="";//����

	public Centre_page_service(String number, String time, String title, String state, String statetime, String id,
			String WorkStatus, String userid, String icon) {
		super();
		this.id = id;
		this.number = number;
		this.time = time;
		this.title = title;
		this.state = state;
		this.WorkStatus = WorkStatus;
		this.statetime = statetime;
		this.userid = userid;
		this.icon = icon;
	}
	public Centre_page_service(String number, String time, String title, String state, String statetime, String id,
			String WorkStatus, String userid, String icon,String score) {
		super();
		this.id = id;
		this.number = number;
		this.time = time;
		this.title = title;
		this.state = state;
		this.WorkStatus = WorkStatus;
		this.statetime = statetime;
		this.userid = userid;
		this.icon = icon;
		this.score=score;
	}

	

	public Centre_page_service() {
		super();
		// TODO Auto-generated constructor stub
	}

}
