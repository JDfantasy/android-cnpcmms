package com.cnpc.zhibo.app.fragment;

//վ���ε�ά�޲鿴ά�޵��б�Ľ���
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
	private TextView zzwx, lswx;// ����ά�ޡ��ȴ�ά�ޡ���ʷά��
	private RequestQueue mqueQueue;// �������
	private ListView list;// �б����
	private List<Centre_page_service> data;// ���ݼ���
	private ImageView seek;// ����
	// ����ά�޵�adapter���ȴ�ά�޵�adapter����ʷά�޵�adapter
	private Item_centre_page_service_adapter adapter_zzwx, adapter_lswx;
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private boolean number = false;// �����ж��Ƿ����ڽ���ˢ��
	private boolean state = false;// �жϵ�ǰ���б�����ʷά�޻�������ά��
	/*
	 * �����б�
	 */
	private Handler noticehandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					get_listprogress();// �������ݵķ���
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
		get_listprogress();// ��ȡ����ά�޵��б�
		list.setOnItemClickListener(listener);
		SysApplication.getInstance().setListHandler("site-fixorder", noticehandler);// ����ˢ�½�����ṩ�ķ���
		return v;
	}

	// �б�ļ����¼�
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

	// ���ý���ؼ��ķ���
	private void setview(View v) {
		mqueQueue = Volley.newRequestQueue(getActivity());// ��ʼ���������
		zzwx = (TextView) v.findViewById(R.id.textview_fragment_centre_repairs_zzwx);
		lswx = (TextView) v.findViewById(R.id.textview_fragment_centre_repairs_lswx);
		zzwx.setOnClickListener(l);
		lswx.setOnClickListener(l);
		setbuttoncoloer(R.id.textview_fragment_centre_repairs_zzwx);// ���ð�ť����ɫ
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.listView_fragment_repairs);
		list = mPullListView.getRefreshableView();// ��ȡlistview
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�¿���
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��
		mPullListView.setOnRefreshListener(refreshListener);// ����������ˢ�»����ļ����¼�
		adapter_lswx = new Item_centre_page_service_adapter(getActivity());// ��ʷά��
		adapter_zzwx = new Item_centre_page_service_adapter(getActivity());// ����ά��
		data = new ArrayList<Centre_page_service>();
		seek = (ImageView) v.findViewById(R.id.imageView_fragment_centre_repairs_search);
		seek.setOnClickListener(l);
		adapter_lswx.servicestate();
		adapter_zzwx.servicestate();
		zzwx.setTextColor(getResources().getColor(R.color.blue));
		zzwx.setText(Html.fromHtml("<u>" + "ά����" + "</u>"));
		lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
		lswx.setText("��ʷά��");

		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// mPullListView�������������ļ���
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬
				if (state == true) {
					get_listcomplete();// ��ȡ��ʷά�޵�ά�޵����б�
				} else {
					get_listprogress();
					;// ��ȡά���е�ά�޵����б�
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬
				if (state == true) {
					get_listcomplete();// ��ȡ��ʷά�޵�ά�޵����б�
				} else {
					get_listprogress();
					;// ��ȡά���е�ά�޵����б�
				}
			} else {

			}

		}

	};
	// �����ķ���
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textview_fragment_centre_repairs_zzwx:// ��������
				setbuttoncoloer(R.id.textview_fragment_centre_repairs_zzwx);
				break;

			case R.id.textview_fragment_centre_repairs_lswx:// ��ʷ����
				setbuttoncoloer(R.id.textview_fragment_centre_repairs_lswx);
				break;
			case R.id.imageView_fragment_centre_repairs_search:// ������
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

	// �ı䰴ť������ɫ�ķ���
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_fragment_centre_repairs_zzwx:// ����ά��

			get_listprogress();// ��ȡ����ά�޵��б�
			break;

		case R.id.textview_fragment_centre_repairs_lswx:// ��ʷά��

			get_listcomplete();// ��ȡ��ʷά�޵��б�
			break;

		default:
			break;
		}
	}

	// ��ȡ����ά�޵�ά�޵��б�
	private void get_listprogress() {
		zzwx.setTextColor(getResources().getColor(R.color.blue));
		zzwx.setText(Html.fromHtml("<u>" + "ά����" + "</u>"));
		lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
		lswx.setText("��ʷά��");
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
								// û��ͼƬʱ
								data.add(new Centre_page_service(js2.getString("sn"),
										js2// ���
												.getString("createDate"),
										Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
										js2.getString("workStatus"),
										Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
										js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
										""));
							} else {
								// ��ͼƬʱ
								JSONObject jsphone = (JSONObject) repairOrderImages.get(0);

								data.add(new Centre_page_service(js2.getString("sn"),
										js2// ���
												.getString("createDate"),
										Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
										js2.getString("workStatus"),
										Myutil.get_delta_t(Myutil.get_current_time(), js2.getString("createDate")),
										js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
										Myconstant.WEBPAGEPATHURL + jsphone.getString("source")));

							}
						}
						adapter_zzwx.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
						mPullListView.onPullUpRefreshComplete();
						number = false;// �ı��Ƿ�ˢ�µ�״̬
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
				Toast.makeText(getActivity(), "���Ѿ����ߣ������µ�¼" + "", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

		};
		mqueQueue.add(re);
	}

	// ��ȡ��ʷά�޵��б�
	private void get_listcomplete() {
		lswx.setTextColor(getResources().getColor(R.color.blue));
		lswx.setText(Html.fromHtml("<u>" + "��ʷά��" + "</u>"));
		zzwx.setTextColor(getResources().getColor(R.color.zitiyanse5));
		zzwx.setText("ά����");
		state = true;
		String url = Myconstant.CENTRE_GET_HISTORYREPAIRED_ENTRY + "beginDate=" + Myutil.get_month_time() + "&endDate="
				+ Myutil.get_time_ymd();
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// ��Ӧ������
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
								// û��ͼƬʱ
								data.add(new Centre_page_service(js2.getString("sn"),
										js2// ���
												.getString("createDate"),
										Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
										js2.getString("workStatus"),
										Myutil.get_delta_t(js2.getString("modifyDate"), js2.getString("createDate")),
										js2.getString("id"), js2.getString("workStatus"), js4.getString("username"), "",
										js2.getString("score")));
							} else {
								// ��ͼƬʱ
								JSONObject jsphone = (JSONObject) repairOrderImages.get(0);

								data.add(new Centre_page_service(js2.getString("sn"),
										js2// ���
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
						mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
						mPullListView.onPullUpRefreshComplete();
						number = false;// �ı��Ƿ�ˢ�µ�״̬
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
				Toast.makeText(getActivity(), "���Ѿ����ߣ������µ�¼", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;

			}

		};
		mqueQueue.add(re);

	}

}
