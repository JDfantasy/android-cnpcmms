package com.cnpc.zhibo.app.adapter;

//��ʯ�Ͷ˲鿴�������б��adapter
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Petrochina_examine_message;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Item_petrochina_examine_message_adapter extends MyBaseAdapter<Petrochina_examine_message> {
	private boolean ck1 = false;
	private boolean ck2 = false;
	private int tag1;
	private int tag2;

	public Item_petrochina_examine_message_adapter(Context context) {
		super(context);

	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_petrochina_examine_message, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		Petrochina_examine_message p = (Petrochina_examine_message) getItem(position);
		h.expenditure.setText(p.expenditure);// ����
		h.jyzname.setText(p.jyzname);// ����վ����
		h.malfunction.setText(p.malfunction);// ����
		h.number.setText(p.number);// ���
		h.statetime.setText(p.statetime);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(p.icon);
		h.item_title.setVisibility(View.GONE);// ÿ�θ���hoderviewʱ���Ƚ���������

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), p.statetime) < 7 && ck1 == false) {
			h.item_title.setVisibility(View.VISIBLE);
			h.item_title.setText("���һ�ܵ�ά�޵�");
			ck1 = true;
			tag1 = position;
		} else if (position == tag1) {
			h.item_title.setVisibility(View.VISIBLE);
			h.item_title.setText("���һ�ܵ�ά�޵�");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), p.statetime) > 7 && ck2 == false) {
				h.item_title.setVisibility(View.VISIBLE);
				h.item_title.setText("���һ���µ�ά�޵�");
				ck2 = true;
				tag2 = position;
			} else if (position == tag2) {
				h.item_title.setVisibility(View.VISIBLE);
				h.item_title.setText("���һ���µ�ά�޵�");
			}

		}

		return v;
	}

	private class Hoderview {
		private TextView number;// ���
		private TextView jyzname;// ����վ����
		private TextView malfunction;// ����
		private TextView expenditure;// ����
		private TextView statetime;// ʱ��
		private Myroundcacheimageview icon;// ͼƬ
		private TextView item_title;// item�ı���

		public Hoderview(View v) {
			super();
			this.number = (TextView) v.findViewById(R.id.textview_item_petrochina_examine_message_number);
			this.jyzname = (TextView) v.findViewById(R.id.textView_item_petrochina_examine_message_jyzname);
			this.malfunction = (TextView) v.findViewById(R.id.textView_item_petrochina_examine_message_malfunction);
			this.expenditure = (TextView) v.findViewById(R.id.textView_item_petrochina_examine_message_expenditure);
			this.statetime = (TextView) v.findViewById(R.id.textview_item_petrochina_examine_message_statetime);
			this.icon = (Myroundcacheimageview) v.findViewById(R.id.imageview_item_petrochina_examine_message_icon);
			this.item_title = (TextView) v.findViewById(R.id.textView_item_petrochina_examine_message_title);
		}

	}

}
