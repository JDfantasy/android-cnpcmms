package com.cnpc.zhibo.app;

//��¼����
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.domain.EaseUserHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnterintoActivity extends MyActivity {
	private EditText name, pwd;// �û���������
	private TextView wjmm, register;// �������롢ע��
	private CheckBox ck;// ��ס����
	private boolean pd = false;// �ж������Ƿ���Ҫ����
	private Button bu_dl;// ��¼��ť
	private Map<String, String> map;
	private RequestQueue mqueQueue;// �������
	private boolean phoneck = false;// �ж�������û����Ƿ����ֻ�
	private boolean enterinto_ck = false;// �ж�������û����Ƿ����ֻ�
	private String matchingurl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enterinto);
		set_view();// ����¼�����ϵĿؼ�����ʵ���������ü���

	}

	// ����¼�����ϵ����еĿؼ�����ʵ�����ķ���
	private void set_view() {
		set_title_text("��վe��ͨ");
		mqueQueue = Volley.newRequestQueue(EnterintoActivity.this);// ʵ�����������
		name = (EditText) findViewById(R.id.editext_enterinto_name);// �û���
		pwd = (EditText) findViewById(R.id.editext_enterinto_pwd);// ����
		ck = (CheckBox) findViewById(R.id.checkBox_enterinto_jizhumima);// checkbox��ť
		bu_dl = (Button) findViewById(R.id.button_enterinto_denglu);// ��¼��ť
		wjmm = (TextView) findViewById(R.id.textView_enterinto_wangjimima);// ��������
		ck.setOnCheckedChangeListener(listener);// checkbox���ü���
		bu_dl.setOnClickListener(l);// ��¼���ü���
		wjmm.setOnClickListener(l);// ��������
		// ��ƫ�������л�ȡ�洢���û���������
		name.setText(getSharedPreferences("enterinto", 0).getString("name", ""));
		pwd.setText(getSharedPreferences("enterinto", 0).getString("pwd", ""));
		if (getSharedPreferences("enterinto", 0).getBoolean("panduan", false) == true) {
			ck.setChecked(true);// ����checkbox��ֵ
			// set_enterinto();//��¼�ķ���
			// �ж��û��Ƿ�ѡ���ˣ���ס���롣���û�ѡ���˼�ס���룬���û��´δ�APPʱ���Զ�Ϊ�û����е�¼
		}
		name.addTextChangedListener(watcher);
		register = (TextView) findViewById(R.id.textview_myacitivity_you);
		register.setVisibility(View.VISIBLE);
		register.setText(Html.fromHtml("<u>" + "ע��" + "</u>"));
		register.setOnClickListener(l);
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (name.getText().length() < 11) {
				pwd.setText("");
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	// ��ť����ļ����¼�
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_enterinto_denglu:// ��¼��ť
				if (enterinto_ck == false) {
					enterinto_ck = true;
					if (get_data() != null && phoneck == true) {
						set_enterinto();// ��¼��ť��ִ�еķ���
						Toast.makeText(EnterintoActivity.this, "���ڵ�¼....", 0).show();
					} else {
						Toast.makeText(EnterintoActivity.this, "��������û��������ֻ���", 0).show();
						name.setText("");
						pwd.setText("");
						enterinto_ck = false;
					}
				}

				break;
			case R.id.textView_enterinto_wangjimima://��ת���ֻ���֤���桪����������
				Intent in=new Intent(EnterintoActivity.this, Phone_verifyActivity.class);
				SysApplication.addActivity(EnterintoActivity.this);
				in.putExtra("boolean", false);
				startActivity(in);
				Myutil.set_activity_open(EnterintoActivity.this);
				break;
			case R.id.textview_myacitivity_you:// ��ת���ֻ���֤���桪��ע���˺�
				Intent in1=new Intent(EnterintoActivity.this, Phone_verifyActivity.class);
				in1.putExtra("boolean", true);
				SysApplication.addActivity(EnterintoActivity.this);
				startActivity(in1);
				Myutil.set_activity_open(EnterintoActivity.this);
				break;
			default:
				break;
			}

		}
	};

	// ��¼��ť��ִ�еķ���
	private void set_enterinto() {
		get_data();
		String url = Myconstant.CENTRE_ENTER;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// ��¼�ɹ�
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						JSONObject js2 = json.getJSONObject("body");
						SysApplication.token = js2.getString("token");
						Myconstant.token = SysApplication.token;
						get_user_type();// �ж��û����ʹ���Ӧ�Ľ���

					} else {
						Toast.makeText(EnterintoActivity.this, js1.getString("content"), 0).show();
						name.setText("");
						pwd.setText("");
						enterinto_ck = false;

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(EnterintoActivity.this, "��¼ʧ�ܣ��˺Ų�����", 0).show();
				name.setText("");
				pwd.setText("");
				enterinto_ck = false;
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return map;
			}

		};
		mqueQueue.add(re);
	}

	// �ӷ�������ȡ�û�����Ϣ
	private void get_user_type() {
		String url = Myconstant.CENTRE_USERMESSAGE;
		JsonObjectRequest re = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

				try {
					String type = response.getString("type");
					SysApplication.userid = response.getString("username");
					Myconstant.userid = SysApplication.userid;
					// ���ŵ�¼����
					loginHX(Myconstant.userid, "");
					if (type.equals("admin")) {
						Toast.makeText(EnterintoActivity.this, "���˻�������", 0).show();
						finish();
					} else {
						if (type.equals("customer")) {
							startActivity(new Intent(EnterintoActivity.this, Petrochina_HomeActivity.class));
							finish();
							Myutil.set_activity_open(EnterintoActivity.this);
						} else {
							set_matching(type);// ������ʯ���⣬�����û�����Ҫ�����ж��Ƿ�������û�����Ϣȷ��
						}
					}

				} catch (JSONException e) {
					// ��Ӧ��
					e.printStackTrace();

				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("��ȡ�û�����ʧ��" + error);

			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				System.out.println("��ȡ���û���Ϣ�Ľ���е�token��ֵ��" + Myconstant.token);
				return headers;

			}
		};

		mqueQueue.add(re);

	}

	// checkbox�İ�ť��״̬�����仯�ļ����¼�
	private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			pd = isChecked;
		}
	};

	// ��ȡ�û������������Ϣ
	private Map<String, String> get_data() {
		map = new HashMap<String, String>();
		if (name.getText().toString().equals("")) {
			Toast.makeText(this, "����������û���", Toast.LENGTH_LONG).show();

			return null;
		}
		if (pwd.getText().toString().equals("")) {
			Toast.makeText(this, "�������������", Toast.LENGTH_LONG).show();
			return null;
		}
		phoneck = Myutil.isMobileNO(name.getText().toString());
		map.put("mobile", name.getText().toString());
		map.put("password", pwd.getText().toString());
		if (pd == true) {
			System.out.println("������ƫ�����õķ���");
			// ����ƫ������ ���û�����������д洢
			SharedPreferences preferences = getSharedPreferences("enterinto", 0);
			Editor editor = preferences.edit(); // �ļ��༭��
			editor.putString("name", name.getText().toString());
			editor.putString("pwd", pwd.getText().toString());
			editor.putBoolean("panduan", true);
			editor.commit();// �ύ
		} else {
			SharedPreferences preferences = getSharedPreferences("enterinto", 0);
			Editor editor = preferences.edit(); // �ļ��༭��
			editor.putString("name", "");
			editor.putString("pwd", "");
			editor.putBoolean("panduan", false);
			editor.commit();// �ύ
		}
		return map;

	}

	// �鿴�û��Ƿ������ƥ��
	private boolean set_matching(final String type) {
		final Intent in = new Intent();
		if (type.equals("site")) {
			matchingurl = Myconstant.CENTRE_CONFIRMATION_INFORMATION;// վ����
		} else if (type.equals("worker")) {
			matchingurl = Myconstant.MAINTAIN_CONFIRMATION_INFORMATION;// ά�޹�
		} else {
			matchingurl = Myconstant.SUPPLIER_CONFIRMATION_INFORMATION;// ��Ӧ��
		}
		JsonObjectRequest re = new JsonObjectRequest(matchingurl, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {

				try {
					JSONObject js = response.getJSONObject("response");
					if (js.getString("type").equals("success")) {
						// �û��Ѿ���֤��Ϣ�ɹ�
						if (type.equals("site")) {// վ����

							in.setClass(EnterintoActivity.this, Centre_HomeActivity.class);

						} else if (type.equals("worker")) {// ά�޹�

							in.setClass(EnterintoActivity.this, Maintain_HomeActivity.class);

						} else if (type.equals("customer")) {// ��ʯ��
							in.setClass(EnterintoActivity.this, Petrochina_HomeActivity.class);
						} else {// ��Ӧ��

							in.setClass(EnterintoActivity.this, Supplier_HomeActivity.class);

						}

					} else {
						// �û���û����֤��Ϣ
						if (type.equals("site")) {// վ����

							in.setClass(EnterintoActivity.this, Centre_AddressActivity.class);

						} else if (type.equals("worker")) {// ά�޹�

							in.setClass(EnterintoActivity.this, Maintain_select_providerActivity.class);

						} else if (type.equals("customer")) {// ��ʯ��
							in.setClass(EnterintoActivity.this, Petrochina_HomeActivity.class);
						} else {// ��Ӧ��

							in.setClass(EnterintoActivity.this, Supplier_select_firmActivity.class);

						}
					}
					startActivity(in);
					Myutil.set_activity_open(EnterintoActivity.this);
					finish();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				System.out.println("��֤���صĽ����" + response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(EnterintoActivity.this, error.toString(), 0).show();

			}

		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> map = new HashMap<String, String>();
				map.put("accept", "application/json");
				map.put("api_key", Myconstant.token);
				return map;

			}
		};

		mqueQueue.add(re);
		return false;

	}

	/**
	 * ���ŵ�¼����
	 * 
	 * @param name
	 * @param pwd
	 */
	private void loginHX(String name, String pwd) {
		System.out.println("��ʼ��¼���ţ�" + name);
		// ���̨Լ������Ĭ�ϵ�¼����Ϊpassword
		pwd = "password";
		EMChatManager.getInstance().login(name, pwd, new EMCallBack() {// �ص�
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						EMGroupManager.getInstance().loadAllGroups();
						EMChatManager.getInstance().loadAllConversations();
						System.out.println("��½����������ɹ���");
						EaseUserHelper.getUserinfo(EMChatManager.getInstance().getCurrentUser(), new Handler());
					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
				System.out.println("��½���������ʧ�ܣ�");
			}
		});

	}

}
