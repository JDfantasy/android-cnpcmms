package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.util.Myutil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��Ӧ�̵ķ�����Ϣ����
 */
public class Supplier_sendmessageActivity extends MyActivity {
	private ImageView fanhui, lian_xi_ren_add;// ���أ���Ӳ���
	private TextView next, addressee;// ��һ��
	private EditText title, content;// ���⣬����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_sendmessage);
		set_title_text("��������");
		setview();
		send_message();
	}

	private void send_message() {
		// TODO Auto-generated method stub

	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void setview() {
		// lian_xi_ren_add = (ImageView) findViewById(R.id.lian_xi_ren_add);
		// lian_xi_ren_add.setOnClickListener(l);
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		title = (EditText) findViewById(R.id.supplier_notice_edittext_title);
		content = (EditText) findViewById(R.id.supplier_notice_edittext_content);
		// addressee = (TextView)
		// findViewById(R.id.activity_sendmessage_shoujianren);
		next = (TextView) findViewById(R.id.textview_myacitivity_you);
		next.setVisibility(View.VISIBLE);
		next.setText("��һ��");
		next.setTextColor(getResources().getColor(R.color.baise));
		next.setPadding(0, 0, 50, 0);
		next.setOnClickListener(l);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			String result = data.getExtras().getString("result");// �õ���Activity
			addressee.setText(result); // �رպ󷵻ص�����
		} else {
			addressee.setText("");
		}

	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:
				finish();
				Myutil.set_activity_close(Supplier_sendmessageActivity.this);
				break;
			case R.id.textview_myacitivity_you:
				if (title.getText().toString() == null || title.getText().toString().length() == 0) {
					Toast.makeText(Supplier_sendmessageActivity.this, "���������", 0).show();;
				} else if(content.getText().toString() == null || content.getText().toString().length() == 0){
					Toast.makeText(Supplier_sendmessageActivity.this, "����������", 0).show();
				}else {
					startActivity(new Intent(Supplier_sendmessageActivity.this, Supplier_addcontactsActivity.class)
							.putExtra("title1", title.getText().toString())
							.putExtra("content1", content.getText().toString()));
					Myutil.set_activity_open(Supplier_sendmessageActivity.this);
				}

				break;
			// case R.id.lian_xi_ren_add:
			// startActivityForResult(
			// new Intent(Supplier_sendmessageActivity.this,
			// Supplier_addcontactsActivity.class), 1);
			// Myutil.set_activity_open(Supplier_sendmessageActivity.this);
			// break;
			default:
				break;
			}

		}
	};

}
