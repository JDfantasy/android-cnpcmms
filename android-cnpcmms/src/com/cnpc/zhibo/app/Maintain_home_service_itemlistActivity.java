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
import com.cnpc.zhibo.app.adapter.Item_maintainhome_service_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
//ά�޶���ҳ��ά�޵�����
//���ÿ�ʼ�ӵ���ʵ�����adapter
import android.widget.Toast;

public class Maintain_home_service_itemlistActivity extends MyActivity implements
		OnClickListener, OnScrollListener{

	private ImageView back;//����
	private Item_maintainhome_service_adapter adapter;
	private List<Maintain_startList_item> list;
	private ImageView searchImageView;//����
	private int statenumber=0;//ά��״̬
	//����ˢ�¿ؼ�
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//����ˢ��
	private ListView listview;
	private boolean refreshFlag=false;//�����ж��Ƿ����ڽ���ˢ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home_service_itemlist);
		setViews();
		set_title_text("ά�޵�");
		
	}


	// �ؼ���ʼ��
	private void setViews() {
		list=new ArrayList<Maintain_startList_item>();
		//����ˢ��
		mPullListView =(PullToRefreshListView) findViewById(R.id.maintainhome_serviceListview);
		listview = mPullListView.getRefreshableView();// ͨ����ܻ�ȡlistview
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�¿���
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��Ϊ��ǰ
		//listview.setOnItemClickListener(this);
		mPullListView.setOnRefreshListener(refreshListener);
		
		listview.setDividerHeight(0);
		listview.setSelector(new BitmapDrawable());
		//listview.setCacheColorHint(0);
		listview.setOnScrollListener(this);//��������
		
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		//����
		searchImageView=(ImageView) findViewById(R.id.maintainhome_serviceItemlist_search);
		searchImageView.setOnClickListener(this);
		try {
			startLoadData();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "��ȡ����ʧ�ܣ���������", Toast.LENGTH_SHORT).show();
		}
	}

	//����ʱ���صĽ���
	private void startLoadData() {
		adapter=new Item_maintainhome_service_adapter(this);
		listview.setAdapter(adapter);
		getServiceHistoryandProgress();
	}
	
	// listview�������������ļ���
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (refreshFlag==false) {
				refreshFlag=true;//�ı�ˢ�µ�״̬
					getServiceHistoryandProgress();
				Toast.makeText(getApplicationContext(), "����ˢ��", Toast.LENGTH_SHORT).show();
			} 
			adapter.setScrollState(false); 
			mPullListView.onPullDownRefreshComplete();//�ر�ˢ��
			refreshFlag=false;//�ı��Ƿ�ˢ�µ�״̬
			

		}
		//��������ˢ���Ƿ����ʹ��
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			
			Toast.makeText(getApplicationContext(), "����ˢ��", Toast.LENGTH_SHORT).show();
			mPullListView.onPullUpRefreshComplete();//�ر�ˢ��
			refreshFlag=false;//�ı��Ƿ�ˢ�µ�״̬
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ����
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// �����л������Ч��
			break;
		// ����
		case R.id.maintainhome_serviceItemlist_search:
			startActivity(new Intent(this,Maintain_searchhomeservice_Activity.class));
			break;

		}

	}

	// listView����
	/*@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// �����ת���������
		Intent intent = new Intent(this,Maintain_home_service_detail_Activity.class);
		String indentId=list.get(position).id;
		intent.putExtra("indentId", indentId);
		startActivity(intent);

	}*/
	
	// ��ȡά���е��б�
	private void getServiceHistoryandProgress() {
		String url=Myconstant.MAINTAIN_SERVICE_PROGRESS;
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
						list.clear();
						adapter.getData().clear();
						adapter.notifyDataSetChanged();
						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getApplicationContext(), "û���ҵ�����", 0).show();
						} else {
							Toast.makeText(getApplicationContext(), "���ҵ�����", 0).show();
							
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								String modifyDate=js3.getString("modifyDate");
								String userName=js3.getJSONObject("reporter").getString("username");
								JSONObject js4=js3.getJSONObject("station");
								Maintain_startList_item m=new Maintain_startList_item(js3.getString("id"), js3.getString("sn"), 
										userName,js4.getString("name"), problem, js3.getString("modifyDate"),js3.getString("workStatus"));
								JSONArray ary=js3.getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								list.add(m);
								
							}
							
							adapter.setdate(list);
							mPullListView.onPullDownRefreshComplete();//�ر�ˢ��
							mPullListView.onPullUpRefreshComplete();
							refreshFlag=false;//�ı��Ƿ�ˢ�µ�״̬
						}

					} else {
						//���ݴ����ԭ��
						Toast.makeText(getApplicationContext(), js1.getString("content"), 0).show();
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
				//Toast.makeText(getApplicationContext(), "��������ʧ��"+error.getMessage(), 0).show();
				Toast.makeText(getApplicationContext(), "����ʧ�ܣ���������", 0).show();
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


	//��������
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState){  
		  
	        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://ֹͣ����  
	        {  
	            //����Ϊֹͣ����  
	            adapter.setScrollState(false); 
	            adapter.notifyDataSetChanged();
	            //��ǰ��Ļ��listview������ĸ���  
	            break;  
	        }  
	        case AbsListView.OnScrollListener.SCROLL_STATE_FLING://�����������׵Ķ���  
	        {  
	            //����Ϊ���ڹ���  
	            adapter.setScrollState(true);
	            break;  
	        }  
	
	        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://���ڹ���  
	        {  
	            //����Ϊ���ڹ���  
	            adapter.setScrollState(true); 
	            break;  
	        }  
	    }
		
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	

}
