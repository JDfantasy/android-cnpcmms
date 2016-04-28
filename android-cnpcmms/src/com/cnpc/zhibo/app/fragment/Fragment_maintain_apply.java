package com.cnpc.zhibo.app.fragment;

import java.lang.ref.WeakReference;
//维修端的报销界面
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
	private TextView alreadApply,notApply;// 已报销、未报销
	private RequestQueue mqueQueue;// 请求对象
	private Item_maintain_apply_adapter notadapter;// 未报销适配器
	private Item_maintain_apply_adapter alreadadapter;// 已报销适配器
	private List<Maintain_apply_item> list;// 报销单集合
	//下拉刷新控件
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//下拉刷新
	private ListView listview;
	private boolean refreshFlag=false;//用来判断是否正在进行刷新
	private ImageView searchImageView;//搜索
	private int progressnumber=0;//未报销
	private int historynumber=1;//已报销
	private int statenumber=0;//维修状态
	
	//更新列表***
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				//Log.i("tag", "=====case 100");
			case 0:
				try {
					setnotApplylist();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "更新列表失败，请检查网络", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(fragment.getActivity(), "更新列表失败，请检查网络", Toast.LENGTH_SHORT).show();
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
		setview(v);// 设置界面的方法
		SysApplication.getInstance().setListHandler("worker-expenseorder", handler);
		//new Maintain_apply_modifyapplybillActivity().setListHandler(handler);
		return v;
	}

	// 设置界面的方法
	private void setview(View v) {
		list=new ArrayList<Maintain_apply_item>();
		mqueQueue = Volley.newRequestQueue(getActivity());// 初始化请求对象
		alreadApply = (TextView) v
				.findViewById(R.id.textview_fragment_maintain_alreadapply);//已报销
		notApply = (TextView) v
				.findViewById(R.id.textview_fragment_maintain_notapply);//未报销
		alreadApply.setOnClickListener(l);
		notApply.setOnClickListener(l);
		//刷新的初始化
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.maintainApply_fragment_applyListview);// 实例化listview
		listview = mPullListView.getRefreshableView();// 通过框架获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新不可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间为当前
		listview.setOnItemClickListener(this);
		mPullListView.setOnRefreshListener(refreshListener);
		listview.setDividerHeight(0);
		listview.setSelector(new BitmapDrawable());
		listview.setOnScrollListener(this);//滚动监听
		
		//搜索
		searchImageView = (ImageView) v.findViewById(R.id.maintainApply_fragment_apply_search);
		searchImageView.setOnClickListener(l);
		//数据呈现
		setbuttoncoloer(R.id.textview_fragment_maintain_notapply);// 设置按钮的颜色
		notadapter = new Item_maintain_apply_adapter(getActivity());// 实例化适配器
		alreadadapter = new Item_maintain_apply_adapter(getActivity());// 实例化适配器
		getApplynot(progressnumber);
		listview.setAdapter(notadapter);// 设置适配器
	}
	
	
	// listview的上下拉滑动的监听
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			notadapter.setScrollState(false); 
			alreadadapter.setScrollState(false); 
			if (refreshFlag==false) {
				refreshFlag=true;//改变刷新的状态
				if(statenumber==progressnumber){
					getApplynot(progressnumber);
				}else if(statenumber==historynumber){
					getApplyalready(historynumber);
				}
				Toast.makeText(getActivity(), "下拉刷新", Toast.LENGTH_SHORT).show();
			} 
			mPullListView.onPullDownRefreshComplete();//关闭刷新
			refreshFlag=false;//改变是否刷新的状态
			

		}
		//设置下拉刷新是否可以使用
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			/*if (state == false) {
				get_historydata();// 获取历史维修的报修单的列表
			} else {
				get_progressdata();// 获取等待维修中的报修单的列表
			}*/
			Toast.makeText(getActivity(), "上拉刷新", Toast.LENGTH_SHORT).show();
			mPullListView.onPullUpRefreshComplete();//关闭刷新
			refreshFlag=false;//改变是否刷新的状态
		}

	};
		
	

	// 监听方法
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textview_fragment_maintain_alreadapply:// 已报销
				setalreadApplylist();
				break;
			case R.id.textview_fragment_maintain_notapply:// 未报销
				setnotApplylist();
				break;
			case R.id.maintainApply_fragment_apply_search:// 搜索框的监听事件
				//TODO
				startActivity(new Intent(getActivity(),Maintain_searchapplylist_Activity.class));
				break;
			default:
				break;
			}
		}

	};
	
	//设置未报销列表
	public void setnotApplylist(){
		setbuttoncoloer(R.id.textview_fragment_maintain_notapply);
		list.clear();
		notadapter.getData().clear();
		notadapter.setdate(list);
		getApplynot(progressnumber);
		listview.setAdapter(notadapter);
	}
	//设置已报销列表
	public void setalreadApplylist(){
		setbuttoncoloer(R.id.textview_fragment_maintain_alreadapply);
		list.clear();
		alreadadapter.getData().clear();
		alreadadapter.setdate(list);
		getApplyalready(historynumber);
		listview.setAdapter(alreadadapter);
	}
	

	// 改变按钮字体颜色的方法
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.textview_fragment_maintain_alreadapply:// 已报销
			notApply.setTextColor(getResources().getColor(R.color.zitiyanse5));
			alreadApply.setTextColor(getResources().getColor(R.color.blue));
			alreadApply.setText(Html.fromHtml("<u>"+"已报销"+"</u>"));
			notApply.setText("未报销");
			break;
		case R.id.textview_fragment_maintain_notapply:// 未报销
			alreadApply.setTextColor(getResources().getColor(R.color.zitiyanse5));
			notApply.setTextColor(getResources().getColor(R.color.blue));
			notApply.setText(Html.fromHtml("<u>"+"未报销"+"</u>"));
			alreadApply.setText("已报销");
			break;

		default:
			break;
		}
	}




	//item监听
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 点击跳转到详情界面
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
	
	//获得报销单数据 未报销
	private void getApplynot(final int number) {
		String url=Myconstant.MAINTAIN_APPLY_PROGRESS;
		statenumber=progressnumber;
		notadapter.tag1=0;
		notadapter.tag2=0;
		notadapter.ck1 = false;
		notadapter.ck2 = false;
		/*if(number==progressnumber){//未报销
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
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getActivity(), "没有找到数据", 0).show();
						} else {
							Toast.makeText(getActivity(), "已找到数据", 0).show();
							list.clear();
							notadapter.getData().clear();
							alreadadapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								String modifyDate=js3.getString("modifyDate");
								JSONObject js4=js3.getJSONObject("fixOrder").getJSONObject("station");
								Maintain_apply_item m=new Maintain_apply_item(js3.getString("id"), js3.getString("sn"),
										js4.getString("name"), problem, modifyDate, js3.getString("amount"));//总金额
								m.applyState=js3.getString("status");
								Log.i("tag", "applystate="+m.applyState);
								JSONArray ary=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								/*if(number==progressnumber){//未报销
									m.applyState="not";
								}else if (number==historynumber) {//已报销
									m.applyState="alread";
								}*/
								list.add(m);
							}
							notadapter.setdate(list);
							mPullListView.onPullDownRefreshComplete();//关闭刷新
							mPullListView.onPullUpRefreshComplete();
							refreshFlag=false;//改变是否刷新的状态
						}

					} else {
						//数据错误的原因
						//Toast.makeText(getActivity(), js1.getString("content"), 0).show();
						Toast.makeText(getActivity(), "数据错误", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json获取数据错误");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//请求失败的原因
				//Log.i("tag", "error="+error);
				//Toast.makeText(getActivity(), "请求数据失败"+error.getMessage(), 0).show();
				Toast.makeText(getActivity(), "请求数据失败,请检查网络", 0).show();
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
	
	//获得历史维修中的列表数据
	private void getApplyalready(final int number) {
		String url=url = Myconstant.MAINTAIN_APPLY_HISTORY+"beginDate="
				+ Myutil.get_month_time() + "&endDate=" + Myutil.get_time_ymd();
		statenumber=historynumber;
		alreadadapter.tag1=0;
		alreadadapter.tag2=0;
		alreadadapter.ck1 = false;
		alreadadapter.ck2 = false;
		/*if(number==progressnumber){//未报销
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
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getActivity(), "没有找到数据", 0).show();
						} else {
							Toast.makeText(getActivity(), "已找到数据", 0).show();
							list.clear();
							notadapter.getData().clear();
							alreadadapter.getData().clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								String problem=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONObject("project").getString("name")+js3.getString("name");
								String modifyDate=js3.getString("modifyDate");
								JSONObject js4=js3.getJSONObject("fixOrder").getJSONObject("station");
								Maintain_apply_item m=new Maintain_apply_item(js3.getString("id"), js3.getString("sn"),
										js4.getString("name"), problem, modifyDate, js3.getString("amount"));//总金额
								m.applyState=js3.getString("status");
								Log.i("tag", "applystate="+m.applyState);
								JSONArray ary=js3.getJSONObject("fixOrder").getJSONObject("repairOrder").getJSONArray("repairOrderImages");
								if(ary.length()==0){
									m.iconPath="";
								}else if(ary.length()!=0){
									m.iconPath=Myconstant.WEBPAGEPATHURL+ary.getJSONObject(0).getString("source");
								}
								/*if(number==progressnumber){//未报销
									m.applyState="not";
								}else if (number==historynumber) {//已报销
									m.applyState="alread";
								}*/
								list.add(m);
							}
							alreadadapter.setdate(list);
							mPullListView.onPullDownRefreshComplete();//关闭刷新
							mPullListView.onPullUpRefreshComplete();
							refreshFlag=false;//改变是否刷新的状态
						}

					} else {
						//数据错误的原因
						//Toast.makeText(getActivity(), js1.getString("content"), 0).show();
						Toast.makeText(getActivity(), "数据错误", 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json获取数据错误");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//请求失败的原因
				//Log.i("tag", "error="+error);
				//Toast.makeText(getActivity(), "请求数据失败"+error.getMessage(), 0).show();
				Toast.makeText(getActivity(), "请求数据失败,请检查网络", 0).show();
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

	//滚动监听
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState){  
		  
	        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止滚动  
	        {  
	            //设置为停止滚动  
	            notadapter.setScrollState(false); 
	            notadapter.notifyDataSetChanged();
	            alreadadapter.setScrollState(false); 
	            alreadadapter.notifyDataSetChanged();
	            //当前屏幕中listview的子项的个数  
	            break;  
	        }  
	        case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动做出了抛的动作  
	        {  
	            //设置为正在滚动  
	            notadapter.setScrollState(true);
	            alreadadapter.setScrollState(true);
	            break;  
	        }  
	
	        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://正在滚动  
	        {  
	            //设置为正在滚动  
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
