package com.cnpc.zhibo.app.entity;

import java.io.Serializable;

public class Maintain_home_alloction_item implements Serializable{
	
	//维修端主页分配通知的实体类
	public String id;
	public String userName;//站长的userName用于聊天
	public String indentNumber;//单号
	public String personName;//站长姓名
	public String gasStationname;//加油站名称
	public String gasAddress;//加油站地址
	public String problem;//问题
	public String time;//提交订单时间
	public String mobilephone;//手机
	public String serviceState;//维修状态
	public String timeState;//显示标头的时间状态
	public String faultParts;//故障配件
	public String faultDescription;//故障描述
	public String[] iconPath=new String[4];//图标路径
	
	public Maintain_home_alloction_item() {
		// TODO Auto-generated constructor stub
	}

	public Maintain_home_alloction_item(String id, String userName, String indentNumber, String personName,
			String gasStationname, String gasAddress, String problem, String time, String mobilephone,
			String serviceState, String timeState, String faultParts, String faultDescription) {
		super();
		this.id = id;
		this.userName = userName;
		this.indentNumber = indentNumber;
		this.personName = personName;
		this.gasStationname = gasStationname;
		this.gasAddress = gasAddress;
		this.problem = problem;
		this.time = time;
		this.mobilephone = mobilephone;
		this.serviceState = serviceState;
		this.timeState = timeState;
		this.faultParts = faultParts;
		this.faultDescription = faultDescription;
	}

	
	
	
	
	
	

	
	
	

}
