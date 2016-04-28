package com.cnpc.zhibo.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.platform.comapi.map.r;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_home_alloction_item;
import com.cnpc.zhibo.app.util.Myutil;
import com.easemob.easeui.EaseConstant;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

//ά�޶���ҳ����֪ͨ����б������ı��޵��������
public class Maintain_home_alliction_detail_Activity extends MyActivity implements OnClickListener {
	private WebView webView;
	private Button phone,chat,process;//�绰���Ự������
	private Button finishIndent,closeIndent,addIndent,canle,enter;//��������ɣ��رն������½�ά�޵�
	private ImageView backImageView,popwindowback,phonerightImageview;//����
	private PopupWindow popProcess;//������
	private String indentId; //�ر�ά�޵���id
	private int finishNumber=0;//��ɶ����ķ������
	private int closeNumber=1;//�رն����������
	private int stateNumber=0;//״̬���
	private String indentNumber;//�������
	private String gasAddress;//����վ��ַ
	private String gasStationname;//����վ����
	private String mobilephone;//�ֻ�����
	private Maintain_home_alloction_item m;//��������
	
	private PopupWindow popCloseandFinish;//�رջ���ɵĵ���
	private PopupWindow poprightPhone;//���Ͻǵĵ绰�ͻỰ����
	private TextView textviewpoprightphone,textViewpoprightchat;//���Ͻǵ����ĵ绰���Ự
	private EditText remarkEditText;//��ע�ؼ�
	private String remarkInfo="�ɹ�";//��ע��Ϣ
	
