package com.cnpc.zhibo.app.entity;

/**
 * ��Ӧ�̵ķ�������б����ʵ����
 */
public class Supplier_page_fenpeiguanl {
	public String id;// ���޵�ID
	public String fenpei_to_user;// ���䵽����
	public String name_of_gas_station;// ����վ����
	public String problem;// ����
	public String title;
	public String time;// ʱ��
	public String imgpath;// ͼƬ
	public String isaccept;// ����״̬
	public String userID;// ά��ԱID
	public String ding_dan_number;// �������

	public Supplier_page_fenpeiguanl() {
		super();
	}

	public Supplier_page_fenpeiguanl(String id, String fenpei_to_user, String name_of_gas_station, String problem,
			String title, String time, String imgpath, String isaccept, String userID, String ding_dan_number) {
		super();
		this.id = id;
		this.fenpei_to_user = fenpei_to_user;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.title = title;
		this.time = time;
		this.imgpath = imgpath;
		this.isaccept = isaccept;
		this.userID = userID;
		this.ding_dan_number = ding_dan_number;
	}

	

}
