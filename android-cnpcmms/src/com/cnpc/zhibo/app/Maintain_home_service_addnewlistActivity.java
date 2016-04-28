package com.cnpc.zhibo.app;

//点击维修单+号的新建维修单界面
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.adapter.Item_maintain_alloctionnewserivelist_adapter;
import com.cnpc.zhibo.app.adapter.Item_tupian_adapter;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.entity.Maintain_home_alloction_item;
import com.cnpc.zhibo.app.util.Myutil;
import com.cnpc.zhibo.app.util.Photograph_util;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Maintain_home_service_addnewlistActivity extends MyActivity
		implements OnClickListener, OnCheckedChangeListener {

	private ImageView back, addImageView;// 返回
	private GridView gridView;// 呈现图片
	private List<String> imagepathlist;//存放图片路径的集合
	private Item_maintain_alloctionnewserivelist_adapter adapter;// 存放图片的adapter对象
	private Item_tupian_adapter adapter_ph;
	private static int number = 0;// 记录图片上传的个数
	private PopupWindow pop;
	private PopupWindow popSend;
	private PopupWindow popSendSuccess;
	private Button startService; // 开始维修
	private Button send;// 发送
	private Button bu1, bu2, bu3;// pop上的三个按钮：相机、相册、取消
	private Button btn_confirm, btn_change;// popsend 确认 更改
	private File[] f = new File[4];;// 暂时存储图片的文件
	private ToggleButton toggleButton,toggleIsManmade;//在保不在保按钮，是否人为损坏
	private String indentNumber1;//订单编号
	private String gasAddress1;//加油站地址
	private String gasStationname1;//加油站名称
	private boolean guaranteingState=true;//在保不在保的状态
	private boolean ismanmadeState=false;//人为损坏的状态
	private Maintain_home_alloction_item m;//信息对象
	//基本信息 订单编号 加油站地址 加油站名称 故障名称 故障描述
	private TextView indentNumber,gasAddress,gasStationname,faultParts,faultDescription;
	private String partsInfo;//配件信息
	//配件的具体参数
	private EditText partsName01,partsOnePricep01,partsCount01;
	private EditText partsName02,partsOnePricep02,partsCount02;
	private EditText partsName03,partsOnePricep03,partsCount03;
	private EditText partsName04,partsOnePricep04,partsCount04;
	private EditText partsName05,partsOnePricep05,partsCount05;
	private TextView partsPricep01,partsPricep02,partsPricep03,partsPricep04,partsPricep05;//单项总价金额
	private TextView totalMoneyTextView;//合计金额控件
	private EditText remarksInfoEditText;//备注信息控件
	private String remarksInfo;//备注信息内容
	private int totalMoney;//总计金额
	private List<EditText> partsNamelist=new ArrayList<EditText>();//名称集合
	private List<EditText> partsOnePriceplist=new ArrayList<EditText>();//单价集合
	private List<EditText> partsCountlist=new ArrayList<EditText>();//数量集合
	private List<TextView> partsPriceplist=new ArrayList<TextView>();//总价集合
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home_service_addnewlist);
		m=(Maintain_home_alloction_item) getIntent().getSerializableExtra("m");
		setViews();
		Toast.makeText(this, "录入更多信息，请到PC端录入", 1).show();
		set_title_text("新建维修单");
		displayImage();
	}
	
	private void displayImage() {
		if (m.iconPath!=null&&m.iconPath.length!=0&&m.iconPath[0]!=null&&!m.iconPath[0].equals("")) {
			for(int i=0;i<m.iconPath.length;i++){
				if(m.iconPath[i]!=null&&!m.iconPath[i].equals("")){
					imagepathlist.add(m.iconPath[i]);
				}
			}
			adapter.setdate(imagepathlist);
			gridView.setAdapter(adapter);
		} else {
			set_initializebitmap();
		}
		
	}

	// 为gridview先放置一张初始化的图片
	private void set_initializebitmap() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.iconfont_jiayouzhan);
		adapter_ph.addDataBottom(bitmap);
	}

	// 控件初始化
	private void setViews() {
		imagepathlist=new ArrayList<String>();
		gridView=(GridView) findViewById(R.id.maintainhome_service_newaddlist_gridView);
		adapter_ph=new Item_tupian_adapter(this);
		adapter=new Item_maintain_alloctionnewserivelist_adapter(this);
		gridView.setAdapter(adapter_ph);
		//备注信息
		remarksInfoEditText=(EditText) findViewById(R.id.maintainhome_service_adddnewlist_servicetime_remarksInfo);
		//合计金额
		totalMoneyTextView=(TextView) findViewById(R.id.maintainhome_service_edittext_totalMoney);
		//totalMoneyTextView.setOnClickListener(this);
		//配件名称
		partsName01=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianName01);
		partsName02=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianName02);
		partsName03=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianName03);
		partsName04=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianName04);
		partsName05=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianName05);
		partsNamelist.add(partsName01);
		partsNamelist.add(partsName02);
		partsNamelist.add(partsName03);
		partsNamelist.add(partsName04);
		partsNamelist.add(partsName05);
		//TODO 第一项名称的监听
		partsName01.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			  cou = before + count;
			  String editable = partsName01.getText().toString();
			  String str = stringFilter(editable); //过滤特殊字符
			  if (!editable.equals(str)) {
			   partsName01.setText(str);
			  }
			  partsName01.setSelection(partsName01.length());
			  cou = partsName01.length();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			   if (cou > mMaxLenth) {
				   selectionEnd = partsName01.getSelectionEnd();
				   s.delete(mMaxLenth, selectionEnd);
			   }

			}
		});
		//TODO 第二项名称的监听
		partsName02.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				cou = before + count;
				String editable = partsName02.getText().toString();
				String str = stringFilter(editable); //过滤特殊字符
				if (!editable.equals(str)) {
					partsName02.setText(str);
				}
				partsName02.setSelection(partsName02.length());
				cou = partsName02.length();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = partsName02.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
				}
				
			}
		});
		//TODO 第三项名称的监听
		partsName03.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				cou = before + count;
				String editable = partsName03.getText().toString();
				String str = stringFilter(editable); //过滤特殊字符
				if (!editable.equals(str)) {
					partsName03.setText(str);
				}
				partsName03.setSelection(partsName03.length());
				cou = partsName03.length();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = partsName03.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
				}
				
			}
		});
		//TODO 第四项名称的监听
		partsName04.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				cou = before + count;
				String editable = partsName04.getText().toString();
				String str = stringFilter(editable); //过滤特殊字符
				if (!editable.equals(str)) {
					partsName04.setText(str);
				}
				partsName04.setSelection(partsName04.length());
				cou = partsName04.length();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = partsName04.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
				}
				
			}
		});
		//TODO 第五项名称的监听
		partsName05.addTextChangedListener(new TextWatcher() {
			private int cou = 0;
			int selectionEnd = 0;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				cou = before + count;
				String editable = partsName05.getText().toString();
				String str = stringFilter(editable); //过滤特殊字符
				if (!editable.equals(str)) {
					partsName05.setText(str);
				}
				partsName05.setSelection(partsName05.length());
				cou = partsName05.length();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (cou > mMaxLenth) {
					selectionEnd = partsName05.getSelectionEnd();
					s.delete(mMaxLenth, selectionEnd);
				}
				
			}
		});
		
		
		//配件单价
		partsOnePricep01=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianOnePrice01);
		partsOnePricep02=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianOnePrice02);
		partsOnePricep03=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianOnePrice03);
		partsOnePricep04=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianOnePrice04);
		partsOnePricep05=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianOnePrice05);
		partsOnePriceplist.add(partsOnePricep01);
		partsOnePriceplist.add(partsOnePricep02);
		partsOnePriceplist.add(partsOnePricep03);
		partsOnePriceplist.add(partsOnePricep04);
		partsOnePriceplist.add(partsOnePricep05);
		//第一行单价
		partsOnePricep01.addTextChangedListener(new TextWatcher() {
			private boolean flag = false;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(flag) return;
				//TODO
				String s1=s.toString();
				if(s1.toString().length()!=0){
					if(s1.toString().contains(".")){
						if(s1.toString().length()-s1.toString().indexOf(".")>2){
							Toast.makeText(Maintain_home_service_addnewlistActivity.this,"只能输入两位小数", Toast.LENGTH_SHORT).show();
							s1=s1.toString().substring(0, s1.toString().indexOf(".")+3);
							flag=true;
							partsOnePricep01.setText(s1.toString());
							partsOnePricep01.clearFocus();
							//partsOnePricep01.setFocusable(false);
							flag=false;
							partsPricep01.invalidate();
						}
					}
				}
				if(s1.toString().length()!=0&&partsCount01.getText().toString().length()!=0){
					/*if(!s1.toString().equals("0")||!s1.toString().equals("0.00")||!partsCount01.getText().toString().equals("0")
							||!partsCount01.getText().toString().equals("0.00")){
						double oneprice=Double.parseDouble(s1.toString());
						double onecount=Double.parseDouble(partsCount01.getText().toString());
						DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
				        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
						getonepricecheng(onepricecheng,partsPricep01);
						totalMoneyTextView.setText(getpartsPricep());
						//Log.i("tag", "if");
					}else {
						//Toast.makeText(getApplicationContext(), "输入内容不能为0", Toast.LENGTH_SHORT).show();
					}*/
					double oneprice=Double.parseDouble(s1.toString());
					double onecount=Double.parseDouble(partsCount01.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep01);
					totalMoneyTextView.setText(getpartsPricep());
					//Log.i("tag", "if");
				}else {
					partsPricep01.setText("");
					//Log.i("tag", "else");
					//Toast.makeText(getApplicationContext(), "输入内容不能为空", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//第2行单价
		partsOnePricep02.addTextChangedListener(new TextWatcher() {
			private boolean flag = false;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(flag) return;
				//TODO
				String s1=s.toString();
				if(s1.toString().length()!=0){
					if(s1.toString().contains(".")){
						if(s1.toString().length()-s1.toString().indexOf(".")>2){
							Toast.makeText(Maintain_home_service_addnewlistActivity.this,"只能输入两位小数", Toast.LENGTH_SHORT).show();
							s1=s1.toString().substring(0, s1.toString().indexOf(".")+3);
							flag=true;
							partsOnePricep02.setText(s1.toString());
							partsOnePricep02.clearFocus();
							flag=false;
							partsOnePricep02.invalidate();
						}
					}
				}
				if(s1.toString().length()!=0&&partsCount02.getText().toString().length()!=0){
					double oneprice = Double.parseDouble(s1.toString());
					double onecount=Double.parseDouble(partsCount02.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep02);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep02.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//第3行单价
		partsOnePricep03.addTextChangedListener(new TextWatcher() {
			private boolean flag = false;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(flag) return;
				//TODO
				String s1=s.toString();
				if(s1.toString().length()!=0){
					if(s1.toString().contains(".")){
						if(s1.toString().length()-s1.toString().indexOf(".")>2){
							Toast.makeText(Maintain_home_service_addnewlistActivity.this,"只能输入两位小数", Toast.LENGTH_SHORT).show();
							s1=s1.toString().substring(0, s1.toString().indexOf(".")+3);
							flag=true;
							partsOnePricep03.setText(s1.toString());
							partsOnePricep03.clearFocus();
							flag=false;
							partsOnePricep03.invalidate();
						}
					}
				}
				if(s1.toString().length()!=0&&partsCount03.getText().toString().length()!=0){
					double oneprice=Double.parseDouble(s1.toString());
					double onecount=Double.parseDouble(partsCount03.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep03);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep03.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//第4行单价
		partsOnePricep04.addTextChangedListener(new TextWatcher() {
			private boolean flag = false;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(flag) return;
				//TODO
				String s1=s.toString();
				if(s.toString().length()!=0){
					if(s1.toString().contains(".")){
						if(s1.toString().length()-s1.toString().indexOf(".")>2){
							Toast.makeText(Maintain_home_service_addnewlistActivity.this,"只能输入两位小数", Toast.LENGTH_SHORT).show();
							s1=s1.toString().substring(0, s1.toString().indexOf(".")+3);
							flag=true;
							partsOnePricep04.setText(s1.toString());
							partsOnePricep04.clearFocus();
							flag=false;
							partsOnePricep04.invalidate();
						}
					}
				}
				if(s1.toString().length()!=0&&partsCount04.getText().toString().length()!=0){
					double oneprice=Double.parseDouble(s1.toString());
					double onecount=Double.parseDouble(partsCount04.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep04);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep04.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//第5行单价
		partsOnePricep05.addTextChangedListener(new TextWatcher() {
			private boolean flag = false;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(flag) return;
				//TODO
				String s1=s.toString();
				if(s1.toString().length()!=0){
					if(s1.toString().contains(".")){
						if(s1.toString().length()-s1.toString().indexOf(".")>2){
							Toast.makeText(Maintain_home_service_addnewlistActivity.this,"只能输入两位小数", Toast.LENGTH_SHORT).show();
							s1=s1.toString().substring(0, s1.toString().indexOf(".")+3);
							flag=true;
							partsOnePricep05.setText(s1.toString());
							partsOnePricep05.clearFocus();
							flag=false;
							partsOnePricep05.invalidate();
						}
					}
				}
				if(s1.toString().length()!=0&&partsCount05.getText().toString().length()!=0){
					double oneprice=Double.parseDouble(s1.toString());
					double onecount=Double.parseDouble(partsCount05.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep05);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep05.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		//配件数量
		partsCount01=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianNumber01);
		partsCount02=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianNumber02);
		partsCount03=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianNumber03);
		partsCount04=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianNumber04);
		partsCount05=(EditText) findViewById(R.id.maintainhome_service_edittext_peijianNumber05);
		partsCountlist.add(partsCount01);
		partsCountlist.add(partsCount02);
		partsCountlist.add(partsCount03);
		partsCountlist.add(partsCount04);
		partsCountlist.add(partsCount05);
		//第一行数量
		partsCount01.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length()!=0&&partsOnePricep01.getText().toString().length()!=0){
					double oneprice=Double.parseDouble(s.toString());
					double onecount=Double.parseDouble(partsOnePricep01.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep01);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep01.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//第2行数量
		partsCount02.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length()!=0&&partsOnePricep02.getText().toString().length()!=0){
					double oneprice=Double.parseDouble(s.toString());
					double onecount=Double.parseDouble(partsOnePricep02.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep02);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep02.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//第3行数量
		partsCount03.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length()!=0&&partsOnePricep03.getText().toString().length()!=0){
					double oneprice=Double.parseDouble(s.toString());
					double onecount=Double.parseDouble(partsOnePricep03.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep03);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep03.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//第4行数量
		partsCount04.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length()!=0&&partsOnePricep04.getText().toString().length()!=0){
					double oneprice=Double.parseDouble(s.toString());
					double onecount=Double.parseDouble(partsOnePricep04.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep04);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep04.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		//第5行数量
		partsCount05.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length()!=0&&partsOnePricep05.getText().toString().length()!=0){
					double oneprice=Double.parseDouble(s.toString());
					double onecount=Double.parseDouble(partsOnePricep05.getText().toString());
					DecimalFormat decimalFormat = new DecimalFormat("##0.00");//格式化设置  
			        String onepricecheng = String.valueOf(decimalFormat.format(oneprice*onecount));
					getonepricecheng(onepricecheng,partsPricep05);
					totalMoneyTextView.setText(getpartsPricep());
				}else {
					partsPricep05.setText("");
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		//配件总价
		partsPricep01=(TextView) findViewById(R.id.maintainhome_service_edittext_peijianPrice01);
		partsPricep02=(TextView) findViewById(R.id.maintainhome_service_edittext_peijianPrice02);
		partsPricep03=(TextView) findViewById(R.id.maintainhome_service_edittext_peijianPrice03);
		partsPricep04=(TextView) findViewById(R.id.maintainhome_service_edittext_peijianPrice04);
		partsPricep05=(TextView) findViewById(R.id.maintainhome_service_edittext_peijianPrice05);
		partsPriceplist.add(partsPricep01);
		partsPriceplist.add(partsPricep02);
		partsPriceplist.add(partsPricep03);
		partsPriceplist.add(partsPricep04);
		partsPriceplist.add(partsPricep05);
		//发送
		send = (Button) findViewById(R.id.maintainhome_servicenewlsit_send);
		send.setOnClickListener(this);
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);// 返回
		//在保不在保
		toggleButton=(ToggleButton) findViewById(R.id.maintainhome_service_addnewlist_toggleguaranteing);
		toggleButton.setOnCheckedChangeListener(this);
		//人为损坏
		toggleIsManmade=(ToggleButton) findViewById(R.id.maintainhome_service_addnewlist_toggleismanmade);
		toggleIsManmade.setOnCheckedChangeListener(this);
		//基本信息
		indentNumber=(TextView) findViewById(R.id.maintainhome_servicedetail_indentNumber);
		gasAddress=(TextView) findViewById(R.id.maintainhome_servicedetail_gasstationAddress);
		gasStationname=(TextView) findViewById(R.id.maintainhome_servicedetail_gasstationName);
		faultParts=(TextView) findViewById(R.id.maintainhome_servicedetail_faultInfo);
		faultDescription=(TextView) findViewById(R.id.maintainhome_servicedetail_faultproblem);
		indentNumber.setText(m.indentNumber);
		gasAddress.setText(m.gasAddress);
		gasStationname.setText(m.gasStationname);
		faultParts.setText(m.faultParts);
		faultDescription.setText(m.faultDescription);
		
		
		/* try {
			JSONObject obj = null;
			obj = new JSONObject();
			//obj.put("order", "1");
			obj.put("amount", 300);
			obj.put("unit", 1);
			obj.put("quantity", 1);
			obj.put("price", 27);
			obj.put("name", "油枪");
			JSONArray array = new JSONArray();
			array.put(obj);
			obj = new JSONObject();
			obj.put("name", "门窗");
			obj.put("price", 66);
			obj.put("quantity", 2);
			obj.put("unit", 2);
			obj.put("amount", 200);
			array.put(obj);
			Log.i("tag","ary="+array.toString());
			partsInfo=array.toString();
			 			Map obj1 = new LinkedHashMap();
					    obj.put("a", "foo1");
					    obj.put("b", new Integer(100));
					    obj.put("c", new Double(1000.21));
					    obj.put("d", new Boolean(true));
					    obj.put("e", "foo2");
					    obj.put("f", "foo3");
					    obj.put("g", "foo4");
					    obj.put("h", "foo5");
					    obj.put("x", null);
					    
					    JSONObject json = JSONObject. obj1;
					    Log.i("tag","obj1="+json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		
	}
	
	//TODO 单项合计金额的计算方法
	public void getonepricecheng(String str,TextView partsPricep){
		if(str.contains(".")){
			Log.i("tag", "str.indexOf()="+str.indexOf("."));
			if(str.length()-str.indexOf(".")>2){
				String s1=str.substring(0, str.indexOf(".")+3);
				//BigDecimal bigDecimal = new BigDecimal(s1);  
				partsPricep.setText(s1);
				return;
			}
		}
		partsPricep.setText(str);
	}
	
	//TODO 总计金额合计的方法
	public String getpartsPricep(){
		double total=0.0;
		for(int i=0;i<partsPriceplist.size();i++){
			String s=partsPriceplist.get(i).getText().toString();
			if(s!=null&&s.length()!=0){
				double onepricep=Double.parseDouble(s);
				total+=onepricep;
			}
		}DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");//格式化设置  
		String s2=String.valueOf(decimalFormat.format(total));
		if(s2.length()!=0){
			if(s2.contains(".")){
				if(s2.length()-s2.indexOf(".")>2){
					String s1=s2.substring(0, s2.indexOf(".")+3);
					return s1;
				}
			}
		}
		return String.valueOf(total);
	}
	
	//控制特殊字符的输入
	int mMaxLenth = 15;//设置允许输入的字符长度
	public static String stringFilter(String str) {
		// String regEx = "[///:*?<>|]";
		 String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】《》‘；：”“’。，、？]";
		 Pattern p = Pattern.compile(regEx);
		 Matcher m = p.matcher(str);
		 return m.replaceAll("");
	}
	

	//获取配件具体数据
	public String getPartsData(){
		JSONArray array = new JSONArray();
		for(int i=0;i<partsNamelist.size();i++){
			String str0=partsNamelist.get(i).getText().toString();
			String str1=partsOnePriceplist.get(i).getText().toString();
			String str2=partsCountlist.get(i).getText().toString();
			String str3=partsPriceplist.get(i).getText().toString();
			if(str0!=null&&str0.length()!=0&&str1!=null&&str1.length()!=0&&str2!=null&&str2.length()!=0&&str3!=null&&str3.length()!=0){
				 try {
						JSONObject obj = new JSONObject();
						obj.put("name", str0);
						obj.put("order", i);
						obj.put("price", Double.parseDouble(str1));
						obj.put("quantity", Integer.parseInt(str2));
						obj.put("unit", 1);
						obj.put("amount", Double.parseDouble(str3));
						array.put(obj);
						//Log.i("tag","ary="+array.toString());
						partsInfo=array.toString();
					} catch (JSONException e) {
						e.printStackTrace();
					}
			}
		}
		if(array.toString()!=null&&array.toString().length()!=0) return array.toString();
		else return null;
		
	}
	
	//计算合计金额
	public int gettotalMoney(){
		int count=0;//计数变量;
		List<Integer> countlist=new ArrayList<Integer>();//记录填写各下标位置，有可能跳一项填写
		int totalNumber=0;
		for(int i=0;i<5;i++){
			String str0=partsNamelist.get(i).getText().toString();
			String str1=partsOnePriceplist.get(i).getText().toString();
			String str2=partsCountlist.get(i).getText().toString();
			String str3=partsPriceplist.get(i).getText().toString();
			//if(str0!=null||str0.length()!=0||str1!=null||str1.length()!=0||str2!=null||str2.length()!=0||str3!=null||str3.length()!=0){
			if(str0.length()!=0||str1.length()!=0||str2.length()!=0||str3.length()!=0){
				//partsNamelist.add(partsName01);
				count++;
				countlist.add(i);
//				Log.i("tag", "count="+count);
//				Log.i("tag", "countlist="+countlist.toString());
			}
		}
		if(count==0) return 0;
		for(int i=0;i<count;i++){
			String str0=partsNamelist.get(countlist.get(i)).getText().toString();
			String str1=partsOnePriceplist.get(countlist.get(i)).getText().toString();
			String str2=partsCountlist.get(countlist.get(i)).getText().toString();
			String str3=partsPriceplist.get(countlist.get(i)).getText().toString();
			Log.i("tag", "str==="+str0+"  "+str1+"  "+str2+"  "+str3);
			if(str0!=null&&str0.length()!=0&&str1!=null&&str1.length()!=0&&str2!=null&&str2.length()!=0&&str3!=null&&str3.length()!=0){
				totalNumber+=Double.parseDouble(str3);
//				Log.i("tag", "totalNumber==="+totalNumber);
			}else{
				return -1;
			}
		}
		return totalNumber;
		
	}
	
	//获得备注信息的内容
	public String getremarksInfo(){
		String str=remarksInfoEditText.getText().toString();
		if(str==null||str.length()==0){
			str="创建维修单";
		}
		try {
			str=URLEncoder.encode(str, "UTF-8");
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 点击监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发送
		case R.id.maintainhome_servicenewlsit_send:
			//TODO
			if(gettotalMoney()==-1){
				Toast.makeText(getApplicationContext(), "输入项不完整，无法发送", Toast.LENGTH_SHORT).show();
			/*}else if(gettotalMoney()==0||partsPricep01.getText().toString().equals("0.00")||partsPricep02.getText().toString().equals("0.00")
					||partsPricep03.getText().toString().equals("0.00")||partsPricep04.getText().toString().equals("0.00")||partsPricep05.getText().toString().equals("0.00")){
				Toast.makeText(getApplicationContext(), "输入内容为空或0，无法发送", Toast.LENGTH_SHORT).show();*/
			}else if(totalMoneyTextView.getText().toString()==null||totalMoneyTextView.getText().toString().length()==0){
				Toast.makeText(getApplicationContext(), "输入内容为空，无法发送", Toast.LENGTH_SHORT).show();
			}else {
				remarksInfo=getremarksInfo();
				partsInfo=getPartsData();
				try {
					sendServiceform();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
				}
			}
			//Toast.makeText(getApplicationContext(), "已点击发送按钮", Toast.LENGTH_SHORT).show();
			break;
		// 返回
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// 设置切换界面的效果
			break;
		// 计算合计金额
		case R.id.maintainhome_service_edittext_totalMoney:
			if(gettotalMoney()==-1){
				Toast.makeText(getApplicationContext(), "各项输入完整才能计算总计金额", Toast.LENGTH_SHORT).show();
			}else if(gettotalMoney()==0){
				Toast.makeText(getApplicationContext(), "输入内容为空", Toast.LENGTH_SHORT).show();
			}else {
				totalMoneyTextView.setText(String.valueOf(gettotalMoney()));
			}
			break;
		// 点击添加图片
		/*case R.id.maintainhome_servicenewlist_addimageview:
			if (number < 4) {
				View v1 = LayoutInflater.from(
						Maintain_home_service_addnewlistActivity.this).inflate(
						R.layout.popup_phone_select, null);// 创建view
				pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法
				pop.setBackgroundDrawable(new ColorDrawable());
				// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
				pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
				bu1 = (Button) v1.findViewById(R.id.btn_popup_picture);// 相册按钮
				bu2 = (Button) v1.findViewById(R.id.btn_popup_photo);// 相机按钮
				bu3 = (Button) v1.findViewById(R.id.btn_popup_enter);// 取消按钮
				bu1.setOnClickListener(l);
				bu2.setOnClickListener(l);
				bu3.setOnClickListener(l);
				number++;
			} else {
				Toast.makeText(Maintain_home_service_addnewlistActivity.this,
						"图片上传数量已经最大了", 0).show();
			}
			break;*/
		}

	}

	// 发送提交按钮
	private void send() {
		View v = LayoutInflater.from(
				Maintain_home_service_addnewlistActivity.this).inflate(
				R.layout.popup_send_select, null);// 创建view
		popSend = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法
		popSend.setBackgroundDrawable(new ColorDrawable());
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
		popSend.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		btn_confirm = (Button) v
				.findViewById(R.id.maintainhome_serviceAddlist_confirm);
		btn_change.setOnClickListener(l);
		btn_confirm.setOnClickListener(l);

	}

	// 界面上控件的点击事件
	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_popup_photo:
				// 照相机
				Intent intent1 = new Intent();
				intent1.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri imageUri = Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "image" + number
						+ ".jpg"));
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent1, 100);
				break;
			case R.id.btn_popup_picture:
				// 相册
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 200);
				break;
			case R.id.btn_popup_enter:// 取消按钮
				pop.dismiss();// 将显示的popupwindow进行隐藏
				break;
			case R.id.maintainhome_serviceAddlist_confirm:// 确认按钮
				// 点击确认后直接发送，弹出popwindow
				popSendSuccess();
				break;
			case R.id.maintainhome_serviceAddlist_startservice:// 开始维修倒计时
				// TODO
				break;
			default:
				break;
			}

		}
	};

	// 利用意图 返回值时调用的方法
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		pop.dismiss();// popup消失的方法
		if (requestCode == 200 && resultCode == RESULT_OK) {
			// 相册返回
			Uri uri = data.getData();

			String[] pojo = { MediaStore.Images.Media.DATA };

			@SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(uri, pojo, null, null, null);
			if (cursor != null) {
				ContentResolver cr = this.getContentResolver();
				int colunm_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String path = cursor.getString(colunm_index);
				f[number - 1] = new File(path);
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
					Bitmap newBitmap = Photograph_util.zoomBitmap(bitmap,
							bitmap.getWidth() / 4, bitmap.getHeight() / 4);// 将原图片进行压缩
					bitmap.recycle();// 释放原图片占用的内存
					adapter_ph.addDataBottom(newBitmap);
					// ro.setImageBitmap(newBitmap);// 为imageview添加上图片
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} else if (requestCode == 100 && resultCode == RESULT_OK) {
			// 从照相机获取的图片
			Bitmap bitmap = BitmapFactory
					.decodeFile(Environment.getExternalStorageDirectory()
							+ "/image" + number + ".jpg");// 拿到图片bitmap
			f[number - 1] = new File(Environment.getExternalStorageDirectory()
					+ "/image" + number + ".jpg");
			if (bitmap != null) {
				Bitmap newBitmap = Photograph_util.zoomBitmap(bitmap,
						bitmap.getWidth() / 4, bitmap.getHeight() / 4);// 将原图片进行压缩
				bitmap.recycle();// 释放原图片占用的内存
				adapter_ph.addDataBottom(newBitmap);
			}
		}
	}

	// 点击确认发送成功后弹出对话框
	protected void popSendSuccess() {
		View v = LayoutInflater.from(
				Maintain_home_service_addnewlistActivity.this).inflate(
				R.layout.popup_send_success, null);// 创建view
		popSendSuccess = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);// 实例化popupwindow的方法
		popSendSuccess.setBackgroundDrawable(new ColorDrawable());
		// 这个方法是为了当点击除了popupwindow的其他区域可以关闭popupwindow的方法
		popSendSuccess.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		startService = (Button) v
				.findViewById(R.id.maintainhome_serviceAddlist_startservice);
		startService.setOnClickListener(l);

	}

	//toggleButton按钮的在保与不在保
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 可直接获得isChecked的值
		switch (buttonView.getId()) {
		case R.id.maintainhome_service_addnewlist_toggleguaranteing://是否在保
			guaranteingState=isChecked;
			Log.i("tag", "guaranteingState="+isChecked);
			break;
		case R.id.maintainhome_service_addnewlist_toggleismanmade://是否人为损坏
			ismanmadeState=isChecked;
			Log.i("tag", "ismanmadeState="+isChecked);
			break;
		default:
			break;
		}
	}
	
	//发送维修单
	public void sendServiceform(){
		String url=null;
		if(guaranteingState&&!ismanmadeState){//在保并且不是人为损坏的才调用保存接口
			url=Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVE+"id="+String.valueOf(m.id)+"&inWarranty="+guaranteingState+"&isManMade="+ismanmadeState+"&remark="+remarksInfo+"&amount="+String.valueOf(gettotalMoney());//在保
			//url=Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVE+"id="+String.valueOf(m.id)+"&inWarranty=true&remark="+remarksInfo;//在保
			//url=Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVE;//在保
		}else {
			url=Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVESEND+"id="+String.valueOf(m.id)+"&inWarranty="+guaranteingState+"&isManMade="+ismanmadeState+"&remark="+remarksInfo+"&amount="+String.valueOf(gettotalMoney());//在保
			//url=Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVESEND+"id="+String.valueOf(m.id)+"&inWarranty=false&remark="+remarksInfo+"&amount="+String.valueOf(gettotalMoney());//不在保
			//url=Myconstant.MAINTAINHOME_ALLOCTIONDETAIL_NEWSERIVECELIST_SAVESEND;//不在保
		}

		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				try {
					System.out.println(response);
					JSONObject json = new JSONObject(response);
					JSONObject js1 = json.getJSONObject("response");
					if (js1.getString("type").equals("success")) {
						Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
						finish();
						Myutil.set_activity_close(Maintain_home_service_addnewlistActivity.this);
						startActivity(new Intent(Maintain_home_service_addnewlistActivity.this,Maintain_HomeActivity.class));
					} else {
						Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), "访问失败,请检查网络", Toast.LENGTH_SHORT).show();
				//Toast.makeText(getApplicationContext(), "网络异常，访问失败："+error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				System.out.println("用来请求的token值：" + Myconstant.token);
				headers.put("Content-Type", "application/json");
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}
			
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map=new HashMap<String, String>();
				map.put("id", String.valueOf(m.id));
				map.put("inWarranty", String.valueOf(guaranteingState));
				Log.i("tag", "map="+map);
				return map;
			}
			
			@Override
			public byte[] getBody() throws AuthFailureError {
				StringBuilder builder=new StringBuilder();
				builder.append(partsInfo);
				Log.i("tag", builder.toString());
				return builder.toString().getBytes();
			}
		};
		SysApplication.getHttpQueues().add(request);
	}
	

}

class Maintain_home_service_addnewlistSubmitForm{
	String name;
	int price;
	int quantity;
	int unit;
	int amount;
	
}
