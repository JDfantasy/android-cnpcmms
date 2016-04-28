package com.cnpc.zhibo.app;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.cnpc.zhibo.app.config.Myconstant;
//维修工端查看自己填写的维修单详情的界面
import com.cnpc.zhibo.app.util.Myutil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class Maintain_home_service_detail_Activity extends MyActivity implements OnClickListener {

	private ImageView backImageView;
	private WebView webView;
	private String indentId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home_service_detail);
		indentId=getIntent().getStringExtra("indentId");
		set_title_text("维修单详情");
		setViews();
		this.registerForContextMenu(webView);//注册上下文菜单
	}

	private void setViews() {
		webView=(WebView) findViewById(R.id.maintain_homeServiceOrService_detail_webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());// 点击链接不调用外部浏览器
		webView.getSettings().setBlockNetworkImage(false);// 可加载图片
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 没有网络时家在缓存
		webView.loadUrl(Myconstant.SERVICEMESSAGENEW + indentId);
		//webView.loadUrl("https://www.baidu.com/");
		backImageView = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		backImageView.setVisibility(View.VISIBLE);
		backImageView.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 回退按钮
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// 设置切换界面的效果
			break;
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		 WebView w = (WebView)v;
		 HitTestResult result = w.getHitTestResult();
		 //只检测图片格式
		if(result.getType() == HitTestResult.IMAGE_TYPE){
		menu.addSubMenu(1, 1, 1, "保存图片");
		//通过result.getExtra()取出URL
		String strUrl = result.getExtra();
		Toast.makeText(getApplicationContext(), strUrl, Toast.LENGTH_SHORT).show();
		}
	}
	
	//保存图片开始
//	private String imgurl = "";
//	 
//    /***
//     * 功能：长按图片保存到手机
//     */
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getTitle() == "保存到手机") {
//                    new SaveImage().execute(); // Android 4.0以后要使用线程来访问网络
//                } else {
//                    return false;
//                }
//                return true;
//            }
//        };
//        if (v instanceof WebView) {
//            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
//            if (result != null) {
//                int type = result.getType();
//                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//                    imgurl = result.getExtra();
//                    menu.setHeaderTitle("提示");
//                    menu.add(0, v.getId(), 0, "保存到手机").setOnMenuItemClickListener(handler);
//                }
//            }
//        }
//    }
// 
//    /***
//     * 功能：用线程保存图片
//     * 
//     * @author wangyp
//     * 
//     */
//    private class SaveImage extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            String result = "";
//            try {
//                String sdcard = Environment.getExternalStorageDirectory().toString();
//                File file = new File(sdcard + "/Download");
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//                int idx = imgurl.lastIndexOf(".");
//                String ext = imgurl.substring(idx);
//                file = new File(sdcard + "/Download/" + new Date().getTime() + ext);
//                InputStream inputStream = null;
//                URL url = new URL(imgurl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setConnectTimeout(20000);
//                if (conn.getResponseCode() == 200) {
//                    inputStream = conn.getInputStream();
//                }
//                byte[] buffer = new byte[4096];
//                int len = 0;
//                FileOutputStream outStream = new FileOutputStream(file);
//                while ((len = inputStream.read(buffer)) != -1) {
//                    outStream.write(buffer, 0, len);
//                }
//                outStream.close();
//                result = "图片已保存至：" + file.getAbsolutePath();
//            } catch (Exception e) {
//                result = "保存失败！" + e.getLocalizedMessage();
//            }
//            return result;
//        }
// 
//        @Override
//        protected void onPostExecute(String result) {
//           Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();;
//        }
//    }
	//保存图片结束


}
