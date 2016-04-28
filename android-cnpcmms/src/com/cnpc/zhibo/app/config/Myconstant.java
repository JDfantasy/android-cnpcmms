package com.cnpc.zhibo.app.config;

import com.cnpc.zhibo.app.application.SysApplication;

//用来存储一些APP常用的常量
public class Myconstant {
	@SuppressWarnings("static-access")
	public static String token = SysApplication.getInstance().token;// 存储用户的签名的
	@SuppressWarnings("static-access")
	public static String userid = SysApplication.getInstance().userid;// 存储用户的id,工号
	public static final String BASEPATHURL = "http://101.201.210.95:8080/cnpcmms/api/";// 总网址
	public static final String WEBPAGEPATHURL = "http://101.201.210.95:8080/cnpcmms/";
	/**
	 * 登录的接口
	 */
	public static final String CENTRE_ENTER = BASEPATHURL + "login/mobile";
	/**
	 * 短信验证码发送接口
	 */
	public static final String SENDSMS = BASEPATHURL + "common/sms/send";
	/**
	 * 短信验证码验证接口
	 */
	public static final String VERIFYSMS = BASEPATHURL + "common/sms/verify";
	/**
	 * 修改密码的接口
	 */
	public static final String CHANGPASSWROD = BASEPATHURL + "password/reset";
	/**
	 * 获取用户信息的接口
	 */
	public static final String CENTRE_USERMESSAGE = BASEPATHURL + "member/profile";
	/**
	 * 判断站长是否进行了信息匹配的接口
	 */
	public static final String CENTRE_CONFIRMATION_INFORMATION = BASEPATHURL + "station/ischecked";
	/**
	 * 判断维修工是否进行了选择自己供应商的接口
	 */
	public static final String MAINTAIN_CONFIRMATION_INFORMATION = BASEPATHURL + "worker/isSelected";
	/**
	 * 判断供应商是否进行了自己的公司的接口
	 */
	public static final String SUPPLIER_CONFIRMATION_INFORMATION = BASEPATHURL + "provider/isSelected";
	/**
	 * 获取省份的接口
	 */
	public static final String CENTRE_GETPROVINCE = BASEPATHURL + "province/list";
	/**
	 * 获取城市的接口
	 */
	public static final String CENTRE_GETCITY = BASEPATHURL + "city/list?provinceId=";
	/**
	 * 获取市区或者县的接口
	 */
	public static final String CENTRE_GETAREA = BASEPATHURL + "area/list?cityId=";
	/**
	 * 站长端获取加油站列表数据的接口
	 */
	public static final String CENTRE_GETSTATIONENTRE = BASEPATHURL + "station/search/area";
	/**
	 * 站长端获取加油站详细信息数据的接口
	 */
	public static final String CENTRE_GETSTATIONMESSAGE = BASEPATHURL + "station/get/";
	/**
	 * 站长端进行加油站信息确认的接口
	 */
	public static final String CENTRE_VERIFYSTATIONMESSAGE = BASEPATHURL + "station/match";
	/**
	 * 站长端获取可以保修的项目列表
	 */
	public static final String CENTRE_GETREPAIRSITEM = BASEPATHURL + "project/list";
	/**
	 * 公共端获取查询的报修项目的列表
	 */
	public static final String COMMONALITY_SEARCHREPAIRSITEM = BASEPATHURL + "project/search";
	/**
	 * 站长端,获取证件列表
	 */
	public static final String CENTRE_GET_CERTIFICATEENTRY = BASEPATHURL + "site/certificate/list";
	/**
	 * 站长端,站长取消该证件的提醒 
	 */
	public static final String CENTRE_SETNUMBERABOLISH = BASEPATHURL + "certificate/remind/cancel";
	/**
	 * 用户进行注册提交信息的接口
	 */
	public static final String REGISTER = BASEPATHURL + "register";
	/**
	 * 站长端提交报修单的接口
	 */
	public static final String CENTRE_SUBMITREPAIRSITEM = BASEPATHURL + "repairorder/save";
	/**
	 * 站长端提交证件的接口
	 */
	public static final String CENTRE_SUBMITCERTIFICATE= BASEPATHURL + "certificate/save";
	/**
	 * 公共端获取自己所属加油站的信息
	 */
	public static final String COMMONALITY_GETSTATIONMESSAGE= BASEPATHURL + "station/user/current";
	/**
	 * 公共端获证件类型列表
	 */
	public static final String COMMONALITY_GETCERTIFICATETYPE= BASEPATHURL + "certificatetype/list";
	/**
	 * 站长端,站长主动取消报修单,关闭报修单
	 */
	public static final String CENTRE_CANCEL_REPAIRSMONAD = BASEPATHURL + "site/repairorder/cancel";

