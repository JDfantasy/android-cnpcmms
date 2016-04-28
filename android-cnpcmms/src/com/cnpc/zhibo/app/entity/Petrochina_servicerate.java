package com.cnpc.zhibo.app.entity;

/*
 * 中石油端维修率模块查看加油站列表的界面的实体类
 */
public class Petrochina_servicerate {
	public String stationname;// 加油站名称
	public String servicenumber;// 维修次数
	public String id;// id

	public Petrochina_servicerate(String stationname, String servicenumber, String id) {
		super();
		this.stationname = stationname;
		this.servicenumber = servicenumber;
		this.id = id;
	}

}
