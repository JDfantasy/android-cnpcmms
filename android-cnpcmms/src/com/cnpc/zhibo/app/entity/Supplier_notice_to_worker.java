package com.cnpc.zhibo.app.entity;

/**
 * ��Ӧ�̹���鿴�Ѷ�δ��ʵ��
 */
public class Supplier_notice_to_worker {
	public String id;// ����ID
	public String userId;// ά��ԱID
	public String name;// ά��Ա����
	public String headerImg;// �û�ͷ��
	public String status;// �Ķ�״̬

	public Supplier_notice_to_worker(String id, String userId, String name, String headerImg, String status) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.headerImg = headerImg;
		this.status = status;
	}

}
