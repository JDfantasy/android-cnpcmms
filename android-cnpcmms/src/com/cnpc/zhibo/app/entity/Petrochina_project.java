package com.cnpc.zhibo.app.entity;

/*
 * 中石油端查看项目维修率的实体
 */
public class Petrochina_project {
	public String name;// 项目名称
	public String number;// 项目维修次数
	public String id;// 项目id
	public Petrochina_project(String name, String number, String id) {
		super();
		this.name = name;
		this.number = number;
		this.id = id;
	}
	
}
