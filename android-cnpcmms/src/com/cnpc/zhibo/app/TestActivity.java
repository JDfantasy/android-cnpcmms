package com.cnpc.zhibo.app;

import com.android.volley.toolbox.NetworkImageView;
//主要用来进行选择用户身份的界面，属于测试界面
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestActivity extends MyActivity {
private NetworkImageView ima;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		set_title_text("选择身份的界面");
		

	}

	private View.OnClickListener l = new View.OnClickListener() {
     Intent in=new Intent();
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			
			}

			startActivity(in);
			Myutil.set_activity_open(TestActivity.this);
		}
	};

}
