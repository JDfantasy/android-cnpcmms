package com.cnpc.zhibo.app;

import java.lang.ref.WeakReference;
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
import com.azy.app.news.view.pullrefresh.PullToRefreshBase;
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.cnpc.zhibo.app.adapter.Item_supplier_page_dingdanfenpei_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_page_dingdanfenpei;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 供应商的查看订单保修列表界面
 */
public class Supplier_dingdanfenpeiActivity extends MyActivity {
	private PullToRefreshListView mPullListView;
	private ListView listView;
	private ImageView fanhui;
	// private RequestQueue requestQueue;
	private List<Supplier_page_dingdanfenpei> data;
	private Item_supplier_page_dingdanfenpei_adapter adapter;
	private boolean number = false;// 用来判断是否正在进行刷新
	private final MyHandler handler = new MyHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_dingdanfenpei);
		set_view();
		get_listprogress();

	}

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Supplier_dingdanfenpeiActivity.this, Supplier_fenpeixiangqingActivity.class);
			in.putExtra("baoxiudan_id", adapter.getData().get(position).id);
			in.putExtra("tag", 2);
			startActivity(in);
			Myutil.set_activity_open(Supplier_dingdanfenpeiActivity.this);

		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		get_listprogress();
	};

	/**
	 * 初始化界面
	 */
	private void set_view() {
		// requestQueue = Volley.newRequestQueue(this);
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		adapter = new Item_supplier_page_dingdanfenpei_adapter(this);
		mPullListView = (PullToRefreshListView) findViewById(R.id.listview_supplier_dingdanfenpei_itemlist);
		listView = mPullListView.getRefreshableView();// 获取listView
		listView.setOnItemClickListener(listener);
		mPullListView.setOnRefreshListener(refreshListener);
		listView.setDividerHeight(0);
		listView.setSelector(new BitmapDrawable());
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		data = new ArrayList<Supplier_page_dingdanfenpei>();
		listView.setAdapter(adapter);
		set_title_text("订单分配");
		SysApplication.getInstance().setListHandler("provider-distribute", handler);
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_dingdanfenpeiActivity.this);
				break;

			default:
				break;
			}
		}
	};

	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态

				get_listprogress();
			}
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			get_listprogress();
		}

	};

	/**
	 * 获得数据
	 */
	private void get_listprogress() {

		String url = Myconstant.SUPPLIER_GETREPAIR;
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
							JSONObject obj4 = obj3.getJSONObject("station");
							JSONObject obj5 = obj3.getJSONObject("project");
							JSONArray obj6 = obj3.getJSONArray("repairOrderImages");

							if (obj6.length() == 0) {
								data.add(new Supplier_page_dingdanfenpei(obj3.getString("id"), obj3.getString("sn"),
										Myutil.get_delta_t_data(Myutil.get_current_time(), obj3.getString("createDate"))
												+ "",
										obj4.getString("name"), obj3.getString("name"), obj5.getString("name"),
										obj3.getString("modifyDate"), ""));
							} else {
								JSONObject obj7 = (JSONObject) obj6.get(0);
								data.add(new Supplier_page_dingdanfenpei(obj3.getString("id"), obj3.getString("sn"),
										Myutil.get_delta_t_data(Myutil.get_current_time(), obj3.getString("createDate"))
												+ "",
										obj4.getString("name"), obj3.getString("name"), obj5.getString("name"),
										obj3.getString("modifyDate"),
										Myconstant.WEBPAGEPATHURL + obj7.getString("source")));
							}

						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_dingdanfenpeiActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_dingdanfenpeiActivity.this, "没有要分配的保修单", Toast.LENGTH_SHORT).show();
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Supplier_dingdanfenpeiActivity.this, obj1.getString("content"),
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
				Toast.makeText(Supplier_dingdanfenpeiActivity.this, "没有加载到数据，请稍后再试...", Toast.LENGTH_LONG).show();

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

	/**
	 * 此handler是为了避免内存泄露
	 *
	 */
	private static class MyHandler extends Handler {
		private final WeakReference<Supplier_dingdanfenpeiActivity> mActivity;

		public MyHandler(Supplier_dingdanfenpeiActivity activity) {
			mActivity = new WeakReference<Supplier_dingdanfenpeiActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			Supplier_dingdanfenpeiActivity activity = mActivity.get();
			if (activity != null) {
				switch (msg.what) {
				case 0:
					try {
						activity.get_listprogress();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		}
	}
}
