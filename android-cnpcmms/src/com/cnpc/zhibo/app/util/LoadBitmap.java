package com.cnpc.zhibo.app.util;
//用来加载图片的工具类
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

public class LoadBitmap {
	private BitmapCallback callback;
	public static LruCache<String, Bitmap> cache;
	public static File bitmappath;
	public Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				//下载成功
				Object[] obj=new Object[]{};
				obj=(Object[]) msg.obj;
				//存入缓存
				storeBitmap((String)obj[0], (Bitmap)obj[1]);
				storeSDbitmap((String)obj[0], (Bitmap)obj[1]);
				break;
			case 1:
				callback.bitmapnull();
				break;

			default:
				break;
			}
		}
	};
	public LoadBitmap(Context context){

		if(cache==null){
			int size=(int) (Runtime.getRuntime().maxMemory()/8);
			cache=new LruCache<String, Bitmap>(size){
				private int bitmapsize(String url,Bitmap bitmap) {
					// TODO Auto-generated method stub
					return bitmap.getRowBytes()*bitmap.getHeight();
				}
		};
		if(bitmappath==null){
			bitmappath=context.getExternalCacheDir();
		}
		if(!bitmappath.exists()){
			bitmappath.mkdirs();
		}
		}
	}
	//将照片存入缓存
	private void storeBitmap(String url,Bitmap bitmap){
		cache.put(url, bitmap);
	}
	//在缓存内拿照片
	private Bitmap takeBitmap(String url){
		return cache.get(url);
	}
	//取一张图片
	public Bitmap bitmaptake(String url,BitmapCallback callback){
		//缓存中有的话直接拿
		Bitmap bitmap=takeBitmap(url);
		if(bitmap!=null){
		return bitmap;
	}
		//从磁盘缓存中直接拿
		bitmap=taskSDbitmap(url, bitmap);
		if(bitmap!=null){
		return bitmap;
		}
		//网络上下载
		takeBitmapNet(url, callback);
		return bitmap;
	}
	private void takeBitmapNet(final String url,final BitmapCallback callback){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputStream is=Http_server_util.Get_inptu_http(url);
				Bitmap bitmap=null;
				if(is!=null){
					bitmap=BitmapFactory.decodeStream(is);
					if(bitmap!=null){
						//存入缓存
						//storeBitmap(url, bitmap);
						Message msg=new Message();
						msg.what=100;
						msg.obj=new Object[]{url,bitmap};
						handler.sendMessage(msg);
				}
					else{
						handler.sendEmptyMessage(1);
					}
			
				}
				
			}
		}).start();
	}
	//存入SD卡
	private void storeSDbitmap(String url,Bitmap bitmap){
		int index=url.lastIndexOf("/");
		String filename=url.substring(index);
		File file=new File(bitmappath,filename);
		try {
			OutputStream out=new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, out);//将流转换为图片
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//从SD卡中拿出
	private Bitmap taskSDbitmap(String url,Bitmap bitmap){
		int index=url.lastIndexOf("/");
		String filename=url.substring(index);
//		File[] file=bitmappath.listFiles();
//		for (File f : file) {
//			if(f.getName().equals(filename)){
//				Bitmap bitmap1=BitmapFactory.decodeFile(f.getAbsolutePath());
//				return bitmap1;
//			}
//		}
		File file=new File(bitmappath,filename);
		if(file.exists()){
			Bitmap bitmap1=BitmapFactory.decodeFile(file.getAbsolutePath());
			return bitmap1;
		}
		return null;
	
	}
	public interface BitmapCallback{
		public void bitmapback(String url,Bitmap bitmap);
		public void bitmapnull();
	}
}



