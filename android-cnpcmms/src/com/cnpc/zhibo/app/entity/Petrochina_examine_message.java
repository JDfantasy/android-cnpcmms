package com.cnpc.zhibo.app.entity;

//中石油查看审批列表的实体类
public class Petrochina_examine_message {
	public String number;// 编号
	public String jyzname;// 加油站名称
	public String malfunction;// 故障类型
	public String expenditure;// 花费
	public String icon;// 图片
	public String id;// 订单的id
	public String statetime;// 时间
	public String workStatus;// 单子状态

	public Petrochina_examine_message(String number, String jyzname, String malfunction, String expenditure,
			String icon, String id, String statetime, String workStatus) {
		super();
		this.number = number;
		this.jyzname = jyzname;
		this.malfunction = malfunction;
		this.expenditure = expenditure;
		this.icon = icon;
		this.id = id;
		this.statetime = statetime;
		this.workStatus = workStatus;
	}

	public Petrochina_examine_message() {
		super();
		// TODO Auto-generated constructor stub
	}

}
