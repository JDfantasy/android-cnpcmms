package com.cnpc.zhibo.app;
import org.apache.commons.collections.functors.InstanceofPredicate;

//ά�޹�����ҳ
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.fragment.Fragment_maintain_apply;
import com.cnpc.zhibo.app.fragment.Fragment_maintain_home;
import com.cnpc.zhibo.app.fragment.Fragment_maintain_repairs;
import com.cnpc.zhibo.app.util.CornerUtil;
import com.cnpc.zhibo.app.util.GlobalConsts;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.view.BadgeView;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Maintain_HomeActivity extends MyActivity {
	
	private Fragment_maintain_apply f_Apply;//����
	private Fragment_maintain_home f_Home;//��ҳ
	private Fragment_maintain_repairs f_Repairs;//ά��
	private Fragment f0;// �����fragment����
	private FragmentManager fm;// fragment�Ĺ��������
	//private LinearLayout home, apply, repairs;// ��ҳ��������ά��
	private RelativeLayout home, apply, repairs;// ��ҳ��������ά��
	private ImageView ima[];
	private TextView hometext,servicetext,applytext;
	private ImageView back;//���ذ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home);
		set_title_text("ά��Ա����ҳ");
		setview();// �������ϵĿؼ�����ʵ����
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//Log.i("tag", "onNewIntent");
		FragmentTransaction transaction = fm.beginTransaction();// ��ʼһ������
		Fragment ft = fm.findFragmentByTag(f_Apply.getClass().getCanonicalName());
		if(ft instanceof Fragment_maintain_apply){
			f_Apply.setnotApplylist();
		}
	}

	//�ؼ���ʼ��
	private void setview() {
		//�ײ����� ����
		hometext=(TextView) findViewById(R.id.maintain_hometext);
		servicetext=(TextView) findViewById(R.id.maintain_servicetext);
		applytext=(TextView) findViewById(R.id.maintain_applytext);
//		hometext.setOnClickListener(l);
//		servicetext.setOnClickListener(l);
//		applytext.setOnClickListener(l);
		//��Ӱ�ť
		/*home = (LinearLayout) findViewById(R.id.linearlayout_maintain_home_shouye);// ��ҳ
		apply = (LinearLayout) findViewById(R.id.linearlayout_maintain_home_baoxiao);// ����
		repairs = (LinearLayout) findViewById(R.id.linearlayout_maintain_home_weixiu);// ά��
*/
		home = (RelativeLayout) findViewById(R.id.relativelayout_maintain_home_shouye);// ��ҳ
		apply = (RelativeLayout) findViewById(R.id.relativelayout_maintain_home_baoxiao);// ����
		repairs = (RelativeLayout) findViewById(R.id.relativelayout_maintain_home_weixiu);// ά��
		//�������ֽǱ�
		/*BadgeView badge=new BadgeView(this, repairs);
		badge.setText("9");
		badge.show();*/
//		setBadgerView(CornerUtil.addCorner(this, repairs, "worker-fixorder", null),
//				"worker-fixorder", null);//��ά�ް�ť��������
//		setBadgerView(CornerUtil.addCorner(this, apply, "worker-expenseorder", null),
//				"worker-expenseorder", null);//��������ť��������
		
		home.setOnClickListener(l);
		apply.setOnClickListener(l);
		repairs.setOnClickListener(l);
		f_Home = new Fragment_maintain_home();// ��ҳ��fragment��ʵ����
		f_Apply = new Fragment_maintain_apply();// ������fragment��ʵ����
		f_Repairs = new Fragment_maintain_repairs();// ά�޵�fragment��ʵ����
		f0 = new Fragment();
		fm = getSupportFragmentManager();// ��ȡfragment�Ĺ�����Ķ���
		xianshiFragment(f_Home);
		ima = new ImageView[3];// ����ײ���������ť��ͼ��
		ima[0] = (ImageView) findViewById(R.id.imageView_maintain_home_shouye);
		ima[1] = (ImageView) findViewById(R.id.imageView_maintain_home_weixiu);
		ima[2] = (ImageView) findViewById(R.id.imageView_maintain_home_baoxiao);
		ima[0].setImageResource(R.drawable.maintain_home);
		ima[1].setImageResource(R.drawable.maintain_servicehui);
		ima[2].setImageResource(R.drawable.maintain_applyhui);
		hometext.setTextColor(getResources().getColor(R.color.dingbubeijingse));
		servicetext.setTextColor(getResources().getColor(R.color.zitiyanse3));
		applytext.setTextColor(getResources().getColor(R.color.zitiyanse3));
		back=(ImageView) findViewById(R.id.imageView_myactity_you);
		back.setVisibility(View.VISIBLE);
		back.setImageResource(R.drawable.useimessagecon);
		back.setOnClickListener(l);
	}
	  @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event)  
	    {  
	        if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {  
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
	    /**�����Ի��������button����¼�*/  
	    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
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
	// �ؼ��ĵ���¼��ļ����ķ���
		private View.OnClickListener l = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.maintain_hometext:// ��ҳ
					seticon(0);
					set_title_text("ά��Ա����ҳ");
					xianshiFragment(f_Home);
					break;
				case R.id.relativelayout_maintain_home_shouye:// ��ҳ
					seticon(0);
					set_title_text("ά��Ա����ҳ");
					xianshiFragment(f_Home);
					break;
				case R.id.maintain_applytext://����
					seticon(2);
					//clearNums("worker-expenseorder");//ȡ��������ť�ϵ�����
					set_title_text("ά��Ա�ı���");
					xianshiFragment(f_Apply);
					break;
				case R.id.relativelayout_maintain_home_baoxiao://����
					seticon(2);
					//clearNums("worker-expenseorder");//ȡ��������ť�ϵ�����
					set_title_text("ά��Ա�ı���");
					xianshiFragment(f_Apply);
					break;
				case R.id.maintain_servicetext:// ά��
					seticon(1);
					//clearNums("worker-fixorder");//ȡ��ά�ް�ť�ϵ�����
					set_title_text("ά��Ա��ά��");
					xianshiFragment(f_Repairs);
					break;
				case R.id.relativelayout_maintain_home_weixiu:// ά��
					seticon(1);
					//clearNums("worker-fixorder");//ȡ��ά�ް�ť�ϵ�����
					set_title_text("ά��Ա��ά��");
					xianshiFragment(f_Repairs);
					break;
				
				case R.id.imageView_myactity_you:
					Intent in=new Intent(Maintain_HomeActivity.this, User_messagectivity.class);
					startActivity(in);
					Myutil.set_activity_open(Maintain_HomeActivity.this);
					break;
				default:
					break;
				}

			}
		};
	
	// Fragment���滻�ķ���
		private void xianshiFragment(Fragment f) {
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
				transaction.add(R.id.linearlayout_centre_home_fragment, f, f
						.getClass().getCanonicalName());
				// ���Ҫ��ʾ��fragment�����������У���ô�������������Ҫ��ʾ��fragment

			}
			f0 = f;// ����ǰ��ʾ��fragment���ó�Ҫ�滻��fragment����
			transaction.commit();// �ύ����
		}

		// �ı�ײ�������ťͼ��ķ���
		private void seticon(int id) {
			switch (id) {
			case 0:
				ima[0].setImageResource(R.drawable.maintain_home);
				ima[1].setImageResource(R.drawable.maintain_servicehui);
				ima[2].setImageResource(R.drawable.maintain_applyhui);
				hometext.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				servicetext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				applytext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				break;
			case 1:
				ima[0].setImageResource(R.drawable.maintain_homehui);
				ima[1].setImageResource(R.drawable.maintain_service);
				ima[2].setImageResource(R.drawable.maintain_applyhui);
				hometext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				servicetext.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				applytext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				break;
			case 2:
				ima[0].setImageResource(R.drawable.maintain_homehui);
				ima[1].setImageResource(R.drawable.maintain_servicehui);
				ima[2].setImageResource(R.drawable.maintain_apply);
				hometext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				servicetext.setTextColor(getResources().getColor(R.color.zitiyanse3));
				applytext.setTextColor(getResources().getColor(R.color.dingbubeijingse));
				break;

			default:
				break;
			}
		}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("tag", "Maintain_HomeActivity-onPause");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.i("tag", "Maintain_HomeActivity-onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("tag", "Maintain_HomeActivity-onDestroy");
	}

	

}
