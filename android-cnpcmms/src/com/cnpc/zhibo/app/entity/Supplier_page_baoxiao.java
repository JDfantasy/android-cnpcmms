package com.cnpc.zhibo.app.entity;

/**
 * 供应商的报销单的列表详情实体类
 */
public class Supplier_page_baoxiao {
	public String id;// 报销单的ID
	public String number;// 维修编号
	public String name_of_gas_station;// 加油站名称
	public String problem;// 问题
	public String title;// 标题
	public String time;// 时间
	public String imgpath;// 图片
	public String state;// 状态
	public String cost;// 费用

	public Supplier_page_baoxiao() {
		super();
	}

	public Supplier_page_baoxiao(String id,String number, String name_of_gas_station, String problem,String title, String time, String imgpath,
			String state, String cost) {
		super();
		this.id=id;
		this.number = number;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.title=title;
		this.time = time;
		this.imgpath = imgpath;
		this.state = state;
		this.cost = cost;
	}

}
