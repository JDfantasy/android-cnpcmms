package com.cnpc.zhibo.app.entity;

public class Maintain_apply_item {
	
	//ά�޶˱�����ʵ����
	public String id;
	public String indentNumber;//������
	public String gasStationname;//����վ����
	public String problem;//����
	public String time;//�ύ����ʱ��
	public String totalMoney;//�ܽ��
	public String applyState;//ά��״̬
	public String iconPath;//ͼ��·��
	
	public Maintain_apply_item() {
		// TODO Auto-generated constructor stub
	}

	public Maintain_apply_item(String id, String indentNumber, String gasStationname, String problem, String time,
			String totalMoney) {
		super();
		this.id = id;
		this.indentNumber = indentNumber;
		this.gasStationname = gasStationname;
		this.problem = problem;
		this.time = time;
		this.totalMoney = totalMoney;
	}

	
	
	
}
