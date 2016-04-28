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
import com.cnpc.zhibo.app.adapter.Item_supplier_page_baoxiudan_adapter;
import com.cnpc.zhibo.app.adapter.Item_supplier_page_weixiudanguanli_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_page_baoxiudanguanli;
import com.cnpc.zhibo.app.entity.Supplier_page_weixiudanguanli;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Supplier_search_weixiudanActivity extends MyActivity {
	private ImageView fanhui;// 返回按钮
	private EditText content;// 用户输入的
	private TextView seek, hint;// 搜索按钮,提示框
	private ListView lv;
	private Item_supplier_page_weixiudanguanli_adapter adapter;
	private Item_supplier_page_baoxiudan_adapter adapter1;
	private List<Supplier_page_weixiudanguanli> data;
	private List<Supplier_page_baoxiudanguanli> data1;
	private int tag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_search_weixiudan);
		Intent intent = getIntent();
		tag = intent.getIntExtra("tag", 0);
		setview();

	}

	/**
	 * 初始化界面
	 */
	private void setview() {
		adapter = new Item_supplier_page_weixiudanguanli_adapter(this);
		adapter1 = new Item_supplier_page_baoxiudan_adapter(this);
		data = new ArrayList<Supplier_page_weixiudanguanli>();
		data1 = new ArrayList<Supplier_page_baoxiudanguanli>();
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		lv = (ListView) findViewById(R.id.listView_supplier_search_repairsentry_entry);
		content = (EditText) findViewById(R.id.editText_supplier_search_repairsentry_message);
		seek = (TextView) findViewById(R.id.textview_supplier_search_repairsentry_seek);
		hint = (TextView) findViewById(R.id.textView_supplier_search_repairsenty_hint);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);

		if (tag == 1) {
			set_title_text("维修单");
			hint.setText("搜索维修单列表");
		} else if (tag == 2) {
			set_title_text("保修单");
			hint.setText("搜索报修单列表");
		}

		seek.setOnClickListener(l);
		lv.setOnItemClickListener(listener);
	}

	/**
	 * 控件监听
	 */
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_search_weixiudanActivity.this);
				break;
			case R.id.textview_supplier_search_repairsentry_seek:
				if (TextUtils.isEmpty(content.getText())) {
					Toast.makeText(Supplier_search_weixiudanActivity.this, "请输入维修单号", Toast.LENGTH_SHORT).show();
					return;
				} else {
					hint.setVisibility(View.INVISIBLE);
					if (tag == 1) {

						set_title_text("维修单查询");
						try {
							get_listprogress();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (tag == 2) {

						set_title_text("保修单查询");
						get_assigned_warranty();
					}
				}

				break;
			default:
				break;
			}

		}
	};

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(Supplier_search_weixiudanActivity.this,
					Supplier_weixiudanxiangqingActivity.class);
			intent.putExtra("weixiudan_id", adapter.getData().get(position).id);
			startActivity(intent);
			Myutil.set_activity_open(Supplier_search_weixiudanActivity.this);

		}
	};

	/**
	 * 获得维修单列表
	 * 
	 * @throws UnsupportedEncodingException
	 */
	protected void get_listprogress() throws UnsupportedEncodingException {
		String url = Myconstant.SUPPLIER_REPAIRS_SEARCH;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						lv.setAdapter(adapter);
						adapter.getData().clear();
						data.clear();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj5 = obj3.getJSONObject("repairOrder");

							JSONObject obj7 = obj5.getJSONObject("project");// 保修项目
							JSONObject obj4 = obj5.getJSONObject("station");// 加油站信息
							JSONArray obj6 = obj5.getJSONArray("repairOrderImages");
							if (obj6.length() == 0) {
								data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"), obj3.getString("sn"),
										obj4.getString("name"), obj5.getString("name"), obj3.getString("completeDate"),
										obj7.getString("name"), "", obj3.getString("status"),
										obj3.getString("workStatus"),obj3.getString("score")));
							} else {
								JSONObject obj8 = (JSONObject) obj6.get(0);
								data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"), obj3.getString("sn"),
										obj4.getString("name"), obj5.getString("name"), obj3.getString("completeDate"),
										obj7.getString("name"), Myconstant.WEBPAGEPATHURL + obj8.getString("source"),
										obj3.getString("status"), obj3.getString("workStatus"),obj3.getString("score")));
							}

						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_search_weixiudanActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_search_weixiudanActivity.this, "没有维修单，请检查单号", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						adapter.addDataBottom(data);
					} else {
						Toast.makeText(Supplier_search_weixiudanActivity.this, obj1.getString("content"),
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
				Toast.makeText(Supplier_search_weixiudanActivity.this, "没有加载到维修单，请稍后再试...", Toast.LENGTH_LONG).show();

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
		SysApplication.getHttpQueues().add(re);
	}

	/**
	 * 获得保修单列表
	 */
	private void get_assigned_warranty() {
		String url = Myconstant.SUPPLIER_WARRANY_SEARCH + content.getText().toString();
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						lv.setAdapter(adapter1);
						adapter1.getData().clear();
						data1.clear();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj4 = obj3.getJSONObject("station");// 加油站信息
							// JSONObject obj5 = obj3.getJSONObject("fixter");//
							// 维修员信息
							JSONObject obj6 = obj3.getJSONObject("project");// 项目内容
							data1.add(new Supplier_page_baoxiudanguanli(obj3.getString("id"), obj3.getString("sn"),
									obj4.getString("name"), obj3.getString("name"), obj6.getString("name"),
									obj3.getString("createDate"), "", obj3.getString("takeStatus")));
						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_search_weixiudanActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_search_weixiudanActivity.this, "没有维修单，请检查单号", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						adapter1.addDataBottom(data1);
					} else {
						Toast.makeText(Supplier_search_weixiudanActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Supplier_search_weixiudanActivity.this, "没有加载到已分配报修单列表，请稍后再试...", Toast.LENGTH_LONG)
						.show();

			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
		};
		SysApplication.getHttpQueues().add(re);
	}
}
