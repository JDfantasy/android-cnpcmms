package com.cnpc.zhibo.app;

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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 供应商的分配管理界面
 */
public class Supplier_fenpeiguanliActivity extends MyActivity {

	private ImageView fanhui, search;// 返回,搜索
	private ListView listView;
	// private RequestQueue requestQueue;
	private List<Supplier_page_fenpeiguanl> data;
	private Item_supplier_page_fenpeiguanli_adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_fenpeiguanli);
		setview();
		get_assigned_warranty();
	}

	/**
	 * 初始化界面
	 */
	private void setview() {
		// requestQueue = Volley.newRequestQueue(this);
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		data = new ArrayList<Supplier_page_fenpeiguanl>();
		search = (ImageView) findViewById(R.id.imageView_fragment_supplier_repairs_search2);
		search.setOnClickListener(l);
		adapter = new Item_supplier_page_fenpeiguanli_adapter(this);
		listView = (ListView) findViewById(R.id.listview_supplier_fenpeiguanli_itemlist);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listener);
		set_title_text("分配管理");
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_fenpeiguanliActivity.this);
				break;

			case R.id.imageView_fragment_supplier_repairs_search2:
				Intent intent = new Intent(Supplier_fenpeiguanliActivity.this, Supplier_search_fenpeiActivity.class);
				startActivity(intent);
				Myutil.set_activity_open(Supplier_fenpeiguanliActivity.this);
				break;
			default:
				break;
			}
		}
	};

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			startActivity(new Intent(Supplier_fenpeiguanliActivity.this, Supplier_fenpeixiangqingActivity.class)
					.putExtra("baoxiudan_id", adapter.getData().get(position).id).putExtra("tag", 1));

		}
	};

	/**
	 * 运营商端的获取所有已分配报修单列表的方法
	 */
	private void get_assigned_warranty() {
		// String url = Myconstant.SUPPLIER_ASSIGNED_WARRANTY + "?beginDate=" +
		// Myutil.get_month_time() + "&endDate="
		// + Myutil.get_time_ymd();
		String url = Myconstant.SUPPLIER_ASSIGNED_WARRANTY;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

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
										obj5.getString("username"),obj3.getString("sn")));
							} else {
								JSONObject obj8 = (JSONObject) obj7.get(0);
								data.add(new Supplier_page_fenpeiguanl(obj3.getString("id"), obj5.getString("name"),
										obj4.getString("name"), obj3.getString("name"), obj6.getString("name"),
										obj3.getString("createDate"),
										Myconstant.WEBPAGEPATHURL + obj8.getString("source"),
										obj3.getString("takeStatus"), obj5.getString("username"),obj3.getString("sn")));
							}
						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_fenpeiguanliActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_fenpeiguanliActivity.this, "没有分配的保修单", Toast.LENGTH_SHORT).show();
						}
						adapter.addDataBottom(data);
					} else {
						Toast.makeText(Supplier_fenpeiguanliActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Supplier_fenpeiguanliActivity.this, "没有加载到已分配报修单列表，请稍后再试...", Toast.LENGTH_LONG).show();

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
