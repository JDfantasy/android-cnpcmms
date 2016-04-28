package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Supplier_page_weixiudanguanli;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * ά�޵������б��adapter
 */
public class Item_supplier_page_weixiudanguanli_adapter extends MyBaseAdapter<Supplier_page_weixiudanguanli> {
	private boolean ck1 = false;
	private boolean ck2 = false;
	private int tag1;
	private int tag2;
	private Context context;

	public Item_supplier_page_weixiudanguanli_adapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View setView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_supplier_weixiudanguanli, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.item_title.setVisibility(View.GONE);
			holder.layout.setVisibility(View.INVISIBLE);
		}
		Supplier_page_weixiudanguanli a = (Supplier_page_weixiudanguanli) getItem(position);
		holder.number.setText(a.number);
		holder.name_of_gas_station.setText(a.name_of_gas_station);
		holder.problem.setText(a.title + a.problem);
		holder.time.setText(a.time);
		if (a.state.equals("closed")) {
			holder.state.setText("�ѹر�");
			holder.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
		} else if (a.state.equals("completed")) {
			holder.state.setText("�����");
			holder.state.setTextColor(context.getResources().getColor(R.color.dingbubeijingse));
			if (a.workState.equals("evaluated") || a.workState.equals("expensed") || a.workState.equals("progress")||a.workState.equals("expenseing")) {
				holder.layout.setVisibility(View.VISIBLE);
				if (a.score.equals("null")) {
					holder.rat.setRating(1);
				} else {
					if (a.score == null) {
						holder.rat.setRating(1);
					} else {
						holder.rat.setRating(Float.parseFloat(a.score));
					}
				}

			}
		} else {
			holder.state.setText("ά����");
			holder.state.setTextColor(context.getResources().getColor(R.color.color_of_wait));
		}
		if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) <= 7) {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) <= 7 && ck1 == false) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ�ܵ�ά�޵�");
				ck1 = true;
				tag1 = position;
			} else if (position == tag1) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ�ܵ�ά�޵�");
			}
		} else if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7) {
			if (Myutil.get_delta_t_data(Myutil.get_current_time(), a.time) > 7 && ck2 == false) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ���µ�ά�޵�");
				ck2 = true;
				tag2 = position;
			} else if (position == tag2) {
				holder.item_title.setVisibility(View.VISIBLE);
				holder.item_title.setText("���һ���µ�ά�޵�");
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
		private TextView item_title;// ʱ�����
		private LinearLayout layout;// �������ֵĲ���
		private RatingBar rat;// ����

		public ViewHolder(View v) {
			super();

			this.number = (TextView) v.findViewById(R.id.textView_item_supplier_dingdanguanli_number);
			this.state = (TextView) v.findViewById(R.id.textView_item_supplier_weixiudan_state);
			this.name_of_gas_station = (TextView) v.findViewById(R.id.jiayouzhanmingcheng2);
			this.problem = (TextView) v.findViewById(R.id.wenti2);
			this.time = (TextView) v.findViewById(R.id.shijian2);
			this.img = (Myroundcacheimageview) v.findViewById(R.id.wentitupian2);
			this.item_title = (TextView) v.findViewById(R.id.textView_item_supplier_page_approve_item_title3);
			this.layout = (LinearLayout) v.findViewById(R.id.LinearLayout_item_supplier_page_service_layout);
			this.rat = (RatingBar) v.findViewById(R.id.ratingBar_item_supplier_page_service);
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
