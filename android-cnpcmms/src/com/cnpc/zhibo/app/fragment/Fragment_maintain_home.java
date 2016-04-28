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
// ά�޶˵���ҳ
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
	private RelativeLayout ksjd, wxd, fftz, xxtz, gywm, yjfk, kscz,gggl;// ��ʼ�ӵ���ά�޵�������֪ͨ����Ϣ֪ͨ���������ǡ�������������ٲ��ҡ��������
	//private ViewPager viewpager;
	//private ImageView bannerimage;//����ͼƬ
	private Fragment_notice f;// �����fragment
	//private boolean isScroller = true;// �ж��������Ź����ֵ
	//private Item_fragment_notice_adapter adapter;
	
	private List<Commonality_notice> list=new ArrayList<Commonality_notice>();
	private LayoutInflater inflater;
	//���¹���
	/*private Handler noticehandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Log.i("tag", "handleMessage");
				list.clear();
				//getNoticeinfo();// ���ع��������
				break;
			default:
				break;
			}
		};
	};*/
	/*@Override
	public void onResume() {
		super.onResume();
		setBannerScroller();//�����ֲ�
	};*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_maintain_home, container,
				false);
		setview(v);
		//���ø��¹����handler
		//SysApplication.getInstance().setNoticeHandler(noticehandler);
		return v;
	}
	
	

	// ��ȡ�����ϵĿؼ�
	private void setview(View v) {
		//bannerimage=(ImageView) v.findViewById(R.id.maintaiinhome_bannerimage);
		//inflater=LayoutInflater.from(getActivity());
		//viewpager=(ViewPager) v.findViewById(R.id.maintainhome_viewPager);
		//list.add(new Commonality_notice("��˾����", "��ӭ���,���ݼ�����...", Myutil.get_current_time(), "32005688",null));
		//adapter = new Item_fragment_notice_adapter(getActivity().getSupportFragmentManager());
		// ksjd = (RelativeLayout)
		// v.findViewById(R.id.relativelayout_fragment_maintain_home_kaishijiedan);//
		// ��ʼ�ӵ�
		wxd = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_weixiudan);// ά�޵�
		fftz = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_fenpeitongzhi);// ����֪ͨ
		xxtz = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_xiaoxitongzhi);// ��Ϣ֪ͨ
		gywm = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_guanyuwomen);// ��������
		yjfk = (RelativeLayout) v
				.findViewById(R.id.relativelayout_fragment_maintain_home_yijianfankui);// �������
		gggl=(RelativeLayout) v.findViewById(R.id.relativelayout_fragment_maintain_home_gonggaoguanli);//�������
		//kscz = (RelativeLayout) v
				//.findViewById(R.id.relativelayout_fragment_maintain_home_kuaisuchazhao);// ���ٲ���
		// ksjd.setOnClickListener(l);// ��ʼ�ӵ�
//		((MyActivity) getActivity()).setBadgerView(fftz, "worker-distribute");//����֪ͨ��������
//		((MyActivity) getActivity()).setBadgerView(gggl, "worker-notice");//���������������
		((MyActivity) getActivity()).setBadgerView(xxtz);//��Ϣ�ػ���������
		wxd.setOnClickListener(l);// ά�޵�
		fftz.setOnClickListener(l);// ����֪ͨ
		xxtz.setOnClickListener(l);// ��Ϣ֪ͨ
		gywm.setOnClickListener(l);// ��������
		yjfk.setOnClickListener(l);// �������
		gggl.setOnClickListener(l);// �������
		//kscz.setOnClickListener(l);// ���ٲ���
		
		//getNoticeinfo();// ���ع��������
		//viewpager.setAdapter(new NoticePagerAdapter(list));
		//setBannerScroller();//�����ֲ�
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/*
			 * case R.id.relativelayout_fragment_maintain_home_kaishijiedan://
			 * ��ʼ�ӵ� startActivity(new Intent(getActivity(),
			 * Maintain_home_start_itemlist_Activity.class));
			 * Myutil.set_activity_open(getActivity()); break;
			 */

			case R.id.relativelayout_fragment_maintain_home_weixiudan:// ά�޵�
				startActivity(new Intent(getActivity(),
						Maintain_home_service_itemlistActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_maintain_home_fenpeitongzhi:// ����֪ͨ
				//((MyActivity) getActivity()).clearNums("worker-distribute");//ȡ����������
				startActivity(new Intent(getActivity(),
						Maintain_home_alloction_Activity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_maintain_home_xiaoxitongzhi:// ��Ϣ֪ͨ
				((MyActivity) getActivity()).clearNums();// ȡ����������
				startActivity(new Intent(getActivity(),
						Maintain_home_newsNotificationActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_maintain_home_guanyuwomen:// ��������
				Intent in=new Intent(getActivity(), AboutUsActivity.class);
				in.putExtra("activity", "worker");
				startActivity(in );
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_maintain_home_yijianfankui:// �������
				startActivity(new Intent(getActivity(), FeedbackActivity.class));
				Myutil.set_activity_open(getActivity());
				break;
				
			case R.id.relativelayout_fragment_maintain_home_gonggaoguanli:// �������
				//((MyActivity) getActivity()).clearNums("worker-notice");//ȡ����������
				Intent in1=new Intent(getActivity(), Centre_see_noticeentryActivity.class);
				in1.putExtra("type", "worker-notice");
				startActivity(in1);
				Myutil.set_activity_open(getActivity());
				break;

			/*case R.id.relativelayout_fragment_maintain_home_kuaisuchazhao:// ���ٲ���
				startActivity(new Intent(getActivity(), QuickLookActivity.class));
				Myutil.set_activity_open(getActivity());
				break;*/

			default:
				break;
			}

		}
	};
	
	/**
	 * ��ͼƬ�Զ�����
	 */
	//boolean flag=false;//ʵ�������ֲ�
	/*private void setBannerScroller() {
		viewpager.setCurrentItem(Integer.MAX_VALUE / 2);// Ĭ�����м䣬ʹ�û��������߽�
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
			title.setText("���⣺"+notice.title);
			centent.setText("���ݣ�"+notice.content);
			time.setText("ʱ�䣺"+notice.time);
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
	
	//��ù�����Ϣ
	public void getNoticeinfo(){
		final List<Commonality_notice> list1=new ArrayList<Commonality_notice>();
		String url=Myconstant.COMMONALITY_GETNOTICEENTRY;
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
							list.add(new Commonality_notice("��˾����", "û�й�����ʾ", Myutil.get_current_time(), null,null));
						} else {
							Toast.makeText(getActivity(), "���ҵ�����", 0).show();
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
							viewpager.setCurrentItem(1000);// Ĭ�����м䣬ʹ�û��������߽�
							//GlobalConsts.isScroller=true;
						}
						

					} else {
						//���ݴ����ԭ��
						Toast.makeText(getActivity(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json�����쳣");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//����ʧ�ܵ�ԭ��
				Log.i("tag", "error="+error);
				Toast.makeText(getActivity(), "��������ʧ��"+error.getMessage(), 0).show();
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
