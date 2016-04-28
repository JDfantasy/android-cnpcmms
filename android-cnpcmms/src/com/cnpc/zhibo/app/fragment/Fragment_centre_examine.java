package com.cnpc.zhibo.app.fragment;

//վ���ε������鿴����
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
	private TextView wait_examine, record_examine;// �ȴ��������ȴ���������ʷ����
	private RequestQueue mqueQueue;// �������
	private ListView list;// �б����
	private Item_centre_page_service_adapter adapter;
	private List<Centre_page_service> data;// ���ݼ���
	private ImageView seek;// ����
	private boolean state = false;// �����жϵ�ǰ�б��ʼ������������δ����
	private boolean number = false;// �����ж��Ƿ����ڽ���ˢ��

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_centre_examine, container, false);
		setview(v);// ���ý���ķ���
		get_listwait();// ��ȡ�����������б�
		list.setOnItemClickListener(listener);
		SysApplication.getInstance().setListHandler("site-approveorder", noticehandler);
		return v;
	}
	/*
	 * �����б�
	 */
	private Handler noticehandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					get_listwait();// �������ݵķ���
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
			default:
				break;
			}
		};
	};
	// �б�ļ����¼�
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

	// ���ý���ķ���
	private void setview(View v) {
		mqueQueue = Volley.newRequestQueue(getActivity());// ��ʼ���������
		wait_examine = (TextView) v.findViewById(R.id.textview_fragment_centre_examine_wait_examine);// �ȴ�����

		record_examine = (TextView) v.findViewById(R.id.textview_fragment_centre_examine_record_examine);// ��ʷ����
		wait_examine.setOnClickListener(l);
		record_examine.setOnClickListener(l);
		setbuttoncoloer(R.id.textview_fragment_centre_examine_wait_examine);// ���ð�ť����ɫ
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.listView_fragment_examine);// ʵ����listview
		list = mPullListView.getRefreshableView();// ��ȡlistview
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�¿���
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��
		mPullListView.setOnRefreshListener(refreshListener);// ����������ˢ�»����ļ����¼�
		adapter = new Item_centre_page_service_adapter(getActivity());// ʵ����������
		data = new ArrayList<Centre_page_service>();// ʵ�������϶���
		list.setAdapter(adapter);// ����������
		seek = (ImageView) v.findViewById(R.id.imageView_fragment_examine_search);
		seek.setOnClickListener(l);
		adapter.setviewstate();// ��item�еİ�ť��������
		
		wait_examine.setTextColor(getResources().getColor(R.color.blue));
		wait_examine.setText(Html.fromHtml("<u>"+"δ����"+"</u>"));
		record_examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
		record_examine.setText("������");
		
		
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
	}

	// mPullListView�������������ļ���
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬
				if (state == false) {
					get_listcomplete();// ��ȡ��������ά�޵����б�
				} else {
					get_listwait();// ��ȡδ������ά�޵����б�
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬
				if (state == false) {
					get_listcomplete();// ��ȡ��������ά�޵����б�
				} else {
					get_listwait();// ��ȡδ������ά�޵����б�
				}
			} else {

			}

		}

	};
	// ��������
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textview_fragment_centre_examine_wait_examine:// ����ͨ��
				setbuttoncoloer(R.id.textview_fragment_centre_examine_wait_examine);
				break;

			case R.id.textview_fragment_centre_examine_record_examine:// ��ʷ����
				setbuttoncoloer(R.id.textview_fragment_centre_examine_record_examine);
				break;
			case R.id.imageView_fragment_examine_search:// ������ļ����¼�
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

	// �ı䰴ť������ɫ�ķ���
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_fragment_centre_examine_wait_examine:// �ȴ�����
			
			get_listwait();// ��ȡδ�������б�
			break;
		case R.id.textview_fragment_centre_examine_record_examine:// ��ʷ����
			
			get_listcomplete();// ��ȡ���������б�
			break;

		default:
			break;
		}
	}

	// ��ȡ�ȴ������б�ķ���
	private void get_listwait() {
		wait_examine.setTextColor(getResources().getColor(R.color.blue));
		wait_examine.setText(Html.fromHtml("<u>"+"δ����"+"</u>"));
		record_examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
		record_examine.setText("������");
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
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
						mPullListView.onPullUpRefreshComplete();
						number = false;// �ı��Ƿ�ˢ�µ�״̬

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
				Toast.makeText(getActivity(), "��������ʧ��", 0).show();
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

	// ��ȡ��ʷ�����б�ķ���
	private void get_listcomplete() {
		record_examine.setTextColor(getResources().getColor(R.color.blue));
		record_examine.setText(Html.fromHtml("<u>"+"������"+"</u>"));
		wait_examine.setTextColor(getResources().getColor(R.color.zitiyanse5));
		wait_examine.setText("δ����");
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
								// û��ͼƬʱ
								data.add(new Centre_page_service(js2.getString("sn"),
										js2// ���
												.getString("createDate"),
										Myutil.set_cutString(js3.getString("name") + "  " + js2.getString("name")),
										js2.getString("workStatus"),
										Myutil.get_delta_t(js2.getString("modifyDate"), js2.getString("createDate")),
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
										Myutil.get_delta_t(js2.getString("modifyDate"), js2.getString("createDate")),
										js2.getString("id"), js2.getString("workStatus"), js4.getString("username"),
										Myconstant.WEBPAGEPATHURL + jsphone.getString("source")));

							}
						}
						adapter.addDataBottom(data);
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
				Toast.makeText(getActivity(), "��������ʧ��", 0).show();
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
