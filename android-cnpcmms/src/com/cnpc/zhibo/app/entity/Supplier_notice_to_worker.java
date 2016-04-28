package com.cnpc.zhibo.app.entity;

/**
 * 供应商公告查看已读未读实体
 */
public class Supplier_notice_to_worker {
	public String id;// 公告ID
	public String userId;// 维修员ID
	public String name;// 维修员名字
	public String headerImg;// 用户头像
	public String status;// 阅读状态

	public Supplier_notice_to_worker(String id, String userId, String name, String headerImg, String status) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.headerImg = headerImg;
		this.status = status;
	}

}
