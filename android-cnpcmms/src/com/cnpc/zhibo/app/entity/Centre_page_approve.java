package com.cnpc.zhibo.app.entity;

//站长端的查看报修单的实体类
public class Centre_page_approve {
	public String number;// 编号
	public String time;// 时间
	public String title;// 简介
	public String state;// 状态
	public String statetime;// 状态时间
	public String id;// 报修单的id
	public String icon;// 图片

	public Centre_page_approve(String number, String time, String title, String state, String statetime, String id,
			String icon) {
		super();
		this.number = number;
		this.time = time;
		this.title = title;
		this.state = state;
		this.statetime = statetime;
		this.id = id;
		this.icon = icon;
	}

	public Centre_page_approve() {
		super();
		// TODO Auto-generated constructor stub
	}

}
