package com.cnpc.zhibo.app.entity;

/**
 * 供应商的分配管理列表参数实体类
 */
public class Supplier_page_fenpeiguanl {
	public String id;// 保修单ID
	public String fenpei_to_user;// 分配到工人
	public String name_of_gas_station;// 加油站名称
	public String problem;// 问题
	public String title;
	public String time;// 时间
	public String imgpath;// 图片
	public String isaccept;// 接收状态
	public String userID;// 维修员ID
	public String ding_dan_number;// 订单编号

	public Supplier_page_fenpeiguanl() {
		super();
	}

	public Supplier_page_fenpeiguanl(String id, String fenpei_to_user, String name_of_gas_station, String problem,
			String title, String time, String imgpath, String isaccept, String userID, String ding_dan_number) {
		super();
		this.id = id;
		this.fenpei_to_user = fenpei_to_user;
		this.name_of_gas_station = name_of_gas_station;
		this.problem = problem;
		this.title = title;
		this.time = time;
		this.imgpath = imgpath;
		this.isaccept = isaccept;
		this.userID = userID;
		this.ding_dan_number = ding_dan_number;
	}

	

}
