package com.cnpc.zhibo.app;

/**
 * �������ǵĽ���
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

	// ���ý���ķ���
	private void setview() {
		set_title_text("��������");
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
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(AboutUsActivity.this);

				break;
			case R.id.textView_about_us_functionintroduce:// ���ܽ���
				Intent in = new Intent();
				String activity = getIntent().getStringExtra("activity");// ��ȡ�򿪹������ǽ�����û�������
				if (activity.equals("site")) {// վ����
                   in.setClass(AboutUsActivity.this, Centre_Function_IntroduceActivity.class);
				} else if (activity.equals("worker")) {// ά��Ա��
					 in.setClass(AboutUsActivity.this, Maintain_function_introduceActivity.class);
				} else if (activity.equals("provider")) {// ��Ӧ�̶�
					 in.setClass(AboutUsActivity.this, Supplier_Function_IntroduceActivity.class);
				} else {// ��ʯ�Ͷ�
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
