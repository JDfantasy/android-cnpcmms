package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.R.id;
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 供应商报销审批的界面
 */
public class Supplier_baoxiaoshenpiActivity extends MyActivity {
	private ImageView fanhui;
	private TextView shenpi, jia_you_zhan_name, ding_dan_number, wei_xiu_time,
			bao_xiao_content, wei_xiu_renyuan, wei_xiu_state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_baoxiaoshenpi);
		setView();

	}

	/**
	 * 初始化控件
	 */
	private void setView() {
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		shenpi = (TextView) findViewById(R.id.Supplier_baoxiaoshenpi);
		jia_you_zhan_name=(TextView) findViewById(R.id.textView2);
		ding_dan_number=(TextView) findViewById(id.textView3);
		wei_xiu_time=(TextView) findViewById(R.id.textView6);
		bao_xiao_content=(TextView) findViewById(R.id.textView9);
		wei_xiu_renyuan=(TextView) findViewById(R.id.textView12);
		wei_xiu_state=(TextView) findViewById(R.id.textView13);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		shenpi.setOnClickListener(l);
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_baoxiaoshenpiActivity.this);
				break;
			case R.id.Supplier_baoxiaoshenpi:
				finish();
				Myutil.set_activity_close(Supplier_baoxiaoshenpiActivity.this);
				break;

			default:
				break;
			}

		}
	};

}
