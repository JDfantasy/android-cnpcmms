package com.cnpc.zhibo.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Supplier_page_baoxiudanguanli;
import com.cnpc.zhibo.app.util.Myutil;

/**
 * ���޵��б��adapter
 */
public class Item_supplier_page_baoxiudan_adapter extends MyBaseAdapter<Supplier_page_baoxiudanguanli> {
	private boolean ck1 = false;
	private boolean ck2 = false;
	private int tag1;
	private int tag2;

	public Item_supplier_page_baoxiudan_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_supplier_baoxiudanguanli, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.item_title.setVisibility(View.GONE);
		}
		Supplier_page_baoxiudanguanli a = (Supplier_page_baoxiudanguanli) getItem(position);
		holder.number.setText(a.number);
		holder.name_of_gas_station.setText(a.name_of_gas_station);
		holder.problem.setText(a.title + a.problem);
		holder.time.setText(a.time);
		if (a.state.equals("yes")) {
			holder.state.setText("�ѽ���");
		} else {
			holder.state.setText("δ����");
		}

		if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) < 7 && ck1 == false) {
			holder.item_title.setVisibility(View.VISIBLE);
			holder.item_title.setText("���һ�ܵ�ά�޵�");
			ck1 = true;
			tag1 = position;
		} else if (position == tag1) {
			holder.item_title.setVisibility(View.VISIBLE);
			holder.item_title.setText("���һ�ܵ�ά�޵�");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7 && ck2 == false) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ���µ�ά�޵�");
				ck2 = true;
				tag2 = position;
			} else if (position == tag2) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ���µ�ά�޵�");
			}

		}
		return convertView;
	}

	class ViewHolder {
		private TextView number;// ά�ޱ��
		private TextView state;// ״̬
		private TextView name_of_gas_station;// ����վ����
		private TextView problem;// ����
		private TextView time;// ʱ��
		private ImageView img;// ͼƬ
		private TextView item_title; // item����

		public ViewHolder(View v) {
			super();

			this.number = (TextView) v.findViewById(R.id.textView_item_supplier_baoxiudan_number);
			this.state = (TextView) v.findViewById(R.id.textView_item_supplier_baoxiudan_state);
			this.name_of_gas_station = (TextView) v.findViewById(R.id.jiayouzhanmingcheng3);
			this.problem = (TextView) v.findViewById(R.id.wenti3);
			this.time = (TextView) v.findViewById(R.id.shijian3);
			this.img = (ImageView) v.findViewById(R.id.wentitupian3);
			this.item_title = (TextView) v.findViewById(R.id.textView_item_supplier_page_approve_item_title);
		}
	}
}
