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
import com.baidu.a.a.a.a;
import com.cnpc.zhibo.app.adapter.Item_maintain_alloction_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_home_alloction_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

//ά�޶���ҳ����֪ͨ����
public class Maintain_home_alloction_Activity extends MyActivity implements
		OnItemClickListener, OnClickListener {

	private Item_maintain_alloction_adapter adapter;
	private List<Maintain_home_alloction_item> list;
//	private List<Maintain_home_alloction_item> weekBeforelist=new ArrayList<Maintain_home_alloction_item>();
//	private List<Maintain_home_alloction_item> weekAfterlist=new ArrayList<Maintain_home_alloction_item>();
	private ImageView back;//����
	//private LinearLayout searchLinearLayout;//����
	//private RefreshableView refreshableView;//����ˢ��
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//����ˢ��
	private ListView listview;
	private boolean number=false;//�����ж��Ƿ����ڽ���ˢ��
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					searchAlloctionList();
					//clearNums("worker-distribute");
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "�����б�ʧ�ܣ���������", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home_alloction_notification);
		set_title_text("����֪ͨ");
		SysApplication.getInstance().setListHandler("worker-distribute", handler);
		setViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setAdapter();
		searchAlloctionList();//������������
	}

	private void setAdapter() {
		adapter = new Item_maintain_alloction_adapter(this);
		list = new ArrayList<Maintain_home_alloction_item>();
		listview.setAdapter(adapter);
	}

	// �ؼ���ʼ��
	private void setViews() {
		//����ˢ��
		mPullListView = (PullToRefreshListView) findViewById(R.id.listView_maintain_alloction);//��ÿ��
		listview = mPullListView.getRefreshableView();// ͨ����ܻ�ȡlistview
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�¿���
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��Ϊ��ǰ
		listview.setOnItemClickListener(this);
		mPullListView.setOnRefreshListener(refreshListener);
		listview.setDividerHeight(0);
		listview.setSelector(new BitmapDrawable());
		
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		//����
//		searchLinearLayout=(LinearLayout) findViewById(R.id.linearlayout_maintain_home_alloction_SearchView);
//		searchLinearLayout.setOnClickListener(this);
	}
	
	// listview�������������ļ���
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (number==false) {
				number=true;//�ı�ˢ�µ�״̬
				/*if (state == false) {
					get_historydata();// ��ȡ��ʷά�޵ı��޵����б�
				} else {
					get_progressdata();// ��ȡ�ȴ�ά���еı��޵����б�
				}*/
				Toast.makeText(getApplicationContext(), "����ˢ��", Toast.LENGTH_SHORT).show();
				searchAlloctionList();
			} else {

			}
			

		}
		//��������ˢ���Ƿ����ʹ��
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			/*if (state == false) {
				get_historydata();// ��ȡ��ʷά�޵ı��޵����б�
			} else {
				get_progressdata();// ��ȡ�ȴ�ά���еı��޵����б�
			}*/
			Toast.makeText(getApplicationContext(), "����ˢ��", Toast.LENGTH_SHORT).show();
		}

	};

	// listview����
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		// �����ת���������
		Intent intent = new Intent(Maintain_home_alloction_Activity.this,
				Maintain_home_alliction_detail_Activity.class);
		Maintain_home_alloction_item m = list.get(position);
		intent.putExtra("m", m);
		startActivity(intent);

	}

	//�������
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//���ذ�ť
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// �����л������Ч��
			break;
		// ����
		/*case R.id.linearlayout_maintain_home_alloction_SearchView:
			Intent intent=new Intent(this,Maintain_search_Activity.class);
			intent.putExtra("responseCode", GlobalConsts.MAINTAIN_ALLOCTION_RESPONSECODE);
			startActivity(intent);
			break;*/
		}
		
	}
	
	//��÷�������ݼ���
	public void searchAlloctionList(){
		String url = Myconstant.MAINTAINHOME_ALLOCTION_NOT;
		adapter.tag1=0;
		adapter.tag2=0;
		adapter.ck1 = false;
		adapter.ck2 = false;
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(Maintain_home_alloction_Activity.this, "û���ҵ�����", 0).show();
						} else {
							Toast.makeText(Maintain_home_alloction_Activity.this, "���ҵ�����", 0).show();
							list.clear();
//							weekBeforelist.clear();
//							weekAfterlist.clear();
							adapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("project").getString("name")+js3.getString("description");
								String userName=js3.getJSONObject("reporter").getString("username");
								String modifyDate=js3.getString("modifyDate");
								JSONObject js4=js3.getJSONObject("station");
								Maintain_home_alloction_item m=new Maintain_home_alloction_item(js3.getString("id"), userName,
										js3.getString("sn"), js4.getString("employeeName"), js4.getString("name"), 
										js4.getString("address"), problem,  modifyDate, 
										js4.getString("mobilephone"),null,null,
										js3.getJSONObject("project").getString("name"),js3.getString("description"));
								JSONArray ary=js3.getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath[0]="";
								}else if(ary.length()!=0){
									for(int j=0;j<ary.length();j++){
										m.iconPath[j]=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(j).getString("source");
										Log.i("tag", "m.iconPath["+j+"]="+m.iconPath[j]);
									}
								}
								list.add(m);
							}
							/*for(int i=0;i<list.size();i++){
								
								if (Myutil.get_delta_t_data1(Myutil.get_current_time(), list.get(i).time) == 1) {
									weekBeforelist.add(list.get(i));
								}else {
									weekAfterlist.add(list.get(i));
								}
							}
							if(weekBeforelist!=null&&weekBeforelist.size()!=0){
								weekBeforelist.get(0).timeState="weekBefore";
							}
							if(weekAfterlist!=null&&weekAfterlist.size()!=0){
								weekAfterlist.get(0).timeState="weekAfter";
							}
							list.clear();
							list.addAll(weekBeforelist);
							Log.i("tag", list.size()+"  "+list);
							list.addAll(weekAfterlist);
							Log.i("tag", list.size()+"  "+list.toArray());*/
							
							/*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							int count=0;
							for(int i=0;i<list.size();i++){
								try {
									Date d = df.parse(list.get(i).time);
									//double diff = System.currentTimeMillis()/1000000 - d.getTime()/1000000;
									double diff = new Date().getTime()/1000000.0 - d.getTime()/1000000.0;
									//1��=86 400 000���� 1Сʱ3.6 ��Сʱ1.8
									if(diff>=86.4*7){
										count++;
									}
								} catch (Exception e) {
								}
							}
							if(count==0) list.get(0).timeState="weekBefore";
							if(count!=0&&list.size()==count) list.get(0).timeState="weekAfter";
							if(count!=0&&list.size()>count){
								list.get(0).timeState="weekBefore";
								list.get(list.size()-count).timeState="weekAfter";
							}*/ 
							//list.get ��ý���ά�޵�
							adapter.setdate(list);
							mPullListView.onPullDownRefreshComplete();//�ر�ˢ��
							mPullListView.onPullUpRefreshComplete();
							number=false;//�ı��Ƿ�ˢ�µ�״̬
							
						}

					} else {
						//���ݴ����ԭ��
						Toast.makeText(Maintain_home_alloction_Activity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//����ʧ�ܵ�ԭ��
				Log.i("tag", "error="+error);
				//Toast.makeText(Maintain_home_alloction_Activity.this, "��������ʧ��"+error.toString(), 0).show();
				Toast.makeText(Maintain_home_alloction_Activity.this, "��������ʧ��,��������", 0).show();
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