	private LinearLayout bottommenufirstlinearlayout;//�����һ�ε����ĵײ��˵�������ť
	private LinearLayout bottommenusecondlinearlayout;//����ڶ��ε����ĵײ��˵�������ť
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home_alloction_detail);
		set_title_text("���޵�����");
		getIntentId();
		setViews();
		
	}
	
	//�������
	private void getIntentId() {
		Intent intent=getIntent();
		m=(Maintain_home_alloction_item) intent.getSerializableExtra("m");
		indentId=m.id;
		mobilephone=m.mobilephone;
	}

	//�ؼ���ʼ��
	private void setViews() {
		//����
		backImageView = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(this);
		//�ұߵ�����ԻỰ�͵绰��ϵ
		phonerightImageview = (ImageView) findViewById(R.id.imageView_myactity_you);
		phonerightImageview.setVisibility(View.VISIBLE);
		phonerightImageview.setOnClickListener(this);
		//�绰
		phone=(Button) findViewById(R.id.maintain_alloction_phone);
		phone.setOnClickListener(this);
		//�Ự
		chat=(Button) findViewById(R.id.maintain_alloction_chat);
		chat.setOnClickListener(this);
		//����
		process=(Button) findViewById(R.id.maintain_alloction_process);
		process.setOnClickListener(this);
		//webview
		webView=(WebView) findViewById(R.id.maintainhome_alloctionDetail_webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());// ������Ӳ������ⲿ�����
		webView.getSettings().setBlockNetworkImage(false);// �ɼ���ͼƬ
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// û������ʱ���ڻ���
		webView.loadUrl(Myconstant.REPAIRSMESSAGE + indentId);
		//��һ�εĵײ���ť�����岼��
		bottommenufirstlinearlayout=(LinearLayout) findViewById(R.id.maintainhome_alloctionDetail_bottommenufirst);
		bottommenusecondlinearlayout=(LinearLayout) findViewById(R.id.maintainhome_alloctionDetail_bottommenusecond);
		bottommenufirstlinearlayout.setVisibility(View.GONE);
		bottommenusecondlinearlayout.setVisibility(View.VISIBLE);
		//���
		finishIndent = (Button) findViewById(R.id.maintain_alloction_finishIndent);
		//�ر�
		closeIndent = (Button) findViewById(R.id.maintain_alloction_closeIndent);
		//�ӵ�
		addIndent = (Button) findViewById(R.id.maintain_alloction_addIndent);
		finishIndent.setOnClickListener(this);
		closeIndent.setOnClickListener(this);
		addIndent.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���˰�ť
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// �����л������Ч��
			break;
		// �绰�Ự��ϵ��ť
		case R.id.imageView_myactity_you:
			rightPhone();
			break;
		// �绰
		case R.id.popup_maintainalloction_contactphone_phonetextview:
			poprightPhone.dismiss();
			//����������ҪȨ��
			  Intent intent=new Intent(Intent.ACTION_CALL);
			  //Data (����)
			  intent.setData(Uri.parse("tel:"+mobilephone));
			  startActivity(intent);
			break;
		// �Ự
		case R.id.popup_maintainalloction_contactphone_chattextview:
			poprightPhone.dismiss();
			Toast.makeText(getApplicationContext(), "��ת���Ự", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, m.userName));
			break;
		// ����	�½�ά�޵�
		case R.id.maintain_alloction_process:
			//process();
			bottommenufirstlinearlayout.setVisibility(View.GONE);
			bottommenusecondlinearlayout.setVisibility(View.VISIBLE);
			break;
		// ���
		case R.id.maintain_alloction_finishIndent:
			try {
				popCloseandFinish();
				//popProcess.dismiss();
				stateNumber=finishNumber;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// �ر�
		case R.id.maintain_alloction_closeIndent:
			try {
				popCloseandFinish();
				//popProcess.dismiss();
				stateNumber=closeNumber;
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// �ӵ�
		case R.id.maintain_alloction_addIndent:
			Intent intent2=new Intent(this,Maintain_home_service_addnewlistActivity.class);
			intent2.putExtra("m", m);
			startActivity(intent2);
			break;
		// �ر�popwindow
		case R.id.popwindow_imageView_back:
			//popProcess.dismiss();
			//bottommenufirstlinearlayout.setVisibility(View.VISIBLE);
			break;
		// ȷ��
		case R.id.button_popup_maintainhome_enter:
			Toast.makeText(getApplicationContext(), "ȷ��", Toast.LENGTH_SHORT).show();
			if(stateNumber==closeNumber){
				closeAndFinishIndent(closeNumber);
			}
			if(stateNumber==finishNumber){
				closeAndFinishIndent(finishNumber);
			}
			popCloseandFinish.dismiss();
			finish();
			break;
		// ȡ��
		case R.id.button_popup_maintainhome_canle:
			Toast.makeText(getApplicationContext(), "ȡ��", Toast.LENGTH_SHORT).show();
			popCloseandFinish.dismiss();
			break;
		}

	}
	
	//���ؼ���
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	bottommenufirstlinearlayout.setVisibility(View.VISIBLE);
			bottommenusecondlinearlayout.setVisibility(View.GONE);
             return true; 
        }else { 
            return super.onKeyDown(keyCode, event); 
        } 
           
    } 
	
	//���ϽǻỰ���绰��ϵ����
	private void rightPhone(){
		View v=LayoutInflater.from(this).inflate(R.layout.popup_maintainalloction_contactphone, null);
		poprightPhone=new PopupWindow(v,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
		poprightPhone.setBackgroundDrawable(new ColorDrawable());
		// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���
		poprightPhone.showAsDropDown(phonerightImageview, 0, 0);
		//TODO
		textviewpoprightphone=(TextView) v.findViewById(R.id.popup_maintainalloction_contactphone_phonetextview);
		textViewpoprightchat=(TextView) v.findViewById(R.id.popup_maintainalloction_contactphone_chattextview);
		textviewpoprightphone.setOnClickListener(this);
		textViewpoprightchat.setOnClickListener(this);
	}
	
	// ������ ��ɡ��رա��ӵ�
	private void process() {
		/*View v = LayoutInflater.from(this).inflate(
				R.layout.popup_process, null);// ����view
		popProcess = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���
		popProcess.setBackgroundDrawable(new ColorDrawable());
		//popProcess.setOutsideTouchable(true);
		// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���
		//popProcess.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		popProcess.showAsDropDown(webView, 0, 0);
		finishIndent = (Button) v
				.findViewById(R.id.popup_process_finishIndent);
		closeIndent = (Button) v
				.findViewById(R.id.popup_process_closeIndent);
		addIndent = (Button) v
				.findViewById(R.id.popup_process_addIndent);
		popwindowback = (ImageView) v
				.findViewById(R.id.popwindow_imageView_back);
		finishIndent.setOnClickListener(this);
		closeIndent.setOnClickListener(this);
		addIndent.setOnClickListener(this);
		popwindowback.setOnClickListener(this);*/
	}
	
	// ����رջ�����ٵ���
	public void popCloseandFinish() {
		View v = LayoutInflater.from(this).inflate(
				R.layout.poppup_mainhome_alloctiondetil, null);// ����view
		popCloseandFinish = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);// ʵ����popupwindow�ķ���
		popCloseandFinish.setBackgroundDrawable(new ColorDrawable());
		//popProcess.setOutsideTouchable(true);
		// ���������Ϊ�˵��������popupwindow������������Թر�popupwindow�ķ���
		popCloseandFinish.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		canle = (Button) v.findViewById(R.id.button_popup_maintainhome_canle);
		enter = (Button) v.findViewById(R.id.button_popup_maintainhome_enter);
		remarkEditText=(EditText) v.findViewById(R.id.editText_popup_maintainhome_content);
		finishIndent.setOnClickListener(this);
		canle.setOnClickListener(this);
		enter.setOnClickListener(this);
	}
	
	public String getRemarkInfo(){
		String str=remarkEditText.getText().toString();
		try {
			str=URLEncoder.encode(str, "UTF-8");
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//�رն�������ɶ���
	public void closeAndFinishIndent(int number){
		remarkInfo=getRemarkInfo();
		String url = null;
		if(number==finishNumber){
			url = Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_FINISH;
			if(remarkInfo!=null&&remarkInfo.length()!=0) remarkInfo="��ɶ���";
		}else if(number==closeNumber){
			url = Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_CLOSE;
			if(remarkInfo!=null&&remarkInfo.length()!=0) remarkInfo="�رն���";
		}
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(getApplicationContext(), "�ɹ�", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "ʧ��", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Toast.makeText(getApplicationContext(), "����ʧ�ܣ�"+error.toString(), Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(), "����ʧ��,��������", Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("���������tokenֵ��" + Myconstant.token);
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", indentId);//���޵����
				map.put("remark", remarkInfo);//��ע��Ϣ
				return map;
			}
		};
		SysApplication.getHttpQueues().add(request);
	}

}
