package com.cnpc.zhibo.app.entity;

import java.io.Serializable;

public class Maintain_home_alloction_item implements Serializable{
	
	//ά�޶���ҳ����֪ͨ��ʵ����
	public String id;
	public String userName;//վ����userName��������
	public String indentNumber;//����
	public String personName;//վ������
	public String gasStationname;//����վ����
	public String gasAddress;//����վ��ַ
	public String problem;//����
	public String time;//�ύ����ʱ��
	public String mobilephone;//�ֻ�
	public String serviceState;//ά��״̬
	public String timeState;//��ʾ��ͷ��ʱ��״̬
	public String faultParts;//�������
	public String faultDescription;//��������
	public String[] iconPath=new String[4];//ͼ��·��
	
	public Maintain_home_alloction_item() {
		// TODO Auto-generated constructor stub
	}

	public Maintain_home_alloction_item(String id, String userName, String indentNumber, String personName,
			String gasStationname, String gasAddress, String problem, String time, String mobilephone,
			String serviceState, String timeState, String faultParts, String faultDescription) {
		super();
		this.id = id;
		this.userName = userName;
		this.indentNumber = indentNumber;
		this.personName = personName;
		this.gasStationname = gasStationname;
		this.gasAddress = gasAddress;
		this.problem = problem;
		this.time = time;
		this.mobilephone = mobilephone;
		this.serviceState = serviceState;
		this.timeState = timeState;
		this.faultParts = faultParts;
		this.faultDescription = faultDescription;
	}

	
	
	
	
	
	

	
	
	

}