	/**
	 * 公共端获取所有的供应商的列表的接口
	 */
	public static final String COMMONALITY_GETREPAIRSITEMENTY = BASEPATHURL + "provider/list";
	/**
	 * 站长端获取所有的供应商的列表的接口
	 */
	public static final String CENTRE_GETREPAIRSITEMENTY = BASEPATHURL + "provider/choose?id=";
	/**
	 * 站长端,统计获取所有等待处理中的报修单总记录数
	 */
	public static final String CENTRE_GETREPAIRORDE_COUNTRNUMBER = BASEPATHURL + "site/repairorder/count/progress";
	/**
	 * 站长端,获取所有未处理的证件的记录数
	 */
	public static final String CENTRE_GETCERTIFICATE_COUNTRNUMBER = BASEPATHURL + "site/certificate/count/progress";
	/**
	 * 站长端,统计获取所有维修中的维修单总记录数
	 */
	public static final String CENTRE_GETSERVICE_COUNTRNUMBER = BASEPATHURL + "site/fixorder/count/progress";
	/**
	 * 站长端,统计获取所有正在审批的维修单记录数（审批）
	 */
	public static final String CENTRE_GETAPPROVE_COUNTRNUMBER = BASEPATHURL + "site/approve/count/progress";
	/**
	 * 站长端,统计获取所有等待处理的维修单记录数(审批管理)
	 */
	public static final String CENTRE_GETFIXORDER_COUNTRNUMBERAPPROVE = BASEPATHURL + "site/fixorder/count/approve";
	/**
	 * 站长端,获取所有等待处理中的报修单列表 的接口
	 */
	public static final String CENTRE_GETREPAIRORDERLIST = BASEPATHURL + "site/repairorder/list/progress ";
	/**
	 * 站长端,获取所有历史维修的报修单列表 的接口
	 */
	public static final String CENTRE_GETREPAIHISTORYRORDERLIST = BASEPATHURL + "site/repairorder/list/history?";
	/**
	 * 公共端,根据供应商名称模糊查询供应商列表 的接口
	 */
	public static final String SEARCH_SUPPLIERLIST = BASEPATHURL + "provider/search";
	/**
	 * 供应商端,供应商管理员设置所属供应商匹配 的接口
	 */
	public static final String SUPPLIER_SELECTPROVIDER = BASEPATHURL + "provider/select?";

	/**
	 * 供应商端获取所有等待維修报修单列表的接口
	 */
	public static final String SUPPLIER_GETREPAIR = BASEPATHURL + "repairorder/provider/list/wait";

	/**
	 * 供应商端根据id 获取维修单详情的接口
	 */
	public static final String SUPPLIER_GETREPAIR_CONTENT = BASEPATHURL + "repairorder/get";

	/**
	 * 供应商端的获取所有历史维修单列表的接口
	 */
	public static final String SUPPLIER_HISTORY_REPAIR = BASEPATHURL + "provider/fixorder/list/history";

	/**
	 * 供应商端的获取所有维修中的维修单列表的接口
	 */
	public static final String SUPPLIER_REPAIRING = BASEPATHURL + "provider/fixorder/list/progress";

	/**
	 * 供应商端的获取所有已分配报修单列表的接口
	 */
	public static final String SUPPLIER_ASSIGNED_WARRANTY = BASEPATHURL + "provider/list/distributed";

	/**
	 * 维修员端分配通知下获得已分配的未接单的报修列表
	 */
	public static final String MAINTAINHOME_ALLOCTION_NOT = BASEPATHURL + "worker/repairorder/list/untaked?";

