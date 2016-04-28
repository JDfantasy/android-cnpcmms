package com.cnpc.zhibo.app;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/*
 * 站长端通过关键词搜索加油站列表的界面
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
import com.cnpc.zhibo.app.adapter.Item_string_jyz_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_gasstation_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_search_stationentryActivity extends MyActivity {
	private ImageView back;// 搜索按钮
	private ListView list;
	private List<Centre_gasstation_item> data;// 数据集合
	private Item_string_jyz_adapter adapter;
	private TextView seek;// 搜索按钮
	private EditText message;// 信息
	private TextView hint;// 提示信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_search_stationentry);
		setview();// 设置界面的方法
		list.setOnItemClickListener(listener);
	}

	// 列表的监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			adapter.setcolor(position);// 设置选中item的背景颜色
			Intent in = new Intent(Centre_search_stationentryActivity.this, Centre_NewsVerifyActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Centre_search_stationentryActivity.this);
		}
	};

	// 设置界面的方法
	private void setview() {
		set_title_text("搜索");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_centre_search_stationentry_message);// 信息
		seek = (TextView) findViewById(R.id.textview_centre_search_stationentry_seek);// 搜索按钮
		seek.setOnClickListener(l);
		hint = (TextView) findViewById(R.id.textView_centre_search_stationentry_hint);// 提示信息
		hint.setText("搜索加油站列表");
		list = (ListView) findViewById(R.id.listView_centre_search_stationentry_entry);// listview
		adapter = new Item_string_jyz_adapter(Centre_search_stationentryActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Centre_gasstation_item>();
		SysApplication.addActivity(Centre_search_stationentryActivity.this);
	}

	// 监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_search_stationentryActivity.this);

				break;
			case R.id.textview_centre_search_stationentry_seek:// 搜索按钮

				if (message.getText().toString().equals("")) {
					Toast.makeText(Centre_search_stationentryActivity.this, "请先输入关键词，在进行搜索", 0).show();
				} else {
					hint.setVisibility(View.INVISIBLE);
					searchentry();
				}

				break;
			default:
				break;
			}

		}
	};

	// 获取列表数据的方法
	private void searchentry() {
		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.CENTRE_GET_SEARCHSTATIONENTRY, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						data.clear();
						adapter.getData().clear();
						JSONArray js2 = js.getJSONArray("body");
						for (int i = 0; i < js2.length(); i++) {
							JSONObject js3 = js2.getJSONObject(i);
							data.add(new Centre_gasstation_item(js3.getString("id"), js3.getString("name")));

						}
						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Centre_search_stationentryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_search_stationentryActivity.this, "你已经掉线，请重新登录" + "", 0).show();
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
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);

	}

}
