package com.cnpc.zhibo.app.entity;

/**
 * ��Ӧ�̵ı��޵������б����ʵ����
 */
public class Supplier_page_baoxiudanguanli {
	public String id;// ���޵�ID
	public String number;// ���ޱ��
	public String name_of_gas_station;// ����վ����
	public String problem;// ����
	public String title; // ����
	public String time;// ʱ��
	public String imgpath;// ͼƬ
	public String state;// ״̬

	public Supplier_page_baoxiudanguanli() {
		super();
	}

	public Supplier_page_baoxiudanguanli(String id, String number, String name_of_gas_station, String problem,
			String title, String time, String imgpath, String state) {
		super();
		this.id = id;
		this.number = number;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.title = title;
		this.time = time;
		this.imgpath = imgpath;
		this.state = state;
	}

}
