package com.cnpc.zhibo.app.entity;

/*
 * ��ʯ�Ͷ˲鿴����վά����Ŀ���б�
 */
public class Petrochina_station_seerepairsitem {
	public String number;// ���
	public String time;// ʱ��
	public String title;// ���
	public String state;// ״̬
	public String costtime;// ����ʱ��
	public String cost;// ����
	public String id;// id
	public String jyzname;// ����վ����
	public String icon;// ͼƬ����ַ

	public Petrochina_station_seerepairsitem(String number, String time, String title, String state, String costtime,
			String cost, String id) {
		super();
		this.number = number;
		this.time = time;
		this.title = title;
		this.state = state;
		this.costtime = costtime;
		this.cost = cost;
		this.id = id;
	}

	public Petrochina_station_seerepairsitem(String number, String time, String title, String state, String costtime,
			String cost, String id, String jyzname, String icon) {
		super();
		this.number = number;
		this.time = time;
		this.title = title;
		this.state = state;
		this.costtime = costtime;
		this.cost = cost;
		this.id = id;
		this.jyzname = jyzname;
		this.icon = icon;
	}

	public Petrochina_station_seerepairsitem() {
		super();
		// TODO Auto-generated constructor stub
	}

}
