package com.cnpc.zhibo.app.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
//ά�޶˵�ά��
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.tv.TvContract.Channels.Logo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.cnpc.zhibo.app.Maintain_home_alloction_Activity;
import com.cnpc.zhibo.app.Maintain_home_service_detail_Activity;
import com.cnpc.zhibo.app.Maintain_search_Activity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.RefreshableView;
import com.cnpc.zhibo.app.RefreshableView.PullToRefreshListener;
import com.cnpc.zhibo.app.adapter.Item_maintain_serviceing_adapter;
import com.cnpc.zhibo.app.adapter.Item_maintain_startList_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.entity.Maintain_home_alloction_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;

public class Fragment_maintain_repairs extends Fragment implements OnItemClickListener, OnScrollListener {
	private TextView zzwx, ddwx, lswx;// ����ά�ޡ��ȴ�ά�ޡ���ʷά��
	private RequestQueue mqueQueue;// �������
	private List<Maintain_startList_item> data=new ArrayList<Maintain_startList_item>();// ���ݼ���
	private Item_maintain_serviceing_adapter progressadapter;//ά����
	private Item_maintain_serviceing_adapter historyadapter;//��ʷά��
	private List<Maintain_startList_item> list;
	private ImageView searchImageView;//����
	private int progressnumber=0;//ά����
	private int historynumber=1;//��ʷά��
	private int statenumber=0;//ά��״̬
	//����ˢ�¿ؼ�
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//����ˢ��
	private ListView listview;
	private boolean refreshFlag=false;//�����ж��Ƿ����ڽ���ˢ��
	
	//�����б�***
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0://��Ϣ���͸���
				try {
					setprogresslist();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "�����б�ʧ�ܣ���������", Toast.LENGTH_SHORT).show();
				}
				break;
			case 200://����ֱ������
				try {
					sethistorylist();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "�����б�ʧ�ܣ���������", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_maintain_repairs, container,
				false);
		//���ø����б��handler
		SysApplication.getInstance().setListHandler("worker-fixorder", handler);
		setview(v);// ���ý���ķ���
		return v;
	}
	

	// ���ý���ķ���
	private void setview(View v) {
		list=new ArrayList<Maintain_startList_item>();
		mqueQueue = Volley.newRequestQueue(getActivity());// ��ʼ���������
		zzwx = (TextView) v
				.findViewById(R.id.maintainService_fragment_repairs_zzwx);// ����ά��
//		ddwx = (TextView) v
//				.findViewById(R.id.maintainService_fragment_repairs_ddwx);// �ȴ�ά��
		lswx = (TextView) v
				.findViewById(R.id.maintainService_fragment_repairs_lswx);// ��ʷά��
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.maintainService_fragment_repairs_serviceListview);// ʵ����listview
		listview = mPullListView.getRefreshableView();// ͨ����ܻ�ȡlistview
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�²�����
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��Ϊ��ǰ
		listview.setOnItemClickListener(this);
		listview.setOnScrollListener(this);//��������
		mPullListView.setOnRefreshListener(refreshListener);
		listview.setDividerHeight(0);
		listview.setSelector(new BitmapDrawable());
		
		zzwx.setOnClickListener(l);
		lswx.setOnClickListener(l);
		setbuttoncoloer(R.id.maintainService_fragment_repairs_zzwx);// ���ð�ť����ɫ
		//����
		searchImageView = (ImageView) v.findViewById(R.id.maintainService_fragment_repairs_search);
		searchImageView.setOnClickListener(l);
		
		//��ʼ��ʾ��ά���еĽ���
		setbuttoncoloer(R.id.maintainService_fragment_repairs_zzwx);
		progressadapter=new Item_maintain_serviceing_adapter(getActivity());
		historyadapter=new Item_maintain_serviceing_adapter(getActivity());
		getServiceProgress(progressnumber);
		listview.setAdapter(progressadapter);
