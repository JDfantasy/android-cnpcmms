package com.cnpc.zhibo.app;

//APP手机验证和获取验证码的界面
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
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Phone_verifyActivity extends MyActivity {

	private Button bu_dl;// 登录按钮、
	private EditText name, yzm;// 用户名、验证码
	private int time;// 时间
	private Thread t;// 线程
	private RequestQueue mQueue;// volley请求对象
	private ImageView fanhui;
   
	private TextView tx_hqyzm;// 获取验证码
	private Handler handler = new Handler() {
		// 利用handle机制进行计时的操作
		public void handleMessage(Message msg) {
			time = msg.what;
			tx_hqyzm.setText(time + "秒");
			if (time == 0) {
				tx_hqyzm.setClickable(true);
				tx_hqyzm.setText("获取验证码");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mQueue = Volley.newRequestQueue(this);

		// 拿到登录界面上的所有的控件
		set_title_text("手机验证");// 改变标题
		name = (EditText) findViewById(R.id.editext_home_name);// 用户名
		yzm = (EditText) findViewById(R.id.editext_home_yzm);// 验证码
		bu_dl = (Button) findViewById(R.id.button_home_xiayibu);// 登录按钮
		bu_dl.setOnClickListener(l);// 登录按钮的监听
		tx_hqyzm = (TextView) findViewById(R.id.textview_home_huoquyzm);// 获取验证码
		tx_hqyzm.setOnClickListener(l);// 获取验证码的监听
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);

	}

	private View.OnClickListener l = new View.OnClickListener() {
		Intent in = new Intent();

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_home_xiayibu:// 跳转到完善信息的界面
				if (set_denglu() != null) {
					verifysms();

				}

				break;
			case R.id.textview_home_huoquyzm:// 发送验证码的按钮
				send_yzm();// 调用短信验证
				tx_hqyzm.setClickable(false);// 在接受验证码的60秒内 按钮不能点击
				timeBack(60);// 计时的方法
				break;
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Phone_verifyActivity.this);
				break;
			default:
				break;
			}

		}
	};

	private void verifysms() {
		String url = Myconstant.VERIFYSMS;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// 登录成功
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Phone_verifyActivity.this, js1.getString("content"), 0).show();
						Intent in = new Intent();
						SysApplication.addActivity(Phone_verifyActivity.this);
						if (getIntent().getBooleanExtra("boolean", false)) {
							in.setClass(Phone_verifyActivity.this, PerfectActivity.class);
						} else {
							in.setClass(Phone_verifyActivity.this, Change_PasswordActivity.class);
						}
						
						in.putExtra("mobile", name.getText().toString());
						startActivity(in);
						Myutil.set_activity_open(Phone_verifyActivity.this);// 跳转页面的效果
						name.setText("");
						yzm.setText("");
					} else {
						Toast.makeText(Phone_verifyActivity.this, js1.getString("content"), 0).show();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Phone_verifyActivity.this, "因为网络问题，验证失败。", 0).show();
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();

				map.put("mobile", name.getText().toString());
				map.put("verifycode", yzm.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
	}

	// 将用户传递的手机号发送给服务器，并且获取返回的验证码、
	private void send_yzm() {

		String url = Myconstant.SENDSMS;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// 登录成功
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Phone_verifyActivity.this, js1.getString("content"), 0).show();

					} else {
						Toast.makeText(Phone_verifyActivity.this, js1.getString("content"), 0).show();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Phone_verifyActivity.this, "短信验证发送失败，请检查网络。", 0).show();
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				System.out.println(name.getText().toString());
				map.put("mobile", name.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
	}

	// 控制获取验证码按钮的时间倒退
	protected void timeBack(int a) {
		time = a;
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 60; i >= 0; i--) {
					Message msg = new Message();
					msg.what = i;
					handler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		t.start();
	}

	// 登录按钮的方法
	private Map<String, Object> set_denglu() {
		Map<String, Object> map = new HashMap<String, Object>();// 用来存储数据的map集合并且初始化初始化map集合
		// 判断用户名是否为空
		if (name.getText().toString().equals("")) {
			Toast.makeText(Phone_verifyActivity.this, "用户名不能为空，请重新填写", Toast.LENGTH_LONG).show();
			name.setText("");
			yzm.setText("");
			return null;

		}
		// 判断用户名是否为空
		if (Myutil.isMobileNO(name.getText().toString()) == false) {
			Toast.makeText(Phone_verifyActivity.this, "你输入的手机号不正确，请重新填写", Toast.LENGTH_LONG).show();
			name.setText("");
			yzm.setText("");
			return null;

		}
		// 判断验证码是否为空
		if (yzm.getText().toString().equals("")) {
			Toast.makeText(Phone_verifyActivity.this, "验证码不能为空，请重新填写", Toast.LENGTH_LONG).show();
			name.setText("");
			yzm.setText("");
			return null;
		}
		// 当用户名和验证码都不为空时，将用户名和验证码都保存到map集合中，方便下一步的数据上传到服务器
		map.put("name", name.getText().toString());
		map.put("yzm", yzm.getText().toString());

		System.out.println("name:" + name.getText().toString() + "/npwd:" + yzm.getText().toString());
		return map;

	}

}
