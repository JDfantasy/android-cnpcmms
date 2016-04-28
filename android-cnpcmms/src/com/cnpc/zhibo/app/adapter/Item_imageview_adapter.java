package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Item_imageview_adapter extends MyBaseAdapter<Integer>{

	public Item_imageview_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("InflateParams")
	@Override
	public View setView(int position, View v, ViewGroup parent) {

		Hoderview h;
		if (v==null) {
			v=inflater.inflate(R.layout.item_imageview, null);
			h=new Hoderview(v);
			v.setTag(h);
		} else {
			h=(Hoderview) v.getTag();

		}
		int id=(Integer) getItem(position);
		h.icon.setImageResource(id);
		return v;
	}
	private class  Hoderview{
		private ImageView icon;

		public Hoderview(View v) {
			super();
			this.icon = (ImageView) v.findViewById(R.id.imageView_item_imageview_icons);
		}
		
	}

}
