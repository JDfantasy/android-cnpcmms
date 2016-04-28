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
import com.cnpc.zhibo.app.adapter.Item_centre_supplier_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_message;
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

public class Maintain_select_providerActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private TextView seek;// 搜索按钮
	private EditText message;// 信息
	private ListView list;// 列表对象
	private String keyword = "";// 存储输入的信息
	private List<Supplier_message> data;
	private Item_centre_supplier_adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_select_provider);
		setview();// 设置界面的方法
		list.setOnItemClickListener(listener);
	}

	// 列表的监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Maintain_select_providerActivity.this, Maintain_select_org.class);
			in.putExtra("supplier_id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Maintain_select_providerActivity.this);
			finish();

		}
	};

	// 设置界面的方法
	private void setview() {
		set_title_text("信息完善");
		seek = (TextView) findViewById(R.id.textview_maintain_select_provider_seek);
		seek.setOnClickListener(l);
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		message = (EditText) findViewById(R.id.editText_maintain_select_provider_message);
		list = (ListView) findViewById(R.id.listView_maintain_select_provider_list);
		adapter = new Item_centre_supplier_adapter(Maintain_select_providerActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Supplier_message>();
		Toast.makeText(this, "想录入更多信息，请到PC端录入", 0).show();
	}

	// 监听的方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Maintain_select_providerActivity.this);

				break;
			case R.id.textview_maintain_select_provider_seek:
				search_supplierlist();// 获取供应商的列表

				break;
			default:
				break;
			}

		}
	};

	// 根据供应商名称模糊查询供应商列表
	private void search_supplierlist() {
		
		if (message.getText().toString().equals("")) {
			Toast.makeText(Maintain_select_providerActivity.this, "请先输入关键词在查询", 0).show();
			return;
		}

		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.SEARCH_SUPPLIERLIST, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(Maintain_select_providerActivity.this, "对不起没有搜到，你要查询的供应商公司，请及时与后台客服联系。", 0)
									.show();
						} else {
							data.clear();
							adapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								data.add(new Supplier_message(js3.getString("id"), js3.getString("name")));
							}
							adapter.addDataBottom(data);
						}

					} else {
						Toast.makeText(Maintain_select_providerActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Maintain_select_providerActivity.this, "请求数据失败", 0).show();
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
				map.put("keyword", message.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);

	}

}
