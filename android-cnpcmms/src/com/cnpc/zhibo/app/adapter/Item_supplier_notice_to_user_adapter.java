package com.cnpc.zhibo.app.adapter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Supplier_notice_to_worker;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 供应商端公告查看已读未读列表的adapter
 */
public class Item_supplier_notice_to_user_adapter extends MyBaseAdapter<Supplier_notice_to_worker> {

	private Context context;

	public Item_supplier_notice_to_user_adapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View setView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_supplier_notice_to_user, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.remind.setVisibility(View.INVISIBLE);
		}

		final Supplier_notice_to_worker a = (Supplier_notice_to_worker) getItem(position);
		holder.remind.setTag(position);
		// holder.remind.setTag(a.id);
		holder.username.setText(a.name);
		if (a.status.equals("yes")) {
			holder.status.setText("已读");
			holder.status.setBackgroundColor(context.getResources().getColor(R.color.dingbubeijingse));
		} else {

			holder.remind.setVisibility(View.VISIBLE);

			holder.status.setText("未读");
			holder.status.setBackgroundColor(context.getResources().getColor(R.color.color_of_wait));
		}

		holder.remind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("AAA", "结果="+a.name);
				post_alert(a.id);

			}
		});
		//		holder.remind.setOnClickListener(new BtnButtonListener(a.id));
		return convertView;
	}

	class ViewHolder {
		private TextView username;
		private TextView status;
		private TextView remind;// 提醒
		private ImageView img;

		public ViewHolder(View v) {
			super();
			this.username = (TextView) v.findViewById(R.id.supplier_notice_to_user_name);
			this.status = (TextView) v.findViewById(R.id.supplier_notice_to_user_status);
			this.remind = (TextView) v.findViewById(R.id.supplier_notice_to_user_remind);
			this.img = (ImageView) v.findViewById(R.id.supplier_notice_to_user_headerImg);
		}
	}

	private void post_alert(String id) {
		String url = Myconstant.SUPPLIER_POST_NOTICE_ALERT + "?id=" + id;
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						Toast.makeText(context, obj1.getString("content"), 0).show();
					} else {
						Toast.makeText(context, obj1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, "网络情况不佳，请稍后再试", 0).show();

			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
		};
		SysApplication.getHttpQueues().add(re);
	}

//	private long lastClickTime = 0L;

	// /**
	// * 设置防止连续
	// *
	// */
	// public class CommonUtils {
	//
	// public boolean isFastDoubleClick() {
	// long time = System.currentTimeMillis();
	// long timeD = time - lastClickTime;
	// if (checked == 0) {
	// if (0 < timeD && timeD < 10000) {
	// checked = 1;
	// return true;
	// }
	//
	//
	//
	// }
	// lastClickTime = time;
	//
	// return false;
	// }
	// }
	//
	// class BtnButtonListener implements OnClickListener {
	// private String pos;
	// CommonUtils commonUtils = new CommonUtils();
	//
	// public BtnButtonListener(String id) {
	// super();
	// this.pos = id;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// // String index = (String) v.getTag();
	// int index = (Integer) v.getTag();
	// Supplier_notice_to_worker b = (Supplier_notice_to_worker) getItem(index);
	// if (pos == b.id) {
	// if (commonUtils.isFastDoubleClick() == false) {
	// post_alert();
	// } else {
	// Toast.makeText(context, "不能再点了", Toast.LENGTH_SHORT).show();
	//
	// }
	// }
	//
	// }
	//
	// }
}
