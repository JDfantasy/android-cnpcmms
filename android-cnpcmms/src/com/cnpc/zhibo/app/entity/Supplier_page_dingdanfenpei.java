package com.cnpc.zhibo.app.entity;

/**
 * 供应商的保修单列表参数实体类
 */
public class Supplier_page_dingdanfenpei {
	public String id;// 保修单id
	public String number;// 保修单编号
	public String elapsedtime;// 已报修时间
	public String name_of_gas_station;// 加油站名称
	public String problem;// 问题
	public String title;// 标题
	public String time;// 时间
	public String imgpath;// 图片

	public Supplier_page_dingdanfenpei() {
		super();
	}

	public Supplier_page_dingdanfenpei(String id, String number, String elapsedtime, String name_of_gas_station,
			String problem, String title, String time, String imgpath) {
		super();
		this.id = id;
		this.number = number;
		this.elapsedtime = elapsedtime;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.title = title;
		this.time = time;
		this.imgpath = imgpath;
	}

	

}
