package com.cnpc.zhibo.app;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Centre_See_examine_itemActivity extends MyActivity {
	private ImageView fanhui;// ���ذ�ť
	private RequestQueue mqueQueue;// �������

	private WebView web;// ���ؾ�̬��ҳ�Ŀؼ�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_see_sp_news);
		setview();
	}

	// ���ý����ϵĿؼ��ķ���
	private void setview() {
		set_title_text("��������");
		mqueQueue = Volley.newRequestQueue(this);// ��ʼ���������
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);

		web = (WebView) findViewById(R.id.webview_centre_see_sp_news);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// ������Ӳ������ⲿ�����
		web.getSettings().setBlockNetworkImage(false);// �ɼ���ͼƬ
		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// û������ʱ���ڻ���
		web.loadUrl(Myconstant.CENTRESERVICEMESSAGE + getIntent().getStringExtra("id"));

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_See_examine_itemActivity.this);

				break;

			default:
				break;
			}

		}
	};
	

}
