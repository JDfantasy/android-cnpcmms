package com.cnpc.zhibo.app;

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
import com.cnpc.zhibo.app.util.Myutil;

/*
 * 中石油端发布公告的界面
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_sendallActivity extends MyActivity {
	private ImageView back;// 返回
	private TextView send;// 发送
	private EditText title, content;// 标题，内容

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_sendall);
		setview();
	}

	/**
	 * 初始化控件
	 */
	private void setview() {
		set_title_text("发布公告");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);// 返回按钮
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		send = (TextView) findViewById(R.id.textview_myacitivity_you);
		send.setVisibility(View.VISIBLE);
		send.setText("发送");
		send.setOnClickListener(l);
		title = (EditText) findViewById(R.id.editext_petrochina_sendal_title);
		content = (EditText) findViewById(R.id.editext_petrochina_sendal_content);
	}

	// 监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Petrochina_sendallActivity.this);

				break;
			case R.id.textview_myacitivity_you://发送按钮
				if (title.getText().toString() == null || title.getText().toString().length() == 0) {
					Toast.makeText(Petrochina_sendallActivity.this, "请输入标题", 0).show();;
				} else if(content.getText().toString() == null || content.getText().toString().length() == 0){
					Toast.makeText(Petrochina_sendallActivity.this, "请输入内容", 0).show();
				}else {
					sendnotice();
				}
				break;

			default:
				break;
			}

		}
	};
	//发送公告的方法
	private void sendnotice(){

		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.PETROCHINA_SENDDALNOTICE, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						startActivity(
								new Intent(Petrochina_sendallActivity.this, Petrochina_HomeActivity.class));
						Myutil.set_activity_open(Petrochina_sendallActivity.this);
						Toast.makeText(Petrochina_sendallActivity.this, js1.getString("content"), 0).show();

					} else {
						Toast.makeText(Petrochina_sendallActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Petrochina_sendallActivity.this, "你已经掉线，请重新登录" + "", 0).show();
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
				map.put("title", title.getText().toString());
				map.put("content", content.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);

	
		
	}

}
