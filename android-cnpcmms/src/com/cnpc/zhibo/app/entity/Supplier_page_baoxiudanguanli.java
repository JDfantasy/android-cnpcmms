package com.cnpc.zhibo.app.entity;

/**
 * 供应商的报修单管理列表参数实体类
 */
public class Supplier_page_baoxiudanguanli {
	public String id;// 保修单ID
	public String number;// 保修编号
	public String name_of_gas_station;// 加油站名称
	public String problem;// 问题
	public String title; // 标题
	public String time;// 时间
	public String imgpath;// 图片
	public String state;// 状态

	public Supplier_page_baoxiudanguanli() {
		super();
	}

	public Supplier_page_baoxiudanguanli(String id, String number, String name_of_gas_station, String problem,
			String title, String time, String imgpath, String state) {
		super();
		this.id = id;
		this.number = number;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.title = title;
		this.time = time;
		this.imgpath = imgpath;
		this.state = state;
	}

}
