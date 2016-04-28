package com.cnpc.zhibo.app;
import com.cnpc.zhibo.app.util.Myutil;

/*
 * ά��Ա�Ĺ��ܽ���
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
//ά�޶˵Ĺ��ܽ���
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
		set_title_text("���ܽ���");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		servicelist=(LinearLayout) findViewById(R.id.maintain_function_servicelist);//ά�޵�
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
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Maintain_function_introduceActivity.this);
				break;
			case R.id.maintain_function_servicelist://ά�޵�
				jumpfunctiondetailActivity("servicelist");
				break;
			case R.id.maintain_function_alloction://����֪ͨ
				jumpfunctiondetailActivity("alloction");
				break;
			case R.id.maintain_function_notice://�������
				jumpfunctiondetailActivity("notice");
				break;
			case R.id.maintain_function_service://ά��
				jumpfunctiondetailActivity("service");
				break;
			case R.id.maintain_function_apply://����
				jumpfunctiondetailActivity("apply");
			default:
				break;
			}

		}
	};
	
	//������ת
	private void jumpfunctiondetailActivity(String value){
		Intent intent=new Intent(Maintain_function_introduceActivity.this,Maintain_functiondetailsActivity.class);
		intent.putExtra("functionName", value);
		startActivity(intent);
		
	}
	
}
