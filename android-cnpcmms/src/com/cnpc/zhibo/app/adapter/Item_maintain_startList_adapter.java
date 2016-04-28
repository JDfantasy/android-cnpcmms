package com.cnpc.zhibo.app.adapter;
//ά�޶���ҳ��ʼ���桢ά�޵���ά���еȴ�ά�ޡ���ʷά��listview��adapter
//���ǸĹ���
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_newsverify_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;
import com.cnpc.zhibo.app.util.GlobalConsts;

public class Item_maintain_startList_adapter extends
		MyBaseAdapter<Maintain_startList_item> {

	private String state;
	private Maintain_startList_item m;
	
	public Item_maintain_startList_adapter(Context context) {
		super(context);
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintain_startlist, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		m = (Maintain_startList_item) getItem(position);
		h.indentNumber.setText(m.indentNumber);
		//�ж��ĸ�״̬����ʾ��ͬ����
		/*switch (serviceState) {
		case GlobalConsts.MAINTAIN_HOME_NOTFINISH://δ���
			h.days.setText("�Ѿ�����5��");
			break;
		case GlobalConsts.MAINTAIN_HOME_ALREADYTFINISH://�����
			h.days.setText("�����");
			break;
		case GlobalConsts.MAINTAIN_SERVICEING://����ά��
			h.days.setText("����ά��");
			break;
		case GlobalConsts.MAINTAIN_WAITSERVICE://�ȴ�ά��
			h.days.setText("�ȴ�ά��");
			break;
		case GlobalConsts.MAINTAIN_HISTORYSERVICE://��ʷά��
			h.days.setText("�����");
			break;
		default :
			h.days.setText("��֪��");
			break;
		}*/
		h.name.setText(m.gasStationname);
		h.problem.setText(m.problem);
		h.time.setText(m.time);
		/*if(m.timeState!=null&&m.timeState.equals("weekBefore")){
			h.title.setVisibility(View.VISIBLE);
			h.titledate.setText("һ���ڵ�ά�޵�");
		}
		if(m.timeState!=null&&m.timeState.equals("weekAfter")){
			h.title.setVisibility(View.VISIBLE);
			h.titledate.setText("һ��ǰ��ά�޵�");
		}*/
		state=m.serviceState;;
		// �ж�ά�޵���״̬�ķ���
				h.state.setText("�޷�״̬");
				if (state.equals("progress")) {
					h.state.setText("����ά��");
				}
				if (state.equals("unsubmited")) {
					h.state.setText("������");
				}
				if (state.equals("completeconfirm")) {
					h.state.setText("��ɴ�ȷ��");
				}
				if (state.equals("wait")) {
					h.state.setText("�ȴ�����");
				}
				if (state.equals("approved")) {
					h.state.setText("����ͨ��");
				}
				if (state.equals("continued")) {
					h.state.setText("����ά��");
				}
				if (state.equals("unapproved")) {
					h.state.setText("��������");
				}
				if (state.equals("evaluated")) {
					h.state.setText("������");
				}
				if (state.equals("unevaluated")) {
					h.state.setText("������");
				}
				if (state.equals("compelted")) {
					h.state.setText("�����");
				}
				if (state.equals("unsended")) {
					h.state.setText("δ����");
				}
				if (state.equals("closed")) {
					h.state.setText("�ѹر�");
				}
				if (state.equals("closeconfirm")) {
					h.state.setText("�رմ�ȷ��");
				}
		return v;
	}

	private class Hoderview {
		private TextView indentNumber,state,name,problem,time;

		public Hoderview(View v) {
			super();
			this.indentNumber = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_indentNumber);
			this.name = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_name);
			this.state = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_days);
			this.problem = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_problem);
			this.time = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_time);
			
		}

	}

}
