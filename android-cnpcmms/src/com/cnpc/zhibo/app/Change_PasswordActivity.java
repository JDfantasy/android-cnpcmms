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

import android.content.Intent;
/*
 * 公共端用户修改密码的界面
 */
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import net.sf.ezmorph.object.SwitchingMorpher;

public class Change_PasswordActivity extends MyActivity {
	private EditText pwd1, pwd2;// 密码
	private Button confirm;// 确定
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitychange_password);
		setview();
	}

	/*
	 * 设置界面的方法
	 */
	private void setview() {
		set_title_text("密码管理");
		pwd1 = (EditText) findViewById(R.id.editext_activitychangge_password_1);
		pwd2 = (EditText) findViewById(R.id.editext_activitychangge_password_2);
		confirm = (Button) findViewById(R.id.button_activitychange_password_nextstep);
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(l);
		confirm.setOnClickListener(l);

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_activitychange_password_nextstep:
				if (pwd1.getText().toString().equals("") || pwd2.getText().toString().equals("")
						|| !pwd2.getText().toString().equals(pwd2.getText().toString())) {
					Toast.makeText(Change_PasswordActivity.this, "对不起你输入的密码有误，请重新输入。", 0).show();
					pwd1.setText("");
					pwd2.setText("");
				} else {
					changpasswrod();
				}
				break;
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Change_PasswordActivity.this);
				break;
			default:
				break;
			}

		}
	};

	/*
	 * 修改密码的方法
	 */
	private void changpasswrod() {
		String url = Myconstant.CHANGPASSWROD;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			// 登录成功
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Intent in = new Intent(Change_PasswordActivity.this, EnterintoActivity.class);
						startActivity(in);
						Myutil.set_activity_close(Change_PasswordActivity.this);
						finish();
					} else {
						Toast.makeText(Change_PasswordActivity.this, js1.getString("content"), 0).show();

					}
				} catch (JSONException e) {
					Log.i("azy", "抛出了异常");
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Change_PasswordActivity.this, "因为网络问题，修改密码失败。", 0).show();
			}
		}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobile", getIntent().getStringExtra("mobile"));
				map.put("newPassword", pwd1.getText().toString());
				return map;
			}

		};
		SysApplication.getHttpQueues().add(re);
	}
}