	/**
	 * 维修员端 无法处理故障直接关闭订单
	 */
	public static final String MAINTAINHOME_ALLOCTIONDETAIL_CLOSE = BASEPATHURL + "worker/takeorder/close?";

	/**
	 * 维修员端 通过沟通问题已经解决直接生成维修单
	 */
	public static final String MAINTAINHOME_ALLOCTIONDETAIL_FINISH = BASEPATHURL + "worker/takeorder/complete?";

	/**
	 * 维修员端获得历史维修单列表
	 */
	public static final String MAINTAIN_SERVICE_HISTORY = BASEPATHURL + "worker/fixorder/list/history?";

	/**
	 * 维修员端获得维修中表单列表
	 */
	public static final String MAINTAIN_SERVICE_PROGRESS = BASEPATHURL + "worker/fixorder/list/progress?";

	/**
	 * 维修员端_维修 无法维修关闭订单
	 */
	public static final String MAINTAIN_SERVICE_ITEM_CLOSED = BASEPATHURL + "worker/fixorder/close?";

	/**
	 * 维修员端_维修 维修结束,完成订单
	 */
	public static final String MAINTAIN_SERVICE_ITEM_FINISH = BASEPATHURL + "worker/fixorder/completed?";

	/**
	 * 维修员查询报销中报销单
	 */
	public static final String MAINTAIN_APPLY_PROGRESS = BASEPATHURL + "worker/expenseorder/list/progress?";

	/**
	 * 维修员查询已完成的报销单
	 */
	public static final String MAINTAIN_APPLY_HISTORY = BASEPATHURL + "worker/expenseorder/list/history?";

	/**
	 * 维修员 新建并保存报销单
	 */
	public static final String MAINTAIN_NEWAPPLYREPORT = BASEPATHURL + "expenseorder/save?";
	
	/**
	 * 维修员 公共端 根据id 获取报销单详情
	 */
	public static final String MAINTAIN_GETAPPLYBILLDETAIL = BASEPATHURL + "expenseorder/get/";
	
	/**
	 * 维修员 修改报销单
	 */
	public static final String MAINTAIN_MODIFYAPPLYBILL = BASEPATHURL + "expenseorder/update?";
	
	/**
	 * 维修员 将处于未提交状态的报销单提交给供应商
	 */
	public static final String MAINTAIN_NEWAPPLYSUBMIT = BASEPATHURL + "worker/expenseorder/submit";

	/**
	 * 站长端,根据单号模糊查询维修单列表
	 */
	public static final String CENTRE_GET_SEARCHSERVICEMONADENTRY = BASEPATHURL + "site/fixorder/search";
	/**
	 * 通过关键词模糊搜索加油站列表
	 */
	public static final String CENTRE_GET_SEARCHSTATIONENTRY = BASEPATHURL + "station/search";

	/**
	 * 站长端,根据单号模糊查询报修单列表
	 */
	public static final String CENTRE_GET_SEARCHREPAIRSMONADENTRY = BASEPATHURL + "site/repairorder/search";
	/**
	 * 站长端,获取所有维修中的维修单列表
	 */
	public static final String CENTRE_GET_BEINGREPAIRED_ENTRY = BASEPATHURL + "site/fixorder/list/progress";
	/**
	 * 站长端,获取所有等待处理的维修修单列表(审批管理)
	 */
	public static final String CENTRE_GET_WAITAPPROVEENTRY = BASEPATHURL + "site/fixorder/list/approve";
	/**
	 * 站长端,主动关闭维修单
	 */
	public static final String CENTRE_INITIATIVE_SHUTSERVICEMONAD = BASEPATHURL + "site/fixorder/close";
	/**
	 * 站长端,提交不在保的维修单给中石油端审批
	 */
	public static final String CENTRE_SUBMITSERVICECNPC = BASEPATHURL + "site/fixorder/submit";
	/**
	 * 
	 * 站长端,将审批通过的维修单给发送给维修员
	 */
	public static final String CENTRE_SEND_EXAMINEEDSERVICE = BASEPATHURL + "site/fixorder/send";
	/**
	 * 站长端,确认关闭维修单
	 */
	public static final String CENTRE__VERIFY_SHUTMONAD = BASEPATHURL + "site/fixorder/confirmclose";
	/**
	 * 站长端,确认完成维修单
	 */
	public static final String CENTRE__VERIFY_ACCOMPLISHMONAD = BASEPATHURL + "site/fixorder/confirmcompleted";
	/**
	 * 站长端,站长评价维修单,订单已完成
	 */
	public static final String CENTRE__EVALUATEMONAD = BASEPATHURL + "site/fixorder/evaluate";
	/**
	 * 站长端,获取所有历史维修单列表
	 */
	public static final String CENTRE_GET_HISTORYREPAIRED_ENTRY = BASEPATHURL + "site/fixorder/list/history?";
	/**
	 * 站长端,获取所有历史审批维修单列表 (审批)
	 */
	public static final String CENTRE_GET_EXAMINEMONAD = BASEPATHURL + "site/approve/list/history?";
	/**
	 * 站长端,获取所有正在审批的维修单列表 (审批)
	 */
	public static final String CENTRE_GET_UNEXAMINEMONAD = BASEPATHURL + "site/approve/list/progress";

