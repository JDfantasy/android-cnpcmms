package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

/*
 * վ���˵ײ��˵��е�ά�޽����е���б�鿴ά�޵�����Ľ���
 */
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Centre_page_maintainActivity extends MyActivity {
	private ImageView back;// ���ذ�ť��
	private WebView web;// ���ؾ�̬��ҳ�Ŀؼ�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_page_maintain);
		setview();
	}

	// ���ý���ķ���
	private void setview() {
		set_title_text("ά�޵�����");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		web = (WebView) findViewById(R.id.webView_Centre_page_maintain);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// ������Ӳ������ⲿ�����
		web.getSettings().setBlockNetworkImage(false);// �ɼ���ͼƬ
		web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// û������ʱ���ڻ���
		web.loadUrl(Myconstant.CENTRESERVICEMESSAGE + getIntent().getStringExtra("id"));

	}

	// �����¼�
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_page_maintainActivity.this);

				break;

			default:
				break;
			}

		}
	};
}