//		Log.i("tag", "0tag1="+progressadapter.tag1);
//		Log.i("tag", "0tag2="+progressadapter.tag2);
	}

	
	// listview�������������ļ���
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			progressadapter.setScrollState(false); 
			historyadapter.setScrollState(false); 
			if (refreshFlag==false) {
				refreshFlag=true;//�ı�ˢ�µ�״̬
				if(statenumber==progressnumber){
					getServiceProgress(progressnumber);
					listview.setAdapter(progressadapter);
				}else if(statenumber==historynumber){
					getServiceHistory(progressnumber);
					listview.setAdapter(historyadapter);
				}
				Toast.makeText(getActivity(), "����ˢ��", Toast.LENGTH_SHORT).show();
			} 
			mPullListView.onPullDownRefreshComplete();//�ر�ˢ��
			refreshFlag=false;//�ı��Ƿ�ˢ�µ�״̬
			

		}
		//��������ˢ���Ƿ����ʹ��
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			/*if (state == false) {
				get_historydata();// ��ȡ��ʷά�޵ı��޵����б�
			} else {
				get_progressdata();// ��ȡ�ȴ�ά���еı��޵����б�
			}*/
			Toast.makeText(getActivity(), "����ˢ��", Toast.LENGTH_SHORT).show();
			mPullListView.onPullUpRefreshComplete();//�ر�ˢ��
			refreshFlag=false;//�ı��Ƿ�ˢ�µ�״̬
		}

	};
	

	// ��������
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.maintainService_fragment_repairs_zzwx:// ����ά��
				setprogresslist();
				break;
			case R.id.maintainService_fragment_repairs_lswx:// ��ʷά��
				sethistorylist();
				break;
			case R.id.maintainService_fragment_repairs_search:// ������ļ����¼�
				// �����ת����������
				startActivity(new Intent(getActivity(),Maintain_search_Activity.class));
				break;
			default:
				break;
			}
		}

	};
	
	//��ʷά���б�
	public void sethistorylist(){
		setbuttoncoloer(R.id.maintainService_fragment_repairs_lswx);
		list.clear();
		historyadapter.getData().clear();
		historyadapter.setdate(list);
		getServiceHistory(historynumber);
		listview.setAdapter(historyadapter);
	}
	//ά�����б�
	public void setprogresslist(){
		setbuttoncoloer(R.id.maintainService_fragment_repairs_zzwx);
		list.clear();
		progressadapter.getData().clear();
		progressadapter.setdate(list);
		getServiceProgress(progressnumber);
		listview.setAdapter(progressadapter);
	}

	// �ı䰴ť������ɫ�ķ���
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.maintainService_fragment_repairs_zzwx:// ����ά��
			lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
			zzwx.setTextColor(getResources().getColor(R.color.blue));
			zzwx.setText(Html.fromHtml("<u>"+"ά����"+"</u>"));
			lswx.setText("��ʷά��");
			break;
		case R.id.maintainService_fragment_repairs_lswx:// ��ʷά��
			zzwx.setTextColor(getResources().getColor(R.color.zitiyanse5));
			lswx.setTextColor(getResources().getColor(R.color.blue));
			lswx.setText(Html.fromHtml("<u>"+"��ʷά��"+"</u>"));
			zzwx.setText("ά����");
			break;

		default:
			break;
		}
	}



	//����
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// �����ת���������
		Intent intent = new Intent(getActivity(),
				Maintain_home_service_detail_Activity.class);
		//Log.i("tag", "trueid="+list.get(position).id);
		//String indentId=list.get(position).id;
		String indentId="";
		if(statenumber==progressnumber){
			indentId=progressadapter.getData().get(position).id;
		}
		if(statenumber==historynumber){
			indentId=historyadapter.getData().get(position).id;
		}
		intent.putExtra("indentId", indentId);
		startActivity(intent);
		
	}
	
	
	
	// ��ȡ��ʷά�ޡ�ά���е��б�
	private void getServiceProgress(final int number) {
		String url=Myconstant.MAINTAIN_SERVICE_PROGRESS;
		statenumber=progressnumber;
		progressadapter.tag1=0;
		progressadapter.tag2=0;
		progressadapter.ck1 = false;
		progressadapter.ck2 = false;
		/*if(number==progressnumber){//ά����
			url = Myconstant.MAINTAIN_SERVICE_PROGRESS;
			statenumber=progressnumber;
		}else if (number==historynumber) {
			url = Myconstant.MAINTAIN_SERVICE_HISTORY+"beginDate="
					+ Myutil.get_month_time() + "&endDate=" + Myutil.get_time_ymd();
			statenumber=historynumber;
		}*/

		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getActivity(), "û���ҵ�����", 0).show();
						} else {
							Toast.makeText(getActivity(), "���ҵ�����", 0).show();
							list.clear();
							progressadapter.getData().clear();
							historyadapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								//String modifyDate=js3.getString("modifyDate");
								String userName=js3.getJSONObject("reporter").getString("username");
								JSONObject js4=js3.getJSONObject("station");
								Maintain_startList_item m=new Maintain_startList_item(js3.getString("id"), js3.getString("sn"), 
										userName,js4.getString("name"), problem, js3.getString("modifyDate"),js3.getString("workStatus"));
								//���ǵĸ���
								m.starNumber=js3.getString("score");
								JSONArray ary=js3.getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								//Log.i("tag", "m.iconPath="+m.iconPath);
								list.add(m);
							}
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
							} */
							progressadapter.addDataBottom(list);
