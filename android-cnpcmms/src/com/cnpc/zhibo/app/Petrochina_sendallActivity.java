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
import com.cnpc.zhibo.app.util.Myutil;

/*
 * ��ʯ�Ͷ˷�������Ľ���
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_sendallActivity extends MyActivity {
	private ImageView back;// ����
	private TextView send;// ����
	private EditText title, content;// ���⣬����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina_sendall);
		setview();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setview() {
		set_title_text("��������");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);// ���ذ�ť
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		send = (TextView) findViewById(R.id.textview_myacitivity_you);
		send.setVisibility(View.VISIBLE);
		send.setText("����");
		send.setOnClickListener(l);
		title = (EditText) findViewById(R.id.editext_petrochina_sendal_title);
		content = (EditText) findViewById(R.id.editext_petrochina_sendal_content);
	}

	// �����¼�
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Petrochina_sendallActivity.this);

				break;
			case R.id.textview_myacitivity_you://���Ͱ�ť
				if (title.getText().toString() == null || title.getText().toString().length() == 0) {
					Toast.makeText(Petrochina_sendallActivity.this, "���������", 0).show();;
				} else if(content.getText().toString() == null || content.getText().toString().length() == 0){
					Toast.makeText(Petrochina_sendallActivity.this, "����������", 0).show();
				}else {
					sendnotice();
				}
				break;

			default:
				break;
			}

		}
	};
	//���͹���ķ���
	private void sendnotice(){

		StringRequest re = new StringRequest(Request.Method.POST, Myconstant.PETROCHINA_SENDDALNOTICE, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						startActivity(
								new Intent(Petrochina_sendallActivity.this, Petrochina_HomeActivity.class));
						Myutil.set_activity_open(Petrochina_sendallActivity.this);
						Toast.makeText(Petrochina_sendallActivity.this, js1.getString("content"), 0).show();

					} else {
						Toast.makeText(Petrochina_sendallActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Petrochina_sendallActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", title.getText().toString());
				map.put("content", content.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);

	
		
	}

}
