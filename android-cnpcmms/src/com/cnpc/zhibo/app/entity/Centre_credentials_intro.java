package com.cnpc.zhibo.app.entity;
/*
 * 站长端证件的实体类
 */

public class Centre_credentials_intro {
	public String certificatetype;// 证件类型
	public String validtime;// 有效时间
	public String hzslrname;// 换证受理人
	public String icon;// 图片
	public String id;// id
	public String status;// 是否提醒
	public String alarmDate;//提醒日期
	public String  keeper;//保管人

	
	

	public Centre_credentials_intro(String certificatetype, String validtime, String hzslrname, String icon, String id,
			String status, String alarmDate, String keeper) {
		super();
		this.certificatetype = certificatetype;
		this.validtime = validtime;
		this.hzslrname = hzslrname;
		this.icon = icon;
		this.id = id;
		this.status = status;
		this.alarmDate = alarmDate;
		this.keeper = keeper;
	}


	public Centre_credentials_intro() {
		super();
		// TODO Auto-generated constructor stub
	}

}
