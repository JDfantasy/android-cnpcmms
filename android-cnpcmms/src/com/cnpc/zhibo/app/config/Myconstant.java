package com.cnpc.zhibo.app.config;

import com.cnpc.zhibo.app.application.SysApplication;

//�����洢һЩAPP���õĳ���
public class Myconstant {
	@SuppressWarnings("static-access")
	public static String token = SysApplication.getInstance().token;// �洢�û���ǩ����
	@SuppressWarnings("static-access")
	public static String userid = SysApplication.getInstance().userid;// �洢�û���id,����
	public static final String BASEPATHURL = "http://101.201.210.95:8080/cnpcmms/api/";// ����ַ
	public static final String WEBPAGEPATHURL = "http://101.201.210.95:8080/cnpcmms/";
	/**
	 * ��¼�Ľӿ�
	 */
	public static final String CENTRE_ENTER = BASEPATHURL + "login/mobile";
	/**
	 * ������֤�뷢�ͽӿ�
	 */
	public static final String SENDSMS = BASEPATHURL + "common/sms/send";
	/**
	 * ������֤����֤�ӿ�
	 */
	public static final String VERIFYSMS = BASEPATHURL + "common/sms/verify";
	/**
	 * �޸�����Ľӿ�
	 */
	public static final String CHANGPASSWROD = BASEPATHURL + "password/reset";
	/**
	 * ��ȡ�û���Ϣ�Ľӿ�
	 */
	public static final String CENTRE_USERMESSAGE = BASEPATHURL + "member/profile";
	/**
	 * �ж�վ���Ƿ��������Ϣƥ��Ľӿ�
	 */
	public static final String CENTRE_CONFIRMATION_INFORMATION = BASEPATHURL + "station/ischecked";
	/**
	 * �ж�ά�޹��Ƿ������ѡ���Լ���Ӧ�̵Ľӿ�
	 */
	public static final String MAINTAIN_CONFIRMATION_INFORMATION = BASEPATHURL + "worker/isSelected";
	/**
	 * �жϹ�Ӧ���Ƿ�������Լ��Ĺ�˾�Ľӿ�
	 */
	public static final String SUPPLIER_CONFIRMATION_INFORMATION = BASEPATHURL + "provider/isSelected";
	/**
	 * ��ȡʡ�ݵĽӿ�
	 */
	public static final String CENTRE_GETPROVINCE = BASEPATHURL + "province/list";
	/**
	 * ��ȡ���еĽӿ�
	 */
	public static final String CENTRE_GETCITY = BASEPATHURL + "city/list?provinceId=";
	/**
	 * ��ȡ���������صĽӿ�
	 */
	public static final String CENTRE_GETAREA = BASEPATHURL + "area/list?cityId=";
	/**
	 * վ���˻�ȡ����վ�б����ݵĽӿ�
	 */
	public static final String CENTRE_GETSTATIONENTRE = BASEPATHURL + "station/search/area";
	/**
	 * վ���˻�ȡ����վ��ϸ��Ϣ���ݵĽӿ�
	 */
	public static final String CENTRE_GETSTATIONMESSAGE = BASEPATHURL + "station/get/";
	/**
	 * վ���˽��м���վ��Ϣȷ�ϵĽӿ�
	 */
	public static final String CENTRE_VERIFYSTATIONMESSAGE = BASEPATHURL + "station/match";
	/**
	 * վ���˻�ȡ���Ա��޵���Ŀ�б�
	 */
	public static final String CENTRE_GETREPAIRSITEM = BASEPATHURL + "project/list";
	/**
	 * �����˻�ȡ��ѯ�ı�����Ŀ���б�
	 */
	public static final String COMMONALITY_SEARCHREPAIRSITEM = BASEPATHURL + "project/search";
	/**
	 * վ����,��ȡ֤���б�
	 */
	public static final String CENTRE_GET_CERTIFICATEENTRY = BASEPATHURL + "site/certificate/list";
	/**
	 * վ����,վ��ȡ����֤�������� 
	 */
	public static final String CENTRE_SETNUMBERABOLISH = BASEPATHURL + "certificate/remind/cancel";
	/**
	 * �û�����ע���ύ��Ϣ�Ľӿ�
	 */
	public static final String REGISTER = BASEPATHURL + "register";
	/**
	 * վ�����ύ���޵��Ľӿ�
	 */
	public static final String CENTRE_SUBMITREPAIRSITEM = BASEPATHURL + "repairorder/save";
	/**
	 * վ�����ύ֤���Ľӿ�
	 */
	public static final String CENTRE_SUBMITCERTIFICATE= BASEPATHURL + "certificate/save";
	/**
	 * �����˻�ȡ�Լ���������վ����Ϣ
	 */
	public static final String COMMONALITY_GETSTATIONMESSAGE= BASEPATHURL + "station/user/current";
	/**
	 * �����˻�֤�������б�
	 */
	public static final String COMMONALITY_GETCERTIFICATETYPE= BASEPATHURL + "certificatetype/list";
	/**
	 * վ����,վ������ȡ�����޵�,�رձ��޵�
	 */
	public static final String CENTRE_CANCEL_REPAIRSMONAD = BASEPATHURL + "site/repairorder/cancel";

