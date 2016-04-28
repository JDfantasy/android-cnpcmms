package com.cnpc.zhibo.app.fragment;

//站长段的审批查看审批
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
import com.cnpc.zhibo.app.Centre_ItemDetailsActivity;
import com.cnpc.zhibo.app.Centre_lead_wait_itemlist_Activity;
import com.cnpc.zhibo.app.Centre_page_examine_Activity;
import com.cnpc.zhibo.app.Centre_search_serviceentryActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.adapter.Item_centre_page_appove_adapter;
import com.cnpc.zhibo.app.adapter.Item_centre_page_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.util.Myutil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_centre_examine extends Fragment {
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private TextView wait_examine, record_examine;// 等待审批、等待审批、历史审批
	private RequestQueue mqueQueue;// 请求对象
	private ListView list;// 列表对象
	private Item_centre_page_service_adapter adapter;
	private List<Centre_page_service> data;// 数据集合
	private ImageView seek;// 搜索
	private boolean state = false;// 用来判断当前列表打开始是已审批还是未审批
	private boolean number = false;// 用来判断是否正在进行刷新

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_centre_examine, container, false);
		setview(v);// 设置界面的方法
		get_listwait();// 获取正在审批的列表
		list.setOnItemClickListener(listener);
		SysApplication.getInstance().setListHandler("site-approveorder", noticehandler);
		return v;
	}
	/*
	 * 更新列表
	 */
	private Handler noticehandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					get_listwait();// 加载数据的方法
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
			default:
				break;
			}
		};
	};
	// 列表的监听事件
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(getActivity(), Centre_page_examine_Activity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			in.putExtra("state", adapter.getData().get(position).WorkStatus);
			startActivity(in);
			Myutil.set_activity_open(getActivity());

		}
	};

	// 设置界面的方法
	private void setview(View v) {
		mqueQueue = Volley.newRequestQueue(getActivity());// 初始化请求对象
		wait_examine = (TextView) v.findViewById(R.id.textview_fragment_centre_examine_wait_examine);// 等待审批

		record_examine = (TextView) v.findViewById(R.id.textview_fragment_centre_examine_record_examine);// 历史审批
		wait_examine.setOnClickListener(l);
		record_examine.setOnClickListener(l);
		setbuttoncoloer(R.id.textview_fragment_centre_examine_wait_examine);// 设置按钮的颜色
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.listView_fragment_examine);// 实例化listview
		list = mPullListView.getRefreshableView();// 获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);// 设置上下拉刷新滑动的监听事件
		adapter = new Item_centre_page_service_adapter(getActivity());// 实例化适配器
		data = new ArrayList<Centre_page_service>();// 实例化集合对象
		list.setAdapter(adapter);// 设置适配器
		seek = (ImageView) v.findViewById(R.id.imageView_fragment_examine_search);
		seek.setOnClickListener(l);
		adapter.setviewstate();// 将item中的按钮布局隐藏
		
		wait_examine.setTextColor(getResources().getColor(R.color.blue));
		wait_examine.setText(Html.fromHtml("<u>"+"未审批"+"</u>"));
		record_examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
		record_examine.setText("已审批");
		
		
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// mPullListView的上下拉滑动的监听
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (state == false) {
					get_listcomplete();// 获取已审批的维修单的列表
				} else {
					get_listwait();// 获取未审批的维修单的列表
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (state == false) {
					get_listcomplete();// 获取已审批的维修单的列表
				} else {
					get_listwait();// 获取未审批的维修单的列表
				}
			} else {

			}

		}

	};
	// 监听方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textview_fragment_centre_examine_wait_examine:// 审批通过
				setbuttoncoloer(R.id.textview_fragment_centre_examine_wait_examine);
				break;

			case R.id.textview_fragment_centre_examine_record_examine:// 历史审批
				setbuttoncoloer(R.id.textview_fragment_centre_examine_record_examine);
				break;
			case R.id.imageView_fragment_examine_search:// 搜索框的监听事件
				Intent in = new Intent(getActivity(), Centre_search_serviceentryActivity.class);
				in.putExtra("activityname", "examine");
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
		case R.id.textview_fragment_centre_examine_wait_examine:// 等待审批
			
			get_listwait();// 获取未审批的列表
			break;
		case R.id.textview_fragment_centre_examine_record_examine:// 历史审批
			
			get_listcomplete();// 获取已审批的列表
			break;

		default:
			break;
		}
	}

	// 获取等待审批列表的方法
	private void get_listwait() {
		wait_examine.setTextColor(getResources().getColor(R.color.blue));
		wait_examine.setText(Html.fromHtml("<u>"+"未审批"+"</u>"));
		record_examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
		record_examine.setText("已审批");
		state = true;
		String url = Myconstant.CENTRE_GET_UNEXAMINEMONAD;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// System.out.println(response);
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						adapter.getData().clear();
						data.clear();
						adapter.setinitializedata();
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
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态

					} else {
						Toast.makeText(getActivity(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
				Toast.makeText(getActivity(), "请求数据失败", 0).show();
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

	// 获取历史审批列表的方法
	private void get_listcomplete() {
		record_examine.setTextColor(getResources().getColor(R.color.blue));
		record_examine.setText(Html.fromHtml("<u>"+"已审批"+"</u>"));
		wait_examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
		wait_examine.setText("未审批");
		state = false;
		String url = Myconstant.CENTRE_GET_EXAMINEMONAD + "beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();

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
										Myutil.get_delta_t(js2.getString("modifyDate"), js2.getString("createDate")),
										js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
										Myconstant.WEBPAGEPATHURL + jsphone.getString("source")));

							}
						}
						adapter.addDataBottom(data);
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
				Toast.makeText(getActivity(), "请求数据失败", 0).show();
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
