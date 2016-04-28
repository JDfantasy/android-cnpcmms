package com.cnpc.zhibo.app.entity;

/**
 * 供应商的公告信息的实体类
 */
public class Supplier_notice {
	public String id;// 公告ID
	public String noticeTitle;// 标题
	public String content;// 内容
	public String unreadcount;// 未读数量
	public String time;// 时间

	public Supplier_notice() {
		super();
	}

	public Supplier_notice(String id, String noticeTitle, String content, String unreadcount, String time) {
		super();
		this.id = id;
		this.noticeTitle = noticeTitle;
		this.content = content;
		this.unreadcount = unreadcount;
		this.time = time;
	}

	

}
