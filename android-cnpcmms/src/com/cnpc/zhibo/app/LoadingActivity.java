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
			dialog.setTitle("�ף�����û��");
			dialog.setCancelable(false);
			dialog.setPositiveButton("������", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					try {
						// ��ͬ��android�汾���������õ�action�ǲ�һ����

						// �õ�android�汾
						// �ô���õ��ֻ��ĳ��̣����ţ��ֻ���
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
			}).setNegativeButton("ȡ��", new OnClickListener() {

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