	/**
	 * 维修员端创建维修单保存并发送按钮 不在保时调用
	 */
	public static final String MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVESEND = BASEPATHURL
			+ "worker/takeorder/savesend?";

	/**
	 * 维修员端创建维修单保存并发送按钮 不在保时可调用 在保时也可调用
	 */
	public static final String MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVE = BASEPATHURL
			+ "worker/takeorder/save?";

	/**
	 * 维修员端根据单号模糊查询维修单
	 */
	public static final String MAINTAIN_SEARCH = BASEPATHURL + "worker/fixorder/search";

	/**
	 * 维修员端根据单号模糊查询维修中的维修单
	 */
	public static final String MAINTAINHOME_SEARCHING = BASEPATHURL + "worker/fixorder/list/progress/search";

	/**
	 * 维修员端根据单号模糊查询报销单
	 */
	public static final String MAINTAINHOME_SEARCHAPPLYLIST = BASEPATHURL + "worker/expenseorder/progress/search";

	/**
	 * 组织架构列表接口 机构及用户
	 */
	public static String ORGANIZATION_TREE_USER = BASEPATHURL + "organization/provider/tree/user";
	/**
	 * 组织架构列表接口 机构
	 */
	public static String ORGANIZATION_TREE_ORG = BASEPATHURL + "organization/provider/tree/org";
	/**
	 * 组织架构列表接口 根目录
	 */
	public static String ORGANIZATION_TREE = BASEPATHURL + "organization/provider/tree";
	/**
	 * 通过该接口,供应商管理员设置所属供应商及所属机构功能
	 */
	public static String WORKER_SELECT = BASEPATHURL + "worker/select";
	/**
	 * 供应商端,供应商管理员分配订单给维修员
	 */

	public static String PROVIDER_DISTRIBUTE = BASEPATHURL + "provider/distribute";

	/**
	 * 供应商端 根据单号模糊查询维修单的接口
	 */
	public static String SUPPLIER_REPAIRS_SEARCH = BASEPATHURL + "provider/fixorder/search";
	/**
	 * 中石油端,获取所有等待审批中的维修单列表
	 */
	public static final String PETROCHINA_GET_WAITEXAMINEENTRY = BASEPATHURL + "customer/approve/list/wait";
	/**
	 * 中石油端,获取所有历史审批维修单列表
	 */
	public static final String PETROCHINA_GET_HISTORYEXAMINEENTRY = BASEPATHURL + "customer/approve/list/history?";

