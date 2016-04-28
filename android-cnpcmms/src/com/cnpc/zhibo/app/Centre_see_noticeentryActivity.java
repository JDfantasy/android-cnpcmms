package com.cnpc.zhibo.app;

/*
 * վ���˲鿴�����б�Ľ���
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
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.cnpc.zhibo.app.adapter.Item_centre_page_service_adapter;
import com.cnpc.zhibo.app.adapter.Item_commonality_notice_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.entity.Commonality_notice;
//վ���˲鿴ά���е�ά�޵��б����ҿ����Ự
import com.cnpc.zhibo.app.util.Myutil;
import com.easemob.easeui.EaseConstant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Centre_see_noticeentryActivity extends MyActivity {
	private ImageView back;// ���ذ�ť
	private ListView list;// �б����
	private List<Commonality_notice> data;// ���ݼ���
	private Item_commonality_notice_adapter adapter;
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private boolean number = false;// �����ж��Ƿ����ڽ���ˢ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_seewxg_news);
		setview();// ����
		list.setOnItemClickListener(listener);
		SysApplication.getInstance().setListHandler(getIntent().getStringExtra("type"), noticehandler);
	}
	
	/*
	 * �����б�
	 */
		private Handler noticehandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					try {
						get_listprogress();//�������ݵķ���
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					break;
				default:
					break;
				}
			};
		};
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent in = new Intent(Centre_see_noticeentryActivity.this, NoticedetailsActivity.class);
			in.putExtra("noticeId", adapter.getData().get(position).id);

			startActivity(in);
			Myutil.set_activity_open(Centre_see_noticeentryActivity.this);
		}

	};


	protected void onResume() {
		super.onResume();
		get_listprogress();// ��������
		
	};

	// ���ý���ķ���
	private void setview() {
		set_title_text("�������");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_centre_see_serviceentry_news_entry);
		adapter = new Item_commonality_notice_adapter(Centre_see_noticeentryActivity.this);
		list = mPullListView.getRefreshableView();// ��ȡlistview
		list.setAdapter(adapter);
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�¿���
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��
		mPullListView.setOnRefreshListener(refreshListener);// ����������ˢ�»����ļ����¼�
		data = new ArrayList<Commonality_notice>();
		
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Centre_see_noticeentryActivity.this);

				break;

			default:
				break;
			}

		}
	};

	// mPullListView�������������ļ���
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬
				get_listprogress();
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬
				get_listprogress();
			} else {

			}

		}

	};

	// ��ȡ���еĹ���
	private void get_listprogress() {
		String url = Myconstant.COMMONALITY_GETNOTICEENTRY;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						list.setAdapter(adapter);
						adapter.getData().clear();

						data.clear();
						adapter.setinitializedata();
						Toast.makeText(Centre_see_noticeentryActivity.this, js1.getString("content"), 0).show();
						JSONArray jsa = js.getJSONArray("body");
						for (int i = 0; i < jsa.length(); i++) {
							JSONObject js2 = (JSONObject) jsa.get(i);
							JSONObject notice = js2.getJSONObject("notice");
							data.add(new Commonality_notice(notice.getString("title"), notice.getString("content"),
									notice.getString("createDate"), js2.getString("id"), js2.getString("status")));
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
						mPullListView.onPullUpRefreshComplete();
						number = false;// �ı��Ƿ�ˢ�µ�״̬
					} else {
						//Toast.makeText(Centre_see_noticeentryActivity.this, js1.getString("content"), 0).show();
						Toast.makeText(Centre_see_noticeentryActivity.this, "���ݴ���", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
				Toast.makeText(Centre_see_noticeentryActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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
		SysApplication.getHttpQueues().add(re);
	}
}
