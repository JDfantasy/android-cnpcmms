package com.cnpc.zhibo.app.entity;
/*
 * վ����֤����ʵ����
 */

public class Centre_credentials_intro {
	public String certificatetype;// ֤������
	public String validtime;// ��Чʱ��
	public String hzslrname;// ��֤������
	public String icon;// ͼƬ
	public String id;// id
	public String status;// �Ƿ�����
	public String alarmDate;//��������
	public String  keeper;//������

	
	

	public Centre_credentials_intro(String certificatetype, String validtime, String hzslrname, String icon, String id,
			String status, String alarmDate, String keeper) {
		super();
		this.certificatetype = certificatetype;
		this.validtime = validtime;
		this.hzslrname = hzslrname;
		this.icon = icon;
		this.id = id;
		this.status = status;
		this.alarmDate = alarmDate;
		this.keeper = keeper;
	}


	public Centre_credentials_intro() {
		super();
		// TODO Auto-generated constructor stub
	}

}
