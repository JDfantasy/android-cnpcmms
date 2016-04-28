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
import com.cnpc.zhibo.app.Supplier_fenpeixiangqingActivity;
import com.cnpc.zhibo.app.Supplier_search_weixiudanActivity;
import com.cnpc.zhibo.app.Supplier_weixiudanguanliActivity;
import com.cnpc.zhibo.app.Supplier_weixiudanxiangqingActivity;
import com.cnpc.zhibo.app.adapter.Item_supplier_page_baoxiudan_adapter;
import com.cnpc.zhibo.app.adapter.Item_supplier_page_weixiudanguanli_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_page_baoxiudanguanli;
import com.cnpc.zhibo.app.entity.Supplier_page_weixiudanguanli;
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
 * ��Ӧ�̶�ά�޲鿴���޵��б�
 */
public class Fragment_supplier_repairs extends Fragment {

	private ImageView fanhui, search;// ���أ�����
	private TextView zzwx, ddwx, lswx;
	private PullToRefreshListView mPullListView;
	private ListView listView;
	private Item_supplier_page_weixiudanguanli_adapter adapter_zzwx, adapter_lswx;
	private List<Supplier_page_weixiudanguanli> data;
	private boolean ischecked = false;
	private boolean number = false;// �����ж��Ƿ����ڽ���ˢ��
	private final MyHandler handler = new MyHandler(this);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_supplier_weixiudanguanli, container, false);
		setview(v);
		get_listprogress();
		SysApplication.getInstance().setListHandler("provider-fixorder", handler);
		return v;
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setview(View v) {
		search = (ImageView) v.findViewById(R.id.imageView_fragment_supplier_repairs_search);
		fanhui = (ImageView) v.findViewById(R.id.imageView_myacitity_zuo);
		zzwx = (TextView) v.findViewById(R.id.textview_supplier_repairs_zzwx);
		lswx = (TextView) v.findViewById(R.id.textview_supplier_repairs_lswx);
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.listView_supplier_repairs);
		listView = mPullListView.getRefreshableView();// ��ȡlistView
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�²�����
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��
		mPullListView.setOnRefreshListener(refreshListener);
		zzwx.setOnClickListener(l);
		// ddwx.setOnClickListener(l);
		lswx.setOnClickListener(l);
		search.setOnClickListener(l);
		listView.setOnItemClickListener(listener);
		listView.setDividerHeight(0);
		listView.setSelector(new BitmapDrawable());
		zzwx.setTextColor(getResources().getColor(R.color.blue));
		zzwx.setText(Html.fromHtml("<u>" + "ά����" + "</u>"));
		lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
		lswx.setText("��ʷά��");
		data = new ArrayList<Supplier_page_weixiudanguanli>();
		adapter_zzwx = new Item_supplier_page_weixiudanguanli_adapter(getActivity());
		adapter_lswx = new Item_supplier_page_weixiudanguanli_adapter(getActivity());

	}

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity(), Supplier_weixiudanxiangqingActivity.class);
			if (ischecked == false) {
				intent.putExtra("weixiudan_id", adapter_zzwx.getData().get(position).id);
			} else if (ischecked == true) {
				intent.putExtra("weixiudan_id", adapter_lswx.getData().get(position).id);
			}
			startActivity(intent);
			Myutil.set_activity_open(getActivity());

		}
	};

	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬
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
			case R.id.textview_supplier_repairs_zzwx:
				if (ischecked == false) {
					return;
				}
				setbuttoncoloer(R.id.textview_supplier_repairs_zzwx);
				// adapter_zzwx.setdate(data);
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
				Intent intent = new Intent(getActivity(), Supplier_search_weixiudanActivity.class);
				intent.putExtra("tag", 1);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};

	// �ı䰴ť������ɫ�ķ���
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_supplier_repairs_zzwx:// ��������
			zzwx.setTextColor(getResources().getColor(R.color.blue));
			// ddwx.setTextColor(getResources().getColor(R.color.baise));
			lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
			zzwx.setText(Html.fromHtml("<u>" + "ά����" + "</u>"));
			lswx.setText("��ʷά��");
			get_listprogress();// ��ȡ�����������б�
			break;
		// case R.id.textview_supplier_repairs_ddwx:// �ȴ�����
		// zzwx.setTextColor(getResources().getColor(R.color.baise));
		// ddwx.setTextColor(getResources().getColor(R.color.zitiyanse1));
		// lswx.setTextColor(getResources().getColor(R.color.baise));
		// // get_listwait();// ��ȡ�ȴ��������б�
		// break;
		case R.id.textview_supplier_repairs_lswx:// ��ʷ����
			zzwx.setTextColor(getResources().getColor(R.color.zitiyanse5));
			// ddwx.setTextColor(getResources().getColor(R.color.baise));
			lswx.setTextColor(getResources().getColor(R.color.blue));
			lswx.setText(Html.fromHtml("<u>" + "��ʷά��" + "</u>"));
			zzwx.setText("ά����");
			get_listhitory();// ��ȡ��ʷ�������б�
			break;

		default:
			break;
		}
	}

	/**
	 * ��Ӫ�̵Ļ�������ʷά�޵��ķ���
	 */
	private void get_listhitory() {
		if (isAdded()) {
			zzwx.setTextColor(getResources().getColor(R.color.zitiyanse5));
			lswx.setTextColor(getResources().getColor(R.color.blue));
			lswx.setText(Html.fromHtml("<u>" + "��ʷά��" + "</u>"));
			zzwx.setText("ά����");
			String url = Myconstant.SUPPLIER_HISTORY_REPAIR + "?beginDate=" + Myutil.get_month_time() + "&endDate="
					+ Myutil.get_time_ymd();
			StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {

					try {
						JSONObject obj = new JSONObject(response);
						JSONObject obj1 = obj.getJSONObject("response");
						if (obj1.getString("type").equals("success")) {
							listView.setAdapter(adapter_lswx);
							adapter_zzwx.getData().clear();
							adapter_lswx.getData().clear();
							data.clear();
							adapter_lswx.setattrs();
							JSONArray obj2 = obj.getJSONArray("body");
							for (int i = 0; i < obj2.length(); i++) {
								JSONObject obj3 = obj2.getJSONObject(i);
								JSONObject obj5 = obj3.getJSONObject("repairOrder");

								JSONObject obj7 = obj5.getJSONObject("project");// ������Ŀ
								JSONObject obj4 = obj5.getJSONObject("station");// ����վ��Ϣ
								JSONArray obj6 = obj5.getJSONArray("repairOrderImages");// ����ͼƬ
								if (obj6.length() == 0) {
									data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"),
											obj3.getString("sn"), obj4.getString("name"), obj5.getString("name"),
											obj3.getString("completeDate"), obj7.getString("name"), "",
											obj3.getString("status"), obj3.getString("workStatus"),
											obj3.getString("score")));
								} else {
									JSONObject obj8 = (JSONObject) obj6.get(0);
									data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"),
											obj3.getString("sn"), obj4.getString("name"), obj5.getString("name"),
											obj3.getString("completeDate"), obj7.getString("name"),
											Myconstant.WEBPAGEPATHURL + obj8.getString("source"),
											obj3.getString("status"), obj3.getString("workStatus"),
											obj3.getString("score")));
								}

							}
							if (obj2.length() > 0) {
								Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getActivity(), "������û��ά���еı��޵�", Toast.LENGTH_SHORT).show();
							}
							adapter_lswx.addDataBottom(data);
							mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
							mPullListView.onPullUpRefreshComplete();
							number = false;// �ı��Ƿ�ˢ�µ�״̬
						} else {
							Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getActivity(), "û�м��ص���ʷά�޵������Ժ�����...", Toast.LENGTH_LONG).show();

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

	/**
	 * ��Ӫ�̵Ļ�ȡά���е�����ά�޵��ķ���
	 */
	private void get_listprogress() {
		zzwx.setTextColor(getResources().getColor(R.color.blue));
		zzwx.setText(Html.fromHtml("<u>" + "ά����" + "</u>"));
		lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
		lswx.setText("��ʷά��");
		String url = Myconstant.SUPPLIER_REPAIRING;

		StringRequest re1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						listView.setAdapter(adapter_zzwx);
						adapter_lswx.getData().clear();
						adapter_zzwx.getData().clear();
						data.clear();
						adapter_zzwx.setattrs();
						JSONArray obj2 = obj.getJSONArray("body");
						for (int i = 0; i < obj2.length(); i++) {
							JSONObject obj3 = obj2.getJSONObject(i);
							JSONObject obj5 = obj3.getJSONObject("repairOrder");

							JSONObject obj7 = obj5.getJSONObject("project");// ������Ŀ
							JSONObject obj4 = obj5.getJSONObject("station");// ����վ��Ϣ
							JSONArray obj6 = obj5.getJSONArray("repairOrderImages");// ����ͼƬ
							if (obj6.length() == 0) {
								data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"), obj3.getString("sn"),
										obj4.getString("name"), obj5.getString("name"), obj3.getString("modifyDate"),
										obj7.getString("name"), "", obj3.getString("status"),
										obj3.getString("workStatus"), obj3.getString("score")));
							} else {
								JSONObject obj8 = (JSONObject) obj6.get(0);
								data.add(new Supplier_page_weixiudanguanli(obj3.getString("id"), obj3.getString("sn"),
										obj4.getString("name"), obj5.getString("name"), obj3.getString("modifyDate"),
										obj7.getString("name"), Myconstant.WEBPAGEPATHURL + obj8.getString("source"),
										obj3.getString("status"), obj3.getString("workStatus"),
										obj3.getString("score")));
							}

						}
						if (obj2.length() > 0) {
							Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(), "������û��ά���еı��޵�", Toast.LENGTH_SHORT).show();
						}
						adapter_zzwx.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
						mPullListView.onPullUpRefreshComplete();
						number = false;// �ı��Ƿ�ˢ�µ�״̬
					} else {
						Toast.makeText(getActivity(), obj1.getString("content"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity(), "û�м��ص�ά���е�ά�޵������Ժ�����...", Toast.LENGTH_LONG).show();

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

	/**
	 * ��handler��Ϊ�˱����ڴ�й¶
	 *
	 */
	private static class MyHandler extends Handler {
		private final WeakReference<Fragment_supplier_repairs> mFragment;

		public MyHandler(Fragment_supplier_repairs fragmentActivity) {
			mFragment = new WeakReference<Fragment_supplier_repairs>(fragmentActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			Fragment_supplier_repairs fragment = mFragment.get();
			if (fragment != null) {
				switch (msg.what) {
				case 0:
					try {
						fragment.get_listprogress();
						fragment.ischecked = false;
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		}
	}
}
