package com.cnpc.zhibo.app.adapter;
//站长端选中的保修的项目
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_Repairs_item;


public class Item_repairs_select_adapter extends MyBaseAdapter<Centre_Repairs_item> {

	public Item_repairs_select_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {

		Hoderview h;
		if (v==null) {
			v=inflater.inflate(R.layout.item_select_repairsname, null);
			h=new Hoderview(v);
			v.setTag(h);
		} else {

			h=(Hoderview) v.getTag();
		}
		Centre_Repairs_item re=(Centre_Repairs_item) getItem(position);
		h.name.setText(re.name);
		return v;
	}

	private class Hoderview{
		private TextView name;
		private ImageView icon;
		public Hoderview(View v) {
			super();
			this.name = (TextView) v.findViewById(R.id.textView_item_select_repairsname);
			this.icon = (ImageView) v.findViewById(R.id.imageView_item_select_repairsname);
		}
		
	}
}
