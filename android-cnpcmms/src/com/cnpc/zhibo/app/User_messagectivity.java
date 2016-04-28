package com.cnpc.zhibo.app;

/*
 * 用户查看用户个人信息的界面
 */
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class User_messagectivity extends MyActivity {
	private TextView name, jobnumber, phone, phonecall, identity, registrationtime;// 姓名、工号、手机号、电话号、身份证、注册时间
	private ImageView back;// 返回按钮
	private TextView xgpwd;// 修改密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityuser_messagectivity);
		setview();
	}

	private void setview() {
		set_title_text("个人中心");
		name = (TextView) findViewById(R.id.textView_user_message_name);// 姓名
		jobnumber = (TextView) findViewById(R.id.textView_user_message_jobnumber);// 工号
		phone = (TextView) findViewById(R.id.textView_user_message_phonenumber);// 手机号
		phonecall = (TextView) findViewById(R.id.textView_user_message_telephone);// 电话号
		identity = (TextView) findViewById(R.id.textView_user_message_identity);// 身份证
		registrationtime = (TextView) findViewById(R.id.textView_user_message_registrationtime);// 注册时间
		laodingusermessage();
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		xgpwd = (TextView) findViewById(R.id.textview_myacitivity_you);
		xgpwd.setText("修改密码");
		xgpwd.setVisibility(View.VISIBLE);
		xgpwd.setOnClickListener(l);
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(User_messagectivity.this);
				break;
			case R.id.textview_myacitivity_you:// 修改密码
				Intent in = new Intent(User_messagectivity.this, Phone_verifyActivity.class);
				SysApplication.addActivity(User_messagectivity.this);
				in.putExtra("boolean", false);
				startActivity(in);
				Myutil.set_activity_open(User_messagectivity.this);
				break;
			default:
				break;
			}

		}
	};

	/*
	 * 获取用户信息的方法
	 */
	private void laodingusermessage() {
		String url = Myconstant.COMMONALITY_GETUSERMESSAGE;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					name.setText("姓  名：" + js.getString("name"));
					jobnumber.setText("工  号：" + js.getString("username"));
					phone.setText("手机号：" + js.getString("phone"));
					phonecall.setText("电话号：" + js.getString("mobile"));
					identity.setText("身份证：" + js.getString("identification"));
					registrationtime.setText("注册时间：" + js.getString("createDate"));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

		};
		SysApplication.getHttpQueues().add(re);
	}
}
