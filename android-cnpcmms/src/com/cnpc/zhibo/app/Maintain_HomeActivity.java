package com.cnpc.zhibo.app;
import org.apache.commons.collections.functors.InstanceofPredicate;

//维修工的主页
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.fragment.Fragment_maintain_apply;
import com.cnpc.zhibo.app.fragment.Fragment_maintain_home;
import com.cnpc.zhibo.app.fragment.Fragment_maintain_repairs;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.BadgeView;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Maintain_HomeActivity extends MyActivity {
	
	private Fragment_maintain_apply f_Apply;//报销
	private Fragment_maintain_home f_Home;//主页
	private Fragment_maintain_repairs f_Repairs;//维修
	private Fragment f0;// 最初的fragment对象
	private FragmentManager fm;// fragment的管理类对象
	//private LinearLayout home, apply, repairs;// 首页、报销、维修
	private RelativeLayout home, apply, repairs;// 首页、报销、维修
	private ImageView ima[];
	private TextView hometext,servicetext,applytext;
	private ImageView back;//返回按键
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home);
		set_title_text("维修员的首页");
		setview();// 将界面上的控件进行实例化
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//Log.i("tag", "onNewIntent");
		FragmentTransaction transaction = fm.beginTransaction();// 开始一个事务
		Fragment ft = fm.findFragmentByTag(f_Apply.getClass().getCanonicalName());
		if(ft instanceof Fragment_maintain_apply){
			f_Apply.setnotApplylist();
		}
	}

	//控件初始化
	private void setview() {
		//底部文字 三个
		hometext=(TextView) findViewById(R.id.maintain_hometext);
		servicetext=(TextView) findViewById(R.id.maintain_servicetext);
		applytext=(TextView) findViewById(R.id.maintain_applytext);
//		hometext.setOnClickListener(l);
//		servicetext.setOnClickListener(l);
//		applytext.setOnClickListener(l);
		//添加按钮
		/*home = (LinearLayout) findViewById(R.id.linearlayout_maintain_home_shouye);// 首页
		apply = (LinearLayout) findViewById(R.id.linearlayout_maintain_home_baoxiao);// 报销
		repairs = (LinearLayout) findViewById(R.id.linearlayout_maintain_home_weixiu);// 维修
*/
		home = (RelativeLayout) findViewById(R.id.relativelayout_maintain_home_shouye);// 首页
		apply = (RelativeLayout) findViewById(R.id.relativelayout_maintain_home_baoxiao);// 报销
		repairs = (RelativeLayout) findViewById(R.id.relativelayout_maintain_home_weixiu);// 维修
		//测试数字角标
		/*BadgeView badge=new BadgeView(this, repairs);
		badge.setText("9");
		badge.show();*/
//		setBadgerView(CornerUtil.addCorner(this, repairs, "worker-fixorder", null),
//				"worker-fixorder", null);//给维修按钮设置数字
//		setBadgerView(CornerUtil.addCorner(this, apply, "worker-expenseorder", null),
//				"worker-expenseorder", null);//给报销按钮设置数字
		
		home.setOnClickListener(l);
		apply.setOnClickListener(l);
		repairs.setOnClickListener(l);
		f_Home = new Fragment_maintain_home();// 首页的fragment的实例化
		f_Apply = new Fragment_maintain_apply();// 报销的fragment的实例化
		f_Repairs = new Fragment_maintain_repairs();// 维修的fragment的实例化
		f0 = new Fragment();
		fm = getSupportFragmentManager();// 获取fragment的管理类的对象
		xianshiFragment(f_Home);
		ima = new ImageView[3];// 界面底部的三个按钮的图标
		ima[0] = (ImageView) findViewById(R.id.imageView_maintain_home_shouye);
		ima[1] = (ImageView) findViewById(R.id.imageView_maintain_home_weixiu);
		ima[2] = (ImageView) findViewById(R.id.imageView_maintain_home_baoxiao);
		ima[0].setImageResource(R.drawable.maintain_home);
		ima[1].setImageResource(R.drawable.maintain_servicehui);
		ima[2].setImageResource(R.drawable.maintain_applyhui);
		hometext.setTextColor(getResources().getColor(R.color.dingbubeijingse));
		servicetext.setTextColor(getResources().getColor(R.color.zitiyanse3));
		applytext.setTextColor(getResources().getColor(R.color.zitiyanse3));
		back=(ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		back.setImageResource(R.drawable.useimessagecon);
		back.setOnClickListener(l);
	}
	  @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event)  
	    {  
	        if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {  
	            // 创建退出对话框  
	            AlertDialog isExit = new AlertDialog.Builder(this).create();  
	            // 设置对话框标题  
	            isExit.setTitle("系统提示");  
	            // 设置对话框消息  
	            isExit.setMessage("确定要退出吗");  
	            // 添加选择按钮并注册监听  
	            isExit.setButton("确定", listener);  
	            isExit.setButton2("取消", listener);  
	            // 显示对话框  
	            isExit.show();  
	  
	        }  
	          
	        return false;  
	          
	    }  
	    /**监听对话框里面的button点击事件*/  
	    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
	                finish();  
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };    
	// 控件的点击事件的监听的方法
		private View.OnClickListener l = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.maintain_hometext:// 首页
					seticon(0);
					set_title_text("维修员的首页");
					xianshiFragment(f_Home);
					break;
				case R.id.relativelayout_maintain_home_shouye:// 首页
					seticon(0);
					set_title_text("维修员的首页");
					xianshiFragment(f_Home);
					break;
				case R.id.maintain_applytext://报销
					seticon(2);
					//clearNums("worker-expenseorder");//取消报销按钮上的数字
					set_title_text("维修员的报销");
					xianshiFragment(f_Apply);
					break;
				case R.id.relativelayout_maintain_home_baoxiao://报销
					seticon(2);
					//clearNums("worker-expenseorder");//取消报销按钮上的数字
					set_title_text("维修员的报销");
					xianshiFragment(f_Apply);
					break;
				case R.id.maintain_servicetext:// 维修
					seticon(1);
					//clearNums("worker-fixorder");//取消维修按钮上的数字
					set_title_text("维修员的维修");
					xianshiFragment(f_Repairs);
					break;
				case R.id.relativelayout_maintain_home_weixiu:// 维修
					seticon(1);
					//clearNums("worker-fixorder");//取消维修按钮上的数字
					set_title_text("维修员的维修");
					xianshiFragment(f_Repairs);
					break;
				
				case R.id.imageView_myactity_you:
					Intent in=new Intent(Maintain_HomeActivity.this, User_messagectivity.class);
					startActivity(in);
					Myutil.set_activity_open(Maintain_HomeActivity.this);
					break;
				default:
					break;
				}

			}
		};
	
	// Fragment的替换的方法
		private void xianshiFragment(Fragment f) {
			FragmentTransaction transaction = fm.beginTransaction();// 开始一个事务
			Fragment ft = fm.findFragmentByTag(f.getClass().getCanonicalName());
			// 查找事物中的fragment并获取fragment对象
			if (f0 != null && f0 == f) {
				// 当要替换显示的fragment是当前正在显示的fragment就不发生改变，直接退出
				return;
			}
			if (f0 != null) {
				transaction.hide(f0);
				// 当要替换显示的fragment不是当前显示的fragment时，将当前的fragment进行隐藏

			}
			if (ft != null) {
				transaction.show(ft);
				// 如果要显示的fragment在事物中已经存在，那么就将它进行显示就可以了
			} else {
				transaction.add(R.id.linearlayout_centre_home_fragment, f, f
						.getClass().getCanonicalName());
				// 如果要显示的fragment不存在事物中，那么就在事物中添加要显示的fragment

			}
			f0 = f;// 将当前显示的fragment设置成要替换的fragment对象
			transaction.commit();// 提交事务
		}

		// 改变底部三个按钮图标的方法
		private void seticon(int id) {
			switch (id) {
			case 0:
				ima[0].setImageResource(R.drawable.maintain_home);
				ima[1].setImageResource(R.drawable.maintain_servicehui);
				ima[2].setImageResource(R.drawable.maintain_applyhui);
				hometext.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				servicetext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				applytext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				break;
			case 1:
				ima[0].setImageResource(R.drawable.maintain_homehui);
				ima[1].setImageResource(R.drawable.maintain_service);
				ima[2].setImageResource(R.drawable.maintain_applyhui);
				hometext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				servicetext.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				applytext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				break;
			case 2:
				ima[0].setImageResource(R.drawable.maintain_homehui);
				ima[1].setImageResource(R.drawable.maintain_servicehui);
				ima[2].setImageResource(R.drawable.maintain_apply);
				hometext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				servicetext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				applytext.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				break;

			default:
				break;
			}
		}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("tag", "Maintain_HomeActivity-onPause");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.i("tag", "Maintain_HomeActivity-onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("tag", "Maintain_HomeActivity-onDestroy");
	}

	

}
