package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Supplier_page_baoxiao;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ��Ӧ�̵ı������б������adapter
 */
public class Item_supplier_baoxiao_adapter extends MyBaseAdapter<Supplier_page_baoxiao> {
	private Context context;
	private boolean ck1 = false;
	private boolean ck2 = false;
	private int tag1;
	private int tag2;

	public Item_supplier_baoxiao_adapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View setView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_supplier_baoxiao, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Supplier_page_baoxiao a = (Supplier_page_baoxiao) getItem(position);

		holder.number.setText(a.number);
		holder.name_of_gas_station.setText(a.name_of_gas_station);
		holder.problem.setText(a.title + a.problem);
		holder.time.setText(a.time);
		holder.cost.setText(a.cost);
		if (a.state.equals("progress")) {
			holder.state.setText("δ����");
			holder.state.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		} else {
			holder.state.setText("�ѱ���");
			holder.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
		}
		if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) < 7 && ck1 == false) {
			holder.item_title.setVisibility(View.VISIBLE);
			holder.item_title.setText("���һ�ܵı�����");
			ck1 = true;
			tag1 = position;
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) < 7 && position == tag1) {
			holder.item_title.setVisibility(View.VISIBLE);
			holder.item_title.setText("���һ�ܵı�����");
		} else {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7 && ck2 == false) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ���µı�����");
				ck2 = true;
				tag2 = position;
			} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7 && position == tag2) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ���µı�����");
			}
		}
		holder.img.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		holder.img.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		holder.img.setImageUrl(a.imgpath);
		return convertView;
	}

	class ViewHolder {
		private TextView number;// ά�ޱ��
		private TextView state;// ״̬
		private TextView name_of_gas_station;// ����վ����
		private TextView problem;// ����
		private TextView time;// ʱ��
		private Myroundcacheimageview img;// ͼƬ
		private TextView item_title; // item����
		// private TextView pingjiaBtn;
		private TextView cost;

		public ViewHolder(View v) {
			super();
			this.number = (TextView) v.findViewById(R.id.textView_item_supplier_baoxiao_to_user);
			this.state = (TextView) v.findViewById(R.id.baoxiao_state);
			this.name_of_gas_station = (TextView) v.findViewById(R.id.jiayouzhanmingcheng3);
			this.problem = (TextView) v.findViewById(R.id.wenti3);
			this.time = (TextView) v.findViewById(R.id.shijian3);
			this.img = (Myroundcacheimageview) v.findViewById(R.id.wentitupian3);
			this.item_title = (TextView) v.findViewById(R.id.textView_item_supplier_page_baoxiao_item_title);
			// this.pingjiaBtn=(TextView) v.findViewById(R.id.pingjia);
			this.cost = (TextView) v.findViewById(R.id.cost);
		}

	}

	/**
	 * ����adapter�е�����ֵ
	 */
	public void setattrs() {
		ck1 = false;
		ck2 = false;
		tag1 = 0;
		tag2 = 0;
	}
}