	/**
	 * �����˻�ȡ���еĹ�Ӧ�̵��б�Ľӿ�
	 */
	public static final String COMMONALITY_GETREPAIRSITEMENTY = BASEPATHURL + "provider/list";
	/**
	 * վ���˻�ȡ���еĹ�Ӧ�̵��б�Ľӿ�
	 */
	public static final String CENTRE_GETREPAIRSITEMENTY = BASEPATHURL + "provider/choose?id=";
	/**
	 * վ����,ͳ�ƻ�ȡ���еȴ������еı��޵��ܼ�¼��
	 */
	public static final String CENTRE_GETREPAIRORDE_COUNTRNUMBER = BASEPATHURL + "site/repairorder/count/progress";
	/**
	 * վ����,��ȡ����δ�����֤���ļ�¼��
	 */
	public static final String CENTRE_GETCERTIFICATE_COUNTRNUMBER = BASEPATHURL + "site/certificate/count/progress";
	/**
	 * վ����,ͳ�ƻ�ȡ����ά���е�ά�޵��ܼ�¼��
	 */
	public static final String CENTRE_GETSERVICE_COUNTRNUMBER = BASEPATHURL + "site/fixorder/count/progress";
	/**
	 * վ����,ͳ�ƻ�ȡ��������������ά�޵���¼����������
	 */
	public static final String CENTRE_GETAPPROVE_COUNTRNUMBER = BASEPATHURL + "site/approve/count/progress";
	/**
	 * վ����,ͳ�ƻ�ȡ���еȴ������ά�޵���¼��(��������)
	 */
	public static final String CENTRE_GETFIXORDER_COUNTRNUMBERAPPROVE = BASEPATHURL + "site/fixorder/count/approve";
	/**
	 * վ����,��ȡ���еȴ������еı��޵��б� �Ľӿ�
	 */
	public static final String CENTRE_GETREPAIRORDERLIST = BASEPATHURL + "site/repairorder/list/progress ";
	/**
	 * վ����,��ȡ������ʷά�޵ı��޵��б� �Ľӿ�
	 */
	public static final String CENTRE_GETREPAIHISTORYRORDERLIST = BASEPATHURL + "site/repairorder/list/history?";
	/**
	 * ������,���ݹ�Ӧ������ģ����ѯ��Ӧ���б� �Ľӿ�
	 */
	public static final String SEARCH_SUPPLIERLIST = BASEPATHURL + "provider/search";
	/**
	 * ��Ӧ�̶�,��Ӧ�̹���Ա����������Ӧ��ƥ�� �Ľӿ�
	 */
	public static final String SUPPLIER_SELECTPROVIDER = BASEPATHURL + "provider/select?";

	/**
	 * ��Ӧ�̶˻�ȡ���еȴ��S�ޱ��޵��б�Ľӿ�
	 */
	public static final String SUPPLIER_GETREPAIR = BASEPATHURL + "repairorder/provider/list/wait";

