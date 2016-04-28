package com.cnpc.zhibo.app.util;

import java.util.List;

import com.cnpc.zhibo.app.view.BadgeView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;

import android.content.Context;
import android.view.View;

/**
 * �Ǳ깤�ߣ����ø÷������ɿ���Ϊ�ؼ�������ֱ�ʾ
 * @author xicunyou
 *
 */
public class CornerUtil {
	/**
	 * ������ֱ�ʾ
	 * @param context ������
	 * @param view ��Ҫ������ֱ�ʾ�Ŀؼ�
	 * @return ���ر�ʾ�ؼ�
	 */
	public static BadgeView addCorner(Context context,View view,String type,OrderNumsHelper helper){
		BadgeView badge = new BadgeView(context, view);
		return changeCorner(badge, type,helper);
	}
	/**
	 * �ı���ʾ����
	 * @param badgeView
	 * @return
	 */
	public static BadgeView changeCorner(BadgeView badgeView,String type,OrderNumsHelper helper){
		if(badgeView==null){
			return null;
		}
//		if(getCount(type).equals("0")){
		if("0".equals(badgeView.getText())){
			if(badgeView.isShown()){
				badgeView.hide();
			}
		}else{
//			��ʱ���ص���ȡ���ֵķ�ʽ�����ֻ�ȡͨ�����������޸�
//			badgeView.setText(getCount(type));
			helper.setViewNums(badgeView);
			badgeView.show();
		}
		
		return badgeView;
	}
	/**
	 * ��ȡadmin֪ͨtype���͵�δ������
	 * @param type
	 * @return
	 */
/*	private  static String getCount(String type){
		 int i = 0;
		 EMConversation s=EMChatManager.getInstance().getConversation("admin");
		 List<EMMessage> list=s.getAllMessages();
		 for(EMMessage m:list){
			 if(m.isUnread()&&m.getStringAttribute("type", "test").equals(type)){//Ϊδ������������չ������ͬ
				 i++;
			}
		 }
		return ""+i;
	}*/
	
	/**
	 * ���������type���͵�δ����Ϣ����Ѷ�
	 * @param badgeView
	 * @param type
	 */
	public static void clearNums(BadgeView badgeView,String type){
		EMConversation s=EMChatManager.getInstance().getConversation("admin");
		 List<EMMessage> list=s.getAllMessages();
		 for(EMMessage m:list){
			 if(m.isUnread()&&m.getStringAttribute("type", "test").equals(type)){//Ϊδ������������չ������ͬ
				m.setUnread(false);
			}
		 }
		 if(badgeView.isShown()){
				badgeView.hide();
		}
	}
	
	/**
	 * �����������󹤵������Ľӿ�
	 * @author xicunyou
	 *
	 */
	public interface OrderNumsHelper{
		/**
		 * �÷�������Ҫʵ����ʾ�Ǳ��ȡ�Ĵ��벢����ȡ�����������ø�badgeView
		 * ���÷���badgeView.setText( );
		 * @param badgeView
		 */
		public void setViewNums(BadgeView badgeView);
	}
}
