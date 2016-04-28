package com.cnpc.zhibo.app.entity;

/*
 * 站长端、维修工端收到公告的实体类
 */
public class Commonality_notice {
	public String title;// 标题
	public String content;// 内容
	public String time;// 时间
	public String id;// 公告的id
	public String state;// 判断公告是否已读

	public Commonality_notice() {

		// TODO Auto-generated constructor stub
	}

	public Commonality_notice(String title, String content, String time, String id, String state) {
		super();
		this.title = title;
		this.content = content;
		this.time = time;
		this.id = id;
		this.state = state;
	}

}
