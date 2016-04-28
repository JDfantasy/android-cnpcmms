package com.cnpc.zhibo.app;

/*
 * 站长端查看维修单的列表的界面
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
import com.cnpc.zhibo.app.adapter.Item_centre_page_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
/*
 * 站长端底部菜单中维修、审批、单击的搜索框打开的界面
 */
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_search_serviceentryActivity extends MyActivity {
	private ImageView back;// 搜索按钮
	private ListView list;
	private List<Centre_page_service> data;// 数据集合
	private Item_centre_page_service_adapter adapter;
	private TextView seek;// 搜索按钮
	private EditText message;// 信息
	private TextView hint;// 提示信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_page_search);
		setview();// 设置界面的方法
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// 列表的监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent();
			if (getIntent().getStringExtra("activityname").equals("repairs")) {
				// 打开查看维修单详情的界面
				in.setClass(Centre_search_serviceentryActivity.this, Centre_page_maintainActivity.class);

			} else {
				// 打开查看审批单详情的界面
				in.setClass(Centre_search_serviceentryActivity.this, Centre_page_examine_Activity.class);
				in.putExtra("state", adapter.getData().get(position).WorkStatus);
			}

			in.putExtra("id", adapter.getData().get(position).id);

			startActivity(in);
			Myutil.set_activity_open(Centre_search_serviceentryActivity.this);

		}
	};

	// 设置界面的方法
	private void setview() {
		set_title_text("搜索");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_centre_search_serviceentry_message);// 信息
		seek = (TextView) findViewById(R.id.textview_centre_search_serviceentry_seek);// 搜索按钮
		seek.setOnClickListener(l);
		hint = (TextView) findViewById(R.id.textView_centre_search_servicesentry_hint);// 提示信息
		hint.setText("搜索维修单列表");
		list = (ListView) findViewById(R.id.listView_centre_search_serviceentry_entry);// listview
		adapter = new Item_centre_page_service_adapter(this);
		list.setAdapter(adapter);
		data = new ArrayList<Centre_page_service>();
		adapter.servicestate();
	}

	// 监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_search_serviceentryActivity.this);

				break;
			case R.id.textview_centre_search_serviceentry_seek:// 搜索按钮
				hint.setVisibility(View.INVISIBLE);
				if (message.getText().toString().equals("")) {
					Toast.makeText(Centre_search_serviceentryActivity.this, "请先输入关键词，在进行搜索", 0).show();
				} else {
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

		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.CENTRE_GET_SEARCHSERVICEMONADENTRY , new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						Toast.makeText(Centre_search_serviceentryActivity.this, js1.getString("content"), 0).show();
						JSONArray jsa = js.getJSONArray("body");
						if (jsa.length() == 0) {
							Toast.makeText(Centre_search_serviceentryActivity.this, "对不起没有找到你要查询的维修单", 0).show();
						} else {

							adapter.getData().clear();
							data.clear();
							for (int i = 0; i < jsa.length(); i++) {

								JSONObject js2 = jsa.getJSONObject(i);
								JSONObject js4 = js2.getJSONObject("fixter");
								JSONObject js3 = js2.getJSONObject("repairOrder").getJSONObject("project");
								JSONObject repairOrder = js2.getJSONObject("repairOrder");
								JSONArray repairOrderImages = repairOrder.getJSONArray("repairOrderImages");
								if (repairOrderImages.length() == 0) {
									// 没有图片时
									data.add(new Centre_page_service(js2.getString("sn"),
											js2// 编号
													.getString("createDate"),
											Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
											js2.getString("workStatus"),
											Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
											js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
											"", js2.getString("score")));
								} else {
									// 有图片时
									JSONObject jsphone = (JSONObject) repairOrderImages.get(0);

									data.add(new Centre_page_service(js2.getString("sn"),
											js2// 编号
													.getString("createDate"),
											Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
											js2.getString("workStatus"),
											Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
											js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
											Myconstant.WEBPAGEPATHURL + jsphone.getString("source"),
											js2.getString("score")));

								}

							}
							adapter.addDataBottom(data);
						}

					} else {
						Toast.makeText(Centre_search_serviceentryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_search_serviceentryActivity.this, "你已经掉线，请重新登录" + "", 0).show();
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
				map.put("sn", message.getText().toString());
				return map;
			}
		};
		SysApplication.getHttpQueues().add(re);

	}

}
