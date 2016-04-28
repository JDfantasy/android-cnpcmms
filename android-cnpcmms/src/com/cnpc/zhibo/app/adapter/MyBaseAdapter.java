package com.cnpc.zhibo.app.adapter;
//父类的adapter文件
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter{
	protected List<T> data;
	protected LayoutInflater inflater;
	public MyBaseAdapter(Context context) {
		data=new ArrayList<T>();
		inflater=LayoutInflater.from(context);
	}
	public void addDataTop(T t){
		data.add(0, t);
		notifyDataSetChanged();
	}
	public void addDataTop(List<T> list){
		data.addAll(0, list);
		notifyDataSetChanged();
	}
	public void addDataBottom(T t){
		if (t!=null) {
			data.add(t);
			notifyDataSetChanged();
		}
		
	}
	public void addDataBottom(List<T> list){
		data.addAll(list);
		notifyDataSetChanged();
	}
	public void clear(){
		data.clear();
		notifyDataSetChanged();
	}
	public void setdate(List<T> list){
		data.clear();
		data.addAll(list);
		notifyDataSetChanged();
	}
	public List<T> getData(){
		return data;
	}
	@Override
	public int getCount() {
		return data.size();
//		return 10;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return setView(position, convertView, parent);
	}
	public abstract View setView(int position, View convertView, ViewGroup parent);

}
