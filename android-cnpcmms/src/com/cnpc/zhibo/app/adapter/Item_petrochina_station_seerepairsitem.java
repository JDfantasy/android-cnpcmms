package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Petrochina_station_seerepairsitem;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/*
 * 中石油端查看加油站维修项目列表的adapter
 */

public class Item_petrochina_station_seerepairsitem extends MyBaseAdapter<Petrochina_station_seerepairsitem> {
	private Context c;

	public Item_petrochina_station_seerepairsitem(Context context) {
		super(context);
		this.c = context;
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_station_seerepairsitem, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		Petrochina_station_seerepairsitem p = (Petrochina_station_seerepairsitem) getItem(position);
		h.cost.setText(p.cost);
		h.costtime.setText(p.costtime);
		h.number.setText(p.number);
		h.state.setText(Myutil.getjudgestatuscolour(p.state, h.state, c));
		h.time.setText(p.time);
		h.title.setText(p.title);
		h.icon.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		h.icon.setImageUrl(p.icon);
		h.jyzname.setText(p.jyzname);
		return v;
	}

	private class Hoderview {
		private TextView number;// 编号
		private TextView time;// 时间
		private TextView title;// 简介
		private TextView state;// 状态
		private TextView costtime;// 花费时间
		private TextView cost;// 花费
		private TextView jyzname;// 加油站名称
		private Myroundcacheimageview icon;

		public Hoderview(View v) {
			super();
			this.number = (TextView) v.findViewById(R.id.textView_item_station_seerepairsitem_number);
			this.time = (TextView) v.findViewById(R.id.textView_item_station_seerepairsitem_time);
			this.title = (TextView) v.findViewById(R.id.textView_item_station_seerepairsitem_title);
			this.state = (TextView) v.findViewById(R.id.textView_item_station_seerepairsitem_state);
			this.costtime = (TextView) v.findViewById(R.id.textView_item_station_seerepairsitem_costtime);
			this.cost = (TextView) v.findViewById(R.id.textView_item_station_seerepairsitem_cost);
			this.jyzname = (TextView) v.findViewById(R.id.textView_item_station_seerepairsitem_jyzname);
			this.icon = (Myroundcacheimageview) v.findViewById(R.id.imageview_item_station_seerepairsitem_icon);
		}

	}
}
