package com.cnpc.zhibo.app.fragment;

import java.lang.ref.WeakReference;
//ά�޶˵ı�������
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
import com.cnpc.zhibo.app.Maintain_apply_DetailActivity;
import com.cnpc.zhibo.app.Maintain_apply_modifyapplybillActivity;
import com.cnpc.zhibo.app.Maintain_home_service_detail_Activity;
import com.cnpc.zhibo.app.Maintain_search_Activity;
import com.cnpc.zhibo.app.Maintain_searchapplylist_Activity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.RefreshableView;
import com.cnpc.zhibo.app.RefreshableView.PullToRefreshListener;
import com.cnpc.zhibo.app.adapter.Item_centre_page_appove_adapter;
import com.cnpc.zhibo.app.adapter.Item_maintain_apply_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.entity.Maintain_apply_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_maintain_apply extends Fragment implements OnItemClickListener, OnScrollListener {
	private TextView alreadApply,notApply;// �ѱ�����δ����
	private RequestQueue mqueQueue;// �������
	private Item_maintain_apply_adapter notadapter;// δ����������
	private Item_maintain_apply_adapter alreadadapter;// �ѱ���������
	private List<Maintain_apply_item> list;// ����������
	//����ˢ�¿ؼ�
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//����ˢ��
	private ListView listview;
	private boolean refreshFlag=false;//�����ж��Ƿ����ڽ���ˢ��
	private ImageView searchImageView;//����
	private int progressnumber=0;//δ����
	private int historynumber=1;//�ѱ���
	private int statenumber=0;//ά��״̬
	
	//�����б�***
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				//Log.i("tag", "=====case 100");
			case 0:
				try {
					setnotApplylist();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "�����б�ʧ�ܣ���������", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};
	
	/*private Updatelisthandler applyhandler=new Updatelisthandler(this);
	public static class Updatelisthandler extends Handler{
		private final WeakReference<Fragment_maintain_apply> mfragment;
		public Updatelisthandler(Fragment_maintain_apply fragment) {
			mfragment=new WeakReference<Fragment_maintain_apply>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Fragment_maintain_apply fragment = mfragment.get();
			switch (msg.what) {
			case 100:
				//Log.i("tag", "=====case 100");
			case 0:
				try {
					if(fragment!=null){
						fragment.setnotApplylist();
					}
					
				} catch (Exception e) {
					Toast.makeText(fragment.getActivity(), "�����б�ʧ�ܣ���������", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_maintain_apply, container,
				false);
		setview(v);// ���ý���ķ���
		SysApplication.getInstance().setListHandler("worker-expenseorder", handler);
		//new Maintain_apply_modifyapplybillActivity().setListHandler(handler);
		return v;
	}

	// ���ý���ķ���
	private void setview(View v) {
		list=new ArrayList<Maintain_apply_item>();
		mqueQueue = Volley.newRequestQueue(getActivity());// ��ʼ���������
		alreadApply = (TextView) v
				.findViewById(R.id.textview_fragment_maintain_alreadapply);//�ѱ���
		notApply = (TextView) v
				.findViewById(R.id.textview_fragment_maintain_notapply);//δ����
		alreadApply.setOnClickListener(l);
		notApply.setOnClickListener(l);
		//ˢ�µĳ�ʼ��
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.maintainApply_fragment_applyListview);// ʵ����listview
		listview = mPullListView.getRefreshableView();// ͨ����ܻ�ȡlistview
		mPullListView.setPullLoadEnabled(false);// ��������ˢ�²�����
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --���ø��µ���ʾʱ��Ϊ��ǰ
		listview.setOnItemClickListener(this);
		mPullListView.setOnRefreshListener(refreshListener);
		listview.setDividerHeight(0);
		listview.setSelector(new BitmapDrawable());
		listview.setOnScrollListener(this);//��������
		
		//����
		searchImageView = (ImageView) v.findViewById(R.id.maintainApply_fragment_apply_search);
		searchImageView.setOnClickListener(l);
		//���ݳ���
		setbuttoncoloer(R.id.textview_fragment_maintain_notapply);// ���ð�ť����ɫ
		notadapter = new Item_maintain_apply_adapter(getActivity());// ʵ����������
		alreadadapter = new Item_maintain_apply_adapter(getActivity());// ʵ����������
		getApplynot(progressnumber);
		listview.setAdapter(notadapter);// ����������
	}
	
	
	// listview�������������ļ���
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			notadapter.setScrollState(false); 
			alreadadapter.setScrollState(false); 
			if (refreshFlag==false) {
				refreshFlag=true;//�ı�ˢ�µ�״̬
				if(statenumber==progressnumber){
					getApplynot(progressnumber);
				}else if(statenumber==historynumber){
					getApplyalready(historynumber);
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
			case R.id.textview_fragment_maintain_alreadapply:// �ѱ���
				setalreadApplylist();
				break;
			case R.id.textview_fragment_maintain_notapply:// δ����
				setnotApplylist();
				break;
			case R.id.maintainApply_fragment_apply_search:// ������ļ����¼�
				//TODO
				startActivity(new Intent(getActivity(),Maintain_searchapplylist_Activity.class));
				break;
			default:
				break;
			}
		}

	};
	
	//����δ�����б�
	public void setnotApplylist(){
		setbuttoncoloer(R.id.textview_fragment_maintain_notapply);
		list.clear();
		notadapter.getData().clear();
		notadapter.setdate(list);
		getApplynot(progressnumber);
		listview.setAdapter(notadapter);
	}
	//�����ѱ����б�
	public void setalreadApplylist(){
		setbuttoncoloer(R.id.textview_fragment_maintain_alreadapply);
		list.clear();
		alreadadapter.getData().clear();
		alreadadapter.setdate(list);
		getApplyalready(historynumber);
		listview.setAdapter(alreadadapter);
	}
	

	// �ı䰴ť������ɫ�ķ���
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_fragment_maintain_alreadapply:// �ѱ���
			notApply.setTextColor(getResources().getColor(R.color.zitiyanse5));
			alreadApply.setTextColor(getResources().getColor(R.color.blue));
			alreadApply.setText(Html.fromHtml("<u>"+"�ѱ���"+"</u>"));
			notApply.setText("δ����");
			break;
		case R.id.textview_fragment_maintain_notapply:// δ����
			alreadApply.setTextColor(getResources().getColor(R.color.zitiyanse5));
			notApply.setTextColor(getResources().getColor(R.color.blue));
			notApply.setText(Html.fromHtml("<u>"+"δ����"+"</u>"));
			alreadApply.setText("�ѱ���");
			break;

		default:
			break;
		}
	}




	//item����
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// �����ת���������
		Intent intent = new Intent(getActivity(),Maintain_apply_DetailActivity.class);
		//Log.i("tag", indentId);
		//String indentId=list.get(position).id;
		String indentId="";
		if(statenumber==progressnumber){
			indentId=notadapter.getData().get(position).id;
		}else if(statenumber==historynumber){
			indentId=alreadadapter.getData().get(position).id;
		}
		intent.putExtra("indentId", indentId);
		startActivity(intent);
	}
	
	//��ñ��������� δ����
	private void getApplynot(final int number) {
		String url=Myconstant.MAINTAIN_APPLY_PROGRESS;
		statenumber=progressnumber;
		notadapter.tag1=0;
		notadapter.tag2=0;
		notadapter.ck1 = false;
		notadapter.ck2 = false;
		/*if(number==progressnumber){//δ����
			url = Myconstant.MAINTAIN_APPLY_PROGRESS;
			statenumber=progressnumber;
		}else if (number==historynumber) {
			url = Myconstant.MAINTAIN_APPLY_HISTORY+"beginDate="
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
							notadapter.getData().clear();
							alreadadapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								String modifyDate=js3.getString("modifyDate");
								JSONObject js4=js3.getJSONObject("fixOrder").getJSONObject("station");
								Maintain_apply_item m=new Maintain_apply_item(js3.getString("id"), js3.getString("sn"),
										js4.getString("name"), problem, modifyDate, js3.getString("amount"));//�ܽ��
								m.applyState=js3.getString("status");
								Log.i("tag", "applystate="+m.applyState);
								JSONArray ary=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								/*if(number==progressnumber){//δ����
									m.applyState="not";
								}else if (number==historynumber) {//�ѱ���
									m.applyState="alread";
								}*/
								list.add(m);
							}
							notadapter.setdate(list);
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
					Log.e("tag", "json��ȡ���ݴ���");
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
	
	//�����ʷά���е��б�����
	private void getApplyalready(final int number) {
		String url=url = Myconstant.MAINTAIN_APPLY_HISTORY+"beginDate="
				+ Myutil.get_month_time() + "&endDate=" + Myutil.get_time_ymd();
		statenumber=historynumber;
		alreadadapter.tag1=0;
		alreadadapter.tag2=0;
		alreadadapter.ck1 = false;
		alreadadapter.ck2 = false;
		/*if(number==progressnumber){//δ����
			url = Myconstant.MAINTAIN_APPLY_PROGRESS;
			statenumber=progressnumber;
		}else if (number==historynumber) {
			url = Myconstant.MAINTAIN_APPLY_HISTORY+"beginDate="
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
							notadapter.getData().clear();
							alreadadapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								String modifyDate=js3.getString("modifyDate");
								JSONObject js4=js3.getJSONObject("fixOrder").getJSONObject("station");
								Maintain_apply_item m=new Maintain_apply_item(js3.getString("id"), js3.getString("sn"),
										js4.getString("name"), problem, modifyDate, js3.getString("amount"));//�ܽ��
								m.applyState=js3.getString("status");
								Log.i("tag", "applystate="+m.applyState);
								JSONArray ary=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								/*if(number==progressnumber){//δ����
									m.applyState="not";
								}else if (number==historynumber) {//�ѱ���
									m.applyState="alread";
								}*/
								list.add(m);
							}
							alreadadapter.setdate(list);
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
					Log.e("tag", "json��ȡ���ݴ���");
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
	
	//===============================================================

	//��������
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState){  
		  
	        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://ֹͣ����  
	        {  
	            //����Ϊֹͣ����  
	            notadapter.setScrollState(false); 
	            notadapter.notifyDataSetChanged();
	            alreadadapter.setScrollState(false); 
	            alreadadapter.notifyDataSetChanged();
	            //��ǰ��Ļ��listview������ĸ���  
	            break;  
	        }  
	        case AbsListView.OnScrollListener.SCROLL_STATE_FLING://�����������׵Ķ���  
	        {  
	            //����Ϊ���ڹ���  
	            notadapter.setScrollState(true);
	            alreadadapter.setScrollState(true);
	            break;  
	        }  
	
	        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://���ڹ���  
	        {  
	            //����Ϊ���ڹ���  
	        	notadapter.setScrollState(true); 
	        	alreadadapter.setScrollState(true); 
	            break;  
	        }  
	    }
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

}
