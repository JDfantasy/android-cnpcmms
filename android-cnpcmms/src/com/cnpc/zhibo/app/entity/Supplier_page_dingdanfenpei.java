package com.cnpc.zhibo.app.entity;

/**
 * ��Ӧ�̵ı��޵��б����ʵ����
 */
public class Supplier_page_dingdanfenpei {
	public String id;// ���޵�id
	public String number;// ���޵����
	public String elapsedtime;// �ѱ���ʱ��
	public String name_of_gas_station;// ����վ����
	public String problem;// ����
	public String title;// ����
	public String time;// ʱ��
	public String imgpath;// ͼƬ

	public Supplier_page_dingdanfenpei() {
		super();
	}

	public Supplier_page_dingdanfenpei(String id, String number, String elapsedtime, String name_of_gas_station,
			String problem, String title, String time, String imgpath) {
		super();
		this.id = id;
		this.number = number;
		this.elapsedtime = elapsedtime;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.title = title;
		this.time = time;
		this.imgpath = imgpath;
	}

	

}
