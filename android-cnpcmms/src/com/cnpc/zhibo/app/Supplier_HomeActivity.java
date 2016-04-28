package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.fragment.Fragment_supplier_home;
import com.cnpc.zhibo.app.fragment.Fragment_supplier_reimbursement;
import com.cnpc.zhibo.app.fragment.Fragment_supplier_repairs;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ��Ӧ�̵���ҳ
 */
public class Supplier_HomeActivity extends MyActivity {
	private Fragment_supplier_home f_supplier; // ��ҳ
	private Fragment_supplier_repairs f_repairs; // ά��
	private Fragment_supplier_reimbursement f_reimbursement; // ����
	private Fragment f0; // �����fragment����
	private FragmentManager fm; // fragment�Ĺ��������
	private LinearLayout centre, reimbursement, repairs; // ��ҳ��������ά��
	private ImageView img[];
	private TextView text[];
	private ImageView back;//���ذ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_home);
		set_title_text("��Ӧ�̵���ҳ");
		setView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// �����˳��Ի���
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// ���öԻ������
			isExit.setTitle("ϵͳ��ʾ");
			// ���öԻ�����Ϣ
			isExit.setMessage("ȷ��Ҫ�˳���");
			// ���ѡ��ť��ע�����
			isExit.setButton("ȷ��", listener);
			isExit.setButton2("ȡ��", listener);
			// ��ʾ�Ի���
			isExit.show();

		}

		return false;

	}

	/** �����Ի��������button����¼� */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};

	/**
	 * ��ʼ���ؼ�
	 */
	private void setView() {
		centre = (LinearLayout) findViewById(R.id.linearlayout_supplier_home_shouye);
		repairs = (LinearLayout) findViewById(R.id.linearlayout_supplier_home_weixiu);
		reimbursement = (LinearLayout) findViewById(R.id.linearlayout_supplier_home_baoxiao);
//		setBadgerView(CornerUtil.addCorner(this, repairs, "provider-fixorder"), "provider-fixorder");// ��ά�ް�ť��������
//		setBadgerView(CornerUtil.addCorner(this, reimbursement, "provider-expenseorder"), "provider-expenseorder");// ��������ť��������
		f_supplier = new Fragment_supplier_home();
		f_repairs = new Fragment_supplier_repairs();
		f_reimbursement = new Fragment_supplier_reimbursement();

		centre.setOnClickListener(l);
		repairs.setOnClickListener(l);
		reimbursement.setOnClickListener(l);

		f0 = new Fragment();
		fm = getSupportFragmentManager();
		xianshiFragment(f_supplier);
		img = new ImageView[3];
		img[0] = (ImageView) findViewById(R.id.imageView_supplier_home_shouye);
		img[1] = (ImageView) findViewById(R.id.imageView_supplier_home_weixiu);
		img[2] = (ImageView) findViewById(R.id.imageView_supplier_home_baoxiao);
		img[0].setImageResource(R.drawable.centre_home_check);
		text = new TextView[3];
		text[0] = (TextView) findViewById(R.id.supplier_dibu_home);
		text[1] = (TextView) findViewById(R.id.supplier_dibu_repairs);
		text[2] = (TextView) findViewById(R.id.supplier_dibu_baoxiao);
		text[0].setTextColor(getResources().getColor(R.color.dingbubeijingse));
		back=(ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		back.setImageResource(R.drawable.useimessagecon);
		back.setOnClickListener(l);
	}

	// �ؼ��ĵ���¼��ļ����ķ���
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.linearlayout_supplier_home_shouye:// ��ҳ

				seticon(0);
				set_title_text("��Ӫ�̵���ҳ");
				xianshiFragment(f_supplier);
				break;
			case R.id.linearlayout_supplier_home_weixiu:// ά��

				seticon(1);
				set_title_text("��Ӫ�̵�ά��");
				//clearNums("provider-fixorder");// ȡ��ά�ް�ť�ϵ�����
				xianshiFragment(f_repairs);
				break;
			case R.id.linearlayout_supplier_home_baoxiao:// ����

				seticon(2);
				//clearNums("provider-expenseorder");// ȡ�����ް�ť�ϵ�����
				set_title_text("��Ӫ�̵ı���");
				xianshiFragment(f_reimbursement);
				break;
			case R.id.imageView_myactity_you:
				Intent in=new Intent(Supplier_HomeActivity.this, User_messagectivity.class);
				startActivity(in);
				Myutil.set_activity_open(Supplier_HomeActivity.this);
				break;
			default:
				break;
			}

		}
	};

	// �ı�ײ�������ťͼ��ķ���
	protected void seticon(int id) {
		switch (id) {
		case 0:
			img[0].setImageResource(R.drawable.centre_home_check);
			img[1].setImageResource(R.drawable.centre_service_no);
			img[2].setImageResource(R.drawable.supplier_baoxiao2);
			text[0].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			text[1].setTextColor(getResources().getColor(R.color.zitiyanse3));
			text[2].setTextColor(getResources().getColor(R.color.zitiyanse3));
			break;
		case 1:
			img[0].setImageResource(R.drawable.centre_home_no);
			img[1].setImageResource(R.drawable.centre_service_check);
			img[2].setImageResource(R.drawable.supplier_baoxiao2);
			text[0].setTextColor(getResources().getColor(R.color.zitiyanse3));
			text[1].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			text[2].setTextColor(getResources().getColor(R.color.zitiyanse3));
			break;
		case 2:
			img[0].setImageResource(R.drawable.centre_home_no);
			img[1].setImageResource(R.drawable.centre_service_no);
			img[2].setImageResource(R.drawable.supplier_baoxiao1);
			text[0].setTextColor(getResources().getColor(R.color.zitiyanse3));
			text[1].setTextColor(getResources().getColor(R.color.zitiyanse3));
			text[2].setTextColor(getResources().getColor(R.color.dingbubeijingse));
			break;
		default:
			break;
		}

	}

	// Fragment���滻�ķ���
	protected void xianshiFragment(Fragment f) {
		FragmentTransaction transaction = fm.beginTransaction();// ��ʼһ������
		Fragment ft = fm.findFragmentByTag(f.getClass().getCanonicalName());
		// ���������е�fragment����ȡfragment����
		if (f0 != null && f0 == f) {
			// ��Ҫ�滻��ʾ��fragment�ǵ�ǰ������ʾ��fragment�Ͳ������ı䣬ֱ���˳�
			return;
		}
		if (f0 != null) {
			transaction.hide(f0);
			// ��Ҫ�滻��ʾ��fragment���ǵ�ǰ��ʾ��fragmentʱ������ǰ��fragment��������

		}
		if (ft != null) {
			transaction.show(ft);
			// ���Ҫ��ʾ��fragment���������Ѿ����ڣ���ô�ͽ���������ʾ�Ϳ�����
		} else {
			transaction.add(R.id.linearlayout_supplier_home_fragment, f, f.getClass().getCanonicalName());
			// ���Ҫ��ʾ��fragment�����������У���ô�������������Ҫ��ʾ��fragment

		}
		f0 = f;// ����ǰ��ʾ��fragment���ó�Ҫ�滻��fragment����
		transaction.commit();// �ύ����

	}

}
