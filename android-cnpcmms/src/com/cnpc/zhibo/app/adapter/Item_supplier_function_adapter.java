package com.cnpc.zhibo.app.adapter;

import java.util.List;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Centre_Function_introduce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Item_supplier_function_adapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<Centre_Function_introduce> list;
	private Context context;

	
	public Item_supplier_function_adapter(List<Centre_Function_introduce> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		
		return 5;
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_supplier_function, null);
			holder=new ViewHolder();
			holder.img=(ImageView) convertView.findViewById(R.id.supplier_function_tubiao);
			holder.tex=(TextView) convertView.findViewById(R.id.supplier_function_name);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Centre_Function_introduce a = list.get(position);
		holder.img.setImageResource(a.icon);
		holder.tex.setText(a.name);
		return convertView;
	}

	class ViewHolder{
		ImageView img;
		TextView tex;
		
	}
}
