package com.cnpc.zhibo.app;

import java.util.Map;

import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.util.CornerUtil.OrderNumsHelper;
import com.cnpc.zhibo.app.view.BadgeView;

//APP�����activity

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
	private TextView txz, txy, title;// ���Ͻǵ�textview�����Ͻǵ�textview
	private ImageView imaz, imay;// ���Ͻǵ�imageview�����Ͻǵ�imageview
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
		//�������ֱ�ʾ����Ϣ֪ͨhandler
		SysApplication.getInstance().setCornerHandler(cornerHandler);
		//������Ϣ�������ֽǱ�
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
		// ��������ʽ����AnimationType.keyֵ
		/**
		 * FLIP_HORIZON����������ת FLIP_VERTICAL����������ת ALPHA��������ʾ
		 * HORIZION_LEFT�������ƽ����ʾ HORIZION_RIGHT�����Ҳ�ƽ����ʾ HORIZON_CROSS��������ͬʱƽ����ʾ
		 * ROTATE������Բ��ת SCALE��������
		 */
	}
	/**
	 * ��handler�����ݸ�Application����Ϊ������Ϣ����ø������ֽǱ����Ϣ����
	 */
	private Handler cornerHandler = new Handler(){
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			String type =msg.getData().getString("type");
			CornerUtil.changeCorner(getBadgerView(type), type,setters.get(type));
		};
	};
	/**
	 * ���ڴ��handler�ļ���
	 */
	private Map<String, BadgeView> map = SysApplication.getInstance().getMap();

	/**
	 * �Ա�֪ͨ��������ö�Ӧ��setter
	 */
	private Map<String,OrderNumsHelper> setters = SysApplication.getInstance().getSettings();
	
	/**
	 * ������Ҫ�������ֵĽǱ����
	 * @param view
	 * @param type
	 */
	public BadgeView setBadgerView(View view,String type,OrderNumsHelper helper) {
		map.put(type, CornerUtil.addCorner(this, view, type,helper));
		setters.put(type, helper);
		return getBadgerView(type);
	}
	
	/**
	 * ��ȡ����ĵ����ֽǱ����
	 * @param type
	 * @return
	 */
	public BadgeView getBadgerView(String type) {
		return map.get(type);
	}
	/**
	 * ������ֽǱ�
	 * @param type
	 */
	public void clearNums(String type) {
		CornerUtil.clearNums(getBadgerView(type), type);
	}
	/**
	 * ��Ϣģ��Ǳ꣨��������δ����Ϣ������
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
					System.out.println("badgeViewֵΪ��");
				}
				
			}
		};
	};
	/**
	 * �����Ϣģ��Ǳ꣨��������δ����Ϣ������
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
