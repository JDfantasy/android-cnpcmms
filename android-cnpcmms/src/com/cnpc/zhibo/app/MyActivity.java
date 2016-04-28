package com.cnpc.zhibo.app;

import java.util.Map;

import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.util.CornerUtil.OrderNumsHelper;
import com.cnpc.zhibo.app.view.BadgeView;

//APP父类的activity

import com.dk.animation.SwitchAnimationUtil;
import com.easemob.chat.EMChatManager;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MyActivity extends FragmentActivity {
	private int userid = 0;
	private TextView txz, txy, title;// 左上角的textview、右上角的textview
	private ImageView imaz, imay;// 左上角的imageview、右上角的imageview
	private SwitchAnimationUtil sw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sw = new SwitchAnimationUtil();
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		if (getIntent() != null) {
			userid = getIntent().getIntExtra("userid", 0);
		}
		//设置数字标示的消息通知handler
		SysApplication.getInstance().setCornerHandler(cornerHandler);
		//设置消息管理数字角标
		SysApplication.getInstance().setMessageHandler(messageHandler);
		
		if(savedInstanceState!=null){
			Bundle map1 = savedInstanceState.getBundle("Mytoken");
			Myconstant.token=map1.getString("AAA");
		}
	}

	public void set_title_text(String str) {
		title = (TextView) findViewById(R.id.textView_myactity_titlebar);
		title.setText(str);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK ){
			 Myutil.set_activity_close(MyActivity.this);
			 finish();
		 }
		return false;
		
		
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

		// sw.startAnimation(getWindow().getDecorView(),
		// AnimationType.HORIZION_LEFT);
		// 动画的样式——AnimationType.key值
		/**
		 * FLIP_HORIZON——左右旋转 FLIP_VERTICAL——上下旋转 ALPHA——逐渐显示
		 * HORIZION_LEFT——左侧平移显示 HORIZION_RIGHT——右侧平移显示 HORIZON_CROSS——左右同时平移显示
		 * ROTATE——半圆旋转 SCALE——缩放
		 */
	}
	/**
	 * 该handler将传递给Application，作为接收消息后调用更新数字角标的消息传递
	 */
	private Handler cornerHandler = new Handler(){
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			String type =msg.getData().getString("type");
			CornerUtil.changeCorner(getBadgerView(type), type,setters.get(type));
		};
	};
	/**
	 * 用于存放handler的集合
	 */
	private Map<String, BadgeView> map = SysApplication.getInstance().getMap();

	/**
	 * 以便通知到来后调用对应的setter
	 */
	private Map<String,OrderNumsHelper> setters = SysApplication.getInstance().getSettings();
	
	/**
	 * 设置需要更改数字的角标对象
	 * @param view
	 * @param type
	 */
	public BadgeView setBadgerView(View view,String type,OrderNumsHelper helper) {
		map.put(type, CornerUtil.addCorner(this, view, type,helper));
		setters.put(type, helper);
		return getBadgerView(type);
	}
	
	/**
	 * 获取需更改的数字角标对象
	 * @param type
	 * @return
	 */
	public BadgeView getBadgerView(String type) {
		return map.get(type);
	}
	/**
	 * 清除数字角标
	 * @param type
	 */
	public void clearNums(String type) {
		CornerUtil.clearNums(getBadgerView(type), type);
	}
	/**
	 * 消息模块角标（包含所有未读消息数量）
	 * @param view
	 */
	public void setBadgerView(View view){
		badgeView = new BadgeView(this, view);
		SysApplication.getInstance().setCacheView(badgeView);
		int count = EMChatManager.getInstance().getUnreadMsgsCount();
		if(count ==0){
			badgeView.hide();
		}else{
			badgeView.setText(count+"");
			badgeView.show();
		}
	}
	private BadgeView badgeView;
	private Handler messageHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			badgeView = (BadgeView) SysApplication.getInstance().getCacheView();
			int count = EMChatManager.getInstance().getUnreadMsgsCount();
			
			if(count ==0){
				badgeView.hide();
			}else{
				if (badgeView!=null) {
					badgeView.setText(count+"");
					badgeView.show();
				}else {
					System.out.println("badgeView值为空");
				}
				
			}
		};
	};
	/**
	 * 清除消息模块角标（包含所有未读消息数量）
	 */
	public void clearNums(){
		if(badgeView!=null){
				badgeView.hide();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBundle("Mytoken", saveState());
	}

	private Bundle saveState() {
		Bundle map=new Bundle();
		map.putString("AAA", Myconstant.token);
		return map;
	}
}