	/**
	 * ��Ӧ�̶˸���id ��ȡά�޵�����Ľӿ�
	 */
	public static final String SUPPLIER_GETREPAIR_CONTENT = BASEPATHURL + "repairorder/get";

	/**
	 * ��Ӧ�̶˵Ļ�ȡ������ʷά�޵��б�Ľӿ�
	 */
	public static final String SUPPLIER_HISTORY_REPAIR = BASEPATHURL + "provider/fixorder/list/history";

	/**
	 * ��Ӧ�̶˵Ļ�ȡ����ά���е�ά�޵��б�Ľӿ�
	 */
	public static final String SUPPLIER_REPAIRING = BASEPATHURL + "provider/fixorder/list/progress";

	/**
	 * ��Ӧ�̶˵Ļ�ȡ�����ѷ��䱨�޵��б�Ľӿ�
	 */
	public static final String SUPPLIER_ASSIGNED_WARRANTY = BASEPATHURL + "provider/list/distributed";

	/**
	 * ά��Ա�˷���֪ͨ�»���ѷ����δ�ӵ��ı����б�
	 */
	public static final String MAINTAINHOME_ALLOCTION_NOT = BASEPATHURL + "worker/repairorder/list/untaked?";

	/**
	 * ά��Ա�� �޷��������ֱ�ӹرն���
	 */
	public static final String MAINTAINHOME_ALLOCTIONDETAIL_CLOSE = BASEPATHURL + "worker/takeorder/close?";

	/**
	 * ά��Ա�� ͨ����ͨ�����Ѿ����ֱ������ά�޵�
	 */
	public static final String MAINTAINHOME_ALLOCTIONDETAIL_FINISH = BASEPATHURL + "worker/takeorder/complete?";

	/**
	 * ά��Ա�˻����ʷά�޵��б�
	 */
	public static final String MAINTAIN_SERVICE_HISTORY = BASEPATHURL + "worker/fixorder/list/history?";

	/**
	 * ά��Ա�˻��ά���б��б�
	 */
	public static final String MAINTAIN_SERVICE_PROGRESS = BASEPATHURL + "worker/fixorder/list/progress?";

	/**
	 * ά��Ա��_ά�� �޷�ά�޹رն���
	 */
	public static final String MAINTAIN_SERVICE_ITEM_CLOSED = BASEPATHURL + "worker/fixorder/close?";

	/**
	 * ά��Ա��_ά�� ά�޽���,��ɶ���
	 */
	public static final String MAINTAIN_SERVICE_ITEM_FINISH = BASEPATHURL + "worker/fixorder/completed?";

	/**
	 * ά��Ա��ѯ�����б�����
	 */
	public static final String MAINTAIN_APPLY_PROGRESS = BASEPATHURL + "worker/expenseorder/list/progress?";

	/**
	 * ά��Ա��ѯ����ɵı�����
	 */
	public static final String MAINTAIN_APPLY_HISTORY = BASEPATHURL + "worker/expenseorder/list/history?";

	/**
	 * ά��Ա �½������汨����
	 */
	public static final String MAINTAIN_NEWAPPLYREPORT = BASEPATHURL + "expenseorder/save?";
	
	/**
	 * ά��Ա ������ ����id ��ȡ����������
	 */
	public static final String MAINTAIN_GETAPPLYBILLDETAIL = BASEPATHURL + "expenseorder/get/";
	
	/**
	 * ά��Ա �޸ı�����
	 */
	public static final String MAINTAIN_MODIFYAPPLYBILL = BASEPATHURL + "expenseorder/update?";
	
	/**
	 * ά��Ա ������δ�ύ״̬�ı������ύ����Ӧ��
	 */
	public static final String MAINTAIN_NEWAPPLYSUBMIT = BASEPATHURL + "worker/expenseorder/submit";

