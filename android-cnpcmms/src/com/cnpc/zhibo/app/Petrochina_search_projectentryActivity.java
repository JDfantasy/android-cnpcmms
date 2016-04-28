package com.cnpc.zhibo.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.cnpc.zhibo.app.adapter.Item_petrochian_project_adapter;
import com.cnpc.zhibo.app.adapter.Item_petrochina_examine_message_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Petrochina_examine_message;
import com.cnpc.zhibo.app.entity.Petrochina_project;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_search_projectentryActivity extends MyActivity {
	private ImageView back;// 搜索按钮
	private ListView list;
	private Item_petrochian_project_adapter adapter;
	private List<Petrochina_project> data;
	private TextView seek;// 搜索按钮
	private EditText message;// 信息
	private TextView hint;// 提示信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_search_projectentry);
		setview();// 设置界面的方法
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// 列表的监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Petrochina_search_projectentryActivity.this,
					Petrochina_service_numberActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("name", adapter.getData().get(position).name);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_search_projectentryActivity.this);
		}
	};

	// 设置界面的方法
	private void setview() {
		set_title_text("搜索");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_petrochina_search_projectentry_message);// 信息
		seek = (TextView) findViewById(R.id.textview_petrochina_search_projectentry_seek);// 搜索按钮
		seek.setOnClickListener(l);
		hint = (TextView) findViewById(R.id.textView_petrochina_search_projectentry_hint);// 提示信息
		hint.setText("搜索维修单列表");
		list = (ListView) findViewById(R.id.listView_petrochina_search_projectentry_entry);// listview
		adapter = new Item_petrochian_project_adapter(Petrochina_search_projectentryActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Petrochina_project>();
	}

	// 监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Petrochina_search_projectentryActivity.this);

				break;
			case R.id.textview_petrochina_search_projectentry_seek:// 搜索按钮
				hint.setVisibility(View.INVISIBLE);
				if (message.getText().toString().equals("")) {
					Toast.makeText(Petrochina_search_projectentryActivity.this, "请先输入关键词，在进行搜索", 0).show();
				} else {
					searchexamineentry();
				}

				break;
			default:
				break;
			}

		}
	};

	// 中石油端,根据关键词查询维修项目列表
	private void searchexamineentry() {
		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.PETROCHINA_GET_SEARCHPPROJECTENTRYSTATS,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject js1 = js.getJSONObject("response");
							if (js1.getString("type").equals("success")) {
								Toast.makeText(Petrochina_search_projectentryActivity.this, js1.getString("content"), 0)
										.show();
								data.clear();
								adapter.getData().clear();
								JSONArray jsa = js.getJSONArray("body");
								for (int i = 0; i < jsa.length(); i++) {
									JSONObject js2 = jsa.getJSONObject(i);
									data.add(new Petrochina_project("项目：" + js2.getString("name"),
											"维修次数：" + js2.getString("count"), js2.getString("id")));

								}
								adapter.addDataBottom(data);
							} else {
								Toast.makeText(Petrochina_search_projectentryActivity.this, js1.getString("content"), 0)
										.show();
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
						Toast.makeText(Petrochina_search_projectentryActivity.this, "你已经掉线，请重新登录" + "", 0).show();
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
				map.put("keyword", message.getText().toString());
				map.put("endDate", Myutil.get_time_ymd());
				map.put("beginDate", Myutil.get_month_time());
				return map;
			}
		};
		SysApplication.getHttpQueues().add(re);
	}
}
