package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

/*
 * ��ʯ�Ͷ˲鿴��������Ľ���
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class Petrochina_seeannounce_messageActivity extends MyActivity {
	private WebView webView;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_seeannounce_message);
		setview();
	}

	private void setview() {
		set_title_text("��������");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		webView=(WebView) findViewById(R.id.webView_petrochina_seenounce_message);
		webView.loadUrl(Myconstant.CNPCNOTICEDETAILS+ getIntent().getStringExtra("noticeId"));

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Petrochina_seeannounce_messageActivity.this);

				break;

			default:
				break;
			}

		}
	};
}
