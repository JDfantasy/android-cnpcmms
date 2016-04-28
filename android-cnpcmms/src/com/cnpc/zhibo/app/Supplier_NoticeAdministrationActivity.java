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
import com.cnpc.zhibo.app.adapter.Item_supplier_notice_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_notice;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Supplier_NoticeAdministrationActivity extends MyActivity {

	private ImageView back, sendNotice;
	private TextView zzwx, lswx;
	private PullToRefreshListView mPullListView;
	private ListView listView;
	private List<Supplier_notice> data;
	private Item_supplier_notice_adapter adapter;
	private boolean ischecked = false;
	private boolean number = false;// 用来判断是否正在进行刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier__notice_administration);
		set_title_text("公告管理");
		setView();
	}

	/**
	 * 初始化控件
	 */
	private void setView() {
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		sendNotice = (ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		sendNotice.setVisibility(View.VISIBLE);
		sendNotice.setImageResource(R.drawable.send_notice1);
		sendNotice.setPadding(0, 0, 50, 0);
		back.setOnClickListener(l);
		sendNotice.setOnClickListener(l);
		zzwx = (TextView) findViewById(R.id.textview_supplier_notielist);
		lswx = (TextView) findViewById(R.id.textview_supplier_noticesended);
		zzwx.setOnClickListener(l);
		lswx.setOnClickListener(l);
		zzwx.setTextColor(getResources().getColor(R.color.zitiyanse1));
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_supplier_gonggaoguanli);
		listView = mPullListView.getRefreshableView();// 获取listView
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);
		adapter = new Item_supplier_notice_adapter(this);
		data = new ArrayList<Supplier_notice>();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listener);
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_NoticeAdministrationActivity.this);
				break;
			case R.id.imageView_myactity_you:
				startActivity(
						new Intent(Supplier_NoticeAdministrationActivity.this, Supplier_sendmessageActivity.class));
				Myutil.set_activity_open(Supplier_NoticeAdministrationActivity.this);
				break;
			case R.id.textview_supplier_notielist:
				if (ischecked == false) {
					return;
				}
				setbuttoncoloer(R.id.textview_supplier_notielist);
				adapter.setdate(data);
				ischecked = false;
				break;
			case R.id.textview_supplier_noticesended:
				if (ischecked == true) {
					return;
				}
				setbuttoncoloer(R.id.textview_supplier_noticesended);
				ischecked = true;
				break;
			default:
				break;
			}

		}
	};

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (ischecked == false) {

			} else {
				startActivity(
						new Intent(Supplier_NoticeAdministrationActivity.this, Supplier_gonggao_seereadActivity.class));
				Myutil.set_activity_open(Supplier_NoticeAdministrationActivity.this);
			}

		}
	};

	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (ischecked == false) {
					// get_listprogress();
				} else {
					get_notice_history();
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (ischecked == false) {
				// get_listprogress();
			} else {
				get_notice_history();
			}

		}

	};

	// 改变按钮字体颜色的方法
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_supplier_notielist:// 公告列表
			zzwx.setTextColor(getResources().getColor(R.color.zitiyanse1));
			lswx.setTextColor(getResources().getColor(R.color.baise));
			// get_listprogress();// 获取公告列表
			break;
		case R.id.textview_supplier_noticesended:// 已发送公告
			zzwx.setTextColor(getResources().getColor(R.color.baise));
			lswx.setTextColor(getResources().getColor(R.color.zitiyanse1));
			get_notice_history();// 获取已发送公告的列表
			break;

		default:
			break;
		}
	}

	/**
	 * 获取所有历史公告列表
	 */
	private void get_notice_history() {
		String url = Myconstant.SUPPLIER_NOTICELIST_HISTORY + "?beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();

		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

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
							JSONObject obj4 = obj3.getJSONObject("reporter");
							data.add(new Supplier_notice(obj3.getString("id"), obj3.getString("title"),
									obj3.getString("content"), obj3.getString("unread"), obj3.getString("createDate")));
						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_NoticeAdministrationActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_NoticeAdministrationActivity.this, "没有查到公告列表", Toast.LENGTH_SHORT)
									.show();
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Supplier_NoticeAdministrationActivity.this, obj1.getString("content"),
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
				Toast.makeText(Supplier_NoticeAdministrationActivity.this, "网络出问题了，请稍后再试...", Toast.LENGTH_LONG).show();

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
