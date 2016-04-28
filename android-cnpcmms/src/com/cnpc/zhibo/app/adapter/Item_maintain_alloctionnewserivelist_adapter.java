package com.cnpc.zhibo.app.adapter;

//维修端主页分配通知界面中listview的adapter
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.entity.Maintain_home_alloction_item;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.Myroundcacheimageview;

public class Item_maintain_alloctionnewserivelist_adapter extends
		MyBaseAdapter<String> {
			

	public Item_maintain_alloctionnewserivelist_adapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View setView(int position, View v, ViewGroup parent) {
		Hoderview h;
		if (v == null) {
			v = inflater.inflate(R.layout.item_maintain_home_alloctionnewservicelist, null);
			h = new Hoderview(v);
			v.setTag(h);
		} else {
			h = (Hoderview) v.getTag();
		}
		
		/*Maintain_home_alloction_item m=(Maintain_home_alloction_item) getItem(position);
		h.icon.setImageResource(R.drawable.iconfont_jiayouzhan);
		if (m.iconPath!=null&&m.iconPath.length!=0&&!m.iconPath[0].equals("")) {
			for(int i=0;i<m.iconPath.length;i++){
				if(!m.iconPath[i].equals("")){
					h.icon.setImageUrl(m.iconPath[i]);
				}
			}
		}*/ 
		h.icon.setImageUrl(data.get(position));
		return v;
	}

	private class Hoderview {
		//站名名称，加油站名称，问题，时间，会话按钮,标题文字
		private Myroundcacheimageview icon;//左侧显示的图片

		// private Button btn;

		public Hoderview(View v) {
			super();
			this.icon=(Myroundcacheimageview) v.findViewById(R.id.item_maintainhome_alloctionnewservicelist_leftimage);
		}

	}

}
