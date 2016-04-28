package com.cnpc.zhibo.app;

/*
 * //中石油段查看维修率的加油站列表界面
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.adapter.Item_petrochina_servicerate_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Petrochina_project;
import com.cnpc.zhibo.app.entity.Petrochina_servicerate;

import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_service_numberActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private ListView list;
	private List<Petrochina_servicerate> data;
	private Item_petrochina_servicerate_adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_service_number);
		setview();
	}

	// 设置布局界面上的控件
	private void setview() {
		set_title_text(getIntent().getStringExtra("name"));
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		list = (ListView) findViewById(R.id.listView_petrochina_service_number_entry);
		adapter = new Item_petrochina_servicerate_adapter(this);
		data = new ArrayList<Petrochina_servicerate>();
		list.setAdapter(adapter);
		searchexamineentry();
	}

	// 监听的方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Petrochina_service_numberActivity.this);
				break;

			default:
				break;
			}
		}
	};

	// 点击搜索按钮执行的方法
	private void searchexamineentry() {
		String url = Myconstant.PETROCHINA_GET_PROJECTESTATIONNTRYFIXORDER +"id="+getIntent().getStringExtra("id")+ "&beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// 相应的请求
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_service_numberActivity.this, js1.getString("content"), 0).show();
						JSONArray body = js.getJSONArray("body");
						for (int i = 0; i < body.length(); i++) {
							JSONObject js2 = body.getJSONObject(i);
							data.add(new Petrochina_servicerate("名称："+js2.getString("name"), "维修次数："+js2.getString("count"), js2.getString("id")));
						}
						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Petrochina_service_numberActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_service_numberActivity.this, "你已经掉线，请重新登录", 0).show();
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
		SysApplication.getHttpQueues().add(re);
	}
}
