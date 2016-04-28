package com.cnpc.zhibo.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cnpc.zhibo.app.application.SysApplication;
import com.cnpc.zhibo.app.config.Myconstant;
import com.cnpc.zhibo.app.util.Myutil;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 供应商端的报销详情界面
 */
public class Supplier_baoxiaodanxiangqingActivity extends MyActivity {

	private ImageView fanhui, select;// 返回 ,审批
	private WebView webView;// 静态页面
	private TextView shenpi;// 审批
	private PopupWindow pop;// 弹出框
	private Button agree, unagree;// 同意、驳回
	private EditText content;// 备注输入的内容

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplier_baoxiaodanxiangqing);
		String id = getIntent().getStringExtra("bao_xiao_dan_id");
		System.out.println(id);
		setview();
//		registerForContextMenu(webView);//注册上下文菜单
	}

	/**
	 * 初始化控件
	 */
	private void setview() {
		fanhui = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		fanhui.setVisibility(View.VISIBLE);
		fanhui.setOnClickListener(l);
		// select=(ImageView) findViewById(R.id.imageView_myactity_you);
		// select.setVisibility(View.VISIBLE);
		// select.setImageResource(R.drawable.send_notice1);
		// select.setPadding(0, 0, 30, 0);
		webView = (WebView) findViewById(R.id.bao_xiao_dan_xiang_qing);
		shenpi = (TextView) findViewById(R.id.Supplier_baoxiaoshenpi);
		if (getIntent().getStringExtra("state").equals("progress")) {
			set_title_text("未处理的报销单");
			shenpi.setOnClickListener(l);
		} else {
			set_title_text("已处理的报销单");
			shenpi.setVisibility(View.GONE);
		}
		set_webView();
	}

	/**
	 * 设置webView
	 */
	private void set_webView() {
		 webView.getSettings().setJavaScriptEnabled(true);
		 webView.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		 webView.getSettings().setBlockNetworkImage(false);// 可加载图片
//		 webView.getSettings().setAppCacheEnabled(true);// 应用缓存
		 webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 没有网络时家在缓存
		// webView.getSettings().setSupportZoom(true);// 支持缩放
		// webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		// webView.getSettings().setBuiltInZoomControls(true); // 可放大缩小
		// webView.getSettings().setUseWideViewPort(true);
		webView.loadUrl(Myconstant.REIMBURSEMENTMESSAGE + getIntent().getStringExtra("bao_xiao_dan_id"));
	}

	private View.OnClickListener l = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_myacitity_zuo:// 返回按钮
				finish();
				Myutil.set_activity_close(Supplier_baoxiaodanxiangqingActivity.this);
				break;
			case R.id.Supplier_baoxiaoshenpi:// 审批按钮
				// startActivity(
				// new Intent(Supplier_baoxiaodanxiangqingActivity.this,
				// Supplier_baoxiaoshenpiActivity.class));
				//
				// Myutil.set_activity_open(Supplier_baoxiaodanxiangqingActivity.this);
				View v1 = LayoutInflater.from(Supplier_baoxiaodanxiangqingActivity.this)
						.inflate(R.layout.poppup_baoxiaoshenpi, null);
				pop = new PopupWindow(v1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
				pop.setBackgroundDrawable(new ColorDrawable());
				pop.showAtLocation(shenpi, Gravity.BOTTOM, 0, 0);
				agree = (Button) v1.findViewById(R.id.button__popup_baoxiao_agree);
				unagree = (Button) v1.findViewById(R.id.button__popup_baoxiao_unagree);
				content = (EditText) v1.findViewById(R.id.editText_popup_baoxiao_message);
				agree.setOnClickListener(l);
				unagree.setOnClickListener(l);
				break;
			case R.id.button__popup_baoxiao_agree:// 同意审批
				set__disposemonad(Myconstant.SUPPLIER_APPROVED_SERVICEMONAD,
						getIntent().getStringExtra("bao_xiao_dan_id"));
				break;
			case R.id.button__popup_baoxiao_unagree:// 驳回审批
				set__disposemonad(Myconstant.SUPPLIER_UNAPPROVED_SERVICEMONAD,
						getIntent().getStringExtra("bao_xiao_dan_id"));
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 报销单审批上传
	 * 
	 * @param url
	 * @param id
	 */
	private void set__disposemonad(String url, final String id) {
		StringRequest re = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj = new JSONObject(response);
					JSONObject obj1 = obj.getJSONObject("response");
					if (obj1.getString("type").equals("success")) {
						startActivity(
								new Intent(Supplier_baoxiaodanxiangqingActivity.this, Supplier_HomeActivity.class));
						SysApplication.getInstance().getapplyListHandler1().sendEmptyMessage(100);
						Myutil.set_activity_open(Supplier_baoxiaodanxiangqingActivity.this);
						Toast.makeText(Supplier_baoxiaodanxiangqingActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(Supplier_baoxiaodanxiangqingActivity.this, obj1.getString("content"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(Supplier_baoxiaodanxiangqingActivity.this, "提交失败，请稍后再试", Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("accept", "application/json");
				headers.put("api_key", Myconstant.token);
				return headers;
			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("content", content.getText().toString());
				return map;
			}
		};
		SysApplication.getHttpQueues().add(re);
	}
	
	private String imgurl = "";
	 
    /***
     * 功能：长按图片保存到手机
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle() == "保存到手机") {
                    new SaveImage().execute(); // Android 4.0以后要使用线程来访问网络
                } else {
                    return false;
                }
                return true;
            }
        };
        if (v instanceof WebView) {
            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    imgurl = result.getExtra();
                    menu.setHeaderTitle("提示");
                    menu.add(0, v.getId(), 0, "保存到手机").setOnMenuItemClickListener(handler);
                }
            }
        }
    }
 
    /***
     * 功能：用线程保存图片
     * 
     * @author wangyp
     * 
     */
    private class SaveImage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();
                File file = new File(sdcard + "/Download");
                if (!file.exists()) {
                    file.mkdirs();
                }
                int idx = imgurl.lastIndexOf(".");
                String ext = imgurl.substring(idx);
                file = new File(sdcard + "/Download/" + new Date().getTime() + ext);
                InputStream inputStream = null;
                URL url = new URL(imgurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                if (conn.getResponseCode() == 200) {
                    inputStream = conn.getInputStream();
                }
                byte[] buffer = new byte[4096];
                int len = 0;
                FileOutputStream outStream = new FileOutputStream(file);
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                result = "图片已保存至：" + file.getAbsolutePath();
            } catch (Exception e) {
                result = "保存失败！" + e.getLocalizedMessage();
            }
            return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
           Toast.makeText(Supplier_baoxiaodanxiangqingActivity.this, result, Toast.LENGTH_SHORT).show();;
        }
    }

}