	/**
	 * 中石油端,获取供应商信息统计列表
	 */
	public static final String PETROCHINA_GET_PROVIDERENTRYSTATS = BASEPATHURL + "customer/provider/list/stats?";
	/**
	 * 中石油端,统计获取所有等待审批中的维修单记录数
	 */
	public static final String PETROCHINA_GET_COUNTWAIT_RENTRYSTATS = BASEPATHURL + "customer/approve/count/wait";
	/**
	 * 中石油端,通过关键词搜索获取供应商信息统计列表
	 */
	public static final String PETROCHINA_GET_SEARCHPROVIDERENTRYSTATS = BASEPATHURL + "customer/provider/search/stats";
	/**
	 * 中石油端,获取供应商所有维修单列表
	 */
	public static final String PETROCHINA_GET_PROVIDERENTRYFIXORDER = BASEPATHURL + "customer/provider/list/fixorder?";
	/**
	 * 中石油端,获取加油站信息统计列表
	 */
	public static final String PETROCHINA_GET_STATIONENTRYSTATS = BASEPATHURL + "customer/station/list/stats?";
	/**
	 * 中石油端,通过关键词搜索获取加油站信息统计列表
	 */
	public static final String PETROCHINA_GET_SEARCHPSTATIONENTRYSTATS = BASEPATHURL + "customer/station/search/stats";
	/**
	 * 中石油端,获取获取加油站所有维修单列表
	 */
	public static final String PETROCHINA_GET_STATIONNTRYFIXORDER = BASEPATHURL + "customer/station/list/fixorder?";
	/**
	 * 中石油端,获取维修项目信息统计列表
	 */
	public static final String PETROCHINA_GET_PROJECTENTRYSTATS = BASEPATHURL + "customer/project/list/stats?";
	/**
	 * 中石油端,获取某项目下加油站统计信息列表
	 */
	public static final String PETROCHINA_GET_PROJECTESTATIONNTRYFIXORDER = BASEPATHURL
			+ "customer/project/station/stats?";
	/**
	 * 中石油端,通过关键词搜索获取维修项目信息统计列表
	 */
	public static final String PETROCHINA_GET_SEARCHPPROJECTENTRYSTATS = BASEPATHURL + "customer/project/search/stats";

	/**
	 * 中石油端,审批通过维修单
	 */
	public static final String PETROCHINA_APPROVED_SERVICEMONAD = BASEPATHURL + "customer/fixorder/approved";
	/**
	 * 中石油端,审批驳回维修单
	 */
	public static final String PETROCHINA_UNAPPROVED_SERVICEMONAD = BASEPATHURL + "customer/fixorder/unapproved";
	/**
	 * 中石油端，发送公告给所有的站长
	 */
	public static final String PETROCHINA_SENDDALNOTICE = BASEPATHURL + "notice/company/sendstation";
	/**
	 * 中石油端,获取所有历史公告列表
	 */
	public static final String PETROCHINA_GETHISTORYNOTICEENTRY = BASEPATHURL + "customer/notice/list/history";
	/**
	 * 中石油端,根据单号模糊查询维修单列表
	 */
	public static final String PETROCHINA_SEARCHEXAMINEENTRY = BASEPATHURL + "customer/fixorder/search";

	/**
	 * 报修单详情静态网页界面 需要携带保修单ID
	 */
	public static final String REPAIRSMESSAGE = WEBPAGEPATHURL + "repairorder/detail/";
	/**
	 * 证件详情静态网页界面 需要携带保修单ID
	 */
	public static final String CREDENTIALSMESSAGE = WEBPAGEPATHURL + "certificate/detail/";
	/**
	 * 公共端维修单详情静态网页界面 需要携带维修单ID
	 */
	public static final String SERVICEMESSAGE = WEBPAGEPATHURL + "fixorder/detail/";
	/**
	 * 站长端维修单详情静态网页界面 需要携带维修单ID
	 */
	public static final String CENTRESERVICEMESSAGE = WEBPAGEPATHURL + "fixorder/site/detail/";
	/**
	 * 维修单详情静态网页界面更改0323 需要携带维修单ID
	 */
	public static final String SERVICEMESSAGENEW = WEBPAGEPATHURL + "fixorder/worker/detail/";
	/**
	 * 报销单详情静态网页界面 需要携带维修单ID
	 */
	public static final String APPLYMESSAGE = WEBPAGEPATHURL + "expenseorder/detail/";
	/**
	 * 加油站详情静态网页界面 需要携带加油站详情ID
	 */
	public static final String STATIONMESSAGE = WEBPAGEPATHURL + "station/detail/";
	/**
	 * 供应商详情网页接口 需要携带加油站详情ID
	 */
	public static final String PROVIDERMESSAGE = WEBPAGEPATHURL + "provider/detail/";

