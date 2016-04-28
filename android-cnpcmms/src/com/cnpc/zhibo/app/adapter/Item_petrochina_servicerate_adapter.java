package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Petrochina_servicerate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*
 * 中石油端维修率的查看加油站列表的adapter
 */
public class Item_petrochina_servicerate_adapter extends MyBaseAdapter<Petrochina_servicerate> {

	public Item_petrochina_servicerate_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v==null) {
			v=inflater.inflate(R.layout.item_petrochina_servicerate, null);
			h=new Hoderview(v);
			v.setTag(h);
		} else {

			h=(Hoderview) v.getTag();
		}
		Petrochina_servicerate p=(Petrochina_servicerate) getItem(position);
		h.stationname.setText(p.stationname);
		h.servicenumber.setText(p.servicenumber);
		return v;
	}
	private class Hoderview{
		private TextView  stationname;//加油站名称
		private TextView  servicenumber;//维修次数
		public Hoderview(View v) {
			super();
			this.stationname = (TextView) v.findViewById(R.id.textView_item_petrochina_servicerate_stationname);
			this.servicenumber = (TextView) v.findViewById(R.id.textView_item_petrochina_servicerate_servicenumber);
		}
		
	}

}
