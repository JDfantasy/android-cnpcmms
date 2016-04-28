package com.cnpc.zhibo.app.adapter;

import com.cnpc.zhibo.app.ChatActivity;
import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.Supplier_fenpeiguanliActivity;
import com.cnpc.zhibo.app.R.id;
import com.cnpc.zhibo.app.R.layout;
import com.cnpc.zhibo.app.entity.Supplier_page_fenpeiguanl;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;
import com.easemob.easeui.EaseConstant;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * �������ά�޵��б��adapter
 */
public class Item_supplier_page_fenpeiguanli_adapter extends MyBaseAdapter<Supplier_page_fenpeiguanl> {

	private Context context;

	public Item_supplier_page_fenpeiguanli_adapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View setView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_supplier_fenpeiguanlii, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Supplier_page_fenpeiguanl a = (Supplier_page_fenpeiguanl) getItem(position);
		holder.fenpei_to_user.setText("���Ѿ������������" + a.fenpei_to_user);
		holder.name_of_gas_station.setText(a.name_of_gas_station);
		holder.problem.setText(a.title + a.problem);
		holder.time.setText(a.time);
		holder.ding_dan_number.setText(a.ding_dan_number);
		if (a.isaccept.equals("yes")) {
			holder.isaccept.setText("�ѽ���");
			holder.isaccept.setTextColor(context.getResources().getColor(R.color.color_of_wait));
		} else {
			holder.isaccept.setText("δ����");
			holder.isaccept.setTextColor(context.getResources().getColor(R.color.color_of_progress));
		}
		holder.img.setDefaultImageResId(R.drawable.iconfont_jiayouzhan);
		holder.img.setErrorImageResId(R.drawable.iconfont_jiayouzhan);
		holder.img.setImageUrl(a.imgpath);

		holder.huihuaBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra(EaseConstant.EXTRA_USER_ID, a.userID);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView ding_dan_number;// �������
		private TextView fenpei_to_user;// ���䵽����
		private TextView name_of_gas_station;// ����վ����
		private TextView problem;// ����
		private TextView time;// ʱ��
		private TextView isaccept;// ����״̬
		private Myroundcacheimageview img;// ͼƬ
		private TextView huihuaBtn;

		public ViewHolder(View v) {
			super();
			this.ding_dan_number=(TextView) v.findViewById(R.id.dingdanbianhao);
			this.fenpei_to_user = (TextView) v.findViewById(R.id.textView_item_supplier_fenpei_to_user);
			this.name_of_gas_station = (TextView) v.findViewById(R.id.jiayouzhanmingcheng1);
			this.problem = (TextView) v.findViewById(R.id.wenti1);
			this.time = (TextView) v.findViewById(R.id.shijian1);
			this.isaccept = (TextView) v.findViewById(R.id.isaccept);
			this.img = (Myroundcacheimageview) v.findViewById(R.id.wentitupian1);
			this.huihuaBtn = (TextView) v.findViewById(R.id.huihuabtn);
		}

	}
}
