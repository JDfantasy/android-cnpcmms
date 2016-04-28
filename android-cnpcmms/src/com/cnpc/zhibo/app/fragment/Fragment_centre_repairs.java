package com.cnpc.zhibo.app.fragment;

//站长段的维修查看维修单列表的界面
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
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.cnpc.zhibo.app.Centre_page_examine_Activity;
import com.cnpc.zhibo.app.Centre_page_maintainActivity;
import com.cnpc.zhibo.app.Centre_search_serviceentryActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.adapter.Item_centre_page_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_centre_repairs extends Fragment {
	private TextView zzwx, lswx;// 正在维修、等待维修、历史维修
	private RequestQueue mqueQueue;// 请求对象
	private ListView list;// 列表对象
	private List<Centre_page_service> data;// 数据集合
	private ImageView seek;// 搜索
	// 正在维修的adapter、等待维修的adapter、历史维修的adapter
	private Item_centre_page_service_adapter adapter_zzwx, adapter_lswx;
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private boolean number = false;// 用来判断是否正在进行刷新
	private boolean state = false;// 判断当前的列表是历史维修还是正在维修
	/*
	 * 更新列表
	 */
	private Handler noticehandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					get_listprogress();// 加载数据的方法
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_centre_repairs, container, false);
		setview(v);
		get_listprogress();// 获取正在维修的列表
		list.setOnItemClickListener(listener);
		SysApplication.getInstance().setListHandler("site-fixorder", noticehandler);// 调用刷新界面的提供的方法
		return v;
	}

	// 列表的监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(getActivity(), Centre_page_maintainActivity.class);
			if (state) {
				in.putExtra("id", adapter_lswx.getData().get(position).id);
			} else {
				in.putExtra("id", adapter_zzwx.getData().get(position).id);
			}
			startActivity(in);
			Myutil.set_activity_open(getActivity());

		}
	};

	// 设置界面控件的方法
	private void setview(View v) {
		mqueQueue = Volley.newRequestQueue(getActivity());// 初始化请求对象
		zzwx = (TextView) v.findViewById(R.id.textview_fragment_centre_repairs_zzwx);
		lswx = (TextView) v.findViewById(R.id.textview_fragment_centre_repairs_lswx);
		zzwx.setOnClickListener(l);
		lswx.setOnClickListener(l);
		setbuttoncoloer(R.id.textview_fragment_centre_repairs_zzwx);// 设置按钮的颜色
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.listView_fragment_repairs);
		list = mPullListView.getRefreshableView();// 获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);// 设置上下拉刷新滑动的监听事件
		adapter_lswx = new Item_centre_page_service_adapter(getActivity());// 历史维修
		adapter_zzwx = new Item_centre_page_service_adapter(getActivity());// 正在维修
		data = new ArrayList<Centre_page_service>();
		seek = (ImageView) v.findViewById(R.id.imageView_fragment_centre_repairs_search);
		seek.setOnClickListener(l);
		adapter_lswx.servicestate();
		adapter_zzwx.servicestate();
		zzwx.setTextColor(getResources().getColor(R.color.blue));
		zzwx.setText(Html.fromHtml("<u>" + "维修中" + "</u>"));
		lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
		lswx.setText("历史维修");

		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// mPullListView的上下拉滑动的监听
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (state == true) {
					get_listcomplete();// 获取历史维修的维修单的列表
				} else {
					get_listprogress();
					;// 获取维修中的维修单的列表
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (state == true) {
					get_listcomplete();// 获取历史维修的维修单的列表
				} else {
					get_listprogress();
					;// 获取维修中的维修单的列表
				}
			} else {

			}

		}

	};
	// 监听的方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textview_fragment_centre_repairs_zzwx:// 正在审批
				setbuttoncoloer(R.id.textview_fragment_centre_repairs_zzwx);
				break;

			case R.id.textview_fragment_centre_repairs_lswx:// 历史审批
				setbuttoncoloer(R.id.textview_fragment_centre_repairs_lswx);
				break;
			case R.id.imageView_fragment_centre_repairs_search:// 搜索框
				Intent in = new Intent(getActivity(), Centre_search_serviceentryActivity.class);
				in.putExtra("activityname", "repairs");
				startActivity(in);
				Myutil.set_activity_open(getActivity());
				break;
			default:
				break;
			}

		}
	};

	// 改变按钮字体颜色的方法
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_fragment_centre_repairs_zzwx:// 正在维修

			get_listprogress();// 获取正在维修的列表
			break;

		case R.id.textview_fragment_centre_repairs_lswx:// 历史维修

			get_listcomplete();// 获取历史维修的列表
			break;

		default:
			break;
		}
	}

	// 获取正在维修的维修单列表
	private void get_listprogress() {
		zzwx.setTextColor(getResources().getColor(R.color.blue));
		zzwx.setText(Html.fromHtml("<u>" + "维修中" + "</u>"));
		lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
		lswx.setText("历史维修");
		state = false;
		String url = Myconstant.CENTRE_GET_BEINGREPAIRED_ENTRY;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						list.setAdapter(adapter_zzwx);
						adapter_zzwx.getData().clear();
						adapter_lswx.getData().clear();
						data.clear();
						adapter_lswx.setinitializedata();
						adapter_zzwx.setinitializedata();
						Toast.makeText(getActivity(), js1.getString("content"), 0).show();
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
						adapter_zzwx.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(getActivity(), js1.getString("content"), 0).show();
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
				Toast.makeText(getActivity(), "你已经掉线，请重新登录" + "", 0).show();
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

	// 获取历史维修的列表
	private void get_listcomplete() {
		lswx.setTextColor(getResources().getColor(R.color.blue));
		lswx.setText(Html.fromHtml("<u>" + "历史维修" + "</u>"));
		zzwx.setTextColor(getResources().getColor(R.color.zitiyanse5));
		zzwx.setText("维修中");
		state = true;
		String url = Myconstant.CENTRE_GET_HISTORYREPAIRED_ENTRY + "beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// 相应的请求
					if (js1.getString("type").equals("success")) {

						list.setAdapter(adapter_lswx);
						adapter_zzwx.getData().clear();
						adapter_lswx.getData().clear();
						data.clear();
						adapter_lswx.setinitializedata();
						adapter_zzwx.setinitializedata();
						Toast.makeText(getActivity(), js1.getString("content"), 0).show();
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
										Myutil.get_delta_t(js2.getString("modifyDate"), js2.getString("createDate")),
										js2.getString("id"), js2.getString("workStatus"), js4.getString("username"), "",
										js2.getString("score")));
							} else {
								// 有图片时
								JSONObject jsphone = (JSONObject) repairOrderImages.get(0);

								data.add(new Centre_page_service(js2.getString("sn"),
										js2// 编号
												.getString("createDate"),
										Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
										js2.getString("workStatus"),
										Myutil.get_delta_t(js2.getString("modifyDate"), js2.getString("createDate")),
										js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
										Myconstant.WEBPAGEPATHURL + jsphone.getString("source"),
										js2.getString("score")));

							}
						}
						adapter_lswx.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(getActivity(), js1.getString("content"), 0).show();
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
				Toast.makeText(getActivity(), "你已经掉线，请重新登录", 0).show();
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