	/**
	 * վ����,���ݵ���ģ����ѯά�޵��б�
	 */
	public static final String CENTRE_GET_SEARCHSERVICEMONADENTRY = BASEPATHURL + "site/fixorder/search";
	/**
	 * ͨ���ؼ���ģ����������վ�б�
	 */
	public static final String CENTRE_GET_SEARCHSTATIONENTRY = BASEPATHURL + "station/search";

	/**
	 * վ����,���ݵ���ģ����ѯ���޵��б�
	 */
	public static final String CENTRE_GET_SEARCHREPAIRSMONADENTRY = BASEPATHURL + "site/repairorder/search";
	/**
	 * վ����,��ȡ����ά���е�ά�޵��б�
	 */
	public static final String CENTRE_GET_BEINGREPAIRED_ENTRY = BASEPATHURL + "site/fixorder/list/progress";
	/**
	 * վ����,��ȡ���еȴ������ά���޵��б�(��������)
	 */
	public static final String CENTRE_GET_WAITAPPROVEENTRY = BASEPATHURL + "site/fixorder/list/approve";
	/**
	 * վ����,�����ر�ά�޵�
	 */
	public static final String CENTRE_INITIATIVE_SHUTSERVICEMONAD = BASEPATHURL + "site/fixorder/close";
	/**
	 * վ����,�ύ���ڱ���ά�޵�����ʯ�Ͷ�����
	 */
	public static final String CENTRE_SUBMITSERVICECNPC = BASEPATHURL + "site/fixorder/submit";
	/**
	 * 
	 * վ����,������ͨ����ά�޵������͸�ά��Ա
	 */
	public static final String CENTRE_SEND_EXAMINEEDSERVICE = BASEPATHURL + "site/fixorder/send";
	/**
	 * վ����,ȷ�Ϲر�ά�޵�
	 */
	public static final String CENTRE__VERIFY_SHUTMONAD = BASEPATHURL + "site/fixorder/confirmclose";
	/**
	 * վ����,ȷ�����ά�޵�
	 */
	public static final String CENTRE__VERIFY_ACCOMPLISHMONAD = BASEPATHURL + "site/fixorder/confirmcompleted";
	/**
	 * վ����,վ������ά�޵�,���������
	 */
	public static final String CENTRE__EVALUATEMONAD = BASEPATHURL + "site/fixorder/evaluate";
	/**
	 * վ����,��ȡ������ʷά�޵��б�
	 */
	public static final String CENTRE_GET_HISTORYREPAIRED_ENTRY = BASEPATHURL + "site/fixorder/list/history?";
	/**
	 * վ����,��ȡ������ʷ����ά�޵��б� (����)
	 */
	public static final String CENTRE_GET_EXAMINEMONAD = BASEPATHURL + "site/approve/list/history?";
	/**
	 * վ����,��ȡ��������������ά�޵��б� (����)
	 */
	public static final String CENTRE_GET_UNEXAMINEMONAD = BASEPATHURL + "site/approve/list/progress";

	/**
	 * ά��Ա�˴���ά�޵����沢���Ͱ�ť ���ڱ�ʱ����
	 */
	public static final String MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVESEND = BASEPATHURL
			+ "worker/takeorder/savesend?";

	/**
	 * ά��Ա�˴���ά�޵����沢���Ͱ�ť ���ڱ�ʱ�ɵ��� �ڱ�ʱҲ�ɵ���
	 */
	public static final String MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVE = BASEPATHURL
			+ "worker/takeorder/save?";

	/**
	 * ά��Ա�˸��ݵ���ģ����ѯά�޵�
	 */
	public static final String MAINTAIN_SEARCH = BASEPATHURL + "worker/fixorder/search";

	/**
	 * ά��Ա�˸��ݵ���ģ����ѯά���е�ά�޵�
	 */
	public static final String MAINTAINHOME_SEARCHING = BASEPATHURL + "worker/fixorder/list/progress/search";

	/**
	 * ά��Ա�˸��ݵ���ģ����ѯ������
	 */
	public static final String MAINTAINHOME_SEARCHAPPLYLIST = BASEPATHURL + "worker/expenseorder/progress/search";

