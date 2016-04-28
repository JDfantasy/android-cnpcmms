package com.cnpc.zhibo.app;

//APP�ֻ���֤�ͻ�ȡ��֤��Ľ���
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Phone_verifyActivity extends MyActivity {

	private Button bu_dl;// ��¼��ť��
	private EditText name, yzm;// �û�������֤��
	private int time;// ʱ��
	private Thread t;// �߳�
	private RequestQueue mQueue;// volley�������
	private ImageView fanhui;
   
	private TextView tx_hqyzm;// ��ȡ��֤��
	private Handler handler = new Handler() {
		// ����handle���ƽ��м�ʱ�Ĳ���
		public void handleMessage(Message msg) {
			time = msg.what;
			tx_hqyzm.setText(time + "��");
			if (time == 0) {
				tx_hqyzm.setClickable(true);
				tx_hqyzm.setText("��ȡ��֤��");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mQueue = Volley.newRequestQueue(this);

		// �õ���¼�����ϵ����еĿؼ�
		set_title_text("�ֻ���֤");// �ı����
		name = (EditText) findViewById(R.id.editext_home_name);// �û���
		yzm = (EditText) findViewById(R.id.editext_home_yzm);// ��֤��
		bu_dl = (Button) findViewById(R.id.button_home_xiayibu);// ��¼��ť
		bu_dl.setOnClickListener(l);// ��¼��ť�ļ���
		tx_hqyzm = (TextView) findViewById(R.id.textview_home_huoquyzm);// ��ȡ��֤��
		tx_hqyzm.setOnClickListener(l);// ��ȡ��֤��ļ���
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);

	}

	private View.OnClickListener l = new View.OnClickListener() {
		Intent in = new Intent();

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_home_xiayibu:// ��ת��������Ϣ�Ľ���
				if (set_denglu() != null) {
					verifysms();

				}

				break;
			case R.id.textview_home_huoquyzm:// ������֤��İ�ť
				send_yzm();// ���ö�����֤
				tx_hqyzm.setClickable(false);// �ڽ�����֤���60���� ��ť���ܵ��
				timeBack(60);// ��ʱ�ķ���
				break;
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Phone_verifyActivity.this);
				break;
			default:
				break;
			}

		}
	};

	private void verifysms() {
		String url = Myconstant.VERIFYSMS;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// ��¼�ɹ�
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Phone_verifyActivity.this, js1.getString("content"), 0).show();
						Intent in = new Intent();
						SysApplication.addActivity(Phone_verifyActivity.this);
						if (getIntent().getBooleanExtra("boolean", false)) {
							in.setClass(Phone_verifyActivity.this, PerfectActivity.class);
						} else {
							in.setClass(Phone_verifyActivity.this, Change_PasswordActivity.class);
						}
						
						in.putExtra("mobile", name.getText().toString());
						startActivity(in);
						Myutil.set_activity_open(Phone_verifyActivity.this);// ��תҳ���Ч��
						name.setText("");
						yzm.setText("");
					} else {
						Toast.makeText(Phone_verifyActivity.this, js1.getString("content"), 0).show();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Phone_verifyActivity.this, "��Ϊ�������⣬��֤ʧ�ܡ�", 0).show();
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();

				map.put("mobile", name.getText().toString());
				map.put("verifycode", yzm.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
	}

	// ���û����ݵ��ֻ��ŷ��͸������������һ�ȡ���ص���֤�롢
	private void send_yzm() {

		String url = Myconstant.SENDSMS;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// ��¼�ɹ�
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(Phone_verifyActivity.this, js1.getString("content"), 0).show();

					} else {
						Toast.makeText(Phone_verifyActivity.this, js1.getString("content"), 0).show();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Phone_verifyActivity.this, "������֤����ʧ�ܣ��������硣", 0).show();
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				System.out.println(name.getText().toString());
				map.put("mobile", name.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
	}

	// ���ƻ�ȡ��֤�밴ť��ʱ�䵹��
	protected void timeBack(int a) {
		time = a;
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 60; i >= 0; i--) {
					Message msg = new Message();
					msg.what = i;
					handler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		t.start();
	}

	// ��¼��ť�ķ���
	private Map<String, Object> set_denglu() {
		Map<String, Object> map = new HashMap<String, Object>();// �����洢���ݵ�map���ϲ��ҳ�ʼ����ʼ��map����
		// �ж��û����Ƿ�Ϊ��
		if (name.getText().toString().equals("")) {
			Toast.makeText(Phone_verifyActivity.this, "�û�������Ϊ�գ���������д", Toast.LENGTH_LONG).show();
			name.setText("");
			yzm.setText("");
			return null;

		}
		// �ж��û����Ƿ�Ϊ��
		if (Myutil.isMobileNO(name.getText().toString()) == false) {
			Toast.makeText(Phone_verifyActivity.this, "��������ֻ��Ų���ȷ����������д", Toast.LENGTH_LONG).show();
			name.setText("");
			yzm.setText("");
			return null;

		}
		// �ж���֤���Ƿ�Ϊ��
		if (yzm.getText().toString().equals("")) {
			Toast.makeText(Phone_verifyActivity.this, "��֤�벻��Ϊ�գ���������д", Toast.LENGTH_LONG).show();
			name.setText("");
			yzm.setText("");
			return null;
		}
		// ���û�������֤�붼��Ϊ��ʱ�����û�������֤�붼���浽map�����У�������һ���������ϴ���������
		map.put("name", name.getText().toString());
		map.put("yzm", yzm.getText().toString());

		System.out.println("name:" + name.getText().toString() + "/npwd:" + yzm.getText().toString());
		return map;

	}

}
