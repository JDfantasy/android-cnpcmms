package com.cnpc.zhibo.app;
/*
 * ��ʯ�Ͷ˲鿴����վά�޵�����Ľ���
 */
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.ImageView;

public class Petrochina_see_station_repairsmessageActivity extends MyActivity {
private ImageView back;
private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_see_station_repairsmessage);
		setview();
	}
	
	//���ý���ķ���
	private void setview(){
		set_title_text("ά�޵�����");
		back=(ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		web=(WebView) findViewById(R.id.webview_petrochina_see_station_repairsmessage);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// ������Ӳ������ⲿ�����
		web.getSettings().setBlockNetworkImage(false);// �ɼ���ͼƬ
		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// û������ʱ���ڻ���
		web.loadUrl(Myconstant.SERVICEMESSAGE+getIntent().getStringExtra("id"));
	}
	private View.OnClickListener l=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
		    switch (v.getId()) {
			case R.id.imageView_myacitity_zuo://���ذ�ť
				finish();
				Myutil.set_activity_close(Petrochina_see_station_repairsmessageActivity.this);
				
				break;

			default:
				break;
			}
			
		}
	};
}
