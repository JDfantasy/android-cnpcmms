package com.cnpc.zhibo.app.entity;

public class Maintain_apply_item {
	
	//维修端报销的实体类
	public String id;
	public String indentNumber;//订单号
	public String gasStationname;//加油站名称
	public String problem;//问题
	public String time;//提交订单时间
	public String totalMoney;//总金额
	public String applyState;//维修状态
	public String iconPath;//图标路径
	
	public Maintain_apply_item() {
		// TODO Auto-generated constructor stub
	}

	public Maintain_apply_item(String id, String indentNumber, String gasStationname, String problem, String time,
			String totalMoney) {
		super();
		this.id = id;
		this.indentNumber = indentNumber;
		this.gasStationname = gasStationname;
		this.problem = problem;
		this.time = time;
		this.totalMoney = totalMoney;
	}

	
	
	
}
