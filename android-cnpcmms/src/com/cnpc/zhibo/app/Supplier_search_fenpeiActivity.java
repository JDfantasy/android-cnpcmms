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
import com.cnpc.zhibo.app.adapter.Item_supplier_page_fenpeiguanli_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_page_fenpeiguanl;
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 供应商的订单分配搜索界面
 */
public class Supplier_search_fenpeiActivity extends MyActivity {
	private ImageView fanhui;// 返回
	private ListView listView;
	private TextView search, hint;// 搜索，搜索提示
	private EditText content;// 输入框
	private List<Supplier_page_fenpeiguanl> data;
	private Item_supplier_page_fenpeiguanli_adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_search_fenpei);
		set_title_text("搜索");
		setview();
	}

	private void setview() {
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		listView = (ListView) findViewById(R.id.listView_supplier_search_repairsentry_entry1);
		search = (TextView) findViewById(R.id.textview_supplier_search_repairsentry_seek1);
		search.setOnClickListener(l);
		hint = (TextView) findViewById(R.id.textView_supplier_search_repairsenty_hint1);
		content = (EditText) findViewById(R.id.editText_supplier_search_repairsentry_message1);
		hint.setText("搜索已分配的保修单");
		data = new ArrayList<Supplier_page_fenpeiguanl>();
		adapter = new Item_supplier_page_fenpeiguanli_adapter(this);
		listView.setAdapter(adapter);

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_search_fenpeiActivity.this);
				break;
			case R.id.textview_supplier_search_repairsentry_seek1:
				if (TextUtils.isEmpty(content.getText())) {
					Toast.makeText(Supplier_search_fenpeiActivity.this, "请输入已分配的报修单号", Toast.LENGTH_SHORT).show();
					return;
				}
				hint.setVisibility(View.INVISIBLE);
				try {
					get_fenpeiList();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}

		}
	};

	private void get_fenpeiList() throws UnsupportedEncodingException {
		String url = Myconstant.SUPPLIER_WARRANY_SEARCH;

		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						listView.setAdapter(adapter);
						adapter.getData().clear();
						data.clear();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj4 = obj3.getJSONObject("station");// 加油站信息
							JSONObject obj5 = obj3.getJSONObject("fixter");// 维修员信息
							JSONObject obj6 = obj3.getJSONObject("project");// 项目内容
							JSONArray obj7 = obj3.getJSONArray("repairOrderImages");
							if (obj7.length() == 0) {
								data.add(new Supplier_page_fenpeiguanl(obj3.getString("id"), obj5.getString("name"),
										obj4.getString("name"), obj3.getString("name"), obj6.getString("name"),
										obj3.getString("createDate"), "", obj3.getString("takeStatus"),
										obj5.getString("username"), obj3.getString("sn")));
							} else {
								JSONObject obj8 = (JSONObject) obj7.get(0);
								data.add(new Supplier_page_fenpeiguanl(obj3.getString("id"), obj5.getString("name"),
										obj4.getString("name"), obj3.getString("name"), obj6.getString("name"),
										obj3.getString("createDate"),
										Myconstant.WEBPAGEPATHURL + obj8.getString("source"),
										obj3.getString("takeStatus"), obj5.getString("username"),
										obj3.getString("sn")));
							}
						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_search_fenpeiActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_search_fenpeiActivity.this, "没有已分配的保修单，请检查单号", Toast.LENGTH_SHORT)
									.show();
						}
						adapter.addDataBottom(data);
					} else {
						Toast.makeText(Supplier_search_fenpeiActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Supplier_search_fenpeiActivity.this, "没有加载到已分配报修单列表，请稍后再试...", Toast.LENGTH_LONG).show();

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
				map.put("sn", content.getText().toString());
				return map;
			}
		};
		SysApplication.getHttpQueues().add(request);
	}

}
