package com.cnpc.zhibo.app.entity;

/*
 * վ���ˡ�ά�޹����յ������ʵ����
 */
public class Commonality_notice {
	public String title;// ����
	public String content;// ����
	public String time;// ʱ��
	public String id;// �����id
	public String state;// �жϹ����Ƿ��Ѷ�

	public Commonality_notice() {

		// TODO Auto-generated constructor stub
	}

	public Commonality_notice(String title, String content, String time, String id, String state) {
		super();
		this.title = title;
		this.content = content;
		this.time = time;
		this.id = id;
		this.state = state;
	}

}
