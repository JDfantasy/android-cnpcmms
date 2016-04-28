package com.cnpc.zhibo.app;

//快速查找
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class QuickLookActivity extends MyActivity {
	private ImageView fanhui;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_look);
		setview();
	}

	// 设置界面的方法
	private void setview() {
		set_title_text("快速查找");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);

	}

	// 设置监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(QuickLookActivity.this);
				break;

			default:
				break;
			}
		}
	};

}
