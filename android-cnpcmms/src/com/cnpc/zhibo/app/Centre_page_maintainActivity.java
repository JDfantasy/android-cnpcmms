package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

/*
 * 站长端底部菜单中的维修界面中点击列表查看维修单详情的界面
 */
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Centre_page_maintainActivity extends MyActivity {
	private ImageView back;// 返回按钮、
	private WebView web;// 加载静态网页的控件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_page_maintain);
		setview();
	}

	// 设置界面的方法
	private void setview() {
		set_title_text("维修单详情");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		web = (WebView) findViewById(R.id.webView_Centre_page_maintain);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		web.getSettings().setBlockNetworkImage(false);// 可加载图片
		web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 没有网络时家在缓存
		web.loadUrl(Myconstant.CENTRESERVICEMESSAGE + getIntent().getStringExtra("id"));

	}

	// 监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_page_maintainActivity.this);

				break;

			default:
				break;
			}

		}
	};
}
