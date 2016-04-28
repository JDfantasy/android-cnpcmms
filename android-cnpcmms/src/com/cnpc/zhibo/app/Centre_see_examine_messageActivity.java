package com.cnpc.zhibo.app;

//站长端用来查看上级审批详情信息的界面
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
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_see_examine_messageActivity extends MyActivity {
	private ImageView fanhui;// 返回按钮
	private TextView title, name, state, post, phone, mobile, message;// 详情界面、批示人、状态、职位、电话、手机、批示信息
	private LinearLayout l1;// 布局对象
	private String id;
	private RequestQueue mqueQueue;// 请求对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_see_examine_message);
		setview();// 控件实例化的方法
	}

	// 设置界面上控件的方法
	private void setview() {
		mqueQueue = Volley.newRequestQueue(this);// 初始化请求对象
		id = getIntent().getStringExtra("id");// 获取上个界面传递过来的维修单的id
		l1 = (LinearLayout) findViewById(R.id.linearlayout_centre_see_examine_message);
		l1.setVisibility(View.GONE);
		set_title_text("审批信息");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		title = (TextView) findViewById(R.id.textView_centre_see_examine_message_title);// 标题
		name = (TextView) findViewById(R.id.textView_centre_see_examine_message_name);// 批示人
		state = (TextView) findViewById(R.id.textView_centre_see_examine_message_state);// 状态
		post = (TextView) findViewById(R.id.textView_centre_see_examine_message_post);// 职位
		phone = (TextView) findViewById(R.id.textView_centre_see_examine_message_phone);// 电话
		mobile = (TextView) findViewById(R.id.textView_centre_see_examine_message_mobile);// 手机
		message = (TextView) findViewById(R.id.textView_centre_see_examine_message_message);// 信息
		get_massagedata();// 获取审批消息的详情
	}

	// 获取审批消息的详情
	private void get_massagedata() {
		Toast.makeText(Centre_see_examine_messageActivity.this, "正在加载数据请稍后...",
				0).show();
		String url = "http://101.201.210.95:8080/cnpcmms/api/fixorder/get/"
				+ getIntent().getStringExtra("id");
		StringRequest re = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject js1 = js.getJSONObject("response");
							if (js1.getString("type").equals("success")) {
								JSONObject js_body = js.getJSONObject("body");
								state.setText(getfeedback(js_body
										.getString("approveStatus")));

								JSONObject js_approver = js_body
										.getJSONObject("approver");
								title.setText(js_approver.getString("name")
										+ "加油站维修单批示信息");

								JSONObject js_reporter = js_body
										.getJSONObject("reporter");
								name.setText("批示人："
										+ js_reporter.getString("name"));
								phone.setText("电话："
										+ js_reporter.getString("phone"));
								mobile.setText("手机："
										+ js_reporter.getString("mobile"));
								message.setText("    "
										+ js_body.getString("approveContent"));
								l1.setVisibility(View.VISIBLE);
							} else {
								Toast.makeText(
										Centre_see_examine_messageActivity.this,
										js1.getString("content"), 0).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(Centre_see_examine_messageActivity.this,
								"请求数据失败", 0).show();
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

		};
		mqueQueue.add(re);

	}

	// 用来判断这个维修单是否通过了审批
	private String getfeedback(String str) {
		if (str.equals("approved")) {
			return "审批通过";
		} else {
			return "审批驳回";
		}

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Centre_see_examine_messageActivity.this);
				break;

			default:
				break;
			}

		}
	};

}
