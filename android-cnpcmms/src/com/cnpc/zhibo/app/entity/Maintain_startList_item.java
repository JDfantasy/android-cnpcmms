package com.cnpc.zhibo.app.entity;

public class Maintain_startList_item {
	
	//维修端主页开始接单、维修 的实体类
	public String id;
	public String indentNumber;//订单号
	public String userName;//userName用于聊天
	public String gasStationname;//加油站名称
	public String problem;//问题
	public String time;//提交订单时间
	public String serviceState;//维修状态
	public String timeState;//时间日期状态 一周前
	public String iconPath;//图标路径
	public String starNumber;//星星的个数
	
	public Maintain_startList_item() {
		// TODO Auto-generated constructor stub
	}

	public Maintain_startList_item(String id, String indentNumber, String userName, String gasStationname,
			String problem, String time, String serviceState) {
		super();
		this.id = id;
		this.indentNumber = indentNumber;
		this.userName = userName;
		this.gasStationname = gasStationname;
		this.problem = problem;
		this.time = time;
		this.serviceState = serviceState;
	}

	
	
	

	
	
	

	

	
	

	
	

}
