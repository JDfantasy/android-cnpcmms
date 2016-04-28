package com.cnpc.zhibo.app.entity;

/**
 * 供应商的维修单管理列表参数实体类
 */
public class Supplier_page_weixiudanguanli {
	public String id;// 维修单ID
	public String number;// 维修编号
	public String name_of_gas_station;// 加油站名称
	public String problem;// 问题
	public String time;// 时间
	public String title;
	public String imgpath;// 图片
	public String state;// 状态
	public String workState;// 用来判断是否已评价
	public String score;// 评分

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
