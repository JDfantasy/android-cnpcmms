package com.cnpc.zhibo.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Petrochina_station_message;

//中石油端查看加油站信息的列表的adapter
public class Item_petrochina_station_message_adapter extends
		MyBaseAdapter<Petrochina_station_message> {

	public Item_petrochina_station_message_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater
					.inflate(R.layout.item_petrochina_station_message, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = new Hoderview(v);

		}
		Petrochina_station_message p = (Petrochina_station_message) getItem(position);
		h.cost.setText(p.cost);
		h.jyzname.setText(p.jyzname);
		h.number.setText(p.number);
		return v;
	}

	private class Hoderview {
		private TextView jyzname;
		private TextView number;
		private TextView cost;

		public Hoderview(View v) {
			super();
			this.jyzname = (TextView) v
					.findViewById(R.id.textView_item_petrochina_station_message_jyzname);
			this.number = (TextView) v
					.findViewById(R.id.textView_item_petrochina_station_message_number);
			this.cost = (TextView) v
					.findViewById(R.id.textView_item_petrochina_station_message_cost);
		}

	}

}
