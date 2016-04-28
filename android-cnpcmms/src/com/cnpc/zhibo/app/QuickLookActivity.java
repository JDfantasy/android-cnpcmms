package com.cnpc.zhibo.app;

//���ٲ���
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

	// ���ý���ķ���
	private void setview() {
		set_title_text("���ٲ���");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);

	}

	// ���ü����¼�
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
