package com.cnpc.zhibo.app.util;

//常量
public class GlobalConsts {
	public static boolean isScroller = true;// 判断轮流播放公告的值
	//正在维修
	public static final int MAINTAIN_SERVICEING=0;
	//等待维修
	public static final int MAINTAIN_WAITSERVICE=1;
	//维修完成
	public static final int MAINTAIN_HISTORYSERVICE=2;
	//未完成
	public static final int MAINTAIN_HOME_NOTFINISH=3;
	//已完成
	public static final int MAINTAIN_HOME_ALREADYTFINISH=4;
	//已报销
	public static final int MAINTAIN_APPLY_ALREAD=5;
	//未报销
	public static final int MAINTAIN_APPLY_NOT=6;
	
	/**
	 * 下拉刷新的消息通知
	 */
	public static final int REFRESH_MAINTAINHOME_NEWSNOTIFICATION=10;
	/**
	 * 下拉刷新的分配通知
	 */
	public static final int REFRESH_MAINTAINHOME_ALLOCTION=11;
	/**
	 * 下拉刷新的维修单已完成
	 */
	public static final int REFRESH_MAINTAINHOME_SERVICE_ALREAD=12;
	/**
	 * 下拉刷新的维修单未完成
	 */
	public static final int REFRESH_MAINTAINHOME_SERVICE_NOT=13;
	/**
	 * 下拉刷新的维修的维修中
	 */
	public static final int REFRESH_MAINTAIN_SERVICEING=14;
	/**
	 * 下拉刷新的维修的历史维修
	 */
	public static final int REFRESH_MAINTAIN_SERVICEHISTORY=15;
	/**
	 * 下拉刷新的报销的已报销
	 */
	public static final int REFRESH_MAINTAIN_APPLY_ALREAD=16;
	/**
	 * 下拉刷新的报销未报销
	 */
	public static final int REFRESH_MAINTAIN_APPLY_NOT=17;
	
	/**
	 * 分配通知的响应码
	 */
	public static final int MAINTAIN_ALLOCTION_RESPONSECODE=18;
	
}