	/**
	 * ��֯�ܹ��б�ӿ� �������û�
	 */
	public static String ORGANIZATION_TREE_USER = BASEPATHURL + "organization/provider/tree/user";
	/**
	 * ��֯�ܹ��б�ӿ� ����
	 */
	public static String ORGANIZATION_TREE_ORG = BASEPATHURL + "organization/provider/tree/org";
	/**
	 * ��֯�ܹ��б�ӿ� ��Ŀ¼
	 */
	public static String ORGANIZATION_TREE = BASEPATHURL + "organization/provider/tree";
	/**
	 * ͨ���ýӿ�,��Ӧ�̹���Ա����������Ӧ�̼�������������
	 */
	public static String WORKER_SELECT = BASEPATHURL + "worker/select";
	/**
	 * ��Ӧ�̶�,��Ӧ�̹���Ա���䶩����ά��Ա
	 */

	public static String PROVIDER_DISTRIBUTE = BASEPATHURL + "provider/distribute";

	/**
	 * ��Ӧ�̶� ���ݵ���ģ����ѯά�޵��Ľӿ�
	 */
	public static String SUPPLIER_REPAIRS_SEARCH = BASEPATHURL + "provider/fixorder/search";
	/**
	 * ��ʯ�Ͷ�,��ȡ���еȴ������е�ά�޵��б�
	 */
	public static final String PETROCHINA_GET_WAITEXAMINEENTRY = BASEPATHURL + "customer/approve/list/wait";
	/**
	 * ��ʯ�Ͷ�,��ȡ������ʷ����ά�޵��б�
	 */
	public static final String PETROCHINA_GET_HISTORYEXAMINEENTRY = BASEPATHURL + "customer/approve/list/history?";

	/**
	 * ��ʯ�Ͷ�,��ȡ��Ӧ����Ϣͳ���б�
	 */
	public static final String PETROCHINA_GET_PROVIDERENTRYSTATS = BASEPATHURL + "customer/provider/list/stats?";
	/**
	 * ��ʯ�Ͷ�,ͳ�ƻ�ȡ���еȴ������е�ά�޵���¼��
	 */
	public static final String PETROCHINA_GET_COUNTWAIT_RENTRYSTATS = BASEPATHURL + "customer/approve/count/wait";
	/**
	 * ��ʯ�Ͷ�,ͨ���ؼ���������ȡ��Ӧ����Ϣͳ���б�
	 */
	public static final String PETROCHINA_GET_SEARCHPROVIDERENTRYSTATS = BASEPATHURL + "customer/provider/search/stats";
	/**
	 * ��ʯ�Ͷ�,��ȡ��Ӧ������ά�޵��б�
	 */
	public static final String PETROCHINA_GET_PROVIDERENTRYFIXORDER = BASEPATHURL + "customer/provider/list/fixorder?";
	/**
	 * ��ʯ�Ͷ�,��ȡ����վ��Ϣͳ���б�
	 */
	public static final String PETROCHINA_GET_STATIONENTRYSTATS = BASEPATHURL + "customer/station/list/stats?";
	/**
	 * ��ʯ�Ͷ�,ͨ���ؼ���������ȡ����վ��Ϣͳ���б�
	 */
	public static final String PETROCHINA_GET_SEARCHPSTATIONENTRYSTATS = BASEPATHURL + "customer/station/search/stats";
	/**
	 * ��ʯ�Ͷ�,��ȡ��ȡ����վ����ά�޵��б�
	 */
	public static final String PETROCHINA_GET_STATIONNTRYFIXORDER = BASEPATHURL + "customer/station/list/fixorder?";
	/**
	 * ��ʯ�Ͷ�,��ȡά����Ŀ��Ϣͳ���б�
	 */
	public static final String PETROCHINA_GET_PROJECTENTRYSTATS = BASEPATHURL + "customer/project/list/stats?";
	/**
	 * ��ʯ�Ͷ�,��ȡĳ��Ŀ�¼���վͳ����Ϣ�б�
	 */
	public static final String PETROCHINA_GET_PROJECTESTATIONNTRYFIXORDER = BASEPATHURL
			+ "customer/project/station/stats?";
	/**
	 * ��ʯ�Ͷ�,ͨ���ؼ���������ȡά����Ŀ��Ϣͳ���б�
	 */
	public static final String PETROCHINA_GET_SEARCHPPROJECTENTRYSTATS = BASEPATHURL + "customer/project/search/stats";

