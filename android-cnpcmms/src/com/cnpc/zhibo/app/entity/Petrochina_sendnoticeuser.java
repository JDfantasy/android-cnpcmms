package com.cnpc.zhibo.app.entity;
/*
 * 中石油端发送公告的联系人列表的实体类
 */
public class Petrochina_sendnoticeuser {
	public String name;
	public String id;
	public boolean check;

	public Petrochina_sendnoticeuser(String name, String id, boolean check) {
		super();
		this.name = name;
		this.id = id;
		this.check = check;
	}

	public Petrochina_sendnoticeuser() {
		super();
		// TODO Auto-generated constructor stub
	}

}
