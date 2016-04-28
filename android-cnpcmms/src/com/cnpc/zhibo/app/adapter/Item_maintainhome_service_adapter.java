package com.cnpc.zhibo.app.adapter;

//ά�޶˵�ά��������ά�޵�listview��adapter
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.ChatActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Centre_newsverify_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;
import com.easemob.chat.core.f;
import com.easemob.easeui.EaseConstant;

public class Item_maintainhome_service_adapter extends MyBaseAdapter<Maintain_startList_item> {

	private String state;// ����״̬
	private Context context;
	private Maintain_startList_item m;
	private int finishNumber = 0;// ��ɶ����ķ������
	private int closeNumber = 1;// �رն����������
	// �µ���������
	public boolean ck1 = false;
	public boolean ck2 = false;
	public int tag1;
	public int tag2;

	private boolean scrollState = false; // ��ʶ�Ƿ���ڹ�������

	public void setScrollState(boolean scrollState) {
		this.scrollState = scrollState;
	}

	public Item_maintainhome_service_adapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintainhome_service, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
			h.title.setVisibility(View.GONE);
		}
		// ��ӹ��߷�����ǰʱ�䵽����ʱ������ʾ���һ�ܣ����һ�£�����ǰ����һ��ǰ
		m = (Maintain_startList_item) getItem(position);
		state = m.serviceState;

		/*
		 * if (m.iconPath!=null&&!m.iconPath.equals("")) {
		 * h.icon.setImageUrl(m.iconPath); }
		 */

		/*
		 * if(m.timeState!=null&&m.timeState.equals("weekBefore")){
		 * h.title.setVisibility(View.VISIBLE);
		 * h.displayDate.setText("һ���ڵ�ά�޵�"); }
		 * if(m.timeState!=null&&m.timeState.equals("weekAfter")){
		 * h.title.setVisibility(View.VISIBLE);
		 * h.displayDate.setText("һ��ǰ��ά�޵�"); }
		 */

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && ck1 == false) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("���һ�ܵ�ά�޵�");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) <= 7 && position == tag1) {
			h.title.setVisibility(View.VISIBLE);
			h.displayDate.setText("���һ�ܵ�ά�޵�");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && ck2 == false) {
				h.title.setVisibility(View.VISIBLE);
				h.displayDate.setText("���һ���µ�ά�޵�");
				ck2 = true;
				tag2 = position;
			} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), m.time) > 7 && position == tag2) {
				h.title.setVisibility(View.VISIBLE);
				h.displayDate.setText("���һ���µ�ά�޵�");
			}
		}
		

		h.indentNumber.setText(m.indentNumber);
		h.gasstationname.setText(m.gasStationname);
		h.problem.setText(m.problem);
		h.time.setText(m.time);
		// h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(m.iconPath);

		// �ж�ά�޵���״̬�ķ���
		h.state.setText("ά����");
		if (state != null && state.length() != 0) {

			if (state.equals("unsended")) {
				h.state.setText("δ����");
			}
			if (state.equals("unsubmited")) {
				h.state.setText("���ύ");
			}
			if (state.equals("progress")) {
				h.state.setText("����ά��");
			}
			if (state.equals("wait")) {
				h.state.setText("�ȴ�����");
			}
			if (state.equals("approved")) {
				h.state.setText("����ͨ��");
			}
			if (state.equals("completeconfirm")) {
				h.state.setText("��ɴ�ȷ��");
			}
			if (state.equals("continued")) {
				h.state.setText("����ά��");
			}
			if (state.equals("unapproved")) {
				h.state.setText("��������");
			}
			if (state.equals("closed")) {
				h.state.setText("�ѹر�");
			}
			if (state.equals("closeconfirm")) {
				h.state.setText("�رմ�ȷ��");
			}
			if (state.equals("compelted")) {
				h.state.setText("�����");
			}
			if (state.equals("unevaluated")) {
				h.state.setText("������");
			}
			if (state.equals("evaluated")) {
				h.state.setText("������");
			}
			if (state.equals("expenseing")) {
				h.state.setText("������");
			}
			if (state.equals("expensed")) {
				h.state.setText("�ѱ���");
			}
		}

	

		/*if (!scrollState) {
			h.indentNumber.setText(m.indentNumber);
			h.gasstationname.setText(m.gasStationname);
			h.problem.setText(m.problem);
			h.time.setText(m.time);
			// h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
			h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
			h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
			h.icon.setImageUrl(m.iconPath);

			// �ж�ά�޵���״̬�ķ���
			h.state.setText("ά����");
			if (state != null && state.length() != 0) {

				if (state.equals("unsended")) {
					h.state.setText("δ����");
				}
				if (state.equals("unsubmited")) {
					h.state.setText("���ύ");
				}
				if (state.equals("progress")) {
					h.state.setText("����ά��");
				}
				if (state.equals("wait")) {
					h.state.setText("�ȴ�����");
				}
				if (state.equals("approved")) {
					h.state.setText("����ͨ��");
				}
				if (state.equals("completeconfirm")) {
					h.state.setText("��ɴ�ȷ��");
				}
				if (state.equals("continued")) {
					h.state.setText("����ά��");
				}
				if (state.equals("unapproved")) {
					h.state.setText("��������");
				}
				if (state.equals("closed")) {
					h.state.setText("�ѹر�");
				}
				if (state.equals("closeconfirm")) {
					h.state.setText("�رմ�ȷ��");
				}
				if (state.equals("compelted")) {
					h.state.setText("�����");
				}
				if (state.equals("unevaluated")) {
					h.state.setText("������");
				}
				if (state.equals("evaluated")) {
					h.state.setText("������");
				}
				if (state.equals("expenseing")) {
					h.state.setText("������");
				}
				if (state.equals("expensed")) {
					h.state.setText("�ѱ���");
				}
			}

		} else {
			h.indentNumber.setText(m.indentNumber);
			h.gasstationname.setText(m.gasStationname);
			h.problem.setText(m.problem);
			h.time.setText(m.time);
			h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
			h.state.setText("������");
			h.state.setTextColor(context.getResources().getColor(R.color.huise));
		}*/

		h.chat.setOnClickListener(l);

		return v;
	}

	private class Hoderview {
		// ������š�״̬������վ���ơ����⡢ʱ�䡢�ߡ��ײ�������ͷ����ʾʱ��
		private TextView indentNumber, state, gasstationname, problem, time, line, chat, displayDate;
		private FrameLayout title;
		private LinearLayout footer;
		private Myroundcacheimageview icon;// �����ʾ��ͼƬ

		public Hoderview(View v) {
			super();
			this.icon = (Myroundcacheimageview) v.findViewById(R.id.item_maintainhome_service_leftimage);
			this.indentNumber = (TextView) v.findViewById(R.id.textView_item_maintainhome_serviceing_indentNumber);
			this.gasstationname = (TextView) v.findViewById(R.id.item_maintainhome_service_gasstationname);
			this.problem = (TextView) v.findViewById(R.id.item_maintainhome_service_problem);
			this.time = (TextView) v.findViewById(R.id.item_maintainhome_service_time);
			this.state = (TextView) v.findViewById(R.id.maintainhome_service_stateTextView);
			// ��ʼ�Ự
			this.chat = (TextView) v.findViewById(R.id.item_maintainhome_serviceing_chat);
			// ��ͷ��ʾ����
			this.displayDate = (TextView) v.findViewById(R.id.item_maintainhome_service_dateTextView);
			// ��ͷ
			this.title = (FrameLayout) v.findViewById(R.id.item_maintainhome_service_title);
		}
	}

	// ��������
	private View.OnClickListener l = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// ��ʼ�Ự
			case R.id.item_maintainhome_serviceing_chat:
				context.startActivity(
						new Intent(context, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, m.userName));
				break;
			}

		}
	};

}
