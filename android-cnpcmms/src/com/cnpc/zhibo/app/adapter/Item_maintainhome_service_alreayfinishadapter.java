package com.cnpc.zhibo.app.adapter;
//维修端主页开始界面中listview的adapter
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_newsverify_item;
import com.cnpc.zhibo.app.entity.Maintain_startList_item;

public class Item_maintainhome_service_alreayfinishadapter extends
		MyBaseAdapter<Maintain_startList_item> {

	public Item_maintainhome_service_alreayfinishadapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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
		//Maintain_startList_item i = (Maintain_startList_item) getItem(position);
		h.indentNumber.setText("订单编号：123456");
		h.days.setText("已完成");
		h.name.setText("月坛加油站");
		h.problem.setText("加油机不出油");
		h.time.setText("2016.02.16");
		return v;
	}

	private class Hoderview {
		private TextView indentNumber,days,name,problem,time;

		public Hoderview(View v) {
			super();
			this.indentNumber = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_indentNumber);
			this.name = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_name);
			this.days = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_days);
			this.problem = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_problem);
			this.time = (TextView) v
					.findViewById(R.id.textView_item_maintain_startlist_time);
			
		}

	}

}
