package com.cnpc.zhibo.app;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_NewsVerifyActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private TextView fulfill;// 完成按钮
	private RequestQueue mqueQueue;// 请求对象
	private WebView web;// 加载静态网页的控件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_verify);
		setview();
	}

	// 设置界面上的控件的方法
	private void setview() {
		mqueQueue = Volley.newRequestQueue(this);// 初始化请求对象
		set_title_text("信息确认");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		fulfill = (TextView) findViewById(R.id.textview_myacitivity_you);
		fulfill.setVisibility(View.VISIBLE);
		fulfill.setText("完成");
		fulfill.setOnClickListener(l);
		web = (WebView) findViewById(R.id.webView_Centre_news_verify_station);
		web.loadUrl(Myconstant.STATIONMESSAGE + getIntent().getStringExtra("id"));

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回键
				finish();
				Myutil.set_activity_close(Centre_NewsVerifyActivity.this);

				break;
			case R.id.textview_myacitivity_you:// 完成
				set_verify();// 点击确认按钮执行的方法

				break;

			default:
				break;
			}

		}
	};

	// 站长进行信息的确认的界面
	private void set_verify() {

		String url = Myconstant.CENTRE_VERIFYSTATIONMESSAGE;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				System.out.println("返回的结果：" + response);
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						startActivity(new Intent(Centre_NewsVerifyActivity.this, Centre_HomeActivity.class));
						Myutil.set_activity_open(Centre_NewsVerifyActivity.this);//
						SysApplication.exit();
						// 给activity设置跳转效果
						finish();

					} else {
						Toast.makeText(Centre_NewsVerifyActivity.this, "站点信息无法匹配，请返回选择地址界面重新选择加油站，或者请联系管理员", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
				Toast.makeText(Centre_NewsVerifyActivity.this, "请求数据失败", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> map = new HashMap<String, String>();
				map.put("accept", "application/json");
				map.put("api_key", Myconstant.token);
				return map;

			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("id", getIntent().getStringExtra("id"));
				return map1;
			}
		};

		mqueQueue.add(re);

	}

}
