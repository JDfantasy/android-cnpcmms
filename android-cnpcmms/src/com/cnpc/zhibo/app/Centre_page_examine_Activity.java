package com.cnpc.zhibo.app;
/*
 * վ���˵ĵײ��˵����������ϵ���б�����������Ϣ���������
 */
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Centre_page_examine_Activity extends MyActivity {
	private ImageView back, see;// ���ذ�ť���鿴������Ϣ�Ľ���
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_fragment_grabble_);
		set_view();
	}

	// ���ò����ϵĿؼ�
	private void set_view() {
		set_title_text("��������");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);// ���ذ�ť
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		see = (ImageView) findViewById(R.id.imageView_myactity_you);//�鿴������Ϣ
		web=(WebView) findViewById(R.id.webView_Centre_page_examine_message);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// ������Ӳ������ⲿ�����
		web.getSettings().setBlockNetworkImage(false);// �ɼ���ͼƬ
		web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// û������ʱ���ڻ���
		web.loadUrl(Myconstant.SERVICEMESSAGE+getIntent().getStringExtra("id"));
		
		if (getIntent().getStringExtra("state").equals("wait")) {
			web.loadUrl(Myconstant.CENTRESERVICEMESSAGE+getIntent().getStringExtra("id"));
			//see.setVisibility(View.GONE);
		} else {
			web.loadUrl(Myconstant.APPROVESERVICEMESSAGE+getIntent().getStringExtra("id"));
			//see.setVisibility(View.VISIBLE);
		}
		see.setOnClickListener(l);
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_page_examine_Activity.this);

				break;
			case R.id.imageView_myactity_you:// �鿴������Ϣ�Ľ���
				Intent in = new Intent(Centre_page_examine_Activity.this, Centre_see_examine_messageActivity.class);
				in.putExtra("id", getIntent().getStringExtra("id"));
				startActivity(in);
				Myutil.set_activity_open(Centre_page_examine_Activity.this);
				break;

			default:
				break;
			}

		}
	};

}
