package com.cnpc.zhibo.app;

/*
 * ��ʯ�Ͷ˲鿴ά�޵����ҽ��������Ľ���
 */
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
//��ʯ�Ͷ˲鿴��������,���ҽ��д���Ľ���
import com.cnpc.zhibo.app.util.Myutil;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Petrochina_examine_messageActivity extends MyActivity {
	private ImageView back;// ���ذ�ť
	private Button agree, unagree;// ͬ�⡢����
	private WebView web;
	private TextView select;// �������������İ�ť
	private PopupWindow pop;
	private EditText message;// �����Ϣ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petrochina);
		setview();
	}

	// ���ò��ֽ����ϵĿؼ�
	private void setview() {
		set_title_text("��������");
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		select = (TextView) findViewById(R.id.textview_myacitivity_you);
		select.setOnClickListener(l);
		web = (WebView) findViewById(R.id.webView_petrochina_examine_message);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());// ������Ӳ������ⲿ�����
		web.getSettings().setBlockNetworkImage(false);// �ɼ���ͼƬ
		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// û������ʱ���ڻ���
		if (getIntent().getStringExtra("workStatus").equals("wait")) {
			select.setVisibility(View.VISIBLE);
			select.setText("����");
			web.loadUrl(Myconstant.SERVICEMESSAGE + getIntent().getStringExtra("id"));
		} else {
			web.loadUrl(Myconstant.APPROVESERVICEMESSAGE + getIntent().getStringExtra("id"));
		}

	}

	// �����ķ���
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(Petrochina_examine_messageActivity.this);
				break;
			case R.id.button__popup_cnpcexaminemonad_agree:// ͬ����������
				set_disposemonad(Myconstant.PETROCHINA_APPROVED_SERVICEMONAD, getIntent().getStringExtra("id"));
				break;
			case R.id.button__popup_cnpcexaminemonad_unagree:// ������������
				set_disposemonad(Myconstant.PETROCHINA_UNAPPROVED_SERVICEMONAD, getIntent().getStringExtra("id"));
				break;
			case R.id.textview_myacitivity_you:// �򿪽��������Ľ���
				View v1 = LayoutInflater.from(Petrochina_examine_messageActivity.this)
						.inflate(R.layout.poppup_cnpcexaminemonad, null);// ����view
				pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���
				pop.setBackgroundDrawable(new ColorDrawable());
				// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���
				pop.showAtLocation(select, Gravity.BOTTOM, 0, 0);
				agree = (Button) v1.findViewById(R.id.button__popup_cnpcexaminemonad_agree);// ����ͨ��
				unagree = (Button) v1.findViewById(R.id.button__popup_cnpcexaminemonad_unagree);// ��������
				message = (EditText) v1.findViewById(R.id.editText_popup_cnpcexaminemonad_message);// �������
				agree.setOnClickListener(l);
				unagree.setOnClickListener(l);
				break;
			default:
				break;
			}
		}
	};

	// ͨ�����������������ķ���
	private void set_disposemonad(String url, final String id) {
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						startActivity(
								new Intent(Petrochina_examine_messageActivity.this, Petrochina_HomeActivity.class));
						Myutil.set_activity_open(Petrochina_examine_messageActivity.this);
						Toast.makeText(Petrochina_examine_messageActivity.this, js1.getString("content"), 0).show();

					} else {
						Toast.makeText(Petrochina_examine_messageActivity.this, js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Petrochina_examine_messageActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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
				map.put("id", id);
				map.put("content", message.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);

	}
}
