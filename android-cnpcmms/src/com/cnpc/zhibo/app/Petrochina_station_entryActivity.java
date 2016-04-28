package com.cnpc.zhibo.app;

/*
 * ��ʯ�Ͷ˲鿴����վ�б���Ϣ�Ľ���
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
import com.azy.app.news.view.pullrefresh.PullToRefreshListView;
import com.azy.app.news.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.cnpc.zhibo.app.adapter.Item_petrochina_station_message_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Petrochina_station_message;
import com.cnpc.zhibo.app.entity.Petrochina_suppliers_entry;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_station_entryActivity extends MyActivity {
	private ImageView back;// ���ذ�ť
	private ListView list;// �б����
	private Item_petrochina_station_message_adapter adapter;
	private List<Petrochina_station_message> data;
	private TextView numbers, cost;// ά�޴�����ά�޷���
	private ImageView seek;// ������ť
	private Intent in;// ��ͼ����
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;
	private boolean number = false;// �����ж��Ƿ����ڽ���ˢ��
	private boolean state = false;// �жϵ�ǰ���б���ά�޴�������ά�޷���
	private TextView select;// ˢѡ
	private PopupWindow pop;
	private TextView month1, month3,month6, year1;
	private String beginDate = "2016-01-01";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_station_message);
		setview();
	}

	// ���ò��ֽ����ϵĿؼ�
	private void setview() {

		set_title_text("����վ�б�");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_petrochina_station_message_entry);// �б����
		list = mPullListView.getRefreshableView();// ��ȡlistview
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�¿���
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��
		mPullListView.setOnRefreshListener(refreshListener);// ����������ˢ�»����ļ����¼�
		adapter = new Item_petrochina_station_message_adapter(this);
		data = new ArrayList<Petrochina_station_message>();
		list.setAdapter(adapter);
		cost = (TextView) findViewById(R.id.textView_petrochina_station_message_servicecost);// ά�޷���
		numbers = (TextView) findViewById(R.id.textView_petrochina_station_message_servicenumber);// ά�޴���
		cost.setTextColor(getResources().getColor(R.color.blue));
		cost.setText(Html.fromHtml("<u>" + "ά�޷���" + "</u>"));
		numbers.setTextColor(getResources().getColor(R.color.zitiyanse5));
		numbers.setText("ά�޴���");
		list.setDividerHeight(0);
		list.setSelector(new BitmapDrawable());
		cost.setOnClickListener(l);
		numbers.setOnClickListener(l);
		seek = (ImageView) findViewById(R.id.imageView_petrochina_station_message_search);// ������ť
		seek.setOnClickListener(l);
		set_costdata();// ����ά�޷��õļ���վ�б������
		list.setOnItemClickListener(listener);
		select = (TextView) findViewById(R.id.textview_myacitivity_you);// ˢѡ
		select.setVisibility(View.VISIBLE);
		select.setOnClickListener(l);
		select.setText("ɸѡ");
		beginDate = Myutil.get_month_time();
	}

	// mPullListView�������������ļ���
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬
				if (state == true) {
					set_numberdata();// ����ά�޴���������
				} else {
					set_costdata();
					// ����ά�޷��õ����ݵķ���
				}
			} else {

			}

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number == false) {
				number = true;// �ı�ˢ�µ�״̬

			} else {

			}

		}

	};
	// �б���м����¼�
	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			in = new Intent(Petrochina_station_entryActivity.this, Petrochina_station_seemessageActivity.class);
			in.putExtra("id", adapter.getData().get(position).id);
			startActivity(in);
			Myutil.set_activity_open(Petrochina_station_entryActivity.this);
		}
	};

	// ����ά�޷��õ����ݵķ���
	private void set_costdata() {
		state = false;
		data.clear();
		adapter.getData().clear();

		String url = Myconstant.PETROCHINA_GET_STATIONENTRYSTATS + "beginDate=" + beginDate + "&endDate="
				+ Myutil.get_time_ymd() + "&direction1=" + "" + "&direction2=" + "desc";
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// ��Ӧ������
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_station_entryActivity.this, js1.getString("content"), 0).show();
						JSONArray body = js.getJSONArray("body");
						for (int i = 0; i < body.length(); i++) {
							JSONObject js2 = body.getJSONObject(i);
							data.add(new Petrochina_station_message(js2.getString("name"),
									"��ά��:" + js2.getString("count"), "������:" + js2.getString("amount"),
									js2.getString("id")));
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
						mPullListView.onPullUpRefreshComplete();
						number = false;// �ı��Ƿ�ˢ�µ�״̬
					} else {
						Toast.makeText(Petrochina_station_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_station_entryActivity.this, "���Ѿ����ߣ������µ�¼", 0).show();
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

		adapter.addDataBottom(data);
		mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
		mPullListView.onPullUpRefreshComplete();
		number = false;// �ı��Ƿ�ˢ�µ�״̬
	}

	// ����ά�޴��������ݵķ���
	private void set_numberdata() {
		state = true;

		data.clear();
		adapter.getData().clear();
		String url = Myconstant.PETROCHINA_GET_STATIONENTRYSTATS + "beginDate=" + beginDate + "&endDate="
				+ Myutil.get_time_ymd() + "&direction1=" + "desc" + "&direction2=" + "";
		;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");// ��Ӧ������
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Petrochina_station_entryActivity.this, js1.getString("content"), 0).show();
						JSONArray body = js.getJSONArray("body");
						for (int i = 0; i < body.length(); i++) {
							JSONObject js2 = body.getJSONObject(i);
							data.add(new Petrochina_station_message(js2.getString("name"),
									"��ά��:" + js2.getString("count"), "������:" + js2.getString("amount"),
									js2.getString("id")));
						}
						adapter.addDataBottom(data);
						mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
						mPullListView.onPullUpRefreshComplete();
						number = false;// �ı��Ƿ�ˢ�µ�״̬
					} else {
						Toast.makeText(Petrochina_station_entryActivity.this, js1.getString("content"), 0).show();
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
				Toast.makeText(Petrochina_station_entryActivity.this, "���Ѿ����ߣ������µ�¼", 0).show();
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
		adapter.addDataBottom(data);
		mPullListView.onPullDownRefreshComplete();// �ر�ˢ��
		mPullListView.onPullUpRefreshComplete();
		number = false;// �ı��Ƿ�ˢ�µ�״̬
	}

	// �����ķ���
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Petrochina_station_entryActivity.this);
				break;
			case R.id.textView_petrochina_station_message_servicecost:// ά�޷���
				cost.setTextColor(getResources().getColor(R.color.blue));
				cost.setText(Html.fromHtml("<u>" + "ά�޷���" + "</u>"));
				numbers.setTextColor(getResources().getColor(R.color.zitiyanse5));
				numbers.setText("ά�޴���");
				set_costdata();

				break;
			case R.id.textView_petrochina_station_message_servicenumber:// ά�޴���
				numbers.setTextColor(getResources().getColor(R.color.blue));
				numbers.setText(Html.fromHtml("<u>" + "ά�޴���" + "</u>"));
				cost.setTextColor(getResources().getColor(R.color.zitiyanse5));
				cost.setText("ά�޷���");
				set_numberdata();

				break;
			case R.id.imageView_petrochina_station_message_search:// ������ť
				Intent in = new Intent(Petrochina_station_entryActivity.this,
						Petrochina_search_stationentryActivity.class);
				in.putExtra("state", state);
				startActivity(in);

				Myutil.set_activity_open(Petrochina_station_entryActivity.this);
				break;
			case R.id.textview_myacitivity_you:// ˢѡ
				View v1 = LayoutInflater.from(Petrochina_station_entryActivity.this)
						.inflate(R.layout.popup_shoutime_select, null);// ����view
				pop = new PopupWindow(v1, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);// ʵ����popupwindow�ķ���
				pop.setBackgroundDrawable(new ColorDrawable());
				// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���
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
			case R.id.textView_popup_showtime_select_onemonth:// һ����
				beginDate = Myutil.get_month_time();
				pop.dismiss();
				Toast.makeText(Petrochina_station_entryActivity.this, "��ǰ��¼��ѯʱ���Ѿ���Ϊһ���£��������б����ˢ��", 0).show();
				break;
			case R.id.textView_popup_showtime_select_threemonth:// ������
				beginDate = Myutil.get_threemonth_time();
				pop.dismiss();
				Toast.makeText(Petrochina_station_entryActivity.this, "��ǰ��¼��ѯʱ���Ѿ���Ϊ�����£��������б����ˢ��", 0).show();
				break;
			case R.id.textView_popup_showtime_select_sixmonth:// ������
				beginDate = Myutil.get_sixmonth_time();
				pop.dismiss();
				Toast.makeText(Petrochina_station_entryActivity.this, "��ǰ��¼��ѯʱ���Ѿ���Ϊ�����£��������б����ˢ��", 0).show();
				break;
			case R.id.textView_popup_showtime_select_oneyear:// һ��
				beginDate = Myutil.get_oneyear_time();
				pop.dismiss();
				Toast.makeText(Petrochina_station_entryActivity.this, "��ǰ��¼��ѯʱ���Ѿ���Ϊһ�꣬�������б����ˢ��", 0).show();
				break;
			default:
				break;
			}
		}
	};

}
