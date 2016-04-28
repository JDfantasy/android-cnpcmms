package com.cnpc.zhibo.app;

/*
 * 中石油端查看供应商列表的界面
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
import com.cnpc.zhibo.app.adapter.Item_petrochina_suppliers_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.entity.Petrochina_suppliers_entry;

import com.cnpc.zhibo.app.util.Myutil;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_suppliers_entryActivity extends MyActivity {
	private ImageView back;// 返回按钮
	private ListView list;// 列表对象
	private Item_petrochina_suppliers_adapter adapter;
	private List<Petrochina_suppliers_entry> data;
	private TextView numbers, grade;// 维修次数、维修费用
	private ImageView seek;// 搜索按钮
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private boolean number = false;// 用来判断是否正在进行刷新
	private boolean state = false;// 判断当前的列表是维修次数还是维修费用
	private TextView select;// 刷选
	private PopupWindow pop;
	private TextView month1, month3,month6
	
	, year1;
	private String beginDate = "2016-01-01";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_suppliers);
		setview();
	}

	// 设置布局界面上的控件
	private void setview() {
		set_title_text("供应商列表");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_petrochina_suppliers_entry);// 列表对象
		list = mPullListView.getRefreshableView();// 获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);// 设置上下拉刷新滑动的监听事件
		adapter = new Item_petrochina_suppliers_adapter(this);
		data = new ArrayList<Petrochina_suppliers_entry>();
		list.setAdapter(adapter);
		grade = (TextView) findViewById(R.id.textView_petrochina_suppliers_suppliersgrade);// 供应商服务
		numbers = (TextView) findViewById(R.id.textView_petrochina_suppliers_servicenumber);// 维修次数
		grade.setTextColor(getResources().getColor(R.color.blue));
		grade.setText(Html.fromHtml("<u>" + "未审批" + "</u>"));
		numbers.setTextColor(getResources().getColor(R.color.zitiyanse5));
		numbers.setText("已审批");
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		grade.setOnClickListener(l);
		numbers.setOnClickListener(l);
		seek = (ImageView) findViewById(R.id.imageView_petrochina_suppliers_search);// 搜索按钮
		seek.setOnClickListener(l);
		set_gradedata();// 加载维修费用的加油站列表的数据
		list.setOnItemClickListener(listener);
		select = (TextView) findViewById(R.id.textview_myacitivity_you);// 刷选
		select.setVisibility(View.VISIBLE);
		select.setOnClickListener(l);
		select.setText("筛选");
		beginDate = Myutil.get_month_time();
	}

	// mPullListView的上下拉滑动的监听
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (state == true) {
					set_numberdata();// 加载维修次数的数据
				} else {
					set_gradedata();
					// 加载维修费用的数据的方法
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
	// 列表的行监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Petrochina_suppliers_entryActivity.this,
					Petrochina_seesuppliers_message_Activity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_suppliers_entryActivity.this);
		}
	};

	// 加载供应商服务的数据的方法
	private void set_gradedata() {
		state = false;
		grade.setTextColor(getResources().getColor(R.color.blue));
		grade.setText(Html.fromHtml("<u>" + "供应商服务" + "</u>"));
		numbers.setTextColor(getResources().getColor(R.color.zitiyanse5));
		numbers.setText("维修次数");
		data.clear();
		adapter.getData().clear();

		String url = Myconstant.PETROCHINA_GET_PROVIDERENTRYSTATS + "beginDate=" + beginDate + "&endDate="
				+ Myutil.get_time_ymd() + "&direction1=" + "" + "&direction2=" + "desc";
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// 相应的请求
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_suppliers_entryActivity.this, js1.getString("content"), 0).show();
						JSONArray body = js.getJSONArray("body");
						for (int i = 0; i < body.length(); i++) {
							JSONObject js2 = body.getJSONObject(i);
							data.add(new Petrochina_suppliers_entry(js2.getString("name"),
									"维修次数:" + js2.getString("count"), "服务评分:" + js2.getString("score"),
									js2.getString("id")));
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Petrochina_suppliers_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_suppliers_entryActivity.this, "你已经掉线，请重新登录", 0).show();
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

	// 加载维修次数的数据的方法
	private void set_numberdata() {
		state = true;
		grade.setTextColor(getResources().getColor(R.color.zitiyanse5));
		grade.setText("供应商服务");
		numbers.setTextColor(getResources().getColor(R.color.blue));
		numbers.setText(Html.fromHtml("<u>" + "维修次数" + "</u>"));
		data.clear();
		adapter.getData().clear();
		String url = Myconstant.PETROCHINA_GET_PROVIDERENTRYSTATS + "beginDate=" + beginDate + "&endDate="
				+ Myutil.get_time_ymd() + "&direction1=" + "desc" + "&direction2=" + "";
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// 相应的请求
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_suppliers_entryActivity.this, js1.getString("content"), 0).show();
						JSONArray body = js.getJSONArray("body");
						for (int i = 0; i < body.length(); i++) {
							JSONObject js2 = body.getJSONObject(i);
							data.add(new Petrochina_suppliers_entry(js2.getString("name"),
									"维修次数:" + js2.getString("count"), "服务评分:" + js2.getString("score"),
									js2.getString("id")));
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Petrochina_suppliers_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_suppliers_entryActivity.this, "你已经掉线，请重新登录", 0).show();
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
				Myutil.set_activity_close(Petrochina_suppliers_entryActivity.this);
				break;
			case R.id.textView_petrochina_suppliers_suppliersgrade:// 供应商服务
				set_gradedata();

				break;
			case R.id.textView_petrochina_suppliers_servicenumber:// 维修次数
				set_numberdata();

				break;
			case R.id.imageView_petrochina_suppliers_search:// 搜索按钮
				Intent in = new Intent(Petrochina_suppliers_entryActivity.this,
						Petrochina_search_suppliersentryActivity.class);
				in.putExtra("state", state);
				startActivity(in);
				Myutil.set_activity_open(Petrochina_suppliers_entryActivity.this);
				break;
			case R.id.textview_myacitivity_you:// 刷选
				View v1 = LayoutInflater.from(Petrochina_suppliers_entryActivity.this)
						.inflate(R.layout.popup_shoutime_select, null);// 创建view
				pop = new PopupWindow(v1, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);// 实例化popupwindow的方法
				pop.setBackgroundDrawable(new ColorDrawable());
				// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
				pop.showAsDropDown(select, 0, 0);
				month1 = (TextView) v1.findViewById(R.id.textView_popup_showtime_select_onemonth);
				month3 = (TextView) v1.findViewById(R.id.textView_popup_showtime_select_threemonth);
				month6= (TextView) v1.findViewById(R.id.textView_popup_showtime_select_sixmonth);
				year1 = (TextView) v1.findViewById(R.id.textView_popup_showtime_select_oneyear);
				month1.setOnClickListener(l);
				month3.setOnClickListener(l);
				month6.setOnClickListener(l);
				year1.setOnClickListener(l);
				break;
			case R.id.textView_popup_showtime_select_onemonth:// 一个月
				beginDate = Myutil.get_month_time();
				pop.dismiss();
				Toast.makeText(Petrochina_suppliers_entryActivity.this, "当前记录查询时间已经改为一个月，请下拉列表进行刷新", 0).show();
				break;
			case R.id.textView_popup_showtime_select_threemonth:// 三个月
				beginDate = Myutil.get_threemonth_time();
				pop.dismiss();
				Toast.makeText(Petrochina_suppliers_entryActivity.this, "当前记录查询时间已经改为三个月，请下拉列表进行刷新", 0).show();
				break;
			case R.id.textView_popup_showtime_select_sixmonth:// 六个月
				beginDate = Myutil.get_sixmonth_time();
				pop.dismiss();
				Toast.makeText(Petrochina_suppliers_entryActivity.this, "当前记录查询时间已经改为六个月，请下拉列表进行刷新", 0).show();
				break;
			case R.id.textView_popup_showtime_select_oneyear:// 一年
				beginDate = Myutil.get_oneyear_time();
				pop.dismiss();
				Toast.makeText(Petrochina_suppliers_entryActivity.this, "当前记录查询时间已经改为一年，请下拉列表进行刷新", 0).show();
				break;
			default:
				break;
			}
		}
	};

}
