package com.cnpc.zhibo.app.entity;

//��ʯ�Ͳ鿴�����б��ʵ����
public class Petrochina_examine_message {
	public String number;// ���
	public String jyzname;// ����վ����
	public String malfunction;// ��������
	public String expenditure;// ����
	public String icon;// ͼƬ
	public String id;// ������id
	public String statetime;// ʱ��
	public String workStatus;// ����״̬

	public Petrochina_examine_message(String number, String jyzname, String malfunction, String expenditure,
			String icon, String id, String statetime, String workStatus) {
		super();
		this.number = number;
		this.jyzname = jyzname;
		this.malfunction = malfunction;
		this.expenditure = expenditure;
		this.icon = icon;
		this.id = id;
		this.statetime = statetime;
		this.workStatus = workStatus;
	}

	public Petrochina_examine_message() {
		super();
		// TODO Auto-generated constructor stub
	}

}
