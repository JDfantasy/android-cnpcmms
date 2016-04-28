package com.cnpc.zhibo.app.entity;

/*
 * 中石油端查看加油站维修项目的列表
 */
public class Petrochina_station_seerepairsitem {
	public String number;// 编号
	public String time;// 时间
	public String title;// 简介
	public String state;// 状态
	public String costtime;// 花费时间
	public String cost;// 花费
	public String id;// id
	public String jyzname;// 加油站名称
	public String icon;// 图片的网址

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
