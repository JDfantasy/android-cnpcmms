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
	private ImageView fanhui;// 返回按钮
	private RequestQueue mqueQueue;// 请求对象

	private WebView web;// 加载静态网页的控件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_see_sp_news);
		setview();
	}

	// 设置界面上的控件的方法
	private void setview() {
		set_title_text("审批管理");
		mqueQueue = Volley.newRequestQueue(this);// 初始化请求对象
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);

		web = (WebView) findViewById(R.id.webview_centre_see_sp_news);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		web.getSettings().setBlockNetworkImage(false);// 可加载图片
		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 没有网络时家在缓存
		web.loadUrl(Myconstant.CENTRESERVICEMESSAGE + getIntent().getStringExtra("id"));

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_See_examine_itemActivity.this);

				break;

			default:
				break;
			}

		}
	};
	

}
