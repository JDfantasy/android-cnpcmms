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
import com.cnpc.zhibo.app.Maintain_apply_newapplylistActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

import android.app.ActionBar.LayoutParams;
//ά�޶˵�ά��������ά�޵�listview��adapter
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Item_maintain_serviceing_adapter extends MyBaseAdapter<Maintain_startList_item> {

	private Context context;
	private PopupWindow pop;
	private Maintain_startList_item m;
	private int finishNumber = 0;// ��ɶ����ķ������
	private int closeNumber = 1;// �رն����������
	private Hoderview h;
	// �µ���������
	public boolean ck1 = false;
	public boolean ck2 = false;
	public int tag1;
	public int tag2;

	private  boolean scrollState=false; //��ʶ�Ƿ���ڹ�������
	
	public void setScrollState(boolean scrollState) {  
        this.scrollState = scrollState;  
    } 
	
	public Item_maintain_serviceing_adapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View setView(final int position, View v, ViewGroup parent) {
		
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintain_serviceing, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.title.setVisibility(View.GONE);
		}
		// ��ӹ��߷�����ǰʱ�䵽����ʱ������ʾ���һ�ܣ����һ�£�����ǰ����һ��ǰ
		//m = (Maintain_startList_item) getItem(position);
		m = (Maintain_startList_item) data.get(position);

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && ck1 == false) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("���һ�ܵ�ά�޵�");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && position == tag1) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("���һ�ܵ�ά�޵�");
		} else {
			if(Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7){
				if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && ck2 == false) {
					h.title.setVisibility(View.VISIBLE);
					h.displayDate.setText("���һ���µ�ά�޵�");
					ck2 = true;
					tag2 = position;
				} else if (position == tag2) {
					h.title.setVisibility(View.VISIBLE);
					h.displayDate.setText("���һ���µ�ά�޵�");
				}
			}
		}
		
