package com.cnpc.zhibo.app.adapter;

import java.util.ArrayList;
/*
 * 公告的viewpager的adapter
 */
import java.util.List;

import android.R.integer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Item_fragment_notice_adapter extends FragmentPagerAdapter {
	private List<Fragment> data;

	public Item_fragment_notice_adapter(FragmentManager fm) {
		super(fm);
		data = new ArrayList<Fragment>();

	}
	
	public List<Fragment> getdata(){
		return data;
	}

	@Override
	public Fragment getItem(int positon) {
		// TODO Auto-generated method stub
		return data.get(positon);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
		//return Integer.MAX_VALUE;
	}

	/*
	 * 添加fragment对象的方法
	 */
	public void addfragmentdata(List<Fragment> data) {
		this.data.clear();
		this.data.addAll(getCount(),data);
		notifyDataSetChanged();
	}
	public void addfragmentdata2(Fragment f) {
		this.data.add(f);
		notifyDataSetChanged();
	}
}
