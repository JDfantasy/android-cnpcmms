package com.cnpc.zhibo.app;

//登录界面
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.domain.EaseUserHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnterintoActivity extends MyActivity {
	private EditText name, pwd;// 用户名、密码
	private TextView wjmm, register;// 忘记密码、注册
	private CheckBox ck;// 记住密码
	private boolean pd = false;// 判断密码是否需要保存
	private Button bu_dl;// 登录按钮
	private Map<String, String> map;
	private RequestQueue mqueQueue;// 请求对象
	private boolean phoneck = false;// 判断输入的用户名是否是手机
	private boolean enterinto_ck = false;// 判断输入的用户名是否是手机
	private String matchingurl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enterinto);
		set_view();// 将登录界面上的控件进行实例化和设置监听

	}

	// 将登录界面上的所有的控件进行实例化的方法
	private void set_view() {
		set_title_text("库站e点通");
		mqueQueue = Volley.newRequestQueue(EnterintoActivity.this);// 实例化请求对象
		name = (EditText) findViewById(R.id.editext_enterinto_name);// 用户名
		pwd = (EditText) findViewById(R.id.editext_enterinto_pwd);// 密码
		ck = (CheckBox) findViewById(R.id.checkBox_enterinto_jizhumima);// checkbox按钮
		bu_dl = (Button) findViewById(R.id.button_enterinto_denglu);// 登录按钮
		wjmm = (TextView) findViewById(R.id.textView_enterinto_wangjimima);// 忘记密码
		ck.setOnCheckedChangeListener(listener);// checkbox设置监听
		bu_dl.setOnClickListener(l);// 登录设置监听
		wjmm.setOnClickListener(l);// 忘记密码
		// 从偏好设置中获取存储的用户名、密码
		name.setText(getSharedPreferences("enterinto", 0).getString("name", ""));
		pwd.setText(getSharedPreferences("enterinto", 0).getString("pwd", ""));
		if (getSharedPreferences("enterinto", 0).getBoolean("panduan", false) == true) {
			ck.setChecked(true);// 设置checkbox的值
			// set_enterinto();//登录的方法
			// 判断用户是否选中了，记住密码。当用户选中了记住密码，当用户下次打开APP时，自动为用户进行登录
		}
		name.addTextChangedListener(watcher);
		register = (TextView) findViewById(R.id.textview_myacitivity_you);
		register.setVisibility(View.VISIBLE);
		register.setText(Html.fromHtml("<u>" + "注册" + "</u>"));
		register.setOnClickListener(l);
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (name.getText().length() < 11) {
				pwd.setText("");
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	// 按钮点击的监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_enterinto_denglu:// 登录按钮
				if (enterinto_ck == false) {
					enterinto_ck = true;
					if (get_data() != null && phoneck == true) {
						set_enterinto();// 登录按钮的执行的方法
						Toast.makeText(EnterintoActivity.this, "正在登录....", 0).show();
					} else {
						Toast.makeText(EnterintoActivity.this, "你输入的用户名不是手机号", 0).show();
						name.setText("");
						pwd.setText("");
						enterinto_ck = false;
					}
				}

				break;
			case R.id.textView_enterinto_wangjimima://跳转到手机验证界面——忘记密码
				Intent in=new Intent(EnterintoActivity.this, Phone_verifyActivity.class);
				SysApplication.addActivity(EnterintoActivity.this);
				in.putExtra("boolean", false);
				startActivity(in);
				Myutil.set_activity_open(EnterintoActivity.this);
				break;
			case R.id.textview_myacitivity_you:// 跳转到手机验证界面——注册账号
				Intent in1=new Intent(EnterintoActivity.this, Phone_verifyActivity.class);
				in1.putExtra("boolean", true);
				SysApplication.addActivity(EnterintoActivity.this);
				startActivity(in1);
				Myutil.set_activity_open(EnterintoActivity.this);
				break;
			default:
				break;
			}

		}
	};

	// 登录按钮的执行的方法
	private void set_enterinto() {
		get_data();
		String url = Myconstant.CENTRE_ENTER;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// 登录成功
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONObject js2 = json.getJSONObject("body");
						SysApplication.token = js2.getString("token");
						Myconstant.token = SysApplication.token;
						get_user_type();// 判断用户类型打开相应的界面

					} else {
						Toast.makeText(EnterintoActivity.this, js1.getString("content"), 0).show();
						name.setText("");
						pwd.setText("");
						enterinto_ck = false;

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(EnterintoActivity.this, "登录失败，账号不存在", 0).show();
				name.setText("");
				pwd.setText("");
				enterinto_ck = false;
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return map;
			}

		};
		mqueQueue.add(re);
	}

	// 从服务器获取用户的信息
	private void get_user_type() {
		String url = Myconstant.CENTRE_USERMESSAGE;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

				try {
					String type = response.getString("type");
					SysApplication.userid = response.getString("username");
					Myconstant.userid = SysApplication.userid;
					// 环信登录方法
					loginHX(Myconstant.userid, "");
					if (type.equals("admin")) {
						Toast.makeText(EnterintoActivity.this, "该账户不存在", 0).show();
						finish();
					} else {
						if (type.equals("customer")) {
							startActivity(new Intent(EnterintoActivity.this, Petrochina_HomeActivity.class));
							finish();
							Myutil.set_activity_open(EnterintoActivity.this);
						} else {
							set_matching(type);// 除了中石油外，其他用户都需要进行判断是否进行了用户的信息确认
						}
					}

				} catch (JSONException e) {
					// 供应商
					e.printStackTrace();

				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("获取用户类型失败" + error);

			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				System.out.println("获取的用户信息的借口中的token的值：" + Myconstant.token);
				return headers;

			}
		};

		mqueQueue.add(re);

	}

	// checkbox的按钮的状态发生变化的监听事件
	private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			pd = isChecked;
		}
	};

	// 获取用户名、密码的信息
	private Map<String, String> get_data() {
		map = new HashMap<String, String>();
		if (name.getText().toString().equals("")) {
			Toast.makeText(this, "请输入你的用户名", Toast.LENGTH_LONG).show();

			return null;
		}
		if (pwd.getText().toString().equals("")) {
			Toast.makeText(this, "请输入你的密码", Toast.LENGTH_LONG).show();
			return null;
		}
		phoneck = Myutil.isMobileNO(name.getText().toString());
		map.put("mobile", name.getText().toString());
		map.put("password", pwd.getText().toString());
		if (pd == true) {
			System.out.println("进入了偏好设置的方法");
			// 利用偏好设置 将用户名和密码进行存储
			SharedPreferences preferences = getSharedPreferences("enterinto", 0);
			Editor editor = preferences.edit(); // 文件编辑器
			editor.putString("name", name.getText().toString());
			editor.putString("pwd", pwd.getText().toString());
			editor.putBoolean("panduan", true);
			editor.commit();// 提交
		} else {
			SharedPreferences preferences = getSharedPreferences("enterinto", 0);
			Editor editor = preferences.edit(); // 文件编辑器
			editor.putString("name", "");
			editor.putString("pwd", "");
			editor.putBoolean("panduan", false);
			editor.commit();// 提交
		}
		return map;

	}

	// 查看用户是否进行了匹配
	private boolean set_matching(final String type) {
		final Intent in = new Intent();
		if (type.equals("site")) {
			matchingurl = Myconstant.CENTRE_CONFIRMATION_INFORMATION;// 站长端
		} else if (type.equals("worker")) {
			matchingurl = Myconstant.MAINTAIN_CONFIRMATION_INFORMATION;// 维修工
		} else {
			matchingurl = Myconstant.SUPPLIER_CONFIRMATION_INFORMATION;// 供应商
		}
		JsonObjectRequest re = new JsonObjectRequest(matchingurl, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

				try {
					JSONObject js = response.getJSONObject("response");
					if (js.getString("type").equals("success")) {
						// 用户已经验证信息成功
						if (type.equals("site")) {// 站长端

							in.setClass(EnterintoActivity.this, Centre_HomeActivity.class);

						} else if (type.equals("worker")) {// 维修工

							in.setClass(EnterintoActivity.this, Maintain_HomeActivity.class);

						} else if (type.equals("customer")) {// 中石油
							in.setClass(EnterintoActivity.this, Petrochina_HomeActivity.class);
						} else {// 供应商

							in.setClass(EnterintoActivity.this, Supplier_HomeActivity.class);

						}

					} else {
						// 用户还没有验证信息
						if (type.equals("site")) {// 站长端

							in.setClass(EnterintoActivity.this, Centre_AddressActivity.class);

						} else if (type.equals("worker")) {// 维修工

							in.setClass(EnterintoActivity.this, Maintain_select_providerActivity.class);

						} else if (type.equals("customer")) {// 中石油
							in.setClass(EnterintoActivity.this, Petrochina_HomeActivity.class);
						} else {// 供应商

							in.setClass(EnterintoActivity.this, Supplier_select_firmActivity.class);

						}
					}
					startActivity(in);
					Myutil.set_activity_open(EnterintoActivity.this);
					finish();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				System.out.println("验证返回的结果：" + response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(EnterintoActivity.this, error.toString(), 0).show();

			}

		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> map = new HashMap<String, String>();
				map.put("accept", "application/json");
				map.put("api_key", Myconstant.token);
				return map;

			}
		};

		mqueQueue.add(re);
		return false;

	}

	/**
	 * 环信登录方法
	 * 
	 * @param name
	 * @param pwd
	 */
	private void loginHX(String name, String pwd) {
		System.out.println("开始登录环信：" + name);
		// 与后台约定环信默认登录密码为password
		pwd = "password";
		EMChatManager.getInstance().login(name, pwd, new EMCallBack() {// 回调
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						EMGroupManager.getInstance().loadAllGroups();
						EMChatManager.getInstance().loadAllConversations();
						System.out.println("登陆聊天服务器成功！");
						EaseUserHelper.getUserinfo(EMChatManager.getInstance().getCurrentUser(), new Handler());
					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
				System.out.println("登陆聊天服务器失败！");
			}
		});

	}

}
