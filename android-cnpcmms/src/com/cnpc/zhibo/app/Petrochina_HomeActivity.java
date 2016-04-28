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
import com.cnpc.zhibo.app.util.CornerUtil;
//��ʯ�Ͷ˵�������
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.BadgeView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Petrochina_HomeActivity extends MyActivity {
	private RelativeLayout fbgg, examine, suppliermessage, jyzmessage, servicenumber,aboutus;
	// �������桢��������Ӧ����Ϣ������վ��Ϣ��ά���ʲ�ѯ����������
	private ImageView back;//���ذ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_home);
		setview();
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

	// ���ý���ķ���
	private void setview() {
		set_title_text("�й�ʯ��");
		fbgg = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_pushnotice);// ��������
		examine = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_examine);// ����
		suppliermessage = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_suppliermessage);// ��Ӧ��
		jyzmessage = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_jyzrmessage);// ����վ��Ϣ
		servicenumber = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_hone_servicenumber);// ά���ʲ�ѯ
		aboutus = (RelativeLayout) findViewById(R.id.relativelayout_petrochina_home_aboutus);// ά���ʲ�ѯ
		setBadgerView(examine, "customer-approveorder",examine_helper);// ��������ť��������
		fbgg.setOnClickListener(l);
		examine.setOnClickListener(l);
		suppliermessage.setOnClickListener(l);
		jyzmessage.setOnClickListener(l);
		servicenumber.setOnClickListener(l);
		aboutus.setOnClickListener(l);
		back=(ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		back.setImageResource(R.drawable.useimessagecon);
		back.setOnClickListener(l);
	}
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
	 * ��ȡδ������������������
	 */
	private void get_untreated_examinenumber(final BadgeView badgeView){
		String url = Myconstant.PETROCHINA_GET_COUNTWAIT_RENTRYSTATS;
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
						Toast.makeText(Petrochina_HomeActivity.this, js1.getString("content"), 0).show();
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
	// �����¼�
	private View.OnClickListener l = new View.OnClickListener() {
		Intent in = new Intent();

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.relativelayout_petrochina_hone_pushnotice:// ��������
				in.setClass(Petrochina_HomeActivity.this, Petrochina_announceActivity.class);
				break;
			case R.id.relativelayout_petrochina_hone_examine:// ����
				//clearNums("customer-approve");// ȡ��������ť�ϵ�����
				in.setClass(Petrochina_HomeActivity.this, Petrochina_examine_entryActivity.class);
				break;

			case R.id.relativelayout_petrochina_hone_suppliermessage:// ��Ӧ����Ϣ
				in.setClass(Petrochina_HomeActivity.this, Petrochina_suppliers_entryActivity.class);
				break;

			case R.id.relativelayout_petrochina_hone_jyzrmessage:// ����վ��Ϣ
				in.setClass(Petrochina_HomeActivity.this, Petrochina_station_entryActivity.class);
				break;

			case R.id.relativelayout_petrochina_hone_servicenumber:// ά���ʲ�ѯ
				in.setClass(Petrochina_HomeActivity.this, Petrochina_project_entryActivity.class);
				break;
			case R.id.imageView_myactity_you://�鿴������Ϣ
				in.setClass(Petrochina_HomeActivity.this, User_messagectivity.class);
				break;
			case R.id.relativelayout_petrochina_home_aboutus://��������
				in.setClass(Petrochina_HomeActivity.this, AboutUsActivity.class);
				in.putExtra("activity", "customer");
				break;
			default:
				break;
			}
			startActivity(in);
			Myutil.set_activity_open(Petrochina_HomeActivity.this);

		}
	};

}
