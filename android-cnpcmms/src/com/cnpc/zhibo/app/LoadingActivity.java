package com.cnpc.zhibo.app;

import com.cnpc.zhibo.app.util.NetworkDetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

public class LoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		NetworkDetector.instance().init(this);
	}

	@Override
	protected void onResume() {
		if (!NetworkDetector.instance().isNetworkConnected()) {
			AlertDialog.Builder dialog = new Builder(this);
			dialog.setTitle("亲，现在没网");
			dialog.setCancelable(false);
			dialog.setPositiveButton("打开网络", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					try {
						// 不同的android版本打开网络设置的action是不一样的

						// 得到android版本
						// 用代码得到手机的厂商，串号，手机号
						int sdkVersion = Build.VERSION.SDK_INT;
						if (sdkVersion > 10) {
							Intent intent = new Intent(Settings.ACTION_SETTINGS);
							startActivity(intent);
						}
						dialog.cancel();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}).setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			dialog.show();

		} else if (NetworkDetector.instance().isNetworkConnected()) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					SharedPreferences sp = getSharedPreferences("enterinto", 0);
					boolean isUsed = sp.getBoolean("isUsed", false);
					if (isUsed) {
						startActivity(new Intent(LoadingActivity.this, EnterintoActivity.class));
						finish();
					} else {
						Editor et = sp.edit();
						et.putBoolean("isUsed", true);
						et.commit();
						Intent intent = new Intent(LoadingActivity.this, WelcomeActivity.class);
						startActivity(intent);
						finish();
					}

				}
			}, 2000);
		}
		super.onResume();
	}
}
