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

//�����������
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

public class NoticedetailsActivity extends MyActivity implements OnClickListener {
	
	private ImageView backImageView;//����
	private WebView webView;
	private String noticeId;
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noticedetails);
		noticeId=getIntent().getStringExtra("noticeId");
		set_title_text("��������");
		setViews();
	}
	// ȷ���Ѷ��Ľӿ�
			private void setaffirm(final String id) {

				String url = Myconstant.COMMONALITY_READMEMBERNOTICE;
				StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject js1 = js.getJSONObject("response");
							if (js1.getString("type").equals("success")) {
		                       
							} else {
								Toast.makeText(NoticedetailsActivity.this, js1.getString("content"), 0).show();
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
						Toast.makeText(NoticedetailsActivity.this, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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
						return map;
					}

				};
				SysApplication.getHttpQueues().add(re);

			}
	private void setViews() {
		backImageView = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(this);
		webView=(WebView) findViewById(R.id.noteicdetailsWebview);
		webView.loadUrl(Myconstant.NOTICEDETAILS + noticeId);
		setaffirm(noticeId);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���˰�ť
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// �����л������Ч��
			break;

		}
		
	}
	
}
