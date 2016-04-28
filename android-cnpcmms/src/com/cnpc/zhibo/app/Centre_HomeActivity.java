package com.cnpc.zhibo.app;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.fragment.Fragment_centre_examine;
import com.cnpc.zhibo.app.fragment.Fragment_centre_home;
import com.cnpc.zhibo.app.fragment.Fragment_centre_repairs;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.BadgeView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class Centre_HomeActivity extends MyActivity {

	private Fragment_centre_home f_centre;// ��ҳ
	private Fragment_centre_examine f_examine;// ����
	private Fragment_centre_repairs f_repairs;// ����
	private Fragment f0;// �����fragment����
	private FragmentManager fm;// fragment�Ĺ��������
	private RelativeLayout centre, examine, repairs;// ��ҳ������������
	private ImageView ima[];
	private TextView tx[];
	private ImageView back;// ���ذ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_home);
		set_title_text("վ������ҳ");
		setview();// �������ϵĿؼ�����ʵ����
	}

	// ���ý����ϵĿؼ��ķ���
	private void setview() {
		centre = (RelativeLayout) findViewById(R.id.relativelayout_centre_home_shouye);// ��ҳ
		examine = (RelativeLayout) findViewById(R.id.relativelayout_centre_home_shenpi);// ����
		repairs = (RelativeLayout) findViewById(R.id.relativelayout_centre_home_weixiu);// ά��
		centre.setOnClickListener(l);
		examine.setOnClickListener(l);
		repairs.setOnClickListener(l);
		setBadgerView(repairs, "site-fixorder", repairs_helper);// ��ά�ް�ť��������
		setBadgerView(examine, "site-approveorder",examine_helper);// ��������ť��������
		f_centre = new Fragment_centre_home();// ��ҳ��fragment��ʵ����
		f_examine = new Fragment_centre_examine();// ������fragment��ʵ����
		f_repairs = new Fragment_centre_repairs();// ά�޵�fragment��ʵ����
		f0 = new Fragment();
		fm = getSupportFragmentManager();// ��ȡfragment�Ĺ�����Ķ���
		xianshiFragment(f_centre);
		ima = new ImageView[3];// ����ײ���������ť��ͼ��
		ima[0] = (ImageView) findViewById(R.id.imageView_centre_home_shouye);
		ima[1] = (ImageView) findViewById(R.id.imageView_centre_home_weixiu);
		ima[2] = (ImageView) findViewById(R.id.imageView_centre_home_shenpi);
		tx = new TextView[3];
		tx[0] = (TextView) findViewById(R.id.textview_centre_home_home);
		tx[1] = (TextView) findViewById(R.id.textview_centre_home_service);
		tx[2] = (TextView) findViewById(R.id.textview_centre_home_examine);
		back = (ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		back.setImageResource(R.drawable.useimessagecon);
		back.setOnClickListener(l);

	}

	/*
	 * ʵ��ά��������ʾ��ֵ�Ľӿ�
	 */
	private CornerUtil.OrderNumsHelper repairs_helper = new CornerUtil.OrderNumsHelper() {

		@Override
		public void setViewNums(BadgeView badgeView) {
			get_untreated_servicenumber(badgeView);

		}
	};
	/*
	 * ʵ������������ʾ��ֵ�Ľӿ�
	 */
	private CornerUtil.OrderNumsHelper examine_helper = new CornerUtil.OrderNumsHelper() {

		@Override
		public void setViewNums(BadgeView badgeView) {
			get_untreated_examinenumber(badgeView);

		}
	};
	/*
	 * ��ȡδ��ɵ�ά�޵�������
	 */
	private void get_untreated_servicenumber(final BadgeView badgeView){
		String url = Myconstant.CENTRE_GETSERVICE_COUNTRNUMBER;
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
						Toast.makeText(Centre_HomeActivity.this, js1.getString("content"), 0).show();
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
	/*
	 * ��ȡδ������������������
	 */
	private void get_untreated_examinenumber(final BadgeView badgeView){
		String url = Myconstant.CENTRE_GETAPPROVE_COUNTRNUMBER;
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
						Toast.makeText(Centre_HomeActivity.this, js1.getString("content"), 0).show();
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// �����˳��Ի���
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// ���öԻ������
			isExit.setTitle("ϵͳ��ʾ");
			// ���öԻ�����Ϣ
			isExit.setMessage("ȷ��Ҫ�˳���");
			// ���ѡ��ť��ע�����
			isExit.setButton("ȷ��", listener);
			isExit.setButton2("ȡ��", listener);
			// ��ʾ�Ի���
			isExit.show();

		}

		return false;

	}

	/** �����Ի��������button����¼� */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};

	// �ؼ��ĵ���¼��ļ����ķ���
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relativelayout_centre_home_shouye:// ��ҳ

				seticon(0);
				set_title_text("վ������ҳ");
				xianshiFragment(f_centre);
				break;
			case R.id.relativelayout_centre_home_shenpi:// ����
				seticon(2);
				set_title_text("վ��������");
				//clearNums("site-approveorder");// ȡ��������ť�ϵ�����
				xianshiFragment(f_examine);
				break;
			case R.id.relativelayout_centre_home_weixiu:// ά��
				seticon(1);
				set_title_text("վ����ά��");
				//clearNums("site-fixorder");// ȡ��ά�ް�ť�ϵ�����
				xianshiFragment(f_repairs);
				break;
			case R.id.imageView_myactity_you:
				Intent in = new Intent(Centre_HomeActivity.this, User_messagectivity.class);
				startActivity(in);
				Myutil.set_activity_open(Centre_HomeActivity.this);
				break;
			default:
				break;
			}

		}
	};

	// Fragment���滻�ķ���
	private void xianshiFragment(Fragment f) {
		FragmentTransaction transaction = fm.beginTransaction();// ��ʼһ������
		Fragment ft = fm.findFragmentByTag(f.getClass().getCanonicalName());
		// ���������е�fragment����ȡfragment����
		if (f0 != null && f0 == f) {
			// ��Ҫ�滻��ʾ��fragment�ǵ�ǰ������ʾ��fragment�Ͳ������ı䣬ֱ���˳�
			return;
		}
		if (f0 != null) {
			transaction.hide(f0);
			// ��Ҫ�滻��ʾ��fragment���ǵ�ǰ��ʾ��fragmentʱ������ǰ��fragment��������

		}
		if (ft != null) {
			transaction.show(ft);
			// ���Ҫ��ʾ��fragment���������Ѿ����ڣ���ô�ͽ���������ʾ�Ϳ�����
		} else {
			transaction.add(R.id.linearlayout_centre_home_fragment, f, f.getClass().getCanonicalName());
			// ���Ҫ��ʾ��fragment�����������У���ô�������������Ҫ��ʾ��fragment

		}
		f0 = f;// ����ǰ��ʾ��fragment���ó�Ҫ�滻��fragment����
		transaction.commit();// �ύ����
	}

	// �ı�ײ�������ťͼ��ķ���
	@SuppressLint("ResourceAsColor")
	private void seticon(int id) {
		switch (id) {
		case 0:
			ima[0].setImageResource(R.drawable.centre_home_check);
			ima[1].setImageResource(R.drawable.centre_service_no);
			ima[2].setImageResource(R.drawable.centre_examine_no);
			tx[0].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			tx[1].setTextColor(getResources().getColor(R.color.zitiyanse3));
			tx[2].setTextColor(getResources().getColor(R.color.zitiyanse3));
			break;
		case 1:
			ima[0].setImageResource(R.drawable.centre_home_no);
			ima[1].setImageResource(R.drawable.centre_service_check);
			ima[2].setImageResource(R.drawable.centre_examine_no);
			tx[0].setTextColor(getResources().getColor(R.color.zitiyanse3));
			tx[1].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			tx[2].setTextColor(getResources().getColor(R.color.zitiyanse3));
			break;
		case 2:
			ima[0].setImageResource(R.drawable.centre_home_no);
			ima[1].setImageResource(R.drawable.centre_service_no);
			ima[2].setImageResource(R.drawable.centre_examine_check);
			tx[0].setTextColor(getResources().getColor(R.color.zitiyanse3));
			tx[1].setTextColor(getResources().getColor(R.color.zitiyanse3));
			tx[2].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			break;

		default:
			break;
		}
	}

}
