package com.cnpc.zhibo.app.entity;

/**
 * ��Ӧ�̵�ά�޵������б����ʵ����
 */
public class Supplier_page_weixiudanguanli {
	public String id;// ά�޵�ID
	public String number;// ά�ޱ��
	public String name_of_gas_station;// ����վ����
	public String problem;// ����
	public String time;// ʱ��
	public String title;
	public String imgpath;// ͼƬ
	public String state;// ״̬
	public String workState;// �����ж��Ƿ�������
	public String score;// ����

	public Supplier_page_weixiudanguanli() {
		super();
	}

	public Supplier_page_weixiudanguanli(String id, String number, String name_of_gas_station, String problem,
			String time, String title, String imgpath, String state, String workState,String score) {
		super();
		this.id = id;
		this.number = number;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.time = time;
		this.title = title;
		this.imgpath = imgpath;
		this.state = state;
		this.workState = workState;
		this.score=score;
	}

}