//		Log.i("tag", "adapter.tag1="+tag1);
//		Log.i("tag", "adapter.tag2="+tag2);
		
		/*if (!scrollState){  
			h.indentNumber.setText(m.indentNumber);
			h.name.setText(m.gasStationname);
			h.problem.setText(m.problem);
			h.time.setText(m.time);
			
			//h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
			h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
			h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
			h.icon.setImageUrl(m.iconPath);
			
			h.state.setText("ά����");
			h.finishservice.setVisibility(View.GONE);
			h.closeIndent.setVisibility(View.GONE);
			h.newApplylist.setVisibility(View.GONE);
			h.starrating.setVisibility(View.GONE);
			h.line.setVisibility(View.GONE);
			if (m.serviceState.equals("unsended")) {
				h.state.setText("δ����");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("unsubmited")) {
				h.state.setText("���ύ");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("progress")) {
				h.state.setText("����ά��");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.VISIBLE);
				h.closeIndent.setVisibility(View.VISIBLE);
				h.newApplylist.setVisibility(View.GONE);
				h.starrating.setVisibility(View.GONE);
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("wait")) {
				h.state.setText("�ȴ�����");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("approved")) {
				h.state.setText("����ͨ��");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("completeconfirm")) {
				h.state.setText("��ɴ�ȷ��");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("continued")) {
				h.state.setText("����ά��");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.VISIBLE);
				h.closeIndent.setVisibility(View.VISIBLE);
				h.newApplylist.setVisibility(View.GONE);
				h.starrating.setVisibility(View.GONE);
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("unapproved")) {
				h.state.setText("��������");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("closed")) {
				h.state.setText("�ѹر�");
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			} else if (m.serviceState.equals("closeconfirm")) {
				h.state.setText("�رմ�ȷ��");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
			} else if (m.serviceState.equals("compelted")) {
				h.state.setText("�����");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.GONE);
				h.closeIndent.setVisibility(View.GONE);
				h.newApplylist.setVisibility(View.VISIBLE);
				h.starrating.setVisibility(View.GONE);
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			} else if (m.serviceState.equals("unevaluated")) {
				h.state.setText("������");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.GONE);
				h.closeIndent.setVisibility(View.GONE);
				h.newApplylist.setVisibility(View.VISIBLE);
				h.starrating.setVisibility(View.GONE);
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			} else if (m.serviceState.equals("evaluated")) {
				h.state.setText("������");
				h.line.setVisibility(View.VISIBLE);
				h.footer.setVisibility(View.VISIBLE);
				h.finishservice.setVisibility(View.GONE);
				h.closeIndent.setVisibility(View.GONE);
				h.newApplylist.setVisibility(View.VISIBLE);
				h.starrating.setVisibility(View.VISIBLE);
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
				//h.starrating.setProgressTintList(Color.RED);
				if (m.starNumber == null||m.starNumber.equals("null")) {
					h.starrating.setRating(1);
				} else {
					h.starrating.setRating(Float.parseFloat(m.starNumber));
				}
			}else if (m.serviceState.equals("expenseing")) {
				h.state.setText("������");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
			}else if (m.serviceState.equals("expensed")) {
				h.state.setText("�ѱ���");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
			}
  
        }else{  
        	h.indentNumber.setText(m.indentNumber);
    		h.name.setText(m.gasStationname);
    		h.problem.setText(m.problem);
    		h.time.setText(m.time);
			h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
			h.state.setText("������"); 
			h.state.setTextColor(context.getResources().getColor(R.color.huise));
        }*/  
		  
		h.indentNumber.setText(m.indentNumber);
		h.name.setText(m.gasStationname);
		h.problem.setText(m.problem);
		h.time.setText(m.time);
		
		//h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(m.iconPath);
		
		h.state.setText("ά����");
		h.finishservice.setVisibility(View.GONE);
		h.closeIndent.setVisibility(View.GONE);
		h.newApplylist.setVisibility(View.GONE);
		h.starrating.setVisibility(View.GONE);
		if (m.serviceState.equals("unsended")) {
			h.state.setText("δ����");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("unsubmited")) {
			h.state.setText("���ύ");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("progress")) {
			h.state.setText("����ά��");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.VISIBLE);
			h.closeIndent.setVisibility(View.VISIBLE);
			h.newApplylist.setVisibility(View.GONE);
			h.starrating.setVisibility(View.GONE);
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("wait")) {
			h.state.setText("�ȴ�����");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("approved")) {
			h.state.setText("����ͨ��");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("completeconfirm")) {
			h.state.setText("��ɴ�ȷ��");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("continued")) {
			h.state.setText("����ά��");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.VISIBLE);
			h.closeIndent.setVisibility(View.VISIBLE);
			h.newApplylist.setVisibility(View.GONE);
			h.starrating.setVisibility(View.GONE);
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("unapproved")) {
			h.state.setText("��������");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("closed")) {
			h.state.setText("�ѹر�");
			h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
		} else if (m.serviceState.equals("closeconfirm")) {
			h.state.setText("�رմ�ȷ��");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else if (m.serviceState.equals("compelted")) {
			h.state.setText("�����");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.GONE);
			h.closeIndent.setVisibility(View.GONE);
			h.newApplylist.setVisibility(View.VISIBLE);
			h.starrating.setVisibility(View.GONE);
			h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
		} else if (m.serviceState.equals("unevaluated")) {
			h.state.setText("������");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.GONE);
			h.closeIndent.setVisibility(View.GONE);
			h.newApplylist.setVisibility(View.VISIBLE);
			h.starrating.setVisibility(View.GONE);
			h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
		} else if (m.serviceState.equals("evaluated")) {
			h.state.setText("������");
			h.footer.setVisibility(View.VISIBLE);
			h.finishservice.setVisibility(View.GONE);
			h.closeIndent.setVisibility(View.GONE);
			h.newApplylist.setVisibility(View.VISIBLE);
			h.starrating.setVisibility(View.VISIBLE);
			h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			//h.starrating.setProgressTintList(Color.RED);
			if (m.starNumber == null||m.starNumber.equals("null")) {
				h.starrating.setRating(1);
			} else {
				h.starrating.setRating(Float.parseFloat(m.starNumber));
			}
		}else if (m.serviceState.equals("expenseing")) {
			h.state.setText("������");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
		}else if (m.serviceState.equals("expensed")) {
			h.state.setText("�ѱ���");
			h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
		}

    
		// ���ά��
		h.finishservice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterCanle(v, finishNumber, data.get(position).id, position);
			}
		});
		// �رն���
		h.closeIndent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterCanle(v, closeNumber, data.get(position).id, position);
			}
		});
		// �½�������
		// h.newApplylist.setOnClickListener(l);
		h.newApplylist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(context, Maintain_apply_newapplylistActivity.class);
					intent.putExtra("id", data.get(position).id);
					intent.putExtra("gasStationname", data.get(position).gasStationname);
					context.startActivity(intent);
					h.newApplylist.setVisibility(View.GONE);
