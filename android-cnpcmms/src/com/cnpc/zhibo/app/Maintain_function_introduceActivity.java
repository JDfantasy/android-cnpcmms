package com.cnpc.zhibo.app;
import com.cnpc.zhibo.app.util.Myutil;

/*
 * 维修员的功能介绍
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
//维修端的功能介绍
public class Maintain_function_introduceActivity extends MyActivity {
	private ImageView fanhui;
	private LinearLayout servicelist,alloction,noticemanager,service,apply;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_function_introduce);
		setViews();
	}

	private void setViews() {
		set_title_text("功能介绍");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		servicelist=(LinearLayout) findViewById(R.id.maintain_function_servicelist);//维修单
		alloction=(LinearLayout) findViewById(R.id.maintain_function_alloction);
		noticemanager=(LinearLayout) findViewById(R.id.maintain_function_notice);
		service=(LinearLayout) findViewById(R.id.maintain_function_service);
		apply=(LinearLayout) findViewById(R.id.maintain_function_apply);
		servicelist.setOnClickListener(l);
		alloction.setOnClickListener(l);
		noticemanager.setOnClickListener(l);
		service.setOnClickListener(l);
		apply.setOnClickListener(l);
	}
	
	View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Maintain_function_introduceActivity.this);
				break;
			case R.id.maintain_function_servicelist://维修单
				jumpfunctiondetailActivity("servicelist");
				break;
			case R.id.maintain_function_alloction://分配通知
				jumpfunctiondetailActivity("alloction");
				break;
			case R.id.maintain_function_notice://公告管理
				jumpfunctiondetailActivity("notice");
				break;
			case R.id.maintain_function_service://维修
				jumpfunctiondetailActivity("service");
				break;
			case R.id.maintain_function_apply://报销
				jumpfunctiondetailActivity("apply");
			default:
				break;
			}

		}
	};
	
	//界面跳转
	private void jumpfunctiondetailActivity(String value){
		Intent intent=new Intent(Maintain_function_introduceActivity.this,Maintain_functiondetailsActivity.class);
		intent.putExtra("functionName", value);
		startActivity(intent);
		
	}
	
}