	/**
	 * 查看报销单详情网页接口 需要携带报销单详情ID
	 */
	public static final String REIMBURSEMENTMESSAGE = WEBPAGEPATHURL + "expenseorder/detail/";
	/**
	 * 查看审批单详情网页接口 需要携带报销单详情ID
	 */
	public static final String APPROVESERVICEMESSAGE = WEBPAGEPATHURL + "approve/detail/";

	/**
	 * 站长端根据单号模糊查询报修单
	 */

	public static final String CENTRE_WARRANTY_SEARCH = BASEPATHURL + "site/repairorder/search?sn=";
	/*
	 * 公共端,获取当前用户的公告列表 
	 */
	public static final String COMMONALITY_GETNOTICEENTRY = BASEPATHURL + "notice/member/list/";
	/*
	 * 公共端,获取当前登陆用户信息 
	 */
	public static final String COMMONALITY_GETUSERMESSAGE = BASEPATHURL + "member/profile";
	/*
	 * 公共端,确认当前用户已经收到了公告
	 */
	public static final String COMMONALITY_READMEMBERNOTICE = BASEPATHURL + "notice/member/read";
	/**
	 * 供应商端根据单号模糊查询已分配的报修单
	 */
	public static final String SUPPLIER_WARRANY_SEARCH = BASEPATHURL + "provider/repairorder/distributed/search";

	/**
	 * 供应商获取所有历史的报销单列表的接口
	 */
	public static final String SUPPLIER_REIMBURSEMENTED = BASEPATHURL + "provider/expenseorder/list/history";

	/**
	 * 供应商获取所有处理中的报销单列表的接口
	 */
	public static final String SUPPLIER_REIMBURSEMENT = BASEPATHURL + "provider/expenseorder/list/progress";

	/**
	 * 供应商根据单号模糊查询历史的报销单列表的接口
	 */
	public static final String SUPPLIER_SEARCH_REIMBURSEMENTED = BASEPATHURL + "provider/expenseorder/history/search";

	/**
	 * 供应商根据单号模糊查询处理中的报销单列表的接口
	 */
	public static final String SUPPLIER_SEARCH_REIMBURSEMENT = BASEPATHURL
			+ "provider/expenseorder/progress/search?sn=";

	/**
	 * 供应商审批通过报销单的接口
	 */
	public static final String SUPPLIER_APPROVED_SERVICEMONAD = BASEPATHURL + "provider/expenseorder/approved";

	/**
	 * 供应商审批驳回报销单的接口
	 */
	public static final String SUPPLIER_UNAPPROVED_SERVICEMONAD = BASEPATHURL + "provider/expenseorder/unapproved";

	/**
	 * 供应商端获取所有历史公告列表的接口
	 */
	public static final String SUPPLIER_NOTICELIST_HISTORY = BASEPATHURL + "provider/notice/list/history";

	/**
	 * 供应商端发送通知给指定用户列表的接口
	 */
	public static final String SUPPLIER_SEND_NOTICE = BASEPATHURL + "notice/provider/send";

	/**
	 * 供应商端通过公告Id获取发送列表的接口
	 */

	public static final String SUPPLIER_RECORD_NOTICE = BASEPATHURL + "noticerecord/list";

	/**
	 * 供应商端通过公告Id获取发送列表的接口
	 */
	public static final String SUPPLIER_NOTICE_RECORD_LIST = BASEPATHURL + "noticerecord/list";

	/**
	 * 获得公告详情的界面公共接口
	 */
	public static final String NOTICEDETAILS = WEBPAGEPATHURL + "notice/detail/";
	/**
	 * 中石油端，查看公告详情网页接口
	 */
	public static final String CNPCNOTICEDETAILS= WEBPAGEPATHURL + "notice/customer/detail/";

	/**
	 * 供应商端发送提醒员工 阅读通知的接口
	 */
	public static final String SUPPLIER_POST_NOTICE_ALERT = BASEPATHURL + "noticerecord/provider/alert";

	/**
	 * 供应商端,查看维修单详情网页接口
	 */
	public static final String SERVICEMESSAGE1 = WEBPAGEPATHURL + "fixorder/provider/detail/";
	
}
