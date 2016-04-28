package com.cnpc.zhibo.app.entity;
//中石油端查看加油站列表的实体类
public class Petrochina_station_message {
	public String jyzname;
	public String number;
	public String cost;
	public String id;
	
	public Petrochina_station_message(String jyzname, String number, String cost, String id) {
		super();
		this.jyzname = jyzname;
		this.number = number;
		this.cost = cost;
		this.id = id;
	}

	public Petrochina_station_message() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
