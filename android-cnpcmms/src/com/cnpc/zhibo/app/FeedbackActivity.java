package com.cnpc.zhibo.app;

//意见反馈的界面
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class FeedbackActivity extends MyActivity {
	private ImageView fanhui;// 返回按钮
	private Button submit;// 提交按钮
	private EditText message;// 反馈信息
	private String title = "", content = "";
	private RequestQueue mqueQueue;// 请求对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		setview();
	}

	private void setview() {
		mqueQueue = Volley.newRequestQueue(this);// 初始化请求对象
		set_title_text("意见反馈");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		submit = (Button) findViewById(R.id.button_feedback_queren);// 提交按钮
		submit.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_feedback_message);

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(FeedbackActivity.this);

				break;
			case R.id.button_feedback_queren:
				submit_message();
				break;
			default:
				break;
			}

		}
	};

	// 提交的方法
	private void submit_message() {
		content = message.getText().toString();
		if (content.equals("")) {
			Toast.makeText(this, "请先输入你的宝贵意见，再进行提交", 0).show();
			return;
		}
		if (content.length() > 10) {
			title = content.substring(0, 10);
		} else {
			title = content;
		}
		try {
			String url = "http://101.201.210.95:8080/cnpcmms/api/feedback/save";

			StringRequest re = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								JSONObject js = new JSONObject(response);
								JSONObject js1 = js.getJSONObject("response");
								if (js1.getString("type").equals("success")) {
									message.setText("");
									Toast.makeText(FeedbackActivity.this,
											js1.getString("content"), 0).show();
								} else {
									Toast.makeText(FeedbackActivity.this,
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
							Toast.makeText(FeedbackActivity.this, error.toString(), 0).show();
						}
					}) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {

					Map<String, String> headers = new HashMap<String, String>();
					headers.put("accept", "application/json");
					headers.put("api_key", Myconstant.token);
					return headers;

				}

				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put("title", title);
					map.put("content", content);

					return map;
				}

			};
			mqueQueue.add(re);
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}
}
