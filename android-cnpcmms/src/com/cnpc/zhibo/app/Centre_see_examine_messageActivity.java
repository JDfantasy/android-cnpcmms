package com.cnpc.zhibo.app;

//վ���������鿴�ϼ�����������Ϣ�Ľ���
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
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Centre_see_examine_messageActivity extends MyActivity {
	private ImageView fanhui;// ���ذ�ť
	private TextView title, name, state, post, phone, mobile, message;// ������桢��ʾ�ˡ�״̬��ְλ���绰���ֻ�����ʾ��Ϣ
	private LinearLayout l1;// ���ֶ���
	private String id;
	private RequestQueue mqueQueue;// �������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_see_examine_message);
		setview();// �ؼ�ʵ�����ķ���
	}

	// ���ý����Ͽؼ��ķ���
	private void setview() {
		mqueQueue = Volley.newRequestQueue(this);// ��ʼ���������
		id = getIntent().getStringExtra("id");// ��ȡ�ϸ����洫�ݹ�����ά�޵���id
		l1 = (LinearLayout) findViewById(R.id.linearlayout_centre_see_examine_message);
		l1.setVisibility(View.GONE);
		set_title_text("������Ϣ");
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		title = (TextView) findViewById(R.id.textView_centre_see_examine_message_title);// ����
		name = (TextView) findViewById(R.id.textView_centre_see_examine_message_name);// ��ʾ��
		state = (TextView) findViewById(R.id.textView_centre_see_examine_message_state);// ״̬
		post = (TextView) findViewById(R.id.textView_centre_see_examine_message_post);// ְλ
		phone = (TextView) findViewById(R.id.textView_centre_see_examine_message_phone);// �绰
		mobile = (TextView) findViewById(R.id.textView_centre_see_examine_message_mobile);// �ֻ�
		message = (TextView) findViewById(R.id.textView_centre_see_examine_message_message);// ��Ϣ
		get_massagedata();// ��ȡ������Ϣ������
	}

	// ��ȡ������Ϣ������
	private void get_massagedata() {
		Toast.makeText(Centre_see_examine_messageActivity.this, "���ڼ����������Ժ�...",
				0).show();
		String url = "http://101.201.210.95:8080/cnpcmms/api/fixorder/get/"
				+ getIntent().getStringExtra("id");
		StringRequest re = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject js1 = js.getJSONObject("response");
							if (js1.getString("type").equals("success")) {
								JSONObject js_body = js.getJSONObject("body");
								state.setText(getfeedback(js_body
										.getString("approveStatus")));

								JSONObject js_approver = js_body
										.getJSONObject("approver");
								title.setText(js_approver.getString("name")
										+ "����վά�޵���ʾ��Ϣ");

								JSONObject js_reporter = js_body
										.getJSONObject("reporter");
								name.setText("��ʾ�ˣ�"
										+ js_reporter.getString("name"));
								phone.setText("�绰��"
										+ js_reporter.getString("phone"));
								mobile.setText("�ֻ���"
										+ js_reporter.getString("mobile"));
								message.setText("    "
										+ js_body.getString("approveContent"));
								l1.setVisibility(View.VISIBLE);
							} else {
								Toast.makeText(
										Centre_see_examine_messageActivity.this,
										js1.getString("content"), 0).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(Centre_see_examine_messageActivity.this,
								"��������ʧ��", 0).show();
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
		mqueQueue.add(re);

	}

	// �����ж����ά�޵��Ƿ�ͨ��������
	private String getfeedback(String str) {
		if (str.equals("approved")) {
			return "����ͨ��";
		} else {
			return "��������";
		}

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Centre_see_examine_messageActivity.this);
				break;

			default:
				break;
			}

		}
	};

}
