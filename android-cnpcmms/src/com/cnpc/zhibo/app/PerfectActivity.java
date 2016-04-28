package com.cnpc.zhibo.app;

//用户信息完善界面
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PerfectActivity extends MyActivity {
	private Button bu_wancheng;// 、完成的按钮

	private Map<String, String> map;// 用来存储数据的集合
	private boolean pd = true;// 是否选中了的判断
	private CheckBox che_xy;// 选中协议的checkbox控件
	private TextView tx_xy, zhanzhang, weixiuyuan, yunyingshang, cnpc;// 协议、站长、维修员、运营商、中石油
	private EditText gonghao, name, sfz, lxdh, pwd, yzm;// 工号、姓名、身份证、所属公司、联系电话、密码、验证码
	private String name_type = "";// 用户的类型
	private RequestQueue mQueue;// volley请求对象
	private ImageView fanhui;// 返回按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfect);
		set_title_text("个人信息设置");// 改变标题
		set_views();// 拿到注册界面上的控件
		// 为界面上的按钮设置监听

		bu_wancheng.setOnClickListener(l);// 完成按钮的监听
		tx_xy.setOnClickListener(l);// 协议的具体信息的监听
		che_xy.setOnCheckedChangeListener(listener);// 信息的checkbox的状态改变监听
		zhanzhang.setOnClickListener(l);// 站长按钮
		weixiuyuan.setOnClickListener(l);// 维修员按钮
		yunyingshang.setOnClickListener(l);// 运营商
		cnpc.setOnClickListener(l);// 中石油

	}

	// 身份证的算法
	private static boolean verify(String id) {
		if (id.length() != 18)
			return false;
		/*
		 * 1、将前面的身份证号码17位数分别乘以不同的 系数。 从第一位到第十七位的系数分别为：
		 * 7－9－10－5－8－4－2－1－6－3－7－9－10－5－8－4－2。 将这17位数字和系数相乘的结果相加。
		 */
		int[] w = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		int sum = 0;
		for (int i = 0; i < w.length; i++) {
			sum += (id.charAt(i) - '0') * w[i];
		}
		// 用加出来和除以11，看余数是多少？
		char[] ch = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		// return ch[sum%11]==
		// (id.charAt(17)=='x'?'X': id.charAt(17));
		int c = sum % 11;
		/*
		 * 分别对应的最后 一位身份证的号码为 1－0－X－9－8－7－6－5－4－3－2。
		 */
		char code = ch[c];
		char last = id.charAt(17);
		last = last == 'x' ? 'X' : last;
		return last == code;
	}

	// 将注册界面上的所有的控件进行初始化的方法
	private void set_views() {
		mQueue = Volley.newRequestQueue(this);
		bu_wancheng = (Button) findViewById(R.id.button_perfect_tijiaozhuce);// 提交注册
		che_xy = (CheckBox) findViewById(R.id.checkBox_perfect_xuanzhongxieyi);// 协议的checkbox
		tx_xy = (TextView) findViewById(R.id.textView_perfect_xieyi);// 协议的具体信息
		gonghao = (EditText) findViewById(R.id.edittext_perfect_gonghao);// 工号
		name = (EditText) findViewById(R.id.edittext_perfect_name);// 姓名
		sfz = (EditText) findViewById(R.id.edittext_perfect_shenfenzheng);// 身份证
		lxdh = (EditText) findViewById(R.id.edittext_perfect_lianxidianhua);// 联系电话
		pwd = (EditText) findViewById(R.id.edittext_perfect_pwd1);// 密码
		zhanzhang = (TextView) findViewById(R.id.textview_perfect_zhanzhang);// 站长
		weixiuyuan = (TextView) findViewById(R.id.textview_perfect_weixiuyuan);// 维修工
		yunyingshang = (TextView) findViewById(R.id.textview_perfect_yunyingshan);// 运营商
		cnpc = (TextView) findViewById(R.id.textview_perfect_zhongshiyou);// 中石油
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
	}

	// 获取用户输入的数据
	private boolean get_data() {
		map = new HashMap<String, String>();
		if (gonghao.getText().toString().equals("")) {
			Toast.makeText(PerfectActivity.this, "请输入你的工号", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;
		}
		if (name.getText().toString().equals("")) {
			Toast.makeText(PerfectActivity.this, "请输入你的姓名", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;
		}

		if (sfz.getText().toString().equals("")) {
			Toast.makeText(PerfectActivity.this, "请输入你的身份证", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;
		}
		if (lxdh.getText().toString().equals("")) {
			Toast.makeText(PerfectActivity.this, "请输入你的联系电话", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;
		}
		if (verify(sfz.getText().toString()) == false) {
			Toast.makeText(PerfectActivity.this, "你输入的身份证有误，请重新输入", Toast.LENGTH_LONG).show();
			sfz.setText("");
			return false;
		}
		if (pd == false) {
			Toast.makeText(PerfectActivity.this, "请选中已阅读中国石油协议的选项", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;

		}
		if (name_type.equals("")) {
			Toast.makeText(PerfectActivity.this, "请选择用户类型", Toast.LENGTH_LONG).show();
			return false;
		}
		// 将 工号、用户类型、姓名、身份证、公司、联系人、密码等信息存储到map集合中
		map.put("username", gonghao.getText().toString());// 工号
		map.put("name", name.getText().toString());// 姓名
		map.put("identification", sfz.getText().toString());// 身份证
		map.put("phone", lxdh.getText().toString());// 联系电话
		map.put("mobile", getIntent().getStringExtra("mobile"));
		map.put("type", name_type);// 用户类型
		map.put("password", pwd.getText().toString());// 密码

		return true;

	}

	// checkbox的状态发生变化的监听的方法
	private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			pd = isChecked;
		}
	};
	// 点击事件的监听的方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.button_perfect_tijiaozhuce:// 提交注册
				if (get_data()) {
					volley_post();// 点击完成按钮执行的方法
				}

				break;
			case R.id.textView_perfect_xieyi:// 跳转到协议界面的方法

				break;
			case R.id.textview_perfect_zhanzhang:// 选中站长执行的方法
				select_name(R.id.textview_perfect_zhanzhang);
				break;
			case R.id.textview_perfect_yunyingshan:// 选中运营商执行的方法
				select_name(R.id.textview_perfect_yunyingshan);
				break;
			case R.id.textview_perfect_weixiuyuan:// 选中维修工的方法
				select_name(R.id.textview_perfect_weixiuyuan);
				break;
			case R.id.textview_perfect_zhongshiyou:// 选中中石油的方法
				select_name(R.id.textview_perfect_zhongshiyou);
				break;
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(PerfectActivity.this);
				break;
			default:
				break;
			}

		}
	};

	// 注册按钮执行的操作
	private void volley_post() {
		Toast.makeText(this, "正在提交用户信息请稍等...", 0).show();
		String str = Myconstant.REGISTER;
		StringRequest re = new StringRequest(Request.Method.POST, str, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject jsa = new JSONObject(response);
					JSONObject js1 = jsa.getJSONObject("response");// 状态

					if (js1.getString("type").equals("success")) {
						JSONObject js2 = jsa.getJSONObject("body");// 返回的内容
						Myconstant.token = js2.getString("token");
						// 利用偏好设置 将用户名和密码进行存储
						SharedPreferences preferences = getSharedPreferences("perfect", 0);
						Editor editor = preferences.edit(); // 文件编辑器
						editor.putBoolean("panduan", true);
						editor.commit();// 提交
						startActivity(new Intent(PerfectActivity.this, EnterintoActivity.class));
						Myutil.set_activity_open(PerfectActivity.this);
						SysApplication.exit();
						// finish();
					}
					if (js1.getString("type").equals("error")) {
						Toast.makeText(PerfectActivity.this, js1.getString("content"), 0).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("上传失败" + error.toString());

			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				return map;
			}
		};
		mQueue.add(re);

	}

	// 选择用户类型的方法
	private void select_name(int id) {
		switch (id) {
		case R.id.textview_perfect_zhanzhang:// 站长
			zhanzhang.setTextColor(getResources().getColor(R.color.zitiyanse1));
			weixiuyuan.setTextColor(getResources().getColor(R.color.zitiyanse3));
			yunyingshang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			cnpc.setTextColor(getResources().getColor(R.color.zitiyanse3));
			name_type = "site";

			break;
		case R.id.textview_perfect_weixiuyuan:// 维修员
			zhanzhang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			weixiuyuan.setTextColor(getResources().getColor(R.color.zitiyanse1));
			yunyingshang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			cnpc.setTextColor(getResources().getColor(R.color.zitiyanse3));
			name_type = "worker";
			break;
		case R.id.textview_perfect_yunyingshan:// 运营商
			zhanzhang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			weixiuyuan.setTextColor(getResources().getColor(R.color.zitiyanse3));
			yunyingshang.setTextColor(getResources().getColor(R.color.zitiyanse1));
			cnpc.setTextColor(getResources().getColor(R.color.zitiyanse3));
			name_type = "provider";
			break;
		case R.id.textview_perfect_zhongshiyou:// 中石油
			zhanzhang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			weixiuyuan.setTextColor(getResources().getColor(R.color.zitiyanse3));
			yunyingshang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			cnpc.setTextColor(getResources().getColor(R.color.zitiyanse1));
			name_type = "customer";
			break;

		default:
			break;
		}

	}

}
