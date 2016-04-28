package com.cnpc.zhibo.app.adapter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.Centre_see_noticeentryActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Commonality_notice;
import com.cnpc.zhibo.app.util.Myutil;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Item_commonality_notice_adapter extends MyBaseAdapter<Commonality_notice> {
	private boolean ck1 = false;
	private boolean ck2 = false;
	private int tag1;
	private int tag2;
	private Context c;

	public Item_commonality_notice_adapter(Context context) {
		super(context);
		this.c = context;
	}

	/*
	 * 将adapter中的记录数据变化的值进行初始化
	 */
	public void setinitializedata() {
		tag1 = 0;
		tag2 = 0;
		ck1 = false;
		ck2 = false;
	}

	@Override
	public View setView(final int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_commonality_notice, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.title1.setVisibility(View.GONE);

		}
		h.affirm.setVisibility(View.GONE);
		h.select.setVisibility(View.GONE);
		final Commonality_notice n = (Commonality_notice) getItem(position);
		h.time.setText(n.time);
		h.content.setText(n.content);
		h.title.setText(n.title);
		if (n.state.equals("yes")) {
			h.select.setVisibility(View.VISIBLE);
		} else {
			h.affirm.setVisibility(View.VISIBLE);
		}
		h.affirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setaffirm(n.id, position);

			}
		});

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), n.time) < 7 && ck1 == false) {
			h.title1.setVisibility(View.VISIBLE);
			h.title1.setText("最后一周的公告");
			ck1 = true;
			tag1 = position;
		} else if (position == tag1) {
			h.title1.setVisibility(View.VISIBLE);
			h.title1.setText("最后一周的公告");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), n.time) > 7 && ck2 == false) {
				h.title1.setVisibility(View.VISIBLE);
				h.title1.setText("最后一个月的公告");
				ck2 = true;
				tag2 = position;
			} else if (position == tag2) {
				h.title1.setVisibility(View.VISIBLE);
				h.title1.setText("最后一个月的公告");
			}

		}

		return v;
	}

	// 确认已读的接口
	private void setaffirm(final String id, final int postion) {

		String url = Myconstant.COMMONALITY_READMEMBERNOTICE;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						getData().get(postion).state = "yes";
						notifyDataSetChanged();
					} else {
						Toast.makeText(c, js1.getString("content"), 0).show();
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
				Toast.makeText(c, "你已经掉线，请重新登录" + "", 0).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
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

	private class Hoderview {
		private TextView title;// 广告标题
		private TextView title1;// 广告标题
		private TextView content;// 广告内容
		private TextView time;// 广告时间
		private TextView affirm;// 收到
		private LinearLayout select;// 图标

		public Hoderview(View v) {
			super();
			this.title1 = (TextView) v.findViewById(R.id.textView_item_commonality_notice_title1);
			this.title = (TextView) v.findViewById(R.id.textview_item_commonality_notice_title);
			this.content = (TextView) v.findViewById(R.id.textview_item_commonality_notice_content);
			;
			this.time = (TextView) v.findViewById(R.id.textview_item_commonality_notice_time);
			;
			this.affirm = (TextView) v.findViewById(R.id.textview_item_commonality_notice_shoudao);
			;
			this.select = (LinearLayout) v.findViewById(R.id.linearlayout_item_commonality_notice_select);
		}

	}

}
