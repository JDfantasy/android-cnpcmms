package com.cnpc.zhibo.app.adapter;

/*
 * 站长端底部菜单维修界面中的维修单列表的adapter
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.Centre_HomeActivity;
import com.cnpc.zhibo.app.Centre_ItemDetailsActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_page_service;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

public class Item_centre_page_service_adapter extends MyBaseAdapter<Centre_page_service> {
	private Context c;
	private PopupWindow pop;
	private String[] count;
	private boolean ck1 = false;
	private boolean ck2 = false;
	private boolean alterviewstate = false;// 审批界面隐藏按钮的判断
	private boolean servicestate = false;// 维修界面隐藏关闭订单、发送按钮的判断
	private int tag1 = 0;
	private int tag2 = 0;
	private EditText message;

	public Item_centre_page_service_adapter(Context context) {
		super(context);
		this.c = context;
		count = new String[3];
		count[0] = "1";
		count[1] = "1";
		count[2] = "1";

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

	/*
	 * 将审批界面的item中按钮的布局进行隐藏
	 */
	public void setviewstate() {
		this.alterviewstate = true;
	}

	/*
	 * 将维修界面的发送、关闭订单、提交审批的隐藏
	 */
	public void servicestate() {
		this.servicestate = true;
	}

	@Override
	public View setView(final int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_centre_page_service, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.item_title.setVisibility(View.GONE);

		}
		final Centre_page_service a = (Centre_page_service) getItem(position);
		h.number.setText("编号："+a.number);// 编号
		h.state.setText(Myutil.getjudgestatuscolour(a.state, h.state, c));// 状态
		h.statetime.setText("耗时："+a.statetime);// 状态时间
		h.time.setText("时间："+a.time);// 时间
		h.title.setText("问题："+a.title);// 简介
		h.select.setVisibility(View.VISIBLE);// 将按钮隐藏起来
		h.rat.setVisibility(View.INVISIBLE);
		h.verify.setVisibility(View.INVISIBLE);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(a.icon);
		if (a.WorkStatus.equals("completeconfirm")) {// 将确认完成按钮显示出来
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("确认完成");
		}
		if (a.WorkStatus.equals("closeconfirm")) {// 将确认关闭按钮显示出来
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("确认关闭");
		}
		if (a.WorkStatus.equals("evaluated") || a.WorkStatus.equals("expenseing") || a.WorkStatus.equals("expensed")) {// 将星星显示出来
			if (alterviewstate) {
				//h.select.setVisibility(View.GONE);
			} else {
				h.select.setVisibility(View.VISIBLE);
				h.rat.setVisibility(View.VISIBLE);
				if (a.score.equals("null") || a.score.equals("")) {
					h.rat.setRating(1);
				} else {
					if (a.score == null) {
						h.rat.setRating(1);
					} else {
						h.rat.setRating(Float.parseFloat(a.score));
					}

				}
			}

		} else {
			//h.select.setVisibility(View.GONE);
		}
		if (a.WorkStatus.equals("unevaluated")) {// 将开始评价按钮显示出来
			h.select.setVisibility(View.VISIBLE);
			
			h.verify.setText("开始评价");
		}
		if (a.WorkStatus.equals("approved")) {// 将发送按钮显示出来
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("发送");
			if (servicestate) {
				h.select.setVisibility(View.GONE);
			}
		}
		if (a.WorkStatus.equals("unsubmited")) {// 将提交按钮显示出来
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("提交审批");
			if (servicestate) {
				h.select.setVisibility(View.GONE);
			}
		}
		if (a.WorkStatus.equals("unapproved")) {// 将关闭订单的按钮显示出来
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("关闭订单");
			if (servicestate) {
				h.select.setVisibility(View.GONE);
			}
		}
		if (alterviewstate) {
			h.select.setVisibility(View.VISIBLE);// 将按钮隐藏起来
			h.rat.setVisibility(View.INVISIBLE);
			h.verify.setVisibility(View.INVISIBLE);
		}
		h.verify.setOnClickListener(new View.OnClickListener() {
			// 对按钮设置监听事件
			@Override
			public void onClick(View v) {
				if (a.WorkStatus.equals("closeconfirm")) {
					// 确认关闭订单的方法
					set_disposemonad(Myconstant.CENTRE__VERIFY_SHUTMONAD, a.id, position);
				}
				if (a.WorkStatus.equals("completeconfirm")) {
					// 确认完成订单的方法
					// set_disposemonad(Myconstant.CENTRE__VERIFY_ACCOMPLISHMONAD,
					// a.id, position);
					set_evaluatemonad(v, a.id, position);
				}
				if (a.WorkStatus.equals("unevaluated")) {
					// 开始评价的方法
					set_evaluatemonad(v, a.id, position);

				}
				if (a.WorkStatus.equals("approved")) {
					// 将维修单发送给维修工的方法
					set_disposemonad(Myconstant.CENTRE_SEND_EXAMINEEDSERVICE, a.id, position);
				}
				if (a.WorkStatus.equals("unsubmited")) {
					// 提交审批的方法
					set_disposemonad(Myconstant.CENTRE_SUBMITSERVICECNPC, a.id, position);
				}
				if (a.WorkStatus.equals("unapproved")) {
					// 将主动关闭订单的方法
					set_disposemonad(Myconstant.CENTRE_INITIATIVE_SHUTSERVICEMONAD, a.id, position);

				}
			}
		});

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) <= 7 && ck1 == false) {
			h.item_title.setVisibility(View.VISIBLE);
			h.item_title.setText("最后一周的维修单");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) <= 7 && position == tag1) {
			h.item_title.setVisibility(View.VISIBLE);
			h.item_title.setText("最后一周的维修单");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7) {
				if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7 && ck2 == false) {
					h.item_title.setVisibility(View.VISIBLE);
					h.item_title.setText("最后一个月的维修单");
					ck2 = true;
					tag2 = position;
				} else if (position == tag2) {
					h.item_title.setVisibility(View.VISIBLE);
					h.item_title.setText("最后一个月的维修单");
				}
			}

		}

		return v;
	}

	/*
	 * 开始评价执行的方法,显示评价界面的popupwindow
	 */
	private void set_evaluatemonad(View v, final String id, final int postion) {

		View v1 = LayoutInflater.from(c).inflate(R.layout.popup_grade, null);// 创建view
		pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法
		pop.setBackgroundDrawable(new ColorDrawable());
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
		pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		TextView confirm = (TextView) v1.findViewById(R.id.textView_popup_grade_verify);// 确认按钮
		final RatingBar ratingbar1 = (RatingBar) v1.findViewById(R.id.ratingBar_popup_grade_grade1);
		final RatingBar ratingbar2 = (RatingBar) v1.findViewById(R.id.ratingBar_popup_grade_grade2);
		final RatingBar ratingbar3 = (RatingBar) v1.findViewById(R.id.ratingBar_popup_grade_grade3);
		message = (EditText) v1.findViewById(R.id.editText_popup_grade_message);

		ratingbar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				String str = rating + "";

				count[0] = str.substring(0, 1);
				if (count[0].equals("0")) {
					ratingbar1.setRating(1);
					count[0] = "1";
				}

			}
		});
		ratingbar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				String str = rating + "";
				count[1] = str.substring(0, 1);
				if (count[1].equals("0")) {
					ratingbar2.setRating(1);
					count[1] = "1";
				}
			}
		});
		ratingbar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				String str = rating + "";
				count[2] = str.substring(0, 1);
				if (count[2].equals("0")) {
					ratingbar3.setRating(1);
					count[2] = "1";
				}

			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = "很好";
				try {
					content = URLEncoder.encode(message.getText().toString(), "UTF-8");
					String url = Myconstant.CENTRE__VERIFY_ACCOMPLISHMONAD + "?id=" + id + "&content=" + content;
					StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								JSONObject js = new JSONObject(response);
								JSONObject js1 = js.getJSONObject("response");
								if (js1.getString("type").equals("success")) {
									Toast.makeText(c, js1.getString("content"), 0).show();
									pop.dismiss();
									data.remove(postion);
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
							Toast.makeText(c, "你已经掉线，请重新登录" + "", 0).show();
						}
					}) {
						@Override
						public Map<String, String> getHeaders() throws AuthFailureError {

							Map<String, String> headers = new HashMap<String, String>();
							System.out.println("用来请求的token值：" + Myconstant.token);
							headers.put("Content-Type", "application/json");
							headers.put("accept", "application/json");
							headers.put("api_key", Myconstant.token);
							return headers;

						}

						@Override
						protected Map<String, String> getParams() throws AuthFailureError {
							Map<String, String> map = new HashMap<String, String>();
							// map.put("id", id);
							// map.put("score", count);
							return map;
						}

						@Override
						public byte[] getBody() throws AuthFailureError {
							StringBuilder builder = new StringBuilder();
							builder.append(getevaluatedata());
							System.out.println(builder.toString());
							return builder.toString().getBytes();
						}
					};
					SysApplication.getHttpQueues().add(re);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					pop.dismiss();
				}

			}
		});

	}

	// 获取评价的数据
	private String getevaluatedata() {
		String evaluates = "";
		JSONArray array = new JSONArray();
		for (int i = 0; i < 3; i++) {
			JSONObject obj = new JSONObject();

			try {
				obj.put("score", count[i]);

				if (i == 0) {
					obj.put("type", "speed");
				} else if (i == 1) {
					obj.put("type", "attitude");
				} else {
					obj.put("type", "quality");
				}
				array.put(obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// quality, speed, attitude——质量、速度、态度

		evaluates = array.toString();
		return evaluates;

	}

	// 确认关闭订单、确认完成订单、提交审批的方法
	private void set_disposemonad(String url, final String id, final int postion) {
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						if (data.get(postion).WorkStatus.equals("closeconfirm")) {// 确认关闭
							data.remove(postion);
							notifyDataSetChanged();
						} else if (data.get(postion).WorkStatus.equals("completeconfirm")) {// 确认完成
							data.remove(postion);
							notifyDataSetChanged();
						} else if (data.get(postion).WorkStatus.equals("unsubmited")) {// 提交审批
							data.remove(postion);
							notifyDataSetChanged();
						} else if (data.get(postion).WorkStatus.equals("approved")) {// 发送
							data.remove(postion);
							notifyDataSetChanged();
						} else if (data.get(postion).WorkStatus.equals("unapproved")) {// 主动关闭订单
							data.remove(postion);
							notifyDataSetChanged();
						} else {
							notifyDataSetChanged();
						}

						Toast.makeText(c, js1.getString("content"), 0).show();

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
		private TextView number;// 编号
		private TextView time;// 时间
		private TextView title;// 简介
		private TextView state;// 状态
		private TextView statetime;// 状态时间
		private TextView item_title;// item的标题
		private TextView verify;// 评价订单、完成订单
		private LinearLayout select;// 放置确认完成按钮的布局
		private Myroundcacheimageview icon;// 图片
		private RatingBar rat;// 评价

		public Hoderview(View v) {
			super();
			this.number = (TextView) v.findViewById(R.id.textView_item_centre_page_service_number);
			this.time = (TextView) v.findViewById(R.id.textView_item_centre_page_service_time);
			this.title = (TextView) v.findViewById(R.id.textView_item_centre_page_service_title);
			this.state = (TextView) v.findViewById(R.id.textView_item_centre_page_service_state);
			this.statetime = (TextView) v.findViewById(R.id.textView_item_centre_page_service_statetime);
			this.item_title = (TextView) v.findViewById(R.id.textView_item_centre_page_service_item_title);
			this.verify = (TextView) v.findViewById(R.id.textView_item_centre_page_service_verify);
			this.select = (LinearLayout) v.findViewById(R.id.linearlayout_item_centre_page_service_select);
			this.icon = (Myroundcacheimageview) v.findViewById(R.id.imageview_item_centre_page_service_icon);
			this.rat = (RatingBar) v.findViewById(R.id.ratingBar_item_centre_page_service);
		}

	}

}
