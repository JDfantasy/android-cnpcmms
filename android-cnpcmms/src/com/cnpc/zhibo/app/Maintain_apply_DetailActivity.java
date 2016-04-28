package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

public class Maintain_apply_DetailActivity extends MyActivity implements OnClickListener {
	
	private ImageView backImageView;//����
	private WebView webView;
	private String indentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_apply__detail);
		indentId=getIntent().getStringExtra("indentId");
		set_title_text("����������");
		setViews();
		
	}

	
	private void setViews() {
		backImageView = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(this);
		webView=(WebView) findViewById(R.id.maintain_applyDetail_webview);
		webView.loadUrl(Myconstant.APPLYMESSAGE + indentId);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���˰�ť
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// �����л������Ч��
			break;

		}
		
	}

}
