package com.cnpc.zhibo.app;
/*
 * 站长端的底部菜单审批界面上点击列表看到的审批信息的详情界面
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
	private ImageView back, see;// 返回按钮、查看审批消息的界面
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_fragment_grabble_);
		set_view();
	}

	// 设置布局上的控件
	private void set_view() {
		set_title_text("审批详情");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);// 返回按钮
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		see = (ImageView) findViewById(R.id.imageView_myactity_you);//查看审批消息
		web=(WebView) findViewById(R.id.webView_Centre_page_examine_message);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		web.getSettings().setBlockNetworkImage(false);// 可加载图片
		web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 没有网络时家在缓存
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
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_page_examine_Activity.this);

				break;
			case R.id.imageView_myactity_you:// 查看审批消息的界面
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