	/**
	 * ��ʯ�Ͷ�,����ͨ��ά�޵�
	 */
	public static final String PETROCHINA_APPROVED_SERVICEMONAD = BASEPATHURL + "customer/fixorder/approved";
	/**
	 * ��ʯ�Ͷ�,��������ά�޵�
	 */
	public static final String PETROCHINA_UNAPPROVED_SERVICEMONAD = BASEPATHURL + "customer/fixorder/unapproved";
	/**
	 * ��ʯ�Ͷˣ����͹�������е�վ��
	 */
	public static final String PETROCHINA_SENDDALNOTICE = BASEPATHURL + "notice/company/sendstation";
	/**
	 * ��ʯ�Ͷ�,��ȡ������ʷ�����б�
	 */
	public static final String PETROCHINA_GETHISTORYNOTICEENTRY = BASEPATHURL + "customer/notice/list/history";
	/**
	 * ��ʯ�Ͷ�,���ݵ���ģ����ѯά�޵��б�
	 */
	public static final String PETROCHINA_SEARCHEXAMINEENTRY = BASEPATHURL + "customer/fixorder/search";

	/**
	 * ���޵����龲̬��ҳ���� ��ҪЯ�����޵�ID
	 */
	public static final String REPAIRSMESSAGE = WEBPAGEPATHURL + "repairorder/detail/";
	/**
	 * ֤�����龲̬��ҳ���� ��ҪЯ�����޵�ID
	 */
	public static final String CREDENTIALSMESSAGE = WEBPAGEPATHURL + "certificate/detail/";
	/**
	 * ������ά�޵����龲̬��ҳ���� ��ҪЯ��ά�޵�ID
	 */
	public static final String SERVICEMESSAGE = WEBPAGEPATHURL + "fixorder/detail/";
	/**
	 * վ����ά�޵����龲̬��ҳ���� ��ҪЯ��ά�޵�ID
	 */
	public static final String CENTRESERVICEMESSAGE = WEBPAGEPATHURL + "fixorder/site/detail/";
	/**
	 * ά�޵����龲̬��ҳ�������0323 ��ҪЯ��ά�޵�ID
	 */
	public static final String SERVICEMESSAGENEW = WEBPAGEPATHURL + "fixorder/worker/detail/";
	/**
	 * ���������龲̬��ҳ���� ��ҪЯ��ά�޵�ID
	 */
	public static final String APPLYMESSAGE = WEBPAGEPATHURL + "expenseorder/detail/";
	/**
	 * ����վ���龲̬��ҳ���� ��ҪЯ������վ����ID
	 */
	public static final String STATIONMESSAGE = WEBPAGEPATHURL + "station/detail/";
	/**
	 * ��Ӧ��������ҳ�ӿ� ��ҪЯ������վ����ID
	 */
	public static final String PROVIDERMESSAGE = WEBPAGEPATHURL + "provider/detail/";

	/**
	 * �鿴������������ҳ�ӿ� ��ҪЯ������������ID
	 */
	public static final String REIMBURSEMENTMESSAGE = WEBPAGEPATHURL + "expenseorder/detail/";
	/**
	 * �鿴������������ҳ�ӿ� ��ҪЯ������������ID
	 */
	public static final String APPROVESERVICEMESSAGE = WEBPAGEPATHURL + "approve/detail/";

	/**
	 * վ���˸��ݵ���ģ����ѯ���޵�
	 */