//				getData().remove(position);
//				getData().get(position).serviceState="expenseing";
//				notifyDataSetChanged();
//				applylist();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return v;
	}
	
	private void applylist(){
		this.notifyDataSetChanged();
		h.newApplylist.setVisibility(View.GONE);
	}

	private class Hoderview {
		// ������š�״̬�����ơ����⡢ʱ�䡢�ײ�������ͷ����ʾʱ��,�½�������
		private TextView indentNumber, state, name, problem, time, finishservice, closeIndent, displayDate,
				newApplylist;
		private FrameLayout title;
		private LinearLayout footer;
		private Myroundcacheimageview icon;//�����ʾ��ͼƬ
		private RatingBar starrating;//����

		public Hoderview(View v) {
			super();
			this.starrating=(RatingBar) v.findViewById(R.id.item_maintain_serviceing_ratingBar);//����
			this.icon=(Myroundcacheimageview) v.findViewById(R.id.item_maintainhome_serivce_leftimage);
			this.indentNumber = (TextView) v.findViewById(R.id.textView_item_maintain_serviceing_indentNumber);
			this.name = (TextView) v.findViewById(R.id.item_maintain_service_name);
			this.problem = (TextView) v.findViewById(R.id.item_maintain_service_problem);
			this.time = (TextView) v.findViewById(R.id.item_maintain_service_time);
			this.state = (TextView) v.findViewById(R.id.maintain_service_stateTextView);
			// �ײ�
			this.footer = (LinearLayout) v.findViewById(R.id.item_maintain_page_Linearlayout_footer);
			// ���ά��
			this.finishservice = (TextView) v.findViewById(R.id.item_maintain_serviceing_finishservice);
			// �رն���
			this.closeIndent = (TextView) v.findViewById(R.id.item_maintain_serviceing_closeIndent);
			// ������ͷ
			this.title = (FrameLayout) v.findViewById(R.id.item_maintain_service_title);
			// ��ͷ��ʾ����
			this.displayDate = (TextView) v.findViewById(R.id.item_maintain_service_dateTextView);
			// �½�������
			this.newApplylist = (TextView) v.findViewById(R.id.item_maintain_serviceing_newapplylist);
		}
	}

	// ��������
	/*
	 * private View.OnClickListener l = new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { switch (v.getId()) { // ���ά��
	 * 
	 * case R.id.item_maintain_serviceing_finishservice: Toast.makeText(context,
	 * "��ȴ�վ����ȷ��", 0).show(); closeAndFinishIndent(finishNumber); break; // �رն���
	 * case R.id.item_maintain_serviceing_closeIndent: Toast.makeText(context,
	 * "�رն������������ӵ�ǰ����ɾ��", 0).show(); closeAndFinishIndent(closeNumber);
	 * notifyDataSetChanged(); break;
	 * 
	 * // �½�ά�޵� case R.id.item_maintain_serviceing_newapplylist:
	 * Toast.makeText(context, "�½�������", 0).show(); Intent intent=new
	 * Intent(context, Maintain_apply_newapplylistActivity.class);
	 * intent.putExtra("id", data.get(location)); context.startActivity(intent);
	 * break; }
	 * 
	 * } };
	 */

	// ����ȷ�Ϻ�ȡ����
	private void enterCanle(View v, final int number, final String id, final int positon) {
		System.out.println("postion:" + positon);
		System.out.println("number:" + number);
		View v1 = LayoutInflater.from(context).inflate(R.layout.poppup_mainhome_closefinish_entercanle, null);// ����view
		pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���
		// pop.setBackgroundDrawable(new ColorDrawable());
		// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���
		pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		Button enter = (Button) v1.findViewById(R.id.button_popup_closefinish_enter);// ȷ�ϰ�ť
		Button canle = (Button) v1.findViewById(R.id.button_popup_closefinish_canle);// ȡ����ť
		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeAndFinishIndent(number, id, positon);

				pop.dismiss();

			}
		});
		canle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});
	}

	// �رն�������ɶ���
	public void closeAndFinishIndent(final int number, final String id, final int postion) {
		String url = null;
		if (number == finishNumber) {// ά�޽���,��ɶ���
			url = Myconstant.MAINTAIN_SERVICE_ITEM_FINISH;
		} else if (number == closeNumber) {// �޷�ά�޹رն���
			url = Myconstant.MAINTAIN_SERVICE_ITEM_CLOSED;
		}
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(context, "�ɹ�", Toast.LENGTH_SHORT).show();
						if (number == finishNumber) {// ά�޽���,��ɶ���
							getData().get(postion).serviceState = "completeconfirm";
							notifyDataSetChanged();
						} else if (number == closeNumber) {// �޷�ά�޹رն���
							getData().get(postion).serviceState = "closeconfirm";
							notifyDataSetChanged();

						} else {
							notifyDataSetChanged();
						}
					} else {
						Toast.makeText(context, "ʧ��", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, "����ʧ�ܣ�" + error.toString(), Toast.LENGTH_SHORT).show();
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
				map.put("id", id);// ���޵����
				return map;
			}
		};
		SysApplication.getHttpQueues().add(request);
	}

}
