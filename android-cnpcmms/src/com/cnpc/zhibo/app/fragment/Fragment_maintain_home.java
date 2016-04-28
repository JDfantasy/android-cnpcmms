package com.cnpc.zhibo.app.fragment;

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
import com.cnpc.zhibo.app.AboutUsActivity;
import com.cnpc.zhibo.app.Centre_see_noticeentryActivity;
import com.cnpc.zhibo.app.FeedbackActivity;
import com.cnpc.zhibo.app.Maintain_home_alloction_Activity;
import com.cnpc.zhibo.app.Maintain_home_newsNotificationActivity;
import com.cnpc.zhibo.app.Maintain_home_service_itemlistActivity;
import com.cnpc.zhibo.app.MyActivity;
import com.cnpc.zhibo.app.NoticedetailsActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.adapter.Item_fragment_notice_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Commonality_notice;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.entity.Maintainhome_Notice;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.Activity;
// 维修端的首页
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_maintain_home extends Fragment {
	private RelativeLayout ksjd, wxd, fftz, xxtz, gywm, yjfk, kscz,gggl;// 开始接单、维修单、分配通知、消息通知、关于我们、意见反馈、快速查找、公告管理
	//private ViewPager viewpager;
	//private ImageView bannerimage;//顶部图片
	private Fragment_notice f;// 公告的fragment
	//private boolean isScroller = true;// 判断轮流播放公告的值
	//private Item_fragment_notice_adapter adapter;
	
	private List<Commonality_notice> list=new ArrayList<Commonality_notice>();
	private LayoutInflater inflater;
	//更新公告
	/*private Handler noticehandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Log.i("tag", "handleMessage");
				list.clear();
				//getNoticeinfo();// 加载公告的数据
				break;
			default:
				break;
			}
		};
	};*/
	/*@Override
	public void onResume() {
		super.onResume();
		setBannerScroller();//公告轮播
	};*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_maintain_home, container,
				false);
		setview(v);
		//设置更新公告的handler
		//SysApplication.getInstance().setNoticeHandler(noticehandler);
		return v;
	}
	
	

	// 获取界面上的控件
	private void setview(View v) {
		//bannerimage=(ImageView) v.findViewById(R.id.maintaiinhome_bannerimage);
		//inflater=LayoutInflater.from(getActivity());
		//viewpager=(ViewPager) v.findViewById(R.id.maintainhome_viewPager);
		//list.add(new Commonality_notice("公司公告", "欢迎浏览,数据加载中...", Myutil.get_current_time(), "32005688",null));
		//adapter = new Item_fragment_notice_adapter(getActivity().getSupportFragmentManager());
		// ksjd = (RelativeLayout)
		// v.findViewById(R.id.relativelayout_fragment_maintain_home_kaishijiedan);//
		// 开始接单
		wxd = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_weixiudan);// 维修单
		fftz = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_fenpeitongzhi);// 分配通知
		xxtz = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_xiaoxitongzhi);// 消息通知
		gywm = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_guanyuwomen);// 关于我们
		yjfk = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_yijianfankui);// 意见反馈
		gggl=(RelativeLayout) v.findViewById(R.id.relativelayout_fragment_maintain_home_gonggaoguanli);//公告管理
		//kscz = (RelativeLayout) v
				//.findViewById(R.id.relativelayout_fragment_maintain_home_kuaisuchazhao);// 快速查找
		// ksjd.setOnClickListener(l);// 开始接单
//		((MyActivity) getActivity()).setBadgerView(fftz, "worker-distribute");//分配通知设置数字
//		((MyActivity) getActivity()).setBadgerView(gggl, "worker-notice");//公告管理设置数字
		((MyActivity) getActivity()).setBadgerView(xxtz);//消息回话设置数字
		wxd.setOnClickListener(l);// 维修单
		fftz.setOnClickListener(l);// 分配通知
		xxtz.setOnClickListener(l);// 消息通知
		gywm.setOnClickListener(l);// 关于我们
		yjfk.setOnClickListener(l);// 意见反馈
		gggl.setOnClickListener(l);// 公告管理
		//kscz.setOnClickListener(l);// 快速查找
		
		//getNoticeinfo();// 加载公告的数据
		//viewpager.setAdapter(new NoticePagerAdapter(list));
		//setBannerScroller();//公告轮播
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/*
			 * case R.id.relativelayout_fragment_maintain_home_kaishijiedan://
			 * 开始接单 startActivity(new Intent(getActivity(),
			 * Maintain_home_start_itemlist_Activity.class));
			 * Myutil.set_activity_open(getActivity()); break;
			 */

			case R.id.relativelayout_fragment_maintain_home_weixiudan:// 维修单
				startActivity(new Intent(getActivity(),
						Maintain_home_service_itemlistActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_maintain_home_fenpeitongzhi:// 分配通知
				//((MyActivity) getActivity()).clearNums("worker-distribute");//取消数字提醒
				startActivity(new Intent(getActivity(),
						Maintain_home_alloction_Activity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_maintain_home_xiaoxitongzhi:// 消息通知
				((MyActivity) getActivity()).clearNums();// 取消数字提醒
				startActivity(new Intent(getActivity(),
						Maintain_home_newsNotificationActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_maintain_home_guanyuwomen:// 关于我们
				Intent in=new Intent(getActivity(), AboutUsActivity.class);
				in.putExtra("activity", "worker");
				startActivity(in );
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_maintain_home_yijianfankui:// 意见反馈
				startActivity(new Intent(getActivity(), FeedbackActivity.class));
				Myutil.set_activity_open(getActivity());
				break;
				
			case R.id.relativelayout_fragment_maintain_home_gonggaoguanli:// 公告管理
				//((MyActivity) getActivity()).clearNums("worker-notice");//取消数字提醒
				Intent in1=new Intent(getActivity(), Centre_see_noticeentryActivity.class);
				in1.putExtra("type", "worker-notice");
				startActivity(in1);
				Myutil.set_activity_open(getActivity());
				break;

			/*case R.id.relativelayout_fragment_maintain_home_kuaisuchazhao:// 快速查找
				startActivity(new Intent(getActivity(), QuickLookActivity.class));
				Myutil.set_activity_open(getActivity());
				break;*/

			default:
				break;
			}

		}
	};
	
	/**
	 * 置图片自动播放
	 */
	//boolean flag=false;//实现正反轮播
	/*private void setBannerScroller() {
		viewpager.setCurrentItem(Integer.MAX_VALUE / 2);// 默认在中间，使用户看不到边界
				final Handler h = new Handler();
			h.postDelayed(new Runnable() {
				@Override
				public void run() {
					//viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
					Log.i("tag", "threadid_Fragment_maintain_home="+Thread.currentThread().getId()+"");
					if(isScroller){
						int index = viewpager.getCurrentItem();
						int count = adapter.getCount() - 1;
						if (index == 0) {
							flag = false;
						}
						if(index == count){
							flag =true;
						}
						if (!flag) {
							viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
						}
						else{
							viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
						}
						h.postDelayed(this,500);
					}
					
				}
			}, 10000);
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				//Log.i("tag", "threadid_Fragment_maintain_home="+Thread.currentThread().getId()+"");
				viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
				if (GlobalConsts.isScroller) {
					h.postDelayed(this, 10000);
				}
			}
		}, 10000);
			
	}
	
	class NoticePagerAdapter extends PagerAdapter {
		private List<Commonality_notice> tempLists;
		public NoticePagerAdapter(List<Commonality_notice> lists) {
			this.tempLists=lists;
		}
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}
	
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View v=inflater.inflate(R.layout.maintainhome_notice, container, false);
			TextView title = (TextView) v.findViewById(R.id.textView_maintainhome_notice_title);
			TextView centent = (TextView) v.findViewById(R.id.textView_maintainhome_notice_content);
			TextView time = (TextView) v.findViewById(R.id.textView_maintainhome_notice_time);
			Commonality_notice notice=tempLists.get(position%tempLists.size());
			title.setText("标题："+notice.title);
			centent.setText("内容："+notice.content);
			time.setText("时间："+notice.time);
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(),NoticedetailsActivity.class);
					Commonality_notice notice=tempLists.get(position%tempLists.size());
					intent.putExtra("noticeId", notice.id);
					intent.putExtra("userName", Myconstant.userid);
					startActivity(intent);
				}
			});
			container.addView(v);
			return v;
		}
	}
	
	//获得公告信息
	public void getNoticeinfo(){
		final List<Commonality_notice> list1=new ArrayList<Commonality_notice>();
		String url=Myconstant.COMMONALITY_GETNOTICEENTRY;
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
							list.add(new Commonality_notice("公司公告", "没有公告显示", Myutil.get_current_time(), null,null));
						} else {
							Toast.makeText(getActivity(), "已找到数据", 0).show();
							list.clear();
							for (int i = 0; i < js2.length(); i++) {
								JSONObject js3 = js2.getJSONObject(i);
								f = new Fragment_notice(new Commonality_notice(js3.getString("title"),
										js3.getString("content"), js3.getString("createDate"), js3.getString("id")));
								adapter.addfragmentdata2(f);
								Commonality_notice c=new Commonality_notice(js3.getString("title"),
										js3.getString("content"), js3.getString("createDate"), 
										js3.getString("id"),js3.getJSONObject("reporter").getString("username"));
								list.add(c);
								
							}
							Log.i("tag", "list.size()="+list.size());
							viewpager.setAdapter(new NoticePagerAdapter(list));
							viewpager.setCurrentItem(1000);// 默认在中间，使用户看不到边界
							//GlobalConsts.isScroller=true;
						}
						

					} else {
						//数据错误的原因
						Toast.makeText(getActivity(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json解析异常");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//请求失败的原因
				Log.i("tag", "error="+error);
				Toast.makeText(getActivity(), "请求数据失败"+error.getMessage(), 0).show();
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

	}*/

}
