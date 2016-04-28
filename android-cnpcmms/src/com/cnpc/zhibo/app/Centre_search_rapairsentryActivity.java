package com.cnpc.zhibo.app;

/*
 * 站长端搜索报修单列表的界面
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
import com.cnpc.zhibo.app.adapter.Item_centre_page_appove_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_search_rapairsentryActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private ListView list;
	private Item_centre_page_appove_adapter adapter;// 适配器
	private TextView seek;// 搜索按钮
	private EditText message;// 信息
	private List<Centre_page_approve> data;// 数据集合
	private TextView hint;// 提示信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_search_rapairsentry);
		setview();
	}

	// 设置界面的方法
	private void setview() {
		set_title_text("搜索");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		seek = (TextView) findViewById(R.id.textview_centre_search_repairsentry_seek);// 搜索按钮
		seek.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_centre_search_repairsentry_message);// 信息
		message.addTextChangedListener(tx_listener);

		hint = (TextView) findViewById(R.id.textView_centre_search_repairsenty_hint);
		hint.setText("搜索报修单列表");
		list = (ListView) findViewById(R.id.listView_centre_search_repairsentry_entry);
		adapter = new Item_centre_page_appove_adapter(Centre_search_rapairsentryActivity.this);
		data = new ArrayList<Centre_page_approve>();
		list.setAdapter(adapter);
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	/*
	 * 输入框的监听
	 */
	private TextWatcher tx_listener = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (message.getText().toString().length() != 0) {
				hint.setVisibility(View.INVISIBLE);
				searchentry();

			} else {
				adapter.getData().clear();
				adapter.notifyDataSetChanged();
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
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Centre_search_rapairsentryActivity.this, Centre_See_repairs_messageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("state", adapter.getData().get(position).state);
			startActivity(in);
			Myutil.set_activity_open(Centre_search_rapairsentryActivity.this);

		}
	};
	// 监听事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Centre_search_rapairsentryActivity.this);

				break;
			case R.id.textview_centre_search_repairsentry_seek:// 搜索按钮

				adapter.getData().clear();
				adapter.notifyDataSetChanged();
				message.setText("");
				break;
			default:
				break;
			}

		}
	};

	// 获取列表数据的方法
	private void searchentry() {

		String url = Myconstant.CENTRE_GET_SEARCHREPAIRSMONADENTRY;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						JSONArray jsa = js.getJSONArray("body");
						if (jsa.length() == 0) {
							Toast.makeText(Centre_search_rapairsentryActivity.this, "对不起没有找到你要查询的报修单", 0).show();
						} else {

							for (int i = 0; i < jsa.length(); i++) {
								JSONObject js2 = jsa.getJSONObject(i);
								JSONObject project = js2.getJSONObject("project");
								JSONArray repairOrderImages = js2.getJSONArray("repairOrderImages");
								if (repairOrderImages.length() == 0) {
									// 没有图片时
									data.add(new Centre_page_approve(js2.getString("sn"),
											js2// 编号
													.getString("modifyDate"),
											Myutil.set_cutString(
													project.getString("name") + "  " + js2.getString("description")),
											js2.getString("status"),
											Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
											js2.getString("id"), ""));
								} else {
									// 有图片时
									JSONObject js3 = (JSONObject) repairOrderImages.get(0);
									data.add(new Centre_page_approve(js2.getString("sn"),
											js2// 编号
													.getString("modifyDate"),
											Myutil.set_cutString(
													project.getString("name") + "  " + js2.getString("description")),
											getjudgestatus(js2.getString("status")),
											Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
											js2.getString("id"), Myconstant.WEBPAGEPATHURL + js3.getString("source")));
								}

							}
							adapter.addDataBottom(data);
						}

					} else {
						Toast.makeText(Centre_search_rapairsentryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_search_rapairsentryActivity.this, "你已经掉线，请重新登录" + "", 0).show();
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

	// 判断报修单的状态的方法
	private String getjudgestatus(String name) {
		if (name.equals("wait")) {
			return "等待维修....";
		} else if (name.equals("completed")) {
			return "已完成";

		} else {
			return "已关闭";
		}

	}

}
