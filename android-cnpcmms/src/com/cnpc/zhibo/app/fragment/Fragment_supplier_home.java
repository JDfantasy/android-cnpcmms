package com.cnpc.zhibo.app.fragment;

import com.cnpc.zhibo.app.AboutUsActivity;
import com.cnpc.zhibo.app.FeedbackActivity;
import com.cnpc.zhibo.app.Maintain_home_newsNotificationActivity;
import com.cnpc.zhibo.app.MyActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.Supplier_Function_IntroduceActivity;
import com.cnpc.zhibo.app.Supplier_dingdanfenpeiActivity;
import com.cnpc.zhibo.app.Supplier_fenpeiguanliActivity;
import com.cnpc.zhibo.app.Supplier_gonggaoActivity;
import com.cnpc.zhibo.app.Supplier_weixiudanguanliActivity;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ��Ӧ�̶���ҳ
 */
public class Fragment_supplier_home extends Fragment {
	private RelativeLayout ddfp, xxtz, fpgl, wxygl, gywm, yjfk, fbgg;// �������䡢��Ϣ֪ͨ���������ά��Ա�����������ǡ������������������
	// private TextView gongGao;
	// private ViewPager mPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_supplier_home, container, false);
		setView(v);
		// setBannerScroller();
		return v;
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setView(View v) {
		ddfp = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_supplier_home_dingdanfenpei);// ��������
		// wxdgl = (RelativeLayout)
		// v.findViewById(R.id.relativelayout_fragment_supplier_home_weixiudanguanli);//
		// ά�޵�����
		fpgl = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_supplier_home_fenpeiguanli);// �������
		// wxygl = (RelativeLayout) v
		// .findViewById(R.id.relativelayout_fragment_supplier_home_weixiuyuanguanli);//
		// ά��Ա����
		gywm = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_supplier_home_guanyuwomen);// ��������
		yjfk = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_supplier_home_yijianfankui);// �������
		fbgg = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_supplier_home_fabugonggao);// ��������
		xxtz = (RelativeLayout) v.findViewById(R.id.relativelayout_fragment_supplier_home_xiaoxitongzhi);// ��Ϣ֪ͨ
		//((MyActivity) getActivity()).setBadgerView(ddfp, "provider-distribute");// ����������������
		((MyActivity) getActivity()).setBadgerView(xxtz);//��Ϣ�ػ���������
		// gongGao = (TextView) v
		// .findViewById(R.id.textView_fragment_supplier_home_gonggao);
		ddfp.setOnClickListener(l);// ��������
		// wxdgl.setOnClickListener(l);// ά�޵�����
		fpgl.setOnClickListener(l);// �������
		// wxygl.setOnClickListener(l);// ά��Ա����
		gywm.setOnClickListener(l);// ��������
		yjfk.setOnClickListener(l);// �������
		// kscz.setOnClickListener(l);// ���ٲ���
		fbgg.setOnClickListener(l);// ����
		xxtz.setOnClickListener(l);// ��Ϣ֪ͨ
		// mPager = (ViewPager) v.findViewById(R.id.viewPager);
		// mPager.setAdapter(new BannerAdapter());
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relativelayout_fragment_supplier_home_dingdanfenpei:// ��������
				//((MyActivity) getActivity()).clearNums("provider-distribute");
				startActivity(new Intent(getActivity(), Supplier_dingdanfenpeiActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			// case
			// R.id.relativelayout_fragment_supplier_home_weixiudanguanli://
			// ά�޵�����
			// startActivity(new Intent(getActivity(),
			// Supplier_weixiudanguanliActivity.class));
			// Myutil.set_activity_open(getActivity());
			// break;

			case R.id.relativelayout_fragment_supplier_home_fenpeiguanli:// �������
				startActivity(new Intent(getActivity(), Supplier_fenpeiguanliActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			// case
			// R.id.relativelayout_fragment_supplier_home_weixiuyuanguanli://
			// ά��Ա����
			// startActivity(new Intent(getActivity(),
			// Supplier_weixiudanguanliActivity.class));
			// Myutil.set_activity_open(getActivity());
			// break;

			case R.id.relativelayout_fragment_supplier_home_guanyuwomen:// ��������
				Intent in=new Intent(getActivity(), AboutUsActivity.class);
				in.putExtra("activity", "provider");
				startActivity(in );
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_supplier_home_yijianfankui:// �������
				startActivity(new Intent(getActivity(), FeedbackActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_supplier_home_fabugonggao:// �������
				startActivity(new Intent(getActivity(), Supplier_gonggaoActivity.class));
				// startActivity(new Intent(getActivity(),
				// Supplier_NoticeAdministrationActivity.class));
				Myutil.set_activity_open(getActivity());
				break;

			case R.id.relativelayout_fragment_supplier_home_xiaoxitongzhi:
				((MyActivity) getActivity()).clearNums();// ȡ����������
				startActivity(new Intent(getActivity(), Maintain_home_newsNotificationActivity.class));
				Myutil.set_activity_open(getActivity());
				break;
			default:
				break;
			}

		}
	};

	// private boolean isScroller = true;
	//
	// /**
	// * ��ͼƬ�Զ�����
	// */
	// private void setBannerScroller() {
	// final Handler h = new Handler();
	// h.postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// mPager.setCurrentItem(mPager.getCurrentItem() + 1);
	// if (isScroller) {
	// h.postDelayed(this, 10000);
	// }
	// }
	// }, 10000);
	// }
	//
	// class BannerAdapter extends PagerAdapter {
	// @Override
	// public int getCount() {
	// return Integer.MAX_VALUE;
	// }
	//
	// @Override
	// public boolean isViewFromObject(View arg0, Object arg1) {
	// return arg0 == arg1;
	// }
	//
	// @Override
	// public void destroyItem(ViewGroup container, int position, Object object)
	// {
	// container.removeView((View) object);
	// }
	//
	// @Override
	// public Object instantiateItem(ViewGroup container, final int position) {
	// TextView textView = new TextView(getActivity());
	// textView.setText(bText[position % bText.length]);
	// container.addView(textView);
	//
	// return textView;
	// }
	// }
	//
	// private String bText[] = { "1", "2", "3", "4" };
}
