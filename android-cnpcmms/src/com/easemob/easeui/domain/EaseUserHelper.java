package com.easemob.easeui.domain;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.db.DataHelper;

import android.os.Handler;

/**
 * �����û������࣬���ڻ�ȡ�����û���Ϣ���û���Ϣ�����
 * 
 * @author xicunyou
 *
 */
public class EaseUserHelper {
	
	public static EaseUser getUserinfo(String username,Handler handler) {
		DataHelper helper = new DataHelper(SysApplication.getInstance());
		if(helper.HaveUserInfo(username)){
			EaseUser easeUser=helper.getUserInfo(username);
			helper.Close();
			return easeUser;
		}else{
			if(username.equals("admin")){
				EaseUser user = new EaseUser(username);
				user.setNick("ϵͳ��Ϣ");
				// �������ݿ⻺��
				if(helper.HaveUserInfo(user.getUsername()))
					helper.UpdateUserInfo(user);
				else
					helper.SaveUserInfo(user);
				helper.Close();
			}else
				getNetUserInfo(username,handler);
			return null;
		}
	}
	
	private static void getNetUserInfo(String username,final Handler handler ){
		String url = "http://101.201.210.95:8080/cnpcmms/api/member/get/" + username;
		JsonObjectRequest request = new JsonObjectRequest(Method.GET, url, null, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String type = response.getJSONObject("response").getString("type");
					if ("success".equals(type)) {
						JSONObject body = response.getJSONObject("body");
						EaseUser user = new EaseUser(body.getString("username"));
						user.setNick(body.getString("name"));
						user.setAvatar(body.getString("headImage"));
						// �������ݿ⻺��
						DataHelper helper = new DataHelper(SysApplication.getInstance());
						if(helper.HaveUserInfo(user.getUsername()))
							helper.UpdateUserInfo(user);
						else
							helper.SaveUserInfo(user);
						helper.Close();
						handler.sendEmptyMessage(100);
					} else {
						System.out.println("�����û���Ϣʧ�ܣ�");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println("����ʧ��");
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
		SysApplication.getHttpQueues().add(request);
	}
}