	public static final String CENTRE_WARRANTY_SEARCH = BASEPATHURL + "site/repairorder/search?sn=";
	/*
	 * ������,��ȡ��ǰ�û��Ĺ����б� 
	 */
	public static final String COMMONALITY_GETNOTICEENTRY = BASEPATHURL + "notice/member/list/";
	/*
	 * ������,��ȡ��ǰ��½�û���Ϣ 
	 */
	public static final String COMMONALITY_GETUSERMESSAGE = BASEPATHURL + "member/profile";
	/*
	 * ������,ȷ�ϵ�ǰ�û��Ѿ��յ��˹���
	 */
	public static final String COMMONALITY_READMEMBERNOTICE = BASEPATHURL + "notice/member/read";
	/**
	 * ��Ӧ�̶˸��ݵ���ģ����ѯ�ѷ���ı��޵�
	 */
	public static final String SUPPLIER_WARRANY_SEARCH = BASEPATHURL + "provider/repairorder/distributed/search";

	/**
	 * ��Ӧ�̻�ȡ������ʷ�ı������б�Ľӿ�
	 */
	public static final String SUPPLIER_REIMBURSEMENTED = BASEPATHURL + "provider/expenseorder/list/history";

	/**
	 * ��Ӧ�̻�ȡ���д����еı������б�Ľӿ�
	 */
	public static final String SUPPLIER_REIMBURSEMENT = BASEPATHURL + "provider/expenseorder/list/progress";

	/**
	 * ��Ӧ�̸��ݵ���ģ����ѯ��ʷ�ı������б�Ľӿ�
	 */
	public static final String SUPPLIER_SEARCH_REIMBURSEMENTED = BASEPATHURL + "provider/expenseorder/history/search";

	/**
	 * ��Ӧ�̸��ݵ���ģ����ѯ�����еı������б�Ľӿ�
	 */
	public static final String SUPPLIER_SEARCH_REIMBURSEMENT = BASEPATHURL
			+ "provider/expenseorder/progress/search?sn=";

	/**
	 * ��Ӧ������ͨ���������Ľӿ�
	 */
	public static final String SUPPLIER_APPROVED_SERVICEMONAD = BASEPATHURL + "provider/expenseorder/approved";

	/**
	 * ��Ӧ���������ر������Ľӿ�
	 */
	public static final String SUPPLIER_UNAPPROVED_SERVICEMONAD = BASEPATHURL + "provider/expenseorder/unapproved";

	/**
	 * ��Ӧ�̶˻�ȡ������ʷ�����б�Ľӿ�
	 */
	public static final String SUPPLIER_NOTICELIST_HISTORY = BASEPATHURL + "provider/notice/list/history";

	/**
	 * ��Ӧ�̶˷���֪ͨ��ָ���û��б�Ľӿ�
	 */
	public static final String SUPPLIER_SEND_NOTICE = BASEPATHURL + "notice/provider/send";

	/**
	 * ��Ӧ�̶�ͨ������Id��ȡ�����б�Ľӿ�
	 */

	public static final String SUPPLIER_RECORD_NOTICE = BASEPATHURL + "noticerecord/list";

	/**
	 * ��Ӧ�̶�ͨ������Id��ȡ�����б�Ľӿ�
	 */
	public static final String SUPPLIER_NOTICE_RECORD_LIST = BASEPATHURL + "noticerecord/list";

	/**
	 * ��ù�������Ľ��湫���ӿ�
	 */
	public static final String NOTICEDETAILS = WEBPAGEPATHURL + "notice/detail/";
	/**
	 * ��ʯ�Ͷˣ��鿴����������ҳ�ӿ�
	 */
	public static final String CNPCNOTICEDETAILS= WEBPAGEPATHURL + "notice/customer/detail/";

	/**
	 * ��Ӧ�̶˷�������Ա�� �Ķ�֪ͨ�Ľӿ�
	 */
	public static final String SUPPLIER_POST_NOTICE_ALERT = BASEPATHURL + "noticerecord/provider/alert";

	/**
	 * ��Ӧ�̶�,�鿴ά�޵�������ҳ�ӿ�
	 */
	public static final String SERVICEMESSAGE1 = WEBPAGEPATHURL + "fixorder/provider/detail/";
	
}
