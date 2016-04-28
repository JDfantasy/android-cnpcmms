package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Petrochina_project;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Item_petrochian_project_adapter extends MyBaseAdapter<Petrochina_project>{

	public Item_petrochian_project_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v==null) {
			v=inflater.inflate(R.layout.item_petrochina_project, null);
			h=new Hoderview(v);
			v.setTag(h);
		} else {
			h=(Hoderview) v.getTag();

		}
		Petrochina_project p=(Petrochina_project) getItem(position);
		h.name.setText(p.name);
		h.number.setText(p.number);
		return v;
	}
	private class Hoderview{
		private TextView name,number;

		public Hoderview(View v) {
			super();
			this.name = (TextView) v.findViewById(R.id.textView_item_petrochina_project_name);
			this.number =(TextView) v.findViewById(R.id.textView_item_petrochina_project_number);
		}
		
	}

}
