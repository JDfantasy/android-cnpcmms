package com.cnpc.zhibo.app.entity;
/*
 * 中石油端供应商列表的实体类
 */
public class Petrochina_suppliers_entry {
 public String name;//供应商的名称
 public String number;//供应商的维修次数
 public String grade;//供应商的服务评价
 public String id;//供应商的id
public Petrochina_suppliers_entry(String name, String number, String grade, String id) {
	super();
	this.name = name;
	this.number = number;
	this.grade = grade;
	this.id = id;
}
 
}
