package com.cnpc.zhibo.app.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
//维修端的维修
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
	private TextView zzwx, ddwx, lswx;// 正在维修、等待维修、历史维修
	private RequestQueue mqueQueue;// 请求对象
	private List<Maintain_startList_item> data=new ArrayList<Maintain_startList_item>();// 数据集合
	private Item_maintain_serviceing_adapter progressadapter;//维修中
	private Item_maintain_serviceing_adapter historyadapter;//历史维修
	private List<Maintain_startList_item> list;
	private ImageView searchImageView;//搜索
	private int progressnumber=0;//维修中
	private int historynumber=1;//历史维修
	private int statenumber=0;//维修状态
	//下拉刷新控件
	private com.azy.app.news.view.pullrefresh.PullToRefreshListView mPullListView;//下拉刷新
	private ListView listview;
	private boolean refreshFlag=false;//用来判断是否正在进行刷新
	
	//更新列表***
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0://消息推送更新
				try {
					setprogresslist();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "更新列表失败，请检查网络", Toast.LENGTH_SHORT).show();
				}
				break;
			case 200://不填直返更新
				try {
					sethistorylist();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "更新列表失败，请检查网络", Toast.LENGTH_SHORT).show();
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
		//设置更新列表的handler
		SysApplication.getInstance().setListHandler("worker-fixorder", handler);
		setview(v);// 设置界面的方法
		return v;
	}
	

	// 设置界面的方法
	private void setview(View v) {
		list=new ArrayList<Maintain_startList_item>();
		mqueQueue = Volley.newRequestQueue(getActivity());// 初始化请求对象
		zzwx = (TextView) v
				.findViewById(R.id.maintainService_fragment_repairs_zzwx);// 正在维修
//		ddwx = (TextView) v
//				.findViewById(R.id.maintainService_fragment_repairs_ddwx);// 等待维修
		lswx = (TextView) v
				.findViewById(R.id.maintainService_fragment_repairs_lswx);// 历史维修
		mPullListView = (PullToRefreshListView) v.findViewById(R.id.maintainService_fragment_repairs_serviceListview);// 实例化listview
		listview = mPullListView.getRefreshableView();// 通过框架获取listview
		mPullListView.setPullLoadEnabled(false);// 设置上拉刷新不可用
		mPullListView.setLastUpdatedLabel(Myutil.get_current_time());// --设置更新的显示时间为当前
		listview.setOnItemClickListener(this);
		listview.setOnScrollListener(this);//滚动监听
		mPullListView.setOnRefreshListener(refreshListener);
		listview.setDividerHeight(0);
		listview.setSelector(new BitmapDrawable());
		
		zzwx.setOnClickListener(l);
		lswx.setOnClickListener(l);
		setbuttoncoloer(R.id.maintainService_fragment_repairs_zzwx);// 设置按钮的颜色
		//搜索
		searchImageView = (ImageView) v.findViewById(R.id.maintainService_fragment_repairs_search);
		searchImageView.setOnClickListener(l);
		
		//开始显示的维修中的界面
		setbuttoncoloer(R.id.maintainService_fragment_repairs_zzwx);
		progressadapter=new Item_maintain_serviceing_adapter(getActivity());
		historyadapter=new Item_maintain_serviceing_adapter(getActivity());
		getServiceProgress(progressnumber);
		listview.setAdapter(progressadapter);
//		Log.i("tag", "0tag1="+progressadapter.tag1);
//		Log.i("tag", "0tag2="+progressadapter.tag2);
	}

	
	// listview的上下拉滑动的监听
	private OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			progressadapter.setScrollState(false); 
			historyadapter.setScrollState(false); 
			if (refreshFlag==false) {
				refreshFlag=true;//改变刷新的状态
				if(statenumber==progressnumber){
					getServiceProgress(progressnumber);
					listview.setAdapter(progressadapter);
				}else if(statenumber==historynumber){
					getServiceHistory(progressnumber);
					listview.setAdapter(historyadapter);
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
			case R.id.maintainService_fragment_repairs_zzwx:// 正在维修
				setprogresslist();
				break;
			case R.id.maintainService_fragment_repairs_lswx:// 历史维修
				sethistorylist();
				break;
			case R.id.maintainService_fragment_repairs_search:// 搜索框的监听事件
				// 点击跳转到搜索界面
				startActivity(new Intent(getActivity(),Maintain_search_Activity.class));
				break;
			default:
				break;
			}
		}

	};
	
	//历史维修列表
	public void sethistorylist(){
		setbuttoncoloer(R.id.maintainService_fragment_repairs_lswx);
		list.clear();
		historyadapter.getData().clear();
		historyadapter.setdate(list);
		getServiceHistory(historynumber);
		listview.setAdapter(historyadapter);
	}
	//维修中列表
	public void setprogresslist(){
		setbuttoncoloer(R.id.maintainService_fragment_repairs_zzwx);
		list.clear();
		progressadapter.getData().clear();
		progressadapter.setdate(list);
		getServiceProgress(progressnumber);
		listview.setAdapter(progressadapter);
	}

	// 改变按钮字体颜色的方法
	private void setbuttoncoloer(int id) {
		switch (id) {
		case R.id.maintainService_fragment_repairs_zzwx:// 正在维修
			lswx.setTextColor(getResources().getColor(R.color.zitiyanse5));
			zzwx.setTextColor(getResources().getColor(R.color.blue));
			zzwx.setText(Html.fromHtml("<u>"+"维修中"+"</u>"));
			lswx.setText("历史维修");
			break;
		case R.id.maintainService_fragment_repairs_lswx:// 历史维修
			zzwx.setTextColor(getResources().getColor(R.color.zitiyanse5));
			lswx.setTextColor(getResources().getColor(R.color.blue));
			lswx.setText(Html.fromHtml("<u>"+"历史维修"+"</u>"));
			zzwx.setText("维修中");
			break;

		default:
			break;
		}
	}



	//监听
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 点击跳转到详情界面
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
	
	
	
	// 获取历史维修、维修中的列表
	private void getServiceProgress(final int number) {
		String url=Myconstant.MAINTAIN_SERVICE_PROGRESS;
		statenumber=progressnumber;
		progressadapter.tag1=0;
		progressadapter.tag2=0;
		progressadapter.ck1 = false;
		progressadapter.ck2 = false;
		/*if(number==progressnumber){//维修中
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
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {

						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getActivity(), "没有找到数据", 0).show();
						} else {
							Toast.makeText(getActivity(), "已找到数据", 0).show();
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
								//星星的个数
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
									//1天=86 400 000毫秒 1小时3.6 半小时1.8
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
	
	// 获取历史维修、的列表
	private void getServiceHistory(final int number) {
		String url=Myconstant.MAINTAIN_SERVICE_HISTORY+"beginDate="
				+ Myutil.get_month_time() + "&endDate=" + Myutil.get_time_ymd();
		statenumber=historynumber;
		historyadapter.tag1=0;
		historyadapter.tag2=0;
		historyadapter.ck1 = false;
		historyadapter.ck2 = false;
		/*if(number==progressnumber){//维修中
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
					JSONObject js = new JSONObject(response);// 拿到json数据
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
	
						JSONArray js2 = js.getJSONArray("body");
						if (js2.length() == 0) {
							Toast.makeText(getActivity(), "没有找到数据", 0).show();
						} else {
							Toast.makeText(getActivity(), "已找到数据", 0).show();
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
								//星星的个数
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
									//1天=86 400 000毫秒 1小时3.6 半小时1.8
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
	//======================================================================================



	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		 switch (scrollState){  
		  
         case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止滚动  
         {  
             //设置为停止滚动  
             progressadapter.setScrollState(false); 
             progressadapter.notifyDataSetChanged();
             historyadapter.setScrollState(false);
             historyadapter.notifyDataSetChanged();
             //当前屏幕中listview的子项的个数  
            /* int count = view.getChildCount();  
             Log.e("MainActivity",count+"");  

             for (int i = 0; i < count; i++) {  
                 //获取到item的name  
                 TextView tv_name = (TextView) view.getChildAt(i).findViewById(R.id.main_item_tv_name);  
                 //获取到item的头像  
                 ImageView iv_show= (ImageView) view.getChildAt(i).findViewById(R.id.main_item_iv_icon);  

                 if (tv_name.getTag() != null) { //非null说明需要加载数据  
                     tv_name.setText(tv_name.getTag().toString());//直接从Tag中取出我们存储的数据name并且赋值  
                     tv_name.setTag(null);//设置为已加载过数据  
                 }  

                 if (!iv_show.getTag().equals("1")){//!="1"说明需要加载数据  
                     String image_url=iv_show.getTag().toString();//直接从Tag中取出我们存储的数据image——url  
                     ImageLoader.getInstance().displayImage(image_url, iv_show);//显示图片  
                     iv_show.setTag("1");//设置为已加载过数据  
                 }  
             }*/  
             break;  
         }  
         case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动做出了抛的动作  
         {  
             //设置为正在滚动  
             progressadapter.setScrollState(true);
             historyadapter.setScrollState(true);
             break;  
         }  

         case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://正在滚动  
         {  
             //设置为正在滚动  
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
