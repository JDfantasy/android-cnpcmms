package com.cnpc.zhibo.app;

/*
 * 站长端——查看报修单列表的界面
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
import com.azy.app.news.view.pullrefresh.PullToRefreshBase;
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.cnpc.zhibo.app.adapter.Item_centre_page_appove_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_see_repairs_entryActivity extends MyActivity {
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private ListView list;
	private ImageView back;
	private Item_centre_page_appove_adapter adapter;// 适配器
	private List<Centre_page_approve> data;// 数据集合
	private RequestQueue mqueQueue;// 请求对象
	private TextView history, progress;// 等待维修、历史维修
	private boolean state1 = false;// 用来判断当前列表打开是历史维修还是等待维修
	private boolean number = false;// 用来判断是否正在进行刷新
	private ImageView search;// 搜索按钮
	/*
	 * 更新列表
	 */
		private Handler noticehandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					try {
						get_progressdata();//加载数据的方法
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
		setContentView(R.layout.activity_centre_itemlist_);
		set_view();// 设置界面的方法
		get_progressdata();// 获取等待维修中的
		list.setOnItemClickListener(listener);
		SysApplication.getInstance().setListHandler("site-repairorder", noticehandler);

	}

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Centre_see_repairs_entryActivity.this, Centre_See_repairs_messageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("state", adapter.getData().get(position).state);
			startActivity(in);
			Myutil.set_activity_open(Centre_see_repairs_entryActivity.this);

		}
	};
	// mPullListView的上下拉滑动的监听
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (state1 == false) {
					get_historydata();// 获取历史维修的报修单的列表
				} else {
					get_progressdata();// 获取等待维修中的报修单的列表
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (state1 == false) {
				get_historydata();// 获取历史维修的报修单的列表
			} else {
				get_progressdata();// 获取等待维修中的报修单的列表
			}

		}

	};

	// 设置界面上的控件
	private void set_view() {
		mqueQueue = Volley.newRequestQueue(this);// 初始化请求对象
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		search = (ImageView) findViewById(R.id.imageView_myactity_you);// 搜索按钮
		search.setVisibility(View.VISIBLE);
		search.setImageResource(R.drawable.search_icon);
		search.setOnClickListener(l);
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_centre_itemlist_message);
		list = mPullListView.getRefreshableView();// 获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);// 设置上下拉刷新滑动的监听事件
		adapter = new Item_centre_page_appove_adapter(this);
		data = new ArrayList<Centre_page_approve>();
		list.setAdapter(adapter);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		set_title_text("订单分配");
		progress = (TextView) findViewById(R.id.textview_centre_itemlist_progressentry);// 等待维修...
		history = (TextView) findViewById(R.id.textview_centre_itemlist_historyentry);// 历史维修
		progress.setOnClickListener(l);
		history.setOnClickListener(l);
		
		progress.setTextColor(getResources().getColor(R.color.blue));
		progress.setText(Html.fromHtml("<u>"+"未处理"+"</u>"));
		history.setTextColor(getResources().getColor(R.color.zitiyanse5));
		history.setText("已处理");

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Centre_see_repairs_entryActivity.this);
				break;
			case R.id.textview_centre_itemlist_progressentry:// 等待维修
				get_progressdata();
				break;
			case R.id.textview_centre_itemlist_historyentry:// 历史维修
				get_historydata();
				break;
			case R.id.imageView_myactity_you:// 搜索按钮
				Intent in = new Intent(Centre_see_repairs_entryActivity.this, Centre_search_rapairsentryActivity.class);
				startActivity(in);
				Myutil.set_activity_open(Centre_see_repairs_entryActivity.this);

				break;
			default:
				break;
			}
		}
	};

	// 获取等待维修中的报修单的列表
	private void get_progressdata() {
		state1 = true;

		progress.setText(Html.fromHtml("<u>"+"未处理"+"</u>"));
		progress.setTextColor(getResources().getColor(R.color.blue));
		history.setText("已处理");
		history.setTextColor(getResources().getColor(R.color.zitiyanse5));


		String url = Myconstant.CENTRE_GETREPAIRORDERLIST;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						adapter.setinitializedata();
						Toast.makeText(Centre_see_repairs_entryActivity.this, js1.getString("content"), 0).show();
						JSONArray jsa = js.getJSONArray("body");
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
										js2.getString("status"),
										Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
										js2.getString("id"), Myconstant.WEBPAGEPATHURL + js3.getString("source")));
							}

						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Centre_see_repairs_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_see_repairs_entryActivity.this, "请求数据失败", 0).show();
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

	// 获取历史维修的报修单的列表
	private void get_historydata() {
		state1 = false;
		history.setText(Html.fromHtml("<u>"+"已处理"+"</u>"));
		history.setTextColor(getResources().getColor(R.color.blue));
		progress.setText("未处理");
		progress.setTextColor(getResources().getColor(R.color.zitiyanse5));

		String url = Myconstant.CENTRE_GETREPAIHISTORYRORDERLIST + "beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						adapter.setinitializedata();
						Toast.makeText(Centre_see_repairs_entryActivity.this, js1.getString("content"), 0).show();
						JSONArray jsa = js.getJSONArray("body");
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
										Myutil.get_delta_t(js2.getString("modifyDate"), js2.getString("createDate")),
										js2.getString("id"), ""));
							} else {
								// 有图片时
								JSONObject js3 = (JSONObject) repairOrderImages.get(0);
								data.add(new Centre_page_approve(js2.getString("sn"),
										js2// 编号
												.getString("modifyDate"),
										Myutil.set_cutString(
												project.getString("name") + "  " + js2.getString("description")),
										js2.getString("status"),
										Myutil.get_delta_t(js2.getString("modifyDate"), js2.getString("createDate")),
										js2.getString("id"), Myconstant.WEBPAGEPATHURL + js3.getString("source")));
							}
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Centre_see_repairs_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Centre_see_repairs_entryActivity.this, "请求数据失败", 0).show();
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

	

}
