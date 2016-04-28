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
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.cnpc.zhibo.app.adapter.Item_supplier_page_weixiudanguanli_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_page_weixiudanguanli;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 供应商的维修单管理界面
 */
public class Supplier_weixiudanguanliActivity extends MyActivity {
	private ImageView fanhui, search;// 返回，搜索
	private TextView zzwx, ddwx, lswx;
	// private ListView listView;
	private PullToRefreshListView mPullListView;
	private ListView listView;
	// private RequestQueue queue;
	private Item_supplier_page_weixiudanguanli_adapter adapter;
	private List<Supplier_page_weixiudanguanli> data;
	private boolean ischecked = false;
	private boolean number = false;// 用来判断是否正在进行刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_weixiudanguanli);
		set_title_text("维修单");
		setview();
		get_listprogress();

	}

	/**
	 * 初始化控件
	 */
	private void setview() {
		search = (ImageView) findViewById(R.id.imageView_fragment_supplier_repairs_search);
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		zzwx = (TextView) findViewById(R.id.textview_supplier_repairs_zzwx);
		// ddwx = (TextView) findViewById(R.id.textview_supplier_repairs_ddwx);
		lswx = (TextView) findViewById(R.id.textview_supplier_repairs_lswx);
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_supplier_repairs);
		listView = mPullListView.getRefreshableView();// 获取listView
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		zzwx.setOnClickListener(l);
		// ddwx.setOnClickListener(l);
		lswx.setOnClickListener(l);
		search.setOnClickListener(l);
		listView.setOnItemClickListener(listener);
		zzwx.setTextColor(getResources().getColor(R.color.zitiyanse1));

		data = new ArrayList<Supplier_page_weixiudanguanli>();
		adapter = new Item_supplier_page_weixiudanguanli_adapter(this);
		listView.setAdapter(adapter);

	}

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(Supplier_weixiudanguanliActivity.this,
					Supplier_weixiudanxiangqingActivity.class);
			intent.putExtra("weixiudan_id", adapter.getData().get(position).id);
			startActivity(intent);
			Myutil.set_activity_open(Supplier_weixiudanguanliActivity.this);

		}
	};

	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (ischecked == false) {
					get_listprogress();
				} else {
					get_listhitory();
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (ischecked == false) {
				get_listprogress();
			} else {
				get_listhitory();
			}

		}

	};

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_weixiudanguanliActivity.this);
				break;

			case R.id.textview_supplier_repairs_zzwx:
				if (ischecked == false) {
					return;
				}
				setbuttoncoloer(R.id.textview_supplier_repairs_zzwx);
				adapter.setdate(data);
				ischecked = false;
				break;
			case R.id.textview_supplier_repairs_lswx:
				if (ischecked == true) {
					return;
				}
				setbuttoncoloer(R.id.textview_supplier_repairs_lswx);
				ischecked = true;
				break;

			case R.id.imageView_fragment_supplier_repairs_search:
				Intent intent = new Intent(Supplier_weixiudanguanliActivity.this,
						Supplier_search_weixiudanActivity.class);
				intent.putExtra("tag", 1);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};

	// 改变按钮字体颜色的方法
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_supplier_repairs_zzwx:// 正在审批
			zzwx.setTextColor(getResources().getColor(R.color.zitiyanse1));
			// ddwx.setTextColor(getResources().getColor(R.color.baise));
			lswx.setTextColor(getResources().getColor(R.color.baise));
			get_listprogress();// 获取正在审批的列表
			break;
		// case R.id.textview_supplier_repairs_ddwx:// 等待审批
		// zzwx.setTextColor(getResources().getColor(R.color.baise));
		// ddwx.setTextColor(getResources().getColor(R.color.zitiyanse1));
		// lswx.setTextColor(getResources().getColor(R.color.baise));
		// // get_listwait();// 获取等待审批的列表
		// break;
		case R.id.textview_supplier_repairs_lswx:// 历史审批
			zzwx.setTextColor(getResources().getColor(R.color.baise));
			// ddwx.setTextColor(getResources().getColor(R.color.baise));
			lswx.setTextColor(getResources().getColor(R.color.zitiyanse1));
			get_listhitory();// 获取历史审批的列表
			break;

		default:
			break;
		}
	}

	/**
	 * 运营商的获所有历史维修单的方法
	 */
	private void get_listhitory() {
		String url = Myconstant.SUPPLIER_HISTORY_REPAIR + "?beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();
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
							JSONObject obj5 = obj3.getJSONObject("repairOrder");

							JSONObject obj7 = obj5.getJSONObject("project");// 保修项目
							JSONObject obj4 = obj5.getJSONObject("station");// 加油站信息
							JSONArray obj6 = obj5.getJSONArray("repairOrderImages");// 问题图片
							if (obj6.length() == 0) {
								data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"), obj3.getString("sn"),
										obj4.getString("name"), obj5.getString("name"), obj3.getString("completeDate"),
										obj7.getString("name"), "", obj3.getString("status"),obj3.getString("workStatus"),obj3.getString("score")));
							} else {
								JSONObject obj8 = (JSONObject) obj6.get(0);
								data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"), obj3.getString("sn"),
										obj4.getString("name"), obj5.getString("name"), obj3.getString("completeDate"),
										obj7.getString("name"), Myconstant.WEBPAGEPATHURL + obj8.getString("source"),
										obj3.getString("status"),obj3.getString("workStatus"),obj3.getString("score")));
							}

						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_weixiudanguanliActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_weixiudanguanliActivity.this, "服务器没有维修中的保修单", Toast.LENGTH_SHORT)
									.show();
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Supplier_weixiudanguanliActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Supplier_weixiudanguanliActivity.this, "没有加载到历史维修单，请稍后再试...", Toast.LENGTH_LONG).show();

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
	 * 运营商的获取维修中的所有维修单的方法
	 */
	private void get_listprogress() {
		String url = Myconstant.SUPPLIER_REPAIRING;

		StringRequest re1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						adapter.getData().clear();
						data.clear();
						adapter.notifyDataSetChanged();

						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj5 = obj3.getJSONObject("repairOrder");

							JSONObject obj7 = obj5.getJSONObject("project");// 保修项目
							JSONObject obj4 = obj5.getJSONObject("station");// 加油站信息
							JSONArray obj6 = obj5.getJSONArray("repairOrderImages");// 问题图片
							if (obj6.length() == 0) {
								data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"), obj3.getString("sn"),
										obj4.getString("name"), obj5.getString("name"), obj3.getString("completeDate"),
										obj7.getString("name"), "", obj3.getString("status"),obj3.getString("workStatus"),obj3.getString("score")));
							} else {
								JSONObject obj8 = (JSONObject) obj6.get(0);
								data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"), obj3.getString("sn"),
										obj4.getString("name"), obj5.getString("name"), obj3.getString("completeDate"),
										obj7.getString("name"), Myconstant.WEBPAGEPATHURL + obj8.getString("source"),
										obj3.getString("status"),obj3.getString("workStatus"),obj3.getString("score")));
							}

						}
						if (obj2.length() > 0) {
							Toast.makeText(Supplier_weixiudanguanliActivity.this, obj1.getString("content"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(Supplier_weixiudanguanliActivity.this, "服务器没有维修中的保修单", Toast.LENGTH_SHORT)
									.show();
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(Supplier_weixiudanguanliActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Supplier_weixiudanguanliActivity.this, "没有加载到维修中的维修单，请稍后再试...", Toast.LENGTH_LONG)
						.show();

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
		SysApplication.getHttpQueues().add(re1);
	}
	// private TextView zzwx, ddwx, lswx;// 正在维修、等待维修、历史维修
//		private ImageView search;
//		private PullToRefreshListView mPullListView;
//		private ListView listView;
//		private Item_supplier_page_baoxiudan_adapter adapter;
//		private List<Supplier_page_baoxiudanguanli> data;
//		private boolean number = false;// 用来判断是否正在进行刷新
	//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			View v = inflater.inflate(R.layout.fragment_supplier_repairs, container, false);
//			setview(v);
//			get_assigned_warranty();
//			return v;
//		}
	//
//		@Override
//		public void onResume() {
//			get_assigned_warranty();
//			super.onResume();
	//
//		}
	//
//		/**
//		 * 初始化控件
//		 */
//		private void setview(View v) {
//			// zzwx = (TextView)
//			// v.findViewById(R.id.textview_fragment_supplier_repairs_zzwx);
//			// ddwx = (TextView) v
//			// .findViewById(R.id.textview_fragment_supplier_repairs_ddwx);
//			// lswx = (TextView)
//			// v.findViewById(R.id.textview_fragment_supplier_repairs_lswx);
//			mPullListView = (PullToRefreshListView) v.findViewById(R.id.listView_fragment2_repairs);
//			listView = mPullListView.getRefreshableView();// 获取listView
//			mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
//			mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
//			data = new ArrayList<Supplier_page_baoxiudanguanli>();
//			search =  (ImageView) v.findViewById(R.id.imageView_fragment_supplier_repairs_search1);
//			search.setOnClickListener(l);
//			// zzwx.setOnClickListener(l);
//			// // ddwx.setOnClickListener(l);
//			// lswx.setOnClickListener(l);
//			listView.setOnItemClickListener(listener);
//			mPullListView.setOnRefreshListener(refreshListener);
//			// zzwx.setTextColor(getResources().getColor(R.color.zitiyanse1));
//			adapter = new Item_supplier_page_baoxiudan_adapter(getActivity());
//			listView.setAdapter(adapter);
	//
//		}
	//
//		private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {
	//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//				if (number == false) {
//					number = true;// 改变刷新的状态
	//
//					get_assigned_warranty();
//				}
//			}
	//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//				get_assigned_warranty();
//			}
	//
//		};
	//
//		private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
	//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Intent intent = new Intent(getActivity(), Supplier_fenpeixiangqingActivity.class);
//				intent.putExtra("baoxiudan_id", adapter.getData().get(position).id);
//				intent.putExtra("tag", 1);
//				startActivity(intent);
//				Myutil.set_activity_open(getActivity());
	//
//			}
//		};
	//
//		private View.OnClickListener l = new View.OnClickListener() {
	//
//			@Override
//			public void onClick(View v) {
//				switch (v.getId()) {
//				// case R.id.textview_fragment_supplier_repairs_zzwx:
//				// setbuttoncoloer(R.id.textview_fragment_supplier_repairs_zzwx);
//				// adapter.setdate(data);
//				// break;
//				// // case R.id.textview_fragment_supplier_repairs_ddwx:
//				// // setbuttoncoloer(R.id.textview_fragment_supplier_repairs_ddwx);
//				// // adapter.setdate(data1);
//				// // break;
//				// case R.id.textview_fragment_supplier_repairs_lswx:
//				// setbuttoncoloer(R.id.textview_fragment_supplier_repairs_lswx);
//				//
//				// break;
//				case R.id.imageView_fragment_supplier_repairs_search1:
//					Intent intent = new Intent(getActivity(), Supplier_search_weixiudanActivity.class);
//					intent.putExtra("tag", 2);
//					startActivity(intent);
	//
//					break;
//				default:
//					break;
//				}
//			}
//		};
	//
//		// // 改变按钮字体颜色的方法
//		// private void setbuttoncoloer(int id) {
//		// switch (id) {
//		// case R.id.textview_fragment_supplier_repairs_zzwx:// 正在审批
//		// zzwx.setTextColor(getResources().getColor(R.color.zitiyanse1));
//		// // ddwx.setTextColor(getResources().getColor(R.color.baise));
//		// lswx.setTextColor(getResources().getColor(R.color.baise));
//		// // get_listprogress();// 获取正在审批的列表
//		// break;
//		// // case R.id.textview_fragment_supplier_repairs_ddwx:// 等待审批
//		// // zzwx.setTextColor(getResources().getColor(R.color.baise));
//		// // ddwx.setTextColor(getResources().getColor(R.color.zitiyanse1));
//		// // lswx.setTextColor(getResources().getColor(R.color.baise));
//		// // // get_listwait();// 获取等待审批的列表
//		// // break;
//		// case R.id.textview_fragment_supplier_repairs_lswx:// 历史审批
//		// zzwx.setTextColor(getResources().getColor(R.color.baise));
//		// // ddwx.setTextColor(getResources().getColor(R.color.baise));
//		// lswx.setTextColor(getResources().getColor(R.color.zitiyanse1));
//		// // get_listcomplete();// 获取历史审批的列表
//		// break;
//		//
//		// default:
//		// break;
//		// }
//		// }
	//
//		/**
//		 * 获得数据
//		 */
//		private void get_assigned_warranty() {
//			String url = Myconstant.SUPPLIER_ASSIGNED_WARRANTY + "?beginDate=" + Myutil.get_month_time() + "&endDate="
//					+ Myutil.get_time_ymd();
//			StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
	//
//				@Override
//				public void onResponse(String response) {
	//
//					try {
//						JSONObject obj = new JSONObject(response);
//						JSONObject obj1 = obj.getJSONObject("response");
//						if (obj1.getString("type").equals("success")) {
//							listView.setAdapter(adapter);
//							adapter.getData().clear();
//							data.clear();
//							JSONArray obj2 = obj.getJSONArray("body");
//							for (int i = 0; i < obj2.length(); i++) {
//								JSONObject obj3 = obj2.getJSONObject(i);
//								JSONObject obj4 = obj3.getJSONObject("station");// 加油站信息
//								// JSONObject obj5 = obj3.getJSONObject("fixter");//
//								// 维修员信息
//								JSONObject obj6 = obj3.getJSONObject("project");// 项目内容
//								data.add(new Supplier_page_baoxiudanguanli(obj3.getString("id"), obj3.getString("sn"),
//										obj4.getString("name"), obj3.getString("name"), obj6.getString("name"),
//										obj3.getString("createDate"), "", obj3.getString("takeStatus")));
//							}if(obj2.length()>0){
//								Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
//							}else{
//								Toast.makeText(getActivity(), "没有查到保修单列表", Toast.LENGTH_SHORT).show();
//							}
//							adapter.addDataBottom(data);
//							mPullListView.onPullDownRefreshComplete();// 关闭刷新
//							mPullListView.onPullUpRefreshComplete();
//							number = false;// 改变是否刷新的状态
//						} else {
//							Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
	//
//				}
//			}, new Response.ErrorListener() {
	//
//				@Override
//				public void onErrorResponse(VolleyError error) {
//					Toast.makeText(getActivity(), "网络出问题了，请稍后再试...", Toast.LENGTH_LONG).show();
	//
//				}
//			}) {
//				@Override
//				public Map<String, String> getHeaders() throws AuthFailureError {
//					Map<String, String> headers = new HashMap<String, String>();
//					headers.put("accept", "application/json");
//					headers.put("api_key", Myconstant.token);
//					return headers;
//				}
//			};
//			SysApplication.getHttpQueues().add(re);
//		}
}
