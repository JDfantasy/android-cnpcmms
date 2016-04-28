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
import com.azy.app.news.view.pullrefresh.PullToRefreshBase;
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.cnpc.zhibo.app.adapter.Item_petrochian_project_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Petrochina_project;
import com.cnpc.zhibo.app.entity.Petrochina_station_message;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
/*
 * 中石油端查看项目的维修率
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_project_entryActivity extends MyActivity {
	private ImageView back, seek;
	private ListView list;
	private Item_petrochian_project_adapter adapter;
	private List<Petrochina_project> data;
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private boolean number = false;// 用来判断是否正在进行刷新
	private TextView select;// 刷选
	private PopupWindow pop;
	private TextView month1, month3,month6,year1;
	private String beginDate = "2016-01-01";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_project_entry);
		setview();
		loadingdata();
		list.setOnItemClickListener(listener);
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Petrochina_project_entryActivity.this, Petrochina_service_numberActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("name", adapter.getData().get(position).name);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_project_entryActivity.this);

		}
	};

	private void setview() {
		set_title_text("维修率");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_petrochina_project_entry);// 列表对象
		list = mPullListView.getRefreshableView();// 获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);// 设置上下拉刷新滑动的监听事件
		adapter = new Item_petrochian_project_adapter(Petrochina_project_entryActivity.this);
		list.setAdapter(adapter);
		data = new ArrayList<Petrochina_project>();
		seek = (ImageView) findViewById(R.id.imageView_petrochina_project_entry_search);
		seek.setOnClickListener(l);
		select = (TextView) findViewById(R.id.textview_myacitivity_you);// 刷选
		select.setVisibility(View.VISIBLE);
		select.setOnClickListener(l);
		select.setText("筛选");
		beginDate=Myutil.get_month_time();
	}

	// mPullListView的上下拉滑动的监听
		private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (number == false) {
					number = true;// 改变刷新的状态
					loadingdata();
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
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Petrochina_project_entryActivity.this);

				break;
			case R.id.imageView_petrochina_project_entry_search:// 搜索按钮
				Intent in = new Intent(Petrochina_project_entryActivity.this,
						Petrochina_search_projectentryActivity.class);
				startActivity(in);
				Myutil.set_activity_open(Petrochina_project_entryActivity.this);

				break;
			case R.id.textview_myacitivity_you:// 刷选
				View v1 = LayoutInflater.from(Petrochina_project_entryActivity.this)
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
				Toast.makeText(Petrochina_project_entryActivity.this, "当前记录查询时间已经改为一个月，请下拉列表进行刷新", 0).show();
				break;
			case R.id.textView_popup_showtime_select_threemonth:// 三个月
				beginDate = Myutil.get_threemonth_time();
				pop.dismiss();
				Toast.makeText(Petrochina_project_entryActivity.this, "当前记录查询时间已经改为三个月，请下拉列表进行刷新", 0).show();
				break;
			case R.id.textView_popup_showtime_select_sixmonth:// 六个月
				beginDate = Myutil.get_sixmonth_time();
				pop.dismiss();
				Toast.makeText(Petrochina_project_entryActivity.this, "当前记录查询时间已经改为六个月，请下拉列表进行刷新", 0).show();
				break;
			case R.id.textView_popup_showtime_select_oneyear:// 一年
				beginDate = Myutil.get_oneyear_time();
				pop.dismiss();
				Toast.makeText(Petrochina_project_entryActivity.this, "当前记录查询时间已经改为一年，请下拉列表进行刷新", 0).show();
				break;
			default:
				break;
			}

		}
	};

	private void loadingdata() {

		data.clear();
		adapter.getData().clear();

		String url = Myconstant.PETROCHINA_GET_PROJECTENTRYSTATS + "beginDate=" +beginDate + "&endDate="
				+ Myutil.get_time_ymd();
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// 相应的请求
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_project_entryActivity.this, js1.getString("content"), 0).show();
						JSONArray body = js.getJSONArray("body");
						for (int i = 0; i < body.length(); i++) {
							JSONObject js2 = body.getJSONObject(i);
							data.add(new Petrochina_project("项目：" + js2.getString("name"),
									"维修次数：" + js2.getString("count"), js2.getString("id")));
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Petrochina_project_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_project_entryActivity.this, "你已经掉线，请重新登录", 0).show();
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
}
