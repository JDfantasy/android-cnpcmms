package com.cnpc.zhibo.app.entity;

/**
 * ��Ӧ�̵ı��������б�����ʵ����
 */
public class Supplier_page_baoxiao {
	public String id;// ��������ID
	public String number;// ά�ޱ��
	public String name_of_gas_station;// ����վ����
	public String problem;// ����
	public String title;// ����
	public String time;// ʱ��
	public String imgpath;// ͼƬ
	public String state;// ״̬
	public String cost;// ����

	public Supplier_page_baoxiao() {
		super();
	}

	public Supplier_page_baoxiao(String id,String number, String name_of_gas_station, String problem,String title, String time, String imgpath,
			String state, String cost) {
		super();
		this.id=id;
		this.number = number;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.title=title;
		this.time = time;
		this.imgpath = imgpath;
		this.state = state;
		this.cost = cost;
	}

}
