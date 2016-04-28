package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Supplier_page_dingdanfenpei;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 订单分配listview的adapter
 */
public class Item_supplier_page_dingdanfenpei_adapter extends MyBaseAdapter<Supplier_page_dingdanfenpei> {

	public Item_supplier_page_dingdanfenpei_adapter(Context context) {
		super(context);
	}

	@Override
	public View setView(int position, View convertView, ViewGroup parent) {
		ViewHolder v;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_supplier_dingdanfenpei, null);
			v = new ViewHolder(convertView);
			convertView.setTag(v);
		} else {
			v = (ViewHolder) convertView.getTag();
		}
		Supplier_page_dingdanfenpei a = (Supplier_page_dingdanfenpei) getItem(position);
		v.number.setText(a.number);
		v.elapsedtime.setText("已报修" + a.elapsedtime + "天");
		v.name_of_gas_station.setText(a.name_of_gas_station);
		v.problem.setText(a.title + a.problem);
		v.time.setText(a.time);

		v.img.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		v.img.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		v.img.setImageUrl(a.imgpath);
		return convertView;
	}

	public class ViewHolder {
		private TextView number;// 保修单编号
		private TextView elapsedtime;// 已报修时间
		private TextView name_of_gas_station;// 加油站名称
		private TextView problem;// 问题
		private TextView time;// 时间
		private Myroundcacheimageview img;// 图片
		// private TextView item_title;

		public ViewHolder(View v) {
			super();

			this.number = (TextView) v.findViewById(R.id.textView_item_supplier_dingdanfenpei_number);
			this.elapsedtime = (TextView) v.findViewById(R.id.textView_item_supplier_dingdanfenpei_time);
			this.name_of_gas_station = (TextView) v.findViewById(R.id.jiayouzhanmingcheng);
			this.problem = (TextView) v.findViewById(R.id.wenti);
			this.time = (TextView) v.findViewById(R.id.shijian);
			this.img = (Myroundcacheimageview) v.findViewById(R.id.wentitupian);
			// this.item_title=(TextView)
			// v.findViewById(R.id.textView_item_supplier_page_approve_item_title2);
		}

	}
}
