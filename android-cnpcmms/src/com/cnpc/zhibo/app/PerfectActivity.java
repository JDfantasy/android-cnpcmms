package com.cnpc.zhibo.app;

//�û���Ϣ���ƽ���
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PerfectActivity extends MyActivity {
	private Button bu_wancheng;// ����ɵİ�ť

	private Map<String, String> map;// �����洢���ݵļ���
	private boolean pd = true;// �Ƿ�ѡ���˵��ж�
	private CheckBox che_xy;// ѡ��Э���checkbox�ؼ�
	private TextView tx_xy, zhanzhang, weixiuyuan, yunyingshang, cnpc;// Э�顢վ����ά��Ա����Ӫ�̡���ʯ��
	private EditText gonghao, name, sfz, lxdh, pwd, yzm;// ���š����������֤��������˾����ϵ�绰�����롢��֤��
	private String name_type = "";// �û�������
	private RequestQueue mQueue;// volley�������
	private ImageView fanhui;// ���ذ�ť

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfect);
		set_title_text("������Ϣ����");// �ı����
		set_views();// �õ�ע������ϵĿؼ�
		// Ϊ�����ϵİ�ť���ü���

		bu_wancheng.setOnClickListener(l);// ��ɰ�ť�ļ���
		tx_xy.setOnClickListener(l);// Э��ľ�����Ϣ�ļ���
		che_xy.setOnCheckedChangeListener(listener);// ��Ϣ��checkbox��״̬�ı����
		zhanzhang.setOnClickListener(l);// վ����ť
		weixiuyuan.setOnClickListener(l);// ά��Ա��ť
		yunyingshang.setOnClickListener(l);// ��Ӫ��
		cnpc.setOnClickListener(l);// ��ʯ��

	}

	// ���֤���㷨
	private static boolean verify(String id) {
		if (id.length() != 18)
			return false;
		/*
		 * 1����ǰ������֤����17λ���ֱ���Բ�ͬ�� ϵ���� �ӵ�һλ����ʮ��λ��ϵ���ֱ�Ϊ��
		 * 7��9��10��5��8��4��2��1��6��3��7��9��10��5��8��4��2�� ����17λ���ֺ�ϵ����˵Ľ����ӡ�
		 */
		int[] w = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		int sum = 0;
		for (int i = 0; i < w.length; i++) {
			sum += (id.charAt(i) - '0') * w[i];
		}
		// �üӳ����ͳ���11���������Ƕ��٣�
		char[] ch = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		// return ch[sum%11]==
		// (id.charAt(17)=='x'?'X': id.charAt(17));
		int c = sum % 11;
		/*
		 * �ֱ��Ӧ����� һλ���֤�ĺ���Ϊ 1��0��X��9��8��7��6��5��4��3��2��
		 */
		char code = ch[c];
		char last = id.charAt(17);
		last = last == 'x' ? 'X' : last;
		return last == code;
	}

	// ��ע������ϵ����еĿؼ����г�ʼ���ķ���
	private void set_views() {
		mQueue = Volley.newRequestQueue(this);
		bu_wancheng = (Button) findViewById(R.id.button_perfect_tijiaozhuce);// �ύע��
		che_xy = (CheckBox) findViewById(R.id.checkBox_perfect_xuanzhongxieyi);// Э���checkbox
		tx_xy = (TextView) findViewById(R.id.textView_perfect_xieyi);// Э��ľ�����Ϣ
		gonghao = (EditText) findViewById(R.id.edittext_perfect_gonghao);// ����
		name = (EditText) findViewById(R.id.edittext_perfect_name);// ����
		sfz = (EditText) findViewById(R.id.edittext_perfect_shenfenzheng);// ���֤
		lxdh = (EditText) findViewById(R.id.edittext_perfect_lianxidianhua);// ��ϵ�绰
		pwd = (EditText) findViewById(R.id.edittext_perfect_pwd1);// ����
		zhanzhang = (TextView) findViewById(R.id.textview_perfect_zhanzhang);// վ��
		weixiuyuan = (TextView) findViewById(R.id.textview_perfect_weixiuyuan);// ά�޹�
		yunyingshang = (TextView) findViewById(R.id.textview_perfect_yunyingshan);// ��Ӫ��
		cnpc = (TextView) findViewById(R.id.textview_perfect_zhongshiyou);// ��ʯ��
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
	}

	// ��ȡ�û����������
	private boolean get_data() {
		map = new HashMap<String, String>();
		if (gonghao.getText().toString().equals("")) {
			Toast.makeText(PerfectActivity.this, "��������Ĺ���", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;
		}
		if (name.getText().toString().equals("")) {
			Toast.makeText(PerfectActivity.this, "�������������", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;
		}

		if (sfz.getText().toString().equals("")) {
			Toast.makeText(PerfectActivity.this, "������������֤", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;
		}
		if (lxdh.getText().toString().equals("")) {
			Toast.makeText(PerfectActivity.this, "�����������ϵ�绰", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;
		}
		if (verify(sfz.getText().toString()) == false) {
			Toast.makeText(PerfectActivity.this, "����������֤��������������", Toast.LENGTH_LONG).show();
			sfz.setText("");
			return false;
		}
		if (pd == false) {
			Toast.makeText(PerfectActivity.this, "��ѡ�����Ķ��й�ʯ��Э���ѡ��", Toast.LENGTH_LONG).show();
			pwd.setText("");
			return false;

		}
		if (name_type.equals("")) {
			Toast.makeText(PerfectActivity.this, "��ѡ���û�����", Toast.LENGTH_LONG).show();
			return false;
		}
		// �� ���š��û����͡����������֤����˾����ϵ�ˡ��������Ϣ�洢��map������
		map.put("username", gonghao.getText().toString());// ����
		map.put("name", name.getText().toString());// ����
		map.put("identification", sfz.getText().toString());// ���֤
		map.put("phone", lxdh.getText().toString());// ��ϵ�绰
		map.put("mobile", getIntent().getStringExtra("mobile"));
		map.put("type", name_type);// �û�����
		map.put("password", pwd.getText().toString());// ����

		return true;

	}

	// checkbox��״̬�����仯�ļ����ķ���
	private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			pd = isChecked;
		}
	};
	// ����¼��ļ����ķ���
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.button_perfect_tijiaozhuce:// �ύע��
				if (get_data()) {
					volley_post();// �����ɰ�ťִ�еķ���
				}

				break;
			case R.id.textView_perfect_xieyi:// ��ת��Э�����ķ���

				break;
			case R.id.textview_perfect_zhanzhang:// ѡ��վ��ִ�еķ���
				select_name(R.id.textview_perfect_zhanzhang);
				break;
			case R.id.textview_perfect_yunyingshan:// ѡ����Ӫ��ִ�еķ���
				select_name(R.id.textview_perfect_yunyingshan);
				break;
			case R.id.textview_perfect_weixiuyuan:// ѡ��ά�޹��ķ���
				select_name(R.id.textview_perfect_weixiuyuan);
				break;
			case R.id.textview_perfect_zhongshiyou:// ѡ����ʯ�͵ķ���
				select_name(R.id.textview_perfect_zhongshiyou);
				break;
			case R.id.imageView_myacitity_zuo:// ���ذ�ť
				finish();
				Myutil.set_activity_close(PerfectActivity.this);
				break;
			default:
				break;
			}

		}
	};

	// ע�ᰴťִ�еĲ���
	private void volley_post() {
		Toast.makeText(this, "�����ύ�û���Ϣ���Ե�...", 0).show();
		String str = Myconstant.REGISTER;
		StringRequest re = new StringRequest(Request.Method.POST, str, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject jsa = new JSONObject(response);
					JSONObject js1 = jsa.getJSONObject("response");// ״̬

					if (js1.getString("type").equals("success")) {
						JSONObject js2 = jsa.getJSONObject("body");// ���ص�����
						Myconstant.token = js2.getString("token");
						// ����ƫ������ ���û�����������д洢
						SharedPreferences preferences = getSharedPreferences("perfect", 0);
						Editor editor = preferences.edit(); // �ļ��༭��
						editor.putBoolean("panduan", true);
						editor.commit();// �ύ
						startActivity(new Intent(PerfectActivity.this, EnterintoActivity.class));
						Myutil.set_activity_open(PerfectActivity.this);
						SysApplication.exit();
						// finish();
					}
					if (js1.getString("type").equals("error")) {
						Toast.makeText(PerfectActivity.this, js1.getString("content"), 0).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("�ϴ�ʧ��" + error.toString());

			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				return map;
			}
		};
		mQueue.add(re);

	}

	// ѡ���û����͵ķ���
	private void select_name(int id) {
		switch (id) {
		case R.id.textview_perfect_zhanzhang:// վ��
			zhanzhang.setTextColor(getResources().getColor(R.color.zitiyanse1));
			weixiuyuan.setTextColor(getResources().getColor(R.color.zitiyanse3));
			yunyingshang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			cnpc.setTextColor(getResources().getColor(R.color.zitiyanse3));
			name_type = "site";

			break;
		case R.id.textview_perfect_weixiuyuan:// ά��Ա
			zhanzhang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			weixiuyuan.setTextColor(getResources().getColor(R.color.zitiyanse1));
			yunyingshang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			cnpc.setTextColor(getResources().getColor(R.color.zitiyanse3));
			name_type = "worker";
			break;
		case R.id.textview_perfect_yunyingshan:// ��Ӫ��
			zhanzhang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			weixiuyuan.setTextColor(getResources().getColor(R.color.zitiyanse3));
			yunyingshang.setTextColor(getResources().getColor(R.color.zitiyanse1));
			cnpc.setTextColor(getResources().getColor(R.color.zitiyanse3));
			name_type = "provider";
			break;
		case R.id.textview_perfect_zhongshiyou:// ��ʯ��
			zhanzhang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			weixiuyuan.setTextColor(getResources().getColor(R.color.zitiyanse3));
			yunyingshang.setTextColor(getResources().getColor(R.color.zitiyanse3));
			cnpc.setTextColor(getResources().getColor(R.color.zitiyanse1));
			name_type = "customer";
			break;

		default:
			break;
		}

	}

}
