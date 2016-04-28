package com.cnpc.zhibo.app.adapter;
import java.io.File;
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
import com.cnpc.zhibo.app.Maintain_apply_modifyapplybillActivity;
import com.cnpc.zhibo.app.Maintain_apply_newapplylistActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_apply_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

import android.R.integer;
//ά�޶˵ı�����adapter
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Item_maintain_apply_adapter extends
		MyBaseAdapter<Maintain_apply_item> {

	private Context context;
	private Maintain_apply_item m;
	private String applyState="δ����";//����״̬
	private Hoderview h;
	
	//�µ���������
	public boolean ck1 = false;
	public boolean ck2 = false;
	public int tag1;
	public int tag2;
		
		private  boolean scrollState=false; //��ʶ�Ƿ���ڹ�������
		public void setScrollState(boolean scrollState) {  
	        this.scrollState = scrollState;  
	    } 
	
	public Item_maintain_apply_adapter(Context context) {
		super(context);
		this.context=context;
	}

	@Override
	public View setView(final int position, View v, ViewGroup parent) {
		
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintain_apply, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.title.setVisibility(View.GONE);
		}
		
		m = (Maintain_apply_item) getData().get(position);
		Log.i("tag", "applystate_applyadapter="+m.applyState);
		//����жϲ��ü�
		/*if (m.iconPath!=null&&!m.iconPath.equals("")) {
			h.icon.setImageUrl(m.iconPath);
		} */
		
		//��ӹ��߷�����ǰʱ�䵽����ʱ������ʾ���һ�ܣ����һ�£�����ǰ����һ��ǰ
		if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && ck1 == false) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("���һ�ܵı�����");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && position == tag1) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("���һ�ܵı�����");
		} else {
			if(Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7){
				if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && ck2 == false) {
					h.title.setVisibility(View.VISIBLE);
					h.displayDate.setText("���һ���µı�����");
					ck2 = true;
					tag2 = position;
				} else if (position == tag2) {
					h.title.setVisibility(View.VISIBLE);
					h.displayDate.setText("���һ���µı�����");
				}
			}
		}
		
		h.send.setVisibility(View.GONE);
		h.modify.setVisibility(View.GONE);
		h.indentNumber.setText(m.indentNumber);
		h.name.setText(m.gasStationname);
		h.problem.setText(m.problem);
		h.time.setText(m.time);
		h.totalMoney.setText("����"+m.totalMoney+"Ԫ");
		//h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);//��Ҫ����Ĭ�ϵ�����
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(m.iconPath);
		h.state.setText("����");
		if(m.applyState!=null&&m.applyState.length()!=0){
			if (m.applyState.equals("progress")) {
				h.state.setText("������");
				h.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
				h.totalMoney.setTextColor(context.getResources().getColor(R.color.color_of_wait));
			}else if(m.applyState.equals("unsubmit")){
				h.state.setText("δ�ύ");
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
				h.totalMoney.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
				h.send.setVisibility(View.VISIBLE);
				h.modify.setVisibility(View.VISIBLE);
			}else if(m.applyState.equals("approved")){
				h.state.setText("����ͨ��");
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
				h.totalMoney.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			}else if(m.applyState.equals("unapproved")){
				h.state.setText("��������");
				h.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
				h.totalMoney.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
				h.send.setVisibility(View.VISIBLE);
				h.modify.setVisibility(View.VISIBLE);
			}else {
				h.state.setText("��״̬");
			}
		}
		
		/*if (!scrollState){
			
		}else {
			h.indentNumber.setText(m.indentNumber);
			h.name.setText(m.gasStationname);
			h.problem.setText(m.problem);
			h.time.setText(m.time);
			h.totalMoney.setText("������");
			h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);//��Ҫ����Ĭ�ϵ�����
			h.state.setText("������");
			h.state.setTextColor(context.getResources().getColor(R.color.huise));
			h.totalMoney.setTextColor(context.getResources().getColor(R.color.huise));
		}*/
		//���ͼ���
		h.send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendsubmit(getData().get(position).id,position);
			}
		});
		//�޸ļ���
		h.modify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, Maintain_apply_modifyapplybillActivity.class);
				intent.putExtra("applybillid", getData().get(position).id);
//				Log.i("tag", "m.id="+m.id);
//				Log.i("tag", "m.id="+getData().get(position).id);
				intent.putExtra("gasStationname", m.gasStationname);
				context.startActivity(intent);
//				intent1.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//				context.startActivityForResult(intent, 100);
			}
		});
		
		return v;
	}

	private class Hoderview {
		//������š�״̬�����ơ����⡢ʱ�䡢��ʾʱ��
		private TextView indentNumber,state,name,problem,time,displayDate,totalMoney;
		private FrameLayout title;
		private Myroundcacheimageview icon;//�����ʾ��ͼƬ
		private TextView send,modify;//�ײ���ʾ��ť ���� �޸�
		
		public Hoderview(View v) {
			super();
			this.icon=(Myroundcacheimageview) v.findViewById(R.id.item_maintain_apply_leftimage);
			this.indentNumber = (TextView) v
					.findViewById(R.id.textView_item_maintain_apply_indentNumber);
			this.name = (TextView) v
					.findViewById(R.id.item_maintain_apply_name);
			this.problem = (TextView) v
					.findViewById(R.id.item_maintain_apply_problem);
			this.time = (TextView) v
					.findViewById(R.id.item_maintain_apply_time);
			this.state = (TextView) v
					.findViewById(R.id.maintain_apply_stateTextView);//����״̬
			//������ͷ
			this.title =  (FrameLayout) v
					.findViewById(R.id.item_maintain_apply_title);
			//��ͷ��ʾ�������һ��
			this.displayDate =(TextView) v
					.findViewById(R.id.item_maintain_apply_dateTextView);
			//�ܽ��
			this.totalMoney=(TextView) v.findViewById(R.id.item_maintain_apply_totalMoney);
			//����
			this.send=(TextView) v.findViewById(R.id.item_maintain_apply_footerbottom_send);
			//�޸�
			this.modify=(TextView) v.findViewById(R.id.item_maintain_apply_footerbottom_modify);
			
		}
	}
	
	//���ͷ���
	private void sendsubmit(final String id,final int position){
		String url = Myconstant.MAINTAIN_NEWAPPLYSUBMIT;

		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject js = new JSONObject(response);// �õ�json����
					JSONObject js1 = js.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(context.getApplicationContext(), "���ͳɹ�", 0).show();
						getData().get(position).applyState="progress";
						notifyDataSetChanged();
					} else {
						//���ݴ����ԭ��
						Log.i("tag", "content="+js1.getString("content"));
						Toast.makeText(context.getApplicationContext(), js1.getString("content"), 0).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("tag", "json�����쳣");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//����ʧ�ܵ�ԭ��
				Log.i("tag", "error="+error);
				Toast.makeText(context.getApplicationContext(), "����ʧ��"+error.getMessage(), 0).show();
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
	
	

	
}
