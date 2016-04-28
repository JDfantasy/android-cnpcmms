package com.cnpc.zhibo.app;

/**
 * 关于我们的界面
 */
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsActivity extends MyActivity {
	private ImageView fanhui;
	private TextView functionintroduce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		setview();
	}

	// 设置界面的方法
	private void setview() {
		set_title_text("关于我们");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		functionintroduce = (TextView) findViewById(R.id.textView_about_us_functionintroduce);
		functionintroduce.setOnClickListener(l);
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(AboutUsActivity.this);

				break;
			case R.id.textView_about_us_functionintroduce:// 功能介绍
				Intent in = new Intent();
				String activity = getIntent().getStringExtra("activity");// 获取打开关于我们界面的用户的类型
				if (activity.equals("site")) {// 站长端
                   in.setClass(AboutUsActivity.this, Centre_Function_IntroduceActivity.class);
				} else if (activity.equals("worker")) {// 维修员端
					 in.setClass(AboutUsActivity.this, Maintain_function_introduceActivity.class);
				} else if (activity.equals("provider")) {// 供应商端
					 in.setClass(AboutUsActivity.this, Supplier_Function_IntroduceActivity.class);
				} else {// 中石油端
					 in.setClass(AboutUsActivity.this, Petrochina_function_introduceActivity.class);
				}
				startActivity(in);
				Myutil.set_activity_open(AboutUsActivity.this);

				break;
			default:
				break;
			}

		}
	};

}
