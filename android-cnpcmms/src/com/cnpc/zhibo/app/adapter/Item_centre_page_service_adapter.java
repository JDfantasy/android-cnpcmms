package com.cnpc.zhibo.app.adapter;

/*
 * վ���˵ײ��˵�ά�޽����е�ά�޵��б��adapter
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
	private boolean alterviewstate = false;// �����������ذ�ť���ж�
	private boolean servicestate = false;// ά�޽������عرն��������Ͱ�ť���ж�
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
	 * ��adapter�еļ�¼���ݱ仯��ֵ���г�ʼ��
	 */
	public void setinitializedata() {
		tag1 = 0;
		tag2 = 0;
		ck1 = false;
		ck2 = false;
	}

	/*
	 * �����������item�а�ť�Ĳ��ֽ�������
	 */
	public void setviewstate() {
		this.alterviewstate = true;
	}

	/*
	 * ��ά�޽���ķ��͡��رն������ύ����������
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
		h.number.setText("��ţ�"+a.number);// ���
		h.state.setText(Myutil.getjudgestatuscolour(a.state, h.state, c));// ״̬
		h.statetime.setText("��ʱ��"+a.statetime);// ״̬ʱ��
		h.time.setText("ʱ�䣺"+a.time);// ʱ��
		h.title.setText("���⣺"+a.title);// ���
		h.select.setVisibility(View.VISIBLE);// ����ť��������
		h.rat.setVisibility(View.INVISIBLE);
		h.verify.setVisibility(View.INVISIBLE);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(a.icon);
		if (a.WorkStatus.equals("completeconfirm")) {// ��ȷ����ɰ�ť��ʾ����
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("ȷ�����");
		}
		if (a.WorkStatus.equals("closeconfirm")) {// ��ȷ�Ϲرհ�ť��ʾ����
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("ȷ�Ϲر�");
		}
		if (a.WorkStatus.equals("evaluated") || a.WorkStatus.equals("expenseing") || a.WorkStatus.equals("expensed")) {// ��������ʾ����
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
		if (a.WorkStatus.equals("unevaluated")) {// ����ʼ���۰�ť��ʾ����
			h.select.setVisibility(View.VISIBLE);
			
			h.verify.setText("��ʼ����");
		}
		if (a.WorkStatus.equals("approved")) {// �����Ͱ�ť��ʾ����
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("����");
			if (servicestate) {
				h.select.setVisibility(View.GONE);
			}
		}
		if (a.WorkStatus.equals("unsubmited")) {// ���ύ��ť��ʾ����
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("�ύ����");
			if (servicestate) {
				h.select.setVisibility(View.GONE);
			}
		}
		if (a.WorkStatus.equals("unapproved")) {// ���رն����İ�ť��ʾ����
			h.select.setVisibility(View.VISIBLE);
			h.verify.setVisibility(View.VISIBLE);
			h.verify.setText("�رն���");
			if (servicestate) {
				h.select.setVisibility(View.GONE);
			}
		}
		if (alterviewstate) {
			h.select.setVisibility(View.VISIBLE);// ����ť��������
			h.rat.setVisibility(View.INVISIBLE);
			h.verify.setVisibility(View.INVISIBLE);
		}
		h.verify.setOnClickListener(new View.OnClickListener() {
			// �԰�ť���ü����¼�
			@Override
			public void onClick(View v) {
				if (a.WorkStatus.equals("closeconfirm")) {
					// ȷ�Ϲرն����ķ���
					set_disposemonad(Myconstant.CENTRE__VERIFY_SHUTMONAD, a.id, position);
				}
				if (a.WorkStatus.equals("completeconfirm")) {
					// ȷ����ɶ����ķ���
					// set_disposemonad(Myconstant.CENTRE__VERIFY_ACCOMPLISHMONAD,
					// a.id, position);
					set_evaluatemonad(v, a.id, position);
				}
				if (a.WorkStatus.equals("unevaluated")) {
					// ��ʼ���۵ķ���
					set_evaluatemonad(v, a.id, position);

				}
				if (a.WorkStatus.equals("approved")) {
					// ��ά�޵����͸�ά�޹��ķ���
					set_disposemonad(Myconstant.CENTRE_SEND_EXAMINEEDSERVICE, a.id, position);
				}
				if (a.WorkStatus.equals("unsubmited")) {
					// �ύ�����ķ���
					set_disposemonad(Myconstant.CENTRE_SUBMITSERVICECNPC, a.id, position);
				}
				if (a.WorkStatus.equals("unapproved")) {
					// �������رն����ķ���
					set_disposemonad(Myconstant.CENTRE_INITIATIVE_SHUTSERVICEMONAD, a.id, position);

				}
			}
		});

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) <= 7 && ck1 == false) {
			h.item_title.setVisibility(View.VISIBLE);
			h.item_title.setText("���һ�ܵ�ά�޵�");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) <= 7 && position == tag1) {
			h.item_title.setVisibility(View.VISIBLE);
			h.item_title.setText("���һ�ܵ�ά�޵�");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7) {
				if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7 && ck2 == false) {
					h.item_title.setVisibility(View.VISIBLE);
					h.item_title.setText("���һ���µ�ά�޵�");
					ck2 = true;
					tag2 = position;
				} else if (position == tag2) {
					h.item_title.setVisibility(View.VISIBLE);
					h.item_title.setText("���һ���µ�ά�޵�");
				}
			}

		}

		return v;
	}

	/*
	 * ��ʼ����ִ�еķ���,��ʾ���۽����popupwindow
	 */
	private void set_evaluatemonad(View v, final String id, final int postion) {

		View v1 = LayoutInflater.from(c).inflate(R.layout.popup_grade, null);// ����view
		pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���
		pop.setBackgroundDrawable(new ColorDrawable());
		// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���
		pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		TextView confirm = (TextView) v1.findViewById(R.id.textView_popup_grade_verify);// ȷ�ϰ�ť
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
				String content = "�ܺ�";
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
							Toast.makeText(c, "���Ѿ����ߣ������µ�¼" + "", 0).show();
						}
					}) {
						@Override
						public Map<String, String> getHeaders() throws AuthFailureError {

							Map<String, String> headers = new HashMap<String, String>();
							System.out.println("���������tokenֵ��" + Myconstant.token);
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

	// ��ȡ���۵�����
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
		// quality, speed, attitude�����������ٶȡ�̬��

		evaluates = array.toString();
		return evaluates;

	}

	// ȷ�Ϲرն�����ȷ����ɶ������ύ�����ķ���
	private void set_disposemonad(String url, final String id, final int postion) {
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						if (data.get(postion).WorkStatus.equals("closeconfirm")) {// ȷ�Ϲر�
							data.remove(postion);
							notifyDataSetChanged();
						} else if (data.get(postion).WorkStatus.equals("completeconfirm")) {// ȷ�����
							data.remove(postion);
							notifyDataSetChanged();
						} else if (data.get(postion).WorkStatus.equals("unsubmited")) {// �ύ����
							data.remove(postion);
							notifyDataSetChanged();
						} else if (data.get(postion).WorkStatus.equals("approved")) {// ����
							data.remove(postion);
							notifyDataSetChanged();
						} else if (data.get(postion).WorkStatus.equals("unapproved")) {// �����رն���
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
				Toast.makeText(c, "���Ѿ����ߣ������µ�¼" + "", 0).show();
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

	private class Hoderview {
		private TextView number;// ���
		private TextView time;// ʱ��
		private TextView title;// ���
		private TextView state;// ״̬
		private TextView statetime;// ״̬ʱ��
		private TextView item_title;// item�ı���
		private TextView verify;// ���۶�������ɶ���
		private LinearLayout select;// ����ȷ����ɰ�ť�Ĳ���
		private Myroundcacheimageview icon;// ͼƬ
		private RatingBar rat;// ����

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
