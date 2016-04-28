package com.cnpc.zhibo.app;
/*
 * 中石油端查看加油站维修单详情的界面
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
	
	//设置界面的方法
	private void setview(){
		set_title_text("维修单详情");
		back=(ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		web=(WebView) findViewById(R.id.webview_petrochina_see_station_repairsmessage);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		web.getSettings().setBlockNetworkImage(false);// 可加载图片
		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 没有网络时家在缓存
		web.loadUrl(Myconstant.SERVICEMESSAGE+getIntent().getStringExtra("id"));
	}
	private View.OnClickListener l=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
		    switch (v.getId()) {
			case R.id.imageView_myacitity_zuo://返回按钮
				finish();
				Myutil.set_activity_close(Petrochina_see_station_repairsmessageActivity.this);
				
				break;

			default:
				break;
			}
			
		}
	};
}
