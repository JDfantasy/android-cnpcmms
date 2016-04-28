package com.cnpc.zhibo.app.util;

import java.util.List;

import com.cnpc.zhibo.app.view.BadgeView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;

import android.content.Context;
import android.view.View;

/**
 * 角标工具，调用该方法，可快速为控件添加数字标示
 * @author xicunyou
 *
 */
public class CornerUtil {
	/**
	 * 添加数字标示
	 * @param context 上下文
	 * @param view 需要添加数字标示的控件
	 * @return 返回标示控件
	 */
	public static BadgeView addCorner(Context context,View view,String type,OrderNumsHelper helper){
		BadgeView badge = new BadgeView(context, view);
		return changeCorner(badge, type,helper);
	}
	/**
	 * 改变显示数字
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
//			暂时隐藏掉获取数字的方式，数字获取通过网络请求修改
//			badgeView.setText(getCount(type));
			helper.setViewNums(badgeView);
			badgeView.show();
		}
		
		return badgeView;
	}
	/**
	 * 获取admin通知type类型的未读数量
	 * @param type
	 * @return
	 */
/*	private  static String getCount(String type){
		 int i = 0;
		 EMConversation s=EMChatManager.getInstance().getConversation("admin");
		 List<EMMessage> list=s.getAllMessages();
		 for(EMMessage m:list){
			 if(m.isUnread()&&m.getStringAttribute("type", "test").equals(type)){//为未读并且是与扩展属性相同
				 i++;
			}
		 }
		return ""+i;
	}*/
	
	/**
	 * 点击后将所有type类型的未读消息变成已读
	 * @param badgeView
	 * @param type
	 */
	public static void clearNums(BadgeView badgeView,String type){
		EMConversation s=EMChatManager.getInstance().getConversation("admin");
		 List<EMMessage> list=s.getAllMessages();
		 for(EMMessage m:list){
			 if(m.isUnread()&&m.getStringAttribute("type", "test").equals(type)){//为未读并且是与扩展属性相同
				m.setUnread(false);
			}
		 }
		 if(badgeView.isShown()){
				badgeView.hide();
		}
	}
	
	/**
	 * 用于设置请求工单数量的接口
	 * @author xicunyou
	 *
	 */
	public interface OrderNumsHelper{
		/**
		 * 该方法中需要实现显示角标获取的代码并将获取到的数字设置给badgeView
		 * 设置方法badgeView.setText( );
		 * @param badgeView
		 */
		public void setViewNums(BadgeView badgeView);
	}
}
