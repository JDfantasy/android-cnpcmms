package com.cnpc.zhibo.app.fragment;

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
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.Supplier_baoxiaodanxiangqingActivity;
import com.cnpc.zhibo.app.Supplier_search_baoxiaoActivity;
import com.cnpc.zhibo.app.adapter.Item_supplier_baoxiao_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_page_baoxiao;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 供应商的报销的界面
 */
public class Fragment_supplier_reimbursement extends Fragment {
	private PullToRefreshListView mPullListView;
	private ListView listView;
	private ImageView search;
	private TextView yi_bao_xiao, wei_bao_xiao;
	private Item_supplier_baoxiao_adapter adapter_yi_bao_xiao, adapter_wei_bao_xiao;
	private List<Supplier_page_baoxiao> data;
	private boolean ischecked = false;
	private boolean number = false;
	private final MyHandler handler = new MyHandler(this);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_supplier_reimbursement, container, false);
		setview(v);
		get_reimbursemented();
		SysApplication.getInstance().setListHandler("provider-expenseorder", handler);
		return v;
	}

	/**
	 * 初始化控件
	 */
	private void setview(View v) {
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.listView_fragment1_reimbursement);
		listView = mPullListView.getRefreshableView();
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间
		mPullListView.setOnRefreshListener(refreshListener);
		yi_bao_xiao = (TextView) v.findViewById(R.id.textview_fragment_supplier_yi_bao_xiao);
		wei_bao_xiao = (TextView) v.findViewById(R.id.textview_fragment_supplier_wei_bao_xiao);
		search = (ImageView) v.findViewById(R.id.imageView_fragment_supplier_reimburment_search);
		search.setOnClickListener(l);
		yi_bao_xiao.setTextColor(getResources().getColor(R.color.blue));
		yi_bao_xiao.setText(Html.fromHtml("<u>" + "已报销" + "</u>"));
		wei_bao_xiao.setTextColor(getResources().getColor(R.color.zitiyanse5));
		wei_bao_xiao.setText("未报销");
		yi_bao_xiao.setOnClickListener(l);
		wei_bao_xiao.setOnClickListener(l);
		listView.setOnItemClickListener(listener);
		listView.setDividerHeight(0);
		listView.setSelector(new BitmapDrawable());
		data = new ArrayList<Supplier_page_baoxiao>();
		adapter_yi_bao_xiao = new Item_supplier_baoxiao_adapter(getActivity());
		adapter_wei_bao_xiao = new Item_supplier_baoxiao_adapter(getActivity());
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textview_fragment_supplier_yi_bao_xiao:
				if (ischecked == false) {
					return;
				}
				setbuttoncoloer(R.id.textview_fragment_supplier_yi_bao_xiao);
				ischecked = false;
				break;
			case R.id.textview_fragment_supplier_wei_bao_xiao:
				if (ischecked == true) {
					return;
				}
				setbuttoncoloer(R.id.textview_fragment_supplier_wei_bao_xiao);
				ischecked = true;
				break;
			case R.id.imageView_fragment_supplier_reimburment_search:
				if (ischecked == false) {
					startActivity(new Intent(getActivity(), Supplier_search_baoxiaoActivity.class).putExtra("tag", 1));
				} else if (ischecked == true) {
					startActivity(new Intent(getActivity(), Supplier_search_baoxiaoActivity.class).putExtra("tag", 2));
				}
				break;
			default:
				break;
			}

		}
	};

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (ischecked == false) {
				startActivity(new Intent(getActivity(), Supplier_baoxiaodanxiangqingActivity.class)
						.putExtra("bao_xiao_dan_id", adapter_yi_bao_xiao.getData().get(position).id)
						.putExtra("state", adapter_yi_bao_xiao.getData().get(position).state));
				Myutil.set_activity_open(getActivity());
			} else if (ischecked == true) {
				startActivity(new Intent(getActivity(), Supplier_baoxiaodanxiangqingActivity.class)
						.putExtra("bao_xiao_dan_id", adapter_wei_bao_xiao.getData().get(position).id)
						.putExtra("state", adapter_wei_bao_xiao.getData().get(position).state));
				Myutil.set_activity_open(getActivity());
			}
		}
	};

	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// 改变刷新的状态
				if (ischecked == false) {
					get_reimbursemented();
				} else {
					get_reimbursement();
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (ischecked == false) {
				get_reimbursemented();
			} else {
				get_reimbursement();
			}

		}

	};

	// 改变按钮字体颜色的方法
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_fragment_supplier_yi_bao_xiao:// 已报销
			yi_bao_xiao.setTextColor(getResources().getColor(R.color.blue));
			yi_bao_xiao.setText(Html.fromHtml("<u>" + "已报销" + "</u>"));
			wei_bao_xiao.setTextColor(getResources().getColor(R.color.zitiyanse5));
			wei_bao_xiao.setText("未报销");
			get_reimbursemented();
			break;
		case R.id.textview_fragment_supplier_wei_bao_xiao:// 未报销
			yi_bao_xiao.setTextColor(getResources().getColor(R.color.zitiyanse5));
			wei_bao_xiao.setTextColor(getResources().getColor(R.color.blue));
			wei_bao_xiao.setText(Html.fromHtml("<u>" + "未报销" + "</u>"));
			yi_bao_xiao.setText("已报销");
			get_reimbursement();
			break;

		default:
			break;
		}
	}

	/**
	 * 加载已处理的保修单数据
	 */
	private void get_reimbursemented() {
		String url = Myconstant.SUPPLIER_REIMBURSEMENTED + "?beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();

		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						listView.setAdapter(adapter_yi_bao_xiao);
						adapter_wei_bao_xiao.getData().clear();
						adapter_yi_bao_xiao.getData().clear();
						data.clear();
						adapter_yi_bao_xiao.setattrs();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj4 = obj3.getJSONObject("fixOrder");
							JSONObject obj5 = obj4.getJSONObject("repairOrder");
							JSONObject obj6 = obj5.getJSONObject("station");
							JSONObject obj7 = obj5.getJSONObject("project");
							JSONArray obj8 = obj5.getJSONArray("repairOrderImages");// 问题图片
							if (obj8.length() == 0) {
								data.add(new Supplier_page_baoxiao(obj3.getString("id"), obj3.getString("sn"),
										obj6.getString("name"), obj5.getString("name"), obj7.getString("name"),
										obj3.getString("createDate"), "", obj3.getString("status"),
										obj3.getString("amount")));
							} else {
								JSONObject obj9 = (JSONObject) obj8.get(0);
								data.add(new Supplier_page_baoxiao(obj3.getString("id"), obj3.getString("sn"),
										obj6.getString("name"), obj5.getString("name"), obj7.getString("name"),
										obj3.getString("createDate"),
										Myconstant.WEBPAGEPATHURL + obj9.getString("source"), obj3.getString("status"),
										obj3.getString("amount")));
							}

						}
						if (obj2.length() > 0) {
							Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(), "已经没有已经处理的报销单了", Toast.LENGTH_SHORT).show();
						}
						adapter_yi_bao_xiao.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态

					} else {
						Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), "没有加载到已处理的报销单列表，请稍后再试...", Toast.LENGTH_SHORT).show();

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
	 * 加载未处理的报销单的数据
	 */
	private void get_reimbursement() {
		String url = Myconstant.SUPPLIER_REIMBURSEMENT;

		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						listView.setAdapter(adapter_wei_bao_xiao);
						adapter_yi_bao_xiao.getData().clear();
						adapter_wei_bao_xiao.getData().clear();
						data.clear();
						adapter_wei_bao_xiao.setattrs();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj4 = obj3.getJSONObject("fixOrder");
							JSONObject obj5 = obj4.getJSONObject("repairOrder");
							JSONObject obj6 = obj5.getJSONObject("station");
							JSONObject obj7 = obj5.getJSONObject("project");
							JSONArray obj8 = obj5.getJSONArray("repairOrderImages");// 问题图片
							if (obj8.length() == 0) {
								data.add(new Supplier_page_baoxiao(obj3.getString("id"), obj3.getString("sn"),
										obj6.getString("name"), obj5.getString("name"), obj7.getString("name"),
										obj3.getString("createDate"), "", obj3.getString("status"),
										obj3.getString("amount")));
							} else {
								JSONObject obj9 = (JSONObject) obj8.get(0);
								data.add(new Supplier_page_baoxiao(obj3.getString("id"), obj3.getString("sn"),
										obj6.getString("name"), obj5.getString("name"), obj7.getString("name"),
										obj3.getString("createDate"),
										Myconstant.WEBPAGEPATHURL + obj9.getString("source"), obj3.getString("status"),
										obj3.getString("amount")));
							}
						}
						if (obj2.length() > 0) {
							Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(), "已经没有要处理的报销单了", Toast.LENGTH_SHORT).show();
						}
						adapter_wei_bao_xiao.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// 关闭刷新
						mPullListView.onPullUpRefreshComplete();
						number = false;// 改变是否刷新的状态
					} else {
						Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), "没有加载到未处理的报销单列表，请稍后再试...", Toast.LENGTH_LONG).show();

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
		private final WeakReference<Fragment_supplier_reimbursement> mFragment;

		public MyHandler(Fragment_supplier_reimbursement fragmentActivity) {
			mFragment = new WeakReference<Fragment_supplier_reimbursement>(fragmentActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			Fragment_supplier_reimbursement fragment = mFragment.get();
			if (fragment != null) {
				switch (msg.what) {
				case 0:
					try {
						fragment.get_reimbursement();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 100:
					if (fragment.ischecked == true) {
						fragment.get_reimbursement();
					}
				default:
					break;
				}
			}
		}
	}
}
