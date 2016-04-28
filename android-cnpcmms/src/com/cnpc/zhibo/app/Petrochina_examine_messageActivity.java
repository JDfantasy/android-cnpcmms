package com.cnpc.zhibo.app;

/*
 * 中石油端查看维修单并且进行审批的界面
 */
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
//中石油端查看审批详情,并且进行处理的界面
import com.cnpc.zhibo.app.util.Myutil;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_examine_messageActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private Button agree, unagree;// 同意、驳回
	private WebView web;
	private TextView select;// 代开审批操作的按钮
	private PopupWindow pop;
	private EditText message;// 意见信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina);
		setview();
	}

	// 设置布局界面上的控件
	private void setview() {
		set_title_text("审批管理");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		select = (TextView) findViewById(R.id.textview_myacitivity_you);
		select.setOnClickListener(l);
		web = (WebView) findViewById(R.id.webView_petrochina_examine_message);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		web.getSettings().setBlockNetworkImage(false);// 可加载图片
		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 没有网络时家在缓存
		if (getIntent().getStringExtra("workStatus").equals("wait")) {
			select.setVisibility(View.VISIBLE);
			select.setText("审批");
			web.loadUrl(Myconstant.SERVICEMESSAGE + getIntent().getStringExtra("id"));
		} else {
			web.loadUrl(Myconstant.APPROVESERVICEMESSAGE + getIntent().getStringExtra("id"));
		}

	}

	// 监听的方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Petrochina_examine_messageActivity.this);
				break;
			case R.id.button__popup_cnpcexaminemonad_agree:// 同意审批请求
				set_disposemonad(Myconstant.PETROCHINA_APPROVED_SERVICEMONAD, getIntent().getStringExtra("id"));
				break;
			case R.id.button__popup_cnpcexaminemonad_unagree:// 驳回审批请求
				set_disposemonad(Myconstant.PETROCHINA_UNAPPROVED_SERVICEMONAD, getIntent().getStringExtra("id"));
				break;
			case R.id.textview_myacitivity_you:// 打开进行审批的界面
				View v1 = LayoutInflater.from(Petrochina_examine_messageActivity.this)
						.inflate(R.layout.poppup_cnpcexaminemonad, null);// 创建view
				pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法
				pop.setBackgroundDrawable(new ColorDrawable());
				// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
				pop.showAtLocation(select, Gravity.BOTTOM, 0, 0);
				agree = (Button) v1.findViewById(R.id.button__popup_cnpcexaminemonad_agree);// 审批通过
				unagree = (Button) v1.findViewById(R.id.button__popup_cnpcexaminemonad_unagree);// 驳回审批
				message = (EditText) v1.findViewById(R.id.editText_popup_cnpcexaminemonad_message);// 审批意见
				agree.setOnClickListener(l);
				unagree.setOnClickListener(l);
				break;
			default:
				break;
			}
		}
	};

	// 通过审批、驳回审批的方法
	private void set_disposemonad(String url, final String id) {
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						startActivity(
								new Intent(Petrochina_examine_messageActivity.this, Petrochina_HomeActivity.class));
						Myutil.set_activity_open(Petrochina_examine_messageActivity.this);
						Toast.makeText(Petrochina_examine_messageActivity.this, js1.getString("content"), 0).show();

					} else {
						Toast.makeText(Petrochina_examine_messageActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Petrochina_examine_messageActivity.this, "你已经掉线，请重新登录" + "", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("content", message.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);

	}
}
