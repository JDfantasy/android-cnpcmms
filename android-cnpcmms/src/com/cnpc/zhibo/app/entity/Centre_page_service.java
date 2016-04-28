package com.cnpc.zhibo.app.entity;

//站长端的底部菜单中的维修中的维修单的实体类
public class Centre_page_service {
	public String id;// 维修单的id
	public String number;// 编号
	public String time;// 时间
	public String title;// 简介
	public String state;// 状态
	public String WorkStatus;// 当前状态用来判断维修单当前的状态
	public String statetime;// 状态时间
	public String userid;// 维修工的工号
	public String icon;// 图片
	public String score="";//评分

	public Centre_page_service(String number, String time, String title, String state, String statetime, String id,
			String WorkStatus, String userid, String icon) {
		super();
		this.id = id;
		this.number = number;
		this.time = time;
		this.title = title;
		this.state = state;
		this.WorkStatus = WorkStatus;
		this.statetime = statetime;
		this.userid = userid;
		this.icon = icon;
	}
	public Centre_page_service(String number, String time, String title, String state, String statetime, String id,
			String WorkStatus, String userid, String icon,String score) {
		super();
		this.id = id;
		this.number = number;
		this.time = time;
		this.title = title;
		this.state = state;
		this.WorkStatus = WorkStatus;
		this.statetime = statetime;
		this.userid = userid;
		this.icon = icon;
		this.score=score;
	}

	

	public Centre_page_service() {
		super();
		// TODO Auto-generated constructor stub
	}

}
