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
import com.cnpc.zhibo.app.adapter.Item_supplier_baoxiao_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_page_baoxiao;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 供应商端报销单搜素界面
 */
public class Supplier_search_baoxiaoActivity extends MyActivity {
	private ImageView fanhui;// 返回按钮
	private EditText content;// 用户输入的
	private TextView seek, hint;// 搜索按钮,提示框
	private ListView lv;
	private Item_supplier_baoxiao_adapter adapter;
	private List<Supplier_page_baoxiao> data;
	private int tag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_search_baoxiao);
		tag = getIntent().getIntExtra("tag", 0);
		setview();
	}

	/**
	 * 初始化控件
	 */
	private void setview() {
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		content = (EditText) findViewById(R.id.editText_supplier_search_reimbursement_message);
		lv = (ListView) findViewById(R.id.listView_supplier_search_reimbursement_entry);
		hint = (TextView) findViewById(R.id.textView_supplier_search_reimbursement_hint);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		lv.setOnItemClickListener(listener);
		data = new ArrayList<Supplier_page_baoxiao>();
		adapter = new Item_supplier_baoxiao_adapter(this);

		if (tag == 1) {// 已报销
			set_title_text("已处理的报销单查询");
			hint.setText("搜索已报销的报销单");
		} else if (tag == 2) {// 未报销
			set_title_text("未处理的报销单查询");
			hint.setText("搜索未报销的报销单");
		}
		seek = (TextView) findViewById(R.id.textview_supplier_search_reimbursement_seek);
		seek.setOnClickListener(l);
		lv.setAdapter(adapter);
	}

	/**
	 * 控件监听
	 */
	private View.OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_search_baoxiaoActivity.this);
				break;

			case R.id.textview_supplier_search_reimbursement_seek:
				if (TextUtils.isEmpty(content.getText())) {
					Toast.makeText(Supplier_search_baoxiaoActivity.this, "请输入维修单号", Toast.LENGTH_SHORT).show();
					return;
				} else {
					hint.setVisibility(View.INVISIBLE);
					if (tag == 1) {
						try {
							get_search_reimbursemented();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (tag == 2) {
						get_search_reimbursement();
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
			startActivity(new Intent(Supplier_search_baoxiaoActivity.this, Supplier_baoxiaodanxiangqingActivity.class)
					.putExtra("bao_xiao_dan_id", adapter.getData().get(position).id)
					.putExtra("state", adapter.getData().get(position).state));
			Myutil.set_activity_open(Supplier_search_baoxiaoActivity.this);
		}
	};

	/**
	 * 搜索已处理的报销单
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private void get_search_reimbursemented() throws UnsupportedEncodingException {
		String url = Myconstant.SUPPLIER_SEARCH_REIMBURSEMENTED;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj4 = obj3.getJSONObject("fixOrder");
							JSONObject obj5 = obj4.getJSONObject("repairOrder");
							JSONObject obj6 = obj5.getJSONObject("station");
							JSONObject obj7 = obj5.getJSONObject("project");
							data.add(new Supplier_page_baoxiao(obj3.getString("id"), obj3.getString("sn"),
									obj6.getString("name"), obj5.getString("name"), obj7.getString("name"),
									obj3.getString("createDate"), "", obj3.getString("status"),
									obj3.getString("amount")));
						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_search_baoxiaoActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_search_baoxiaoActivity.this, "没有查到报销单，请检查单号", Toast.LENGTH_SHORT)
									.show();
						}
						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Supplier_search_baoxiaoActivity.this, obj1.getString("content"),
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
				Toast.makeText(Supplier_search_baoxiaoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
	 * 搜索未处理的报销单
	 */
	private void get_search_reimbursement() {
		String url = Myconstant.SUPPLIER_SEARCH_REIMBURSEMENT;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj4 = obj3.getJSONObject("fixOrder");
							JSONObject obj5 = obj4.getJSONObject("repairOrder");
							JSONObject obj6 = obj5.getJSONObject("station");
							JSONObject obj7 = obj5.getJSONObject("project");
							data.add(new Supplier_page_baoxiao(obj3.getString("id"), obj3.getString("sn"),
									obj6.getString("name"), obj5.getString("name"), obj7.getString("name"),
									obj3.getString("createDate"), "", obj3.getString("status"),
									obj3.getString("amount")));
						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_search_baoxiaoActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_search_baoxiaoActivity.this, "没有查到报销单，请检查单号", Toast.LENGTH_SHORT)
									.show();
						}
						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Supplier_search_baoxiaoActivity.this, obj1.getString("content"),
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
				Toast.makeText(Supplier_search_baoxiaoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
}
