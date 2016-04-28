package com.cnpc.zhibo.app.adapter;

//为gridview中添加图片的adapter

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.view.RoundCornerImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

public class Item_tupian_adapter extends MyBaseAdapter<Bitmap> {

	public Item_tupian_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {

		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_tupian, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();

		}
		Bitmap in = (Bitmap) getItem(position);
		h.ro.setImageBitmap(in);
		return v;
	}

	private class Hoderview {
		private RoundCornerImageView ro;

		public Hoderview(View v) {
			super();
			this.ro = (RoundCornerImageView) v
					.findViewById(R.id.roundCornerImageView_item_tupian);
		}

	}

}
