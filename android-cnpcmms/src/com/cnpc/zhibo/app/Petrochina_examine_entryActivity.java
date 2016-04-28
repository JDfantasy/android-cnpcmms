package com.cnpc.zhibo.app;

/*
 * 中石油端查看审批消息列表的界面
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
import com.azy.app.news.view.pullrefresh.PullToRefreshBase;
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.cnpc.zhibo.app.adapter.Item_petrochina_examine_message_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Petrochina_examine_message;
import com.cnpc.zhibo.app.util.Myutil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_examine_entryActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private ListView list;
	private Item_petrochina_examine_message_adapter adapter;
	private List<Petrochina_examine_message> data;
	private ImageView seek;// 搜索按钮
	private TextView examine, unexamine;// 已审批、未审批
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private boolean number = false;// 用来判断是否正在进行刷新
	private boolean state = false;// 判断当前的列表是已审批还是未审批
	private Intent in;
	/*
	 * 更新列表
	 */
	private Handler noticehandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					set_waitexamineentry();// 加载数据的方法
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
		setContentView(R.layout.activity_petrochina_examine);
		setview();
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		SysApplication.getInstance().setListHandler("customer-approve", noticehandler);
	}
	

	// 列表的监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			in.setClass(Petrochina_examine_entryActivity.this, Petrochina_examine_messageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("workStatus", adapter.getData().get(position).workStatus);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_examine_entryActivity.this);

		}
	};

	// 设置布局界面上的控件
	private void setview() {
		set_title_text("审批管理");
		in = new Intent();
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_petrochina_examine_entry);
		list = mPullListView.getRefreshableView();// 获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);// 设置上下拉刷新滑动的监听事件
		adapter = new Item_petrochina_examine_message_adapter(this);
		list.setAdapter(adapter);
		data = new ArrayList<Petrochina_examine_message>();
		seek = (ImageView) findViewById(R.id.imageView_petrochina_examine_search);// 搜索按钮
		seek.setOnClickListener(l);
		unexamine = (TextView) findViewById(R.id.textview_petrochina_examine_unexamine);
		examine = (TextView) findViewById(R.id.textview_petrochina_examine_examine);
		examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
		unexamine.setTextColor(getResources().getColor(R.color.blue));
		unexamine.setText(Html.fromHtml("<u>"+"未审批"+"</u>"));
		examine.setText("已审批");
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		unexamine.setOnClickListener(l);
		examine.setOnClickListener(l);
		set_waitexamineentry();// 加载未审批数据的方法
		
		
	}

	// mPullListView的上下拉滑动的监听
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (state == true) {
					set_historyexamineentry();// 获取历史维修的维修单的列表
				} else {
					set_waitexamineentry();
					// 获取维修中的维修单的列表
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态

			} else {

			}

		}

	};

	// 加载未审批的数据的方法
	private void set_waitexamineentry() {
		state = false;
		data.clear();
		adapter.getData().clear();
		examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
		unexamine.setTextColor(getResources().getColor(R.color.blue));
		unexamine.setText(Html.fromHtml("<u>"+"未审批"+"</u>"));
		examine.setText("已审批");
		StringRequest re = new StringRequest(Request.Method.GET, Myconstant.PETROCHINA_GET_WAITEXAMINEENTRY,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject js1 = js.getJSONObject("response");
							if (js1.getString("type").equals("success")) {
								Toast.makeText(Petrochina_examine_entryActivity.this, js1.getString("content"), 0)
										.show();
								JSONArray jsa = js.getJSONArray("body");
								for (int i = 0; i < jsa.length(); i++) {
									JSONObject js2 = jsa.getJSONObject(i);
									JSONObject station = js2.getJSONObject("station");// 加油站信息
									JSONObject repairOrder = js2.getJSONObject("repairOrder");
									JSONObject project = repairOrder.getJSONObject("project");
									JSONArray repairOrderImages = repairOrder.getJSONArray("repairOrderImages");

									if (repairOrderImages.length() == 0) {
										data.add(new Petrochina_examine_message(js2.getString("sn"),
												station.getString("name"),
												project.getString("name") + " " + repairOrder.getString("name"),
												js2.getString("amount"), "", js2.getString("id"),
												js2.getString("createDate"), js2.getString("workStatus")));
									} else {
										JSONObject phone = (JSONObject) repairOrderImages.get(0);
										data.add(new Petrochina_examine_message(js2.getString("sn"),
												station.getString("name"),
												project.getString("name") + " " + repairOrder.getString("name"),
												js2.getString("amount"),
												Myconstant.WEBPAGEPATHURL + phone.getString("source"),
												js2.getString("id"), js2.getString("createDate"),
												js2.getString("workStatus")));
									}

								}
								adapter.addDataBottom(data);
								mPullListView.onPullDownRefreshComplete();// 关闭刷新
								mPullListView.onPullUpRefreshComplete();
								number = false;// 改变是否刷新的状态
							} else {
								Toast.makeText(Petrochina_examine_entryActivity.this, js1.getString("content"), 0)
										.show();
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
						Toast.makeText(Petrochina_examine_entryActivity.this, "你已经掉线，请重新登录" + "", 0).show();
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
		SysApplication.getHttpQueues().add(re);
	}

	// 加载已审批的数据的方法
	private void set_historyexamineentry() {
		state = true;

		String url = Myconstant.PETROCHINA_GET_HISTORYEXAMINEENTRY + "beginDate=" + Myutil.get_month_time()
				+ "&endDate=" + Myutil.get_time_ymd();
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_examine_entryActivity.this, js1.getString("content"), 0).show();
						data.clear();
						adapter.getData().clear();
						JSONArray jsa = js.getJSONArray("body");
						for (int i = 0; i < jsa.length(); i++) {
							JSONObject js2 = jsa.getJSONObject(i);
							JSONObject station = js2.getJSONObject("station");// 加油站信息
							JSONObject repairOrder = js2.getJSONObject("repairOrder");
							JSONObject project = repairOrder.getJSONObject("project");
							JSONArray repairOrderImages = repairOrder.getJSONArray("repairOrderImages");

							if (repairOrderImages.length() == 0) {
								data.add(new Petrochina_examine_message(js2.getString("sn"), station.getString("name"),
										project.getString("name") + " " + repairOrder.getString("name"),
										js2.getString("amount"), "", js2.getString("id"), js2.getString("createDate"),
										js2.getString("workStatus")));
							} else {
								JSONObject phone = (JSONObject) repairOrderImages.get(0);
								data.add(new Petrochina_examine_message(js2.getString("sn"), station.getString("name"),
										project.getString("name") + " " + repairOrder.getString("name"),
										js2.getString("amount"), Myconstant.WEBPAGEPATHURL + phone.getString("source"),
										js2.getString("id"), js2.getString("createDate"), js2.getString("workStatus")));
							}

						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Petrochina_examine_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_examine_entryActivity.this, "你已经掉线，请重新登录" + "", 0).show();
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
		SysApplication.getHttpQueues().add(re);
	}

	// 监听的方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Petrochina_examine_entryActivity.this);
				break;
			case R.id.imageView_petrochina_examine_search:// 搜索按钮
				Intent in = new Intent(Petrochina_examine_entryActivity.this,
						Petrochina_search_serviceentryActivity.class);
				startActivity(in);
				break;
			case R.id.textview_petrochina_examine_unexamine:// 未审批
				examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
				unexamine.setTextColor(getResources().getColor(R.color.blue));
				unexamine.setText(Html.fromHtml("<u>"+"未审批"+"</u>"));
				examine.setText("已审批");
				set_waitexamineentry();

				break;
			case R.id.textview_petrochina_examine_examine:// 已审批
				examine.setTextColor(getResources().getColor(R.color.blue));
				unexamine.setTextColor(getResources().getColor(R.color.zitiyanse5));
				unexamine.setText("未审批");
				examine.setText(Html.fromHtml("<u>"+"已审批"+"</u>"));
				set_historyexamineentry();
				break;
			default:
				break;
			}
		}
	};

}
