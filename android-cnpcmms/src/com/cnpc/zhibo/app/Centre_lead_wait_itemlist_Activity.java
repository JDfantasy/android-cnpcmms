package com.cnpc.zhibo.app;

/*
 * 站长端的审批管理界面
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.adapter.Item_centre_page_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.util.Myutil;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Centre_lead_wait_itemlist_Activity extends MyActivity {
	private ListView list;
	private ImageView back;// 返回按钮
	private Item_centre_page_service_adapter adapter;
	private List<Centre_page_service> data;// 数据集合
	private RequestQueue mqueQueue;// 请求对象
	/*
	 * 更新列表
	 */
	private Handler noticehandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					get_waitapprvoeentry();// 加载数据的方法
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_lead_wait_itemlist_);
		set_view();// 设置界面的方法
		get_waitapprvoeentry();// 获取数据
		list.setOnItemClickListener(listener);
		SysApplication.getInstance().setListHandler("site-approve", noticehandler);
	}

	
	// listview的
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Centre_lead_wait_itemlist_Activity.this, Centre_See_examine_itemActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);

			startActivity(in);
			Myutil.set_activity_open(Centre_lead_wait_itemlist_Activity.this);
			// finish();

		}
	};

	// 设置界面上的控件
	private void set_view() {
		mqueQueue = Volley.newRequestQueue(this);// 初始化请求对象
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		list = (ListView) findViewById(R.id.listView_Centre_service_wait_itemlist_message);
		adapter = new Item_centre_page_service_adapter(this);
		data = new ArrayList<Centre_page_service>();
		list.setAdapter(adapter);
		set_title_text("审批管理");
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Centre_lead_wait_itemlist_Activity.this);
				break;

			default:
				break;
			}
		}
	};

	// 获取需要进行提交审批的维修单列表的方法
	private void get_waitapprvoeentry() {
		data.clear();
		adapter.getData().clear();
		String url = Myconstant.CENTRE_GET_WAITAPPROVEENTRY;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						adapter.getData().clear();
						data.clear();
						adapter.setinitializedata();
						Toast.makeText(Centre_lead_wait_itemlist_Activity.this, js1.getString("content"), 0).show();
						JSONArray jsa = js.getJSONArray("body");
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
										""));
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
										Myconstant.WEBPAGEPATHURL + jsphone.getString("source")));

							}
						}
						adapter.addDataBottom(data);

					} else {
						Toast.makeText(Centre_lead_wait_itemlist_Activity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_lead_wait_itemlist_Activity.this, "你已经掉线，请重新登录" + "", 0).show();
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

		};
		mqueQueue.add(re);

	}

	// 判断维修单的状态的方法
	private String getjudgestatus(String name) {
		String str = "无法状态";
		if (name.equals("approved")) {
			str = "审批通过";
		}
		if (name.equals("unsubmited")) {
			str = "等待处理";
		}
		if (name.equals("unapproved")) {
			str = "审批驳回";
		}
		return str;
	}
}