//							Log.i("tag", "tag1="+progressadapter.tag1);
//							Log.i("tag", "tag2="+progressadapter.tag2);
							mPullListView.onPullDownRefreshComplete();//�ر�ˢ��
							mPullListView.onPullUpRefreshComplete();
							refreshFlag=false;//�ı��Ƿ�ˢ�µ�״̬
						}

					} else {
						//���ݴ����ԭ��
						//Toast.makeText(getActivity(), js1.getString("content"), 0).show();
						Toast.makeText(getActivity(), "���ݴ���", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//����ʧ�ܵ�ԭ��
				//Log.i("tag", "error="+error);
				//Toast.makeText(getActivity(), "��������ʧ��"+error.getMessage(), 0).show();
				Toast.makeText(getActivity(), "��������ʧ��,��������", 0).show();
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
	
	// ��ȡ��ʷά�ޡ����б�
	private void getServiceHistory(final int number) {
		String url=Myconstant.MAINTAIN_SERVICE_HISTORY+"beginDate="
				+ Myutil.get_month_time() + "&endDate=" + Myutil.get_time_ymd();
		statenumber=historynumber;
		historyadapter.tag1=0;
		historyadapter.tag2=0;
		historyadapter.ck1 = false;
		historyadapter.ck2 = false;
		/*if(number==progressnumber){//ά����
			url = Myconstant.MAINTAIN_SERVICE_PROGRESS;
			statenumber=progressnumber;
		}else if (number==historynumber) {
			url = Myconstant.MAINTAIN_SERVICE_HISTORY+"beginDate="
					+ Myutil.get_month_time() + "&endDate=" + Myutil.get_time_ymd();
			statenumber=historynumber;
		}*/
	
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
	
						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getActivity(), "û���ҵ�����", 0).show();
						} else {
							Toast.makeText(getActivity(), "���ҵ�����", 0).show();
							list.clear();
							historyadapter.getData().clear();
							progressadapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								//String modifyDate=js3.getString("modifyDate");
								String userName=js3.getJSONObject("reporter").getString("username");
								JSONObject js4=js3.getJSONObject("station");
								Maintain_startList_item m=new Maintain_startList_item(js3.getString("id"), js3.getString("sn"), 
										userName,js4.getString("name"), problem, js3.getString("modifyDate"),js3.getString("workStatus"));
								//���ǵĸ���
								m.starNumber=js3.getString("score");
								JSONArray ary=js3.getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								//Log.i("tag", "m.iconPath="+m.iconPath);
								list.add(m);
							}
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
							} */
							historyadapter.addDataBottom(list);
							mPullListView.onPullDownRefreshComplete();//�ر�ˢ��
							mPullListView.onPullUpRefreshComplete();
							refreshFlag=false;//�ı��Ƿ�ˢ�µ�״̬
						}
	
					} else {
						//���ݴ����ԭ��
						//Toast.makeText(getActivity(), js1.getString("content"), 0).show();
						Toast.makeText(getActivity(), "���ݴ���", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
	
			@Override
			public void onErrorResponse(VolleyError error) {
				//����ʧ�ܵ�ԭ��
				//Log.i("tag", "error="+error);
				//Toast.makeText(getActivity(), "��������ʧ��"+error.getMessage(), 0).show();
				Toast.makeText(getActivity(), "��������ʧ��,��������", 0).show();
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
	//======================================================================================



	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		 switch (scrollState){  
		  
         case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://ֹͣ����  
         {  
             //����Ϊֹͣ����  
             progressadapter.setScrollState(false); 
             progressadapter.notifyDataSetChanged();
             historyadapter.setScrollState(false);
             historyadapter.notifyDataSetChanged();
             //��ǰ��Ļ��listview������ĸ���  
            /* int count = view.getChildCount();  
             Log.e("MainActivity",count+"");  

             for (int i = 0; i < count; i++) {  
                 //��ȡ��item��name  
                 TextView tv_name = (TextView) view.getChildAt(i).findViewById(R.id.main_item_tv_name);  
                 //��ȡ��item��ͷ��  
                 ImageView iv_show= (ImageView) view.getChildAt(i).findViewById(R.id.main_item_iv_icon);  

                 if (tv_name.getTag() != null) { //��null˵����Ҫ��������  
                     tv_name.setText(tv_name.getTag().toString());//ֱ�Ӵ�Tag��ȡ�����Ǵ洢������name���Ҹ�ֵ  
                     tv_name.setTag(null);//����Ϊ�Ѽ��ع�����  
                 }  

                 if (!iv_show.getTag().equals("1")){//!="1"˵����Ҫ��������  
                     String image_url=iv_show.getTag().toString();//ֱ�Ӵ�Tag��ȡ�����Ǵ洢������image����url  
                     ImageLoader.getInstance().displayImage(image_url, iv_show);//��ʾͼƬ  
                     iv_show.setTag("1");//����Ϊ�Ѽ��ع�����  
                 }  
             }*/  
             break;  
         }  
         case AbsListView.OnScrollListener.SCROLL_STATE_FLING://�����������׵Ķ���  
         {  
             //����Ϊ���ڹ���  
             progressadapter.setScrollState(true);
             historyadapter.setScrollState(true);
             break;  
         }  

         case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://���ڹ���  
         {  
             //����Ϊ���ڹ���  
        	 progressadapter.setScrollState(true); 
             historyadapter.setScrollState(true); 
             break;  
         }  
     }  
		
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}


}
