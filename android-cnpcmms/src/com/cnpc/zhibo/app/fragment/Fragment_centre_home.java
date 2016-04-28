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
//վ���ε���ҳ
import com.cnpc.zhibo.app.AboutUsActivity;
import com.cnpc.zhibo.app.Centre_select_ItemActivity;
import com.cnpc.zhibo.app.Centre_See_repairs_messageActivity;
import com.cnpc.zhibo.app.Centre_See_examine_itemActivity;
import com.cnpc.zhibo.app.Centre_credential_entryActivity;
import com.cnpc.zhibo.app.Centre_lead_wait_itemlist_Activity;
import com.cnpc.zhibo.app.Centre_see_repairs_entryActivity;
import com.cnpc.zhibo.app.Centre_see_noticeentryActivity;
import com.cnpc.zhibo.app.FeedbackActivity;
import com.cnpc.zhibo.app.Maintain_home_newsNotificationActivity;
import com.cnpc.zhibo.app.MyActivity;
import com.cnpc.zhibo.app.NoticedetailsActivity;
import com.cnpc.zhibo.app.QuickLookActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.adapter.Item_fragment_notice_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_approve;
import com.cnpc.zhibo.app.entity.Commonality_notice;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.BadgeView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView.SelectionBoundsAdjuster;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_centre_home extends Fragment {
	private RelativeLayout examine, repairsitem, wybx, xxhh, gywm, yjfk, zjgl,gggl;
	// �����������޵�����Ҫ���ޡ�ά��Ա��Ϣ���������ǡ������������֤������
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_centre_home, container, false);
		setview(v);
		return v;
	}

	// ��ȡ�����ϵĿؼ�
	private void setview(View v) {
		examine = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_centre_home_shenpiguanli);// ��������
		repairsitem = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_centre_home_weixiudan);// ���޵�
		wybx = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_centre_home_woyaobaoxiu);// ��Ҫ����
		xxhh = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_centre_home_weixiuyuanxiaoxi);//��Ϣ�ػ�
		gywm = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_centre_home_guanyuwomen);// ��������
		yjfk = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_centre_home_yijianfankui);// �������
		zjgl = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_centre_home_zhengjianguanli);// ֤������
		gggl = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_centre_home_gonggaoguanli);//�������
		examine.setOnClickListener(l);// ��������
		repairsitem.setOnClickListener(l);// ά�޵�
		wybx.setOnClickListener(l);// ��Ҫ����
		xxhh.setOnClickListener(l);// ��Ϣ�Ự
		gywm.setOnClickListener(l);// ��������
		yjfk.setOnClickListener(l);// �������
		zjgl.setOnClickListener(l);// ֤������
		gggl.setOnClickListener(l);// �������
		((MyActivity) getActivity()).setBadgerView(repairsitem, "site-repairorder", repairsitem_helper);// ���޵���������
		((MyActivity) getActivity()).setBadgerView(examine, "site-approve", examine_helper);// ����������������
		((MyActivity) getActivity()).setBadgerView(zjgl, "site-certificate", zjgl_helper);// ֤��������������
		//((MyActivity) getActivity()).setBadgerView(gggl, "site-notice", gggl_helper);//���������������

		((MyActivity) getActivity()).setBadgerView(xxhh);//��Ϣ�ػ���������
	}
	/*
	 * ʵ�ֱ��޵�������ʾ��ֵ�Ľӿ�
	 */
	private CornerUtil.OrderNumsHelper repairsitem_helper = new CornerUtil.OrderNumsHelper() {

		@Override
		public void setViewNums(BadgeView badgeView) {
			String url = Myconstant.CENTRE_GETREPAIRORDE_COUNTRNUMBER;
			get_untreated_repairsnumber(badgeView, url);

		}
	};
	/*
	 * ʵ����������������ʾ��ֵ�Ľӿ�
	 */
	private CornerUtil.OrderNumsHelper examine_helper = new CornerUtil.OrderNumsHelper() {

		@Override
		public void setViewNums(BadgeView badgeView) {
			String url = Myconstant.CENTRE_GETFIXORDER_COUNTRNUMBERAPPROVE;
			get_untreated_repairsnumber(badgeView, url);
		}
	};
	/*
	 * ʵ��֤������������ʾ��ֵ�Ľӿ�
	 */
	private CornerUtil.OrderNumsHelper zjgl_helper = new CornerUtil.OrderNumsHelper() {

		@Override
		public void setViewNums(BadgeView badgeView) {
			String url = Myconstant.CENTRE_GETCERTIFICATE_COUNTRNUMBER;
			get_untreated_repairsnumber(badgeView, url);
		}
	};
	/*
	 * ʵ�ֹ������������ʾ��ֵ�Ľӿ�
	 */
	private CornerUtil.OrderNumsHelper gggl_helper = new CornerUtil.OrderNumsHelper() {

		@Override
		public void setViewNums(BadgeView badgeView) {
			String url = Myconstant.CENTRE_GETREPAIRORDE_COUNTRNUMBER;
			get_untreated_repairsnumber(badgeView, url);
		}
	};
	/*
	 * ��ȡͳ�ƽ���ķ���
	 */
	private void get_untreated_repairsnumber(final BadgeView badgeView,String url){
		
		StringRequest re = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						String number=js.getString("body");
						if ("0".equals(number)) {
							badgeView.hide();
						} else {
							badgeView.setText(number);
							badgeView.show();
						}
						
					} else {
						Toast.makeText(getActivity(), js1.getString("content"), 0).show();
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
	
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relativelayout_fragment_centre_home_shenpiguanli:// ��������
				//((MyActivity) getActivity()).clearNums("site-approve");
				startActivity(new Intent(getActivity(), Centre_lead_wait_itemlist_Activity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_centre_home_weixiudan:// ���޵�
				startActivity(new Intent(getActivity(), Centre_see_repairs_entryActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_centre_home_woyaobaoxiu:// ��Ҫ����
				startActivity(new Intent(getActivity(), Centre_select_ItemActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_centre_home_weixiuyuanxiaoxi:// ��Ϣ�Ự
				((MyActivity) getActivity()).clearNums();// ȡ����������
				startActivity(new Intent(getActivity(), Maintain_home_newsNotificationActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_centre_home_guanyuwomen:// ��������
				Intent in=new Intent(getActivity(), AboutUsActivity.class);
				in.putExtra("activity", "site");
				startActivity(in );
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_centre_home_yijianfankui:// �������
				startActivity(new Intent(getActivity(), FeedbackActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_centre_home_zhengjianguanli:// ֤������
				startActivity(new Intent(getActivity(), Centre_credential_entryActivity.class));
				Myutil.set_activity_open(getActivity());
				break;
			case R.id.relativelayout_fragment_centre_home_gonggaoguanli://�������
				//((MyActivity) getActivity()).clearNums("site-notice");// ȡ����������
				Intent in1=new Intent(getActivity(), Centre_see_noticeentryActivity.class);
				in1.putExtra("type", "site-notice");
				startActivity(in1);
				Myutil.set_activity_open(getActivity());
			default:
				break;
			}

		}
	};

	

}
