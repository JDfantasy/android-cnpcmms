package com.cnpc.zhibo.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ImageFileCacheUtils {
	private static final String CHCHEDIR = "ImageChace";//����Ŀ¼
	  private static final String WHOLESALE_CONV = ".cache";//�����ļ���׺��
	  private static final int MB = 1024 * 1024;
	  private static final int CACHE_SIZE = 80;//������������������ͻ�����lru�㷨ɾ���������ʹ�õĻ����ļ���
	  private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 100;//��������SD����ʣ����С����
	   
	  private static ImageFileCacheUtils instance = null; 
	  //����ģʽ
	  public static ImageFileCacheUtils getInstance(){
	    if (instance == null) {
	      synchronized (ImageFileCacheUtils.class) {
	        if (instance == null) {
	          instance = new ImageFileCacheUtils();
	        }
	      }
	    }
	    return instance;
	  }
	   
	  public ImageFileCacheUtils(){
	    removeCache(getDirectory());
	  }
	   
	  /**
	   * ���ļ������л�ȡͼƬ
	   * @param url
	   * @return
	   */
	  public Bitmap getImage(final String url){
	    final String path = getDirectory() + "/" +convertUrlToFileName(url);
	    File file = new File(path);
	    if(file.exists()){
	      Bitmap bitmap = BitmapFactory.decodeFile(path);
	      if(bitmap == null){
	        file.delete();
	        return null;
	      }else {
	        updateFileTime(path);//�����ļ����·���ʱ��
	        return bitmap;
	      }
	    }else {
	      return null;
	    }
	  }
	   
	  /**
	   * ��û���Ŀ¼
	   * @return
	   */
	  private String getDirectory() {
	    String dir = getSDPath() + "/" + CHCHEDIR;
	    return dir;
	  }
	   
	  private String getSDPath(){
	    File sdDir = null;
	    boolean adCardExit = Environment.getExternalStorageState()
	        .endsWith(Environment.MEDIA_MOUNTED);//�ж�SD���Ƿ����
	    if (adCardExit) {
	      sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼
	    }
	    if (sdDir != null) {
	      return sdDir.toString();
	    }else {
	      return "";
	    }
	  }
	   
	  /**
	   * �޸��ļ�������޸�ʱ��
	   * @param path
	   */
	  private void updateFileTime(String path) {
	    File file = new File(path);
	    long newModeifyTime = System.currentTimeMillis();
	    file.setLastModified(newModeifyTime);
	  }
	 
	  public void saveBitmap(Bitmap bitmap,String url){
	    if (bitmap == null) {
	      return ;
	    }
	    //�ж�SD���ϵĿռ�
	    if(FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()){
	      return;
	    }
	    String fileName = convertUrlToFileName(url);
	    String dir = getDirectory();
	    File dirFile = new File(dir);
	    if (!dirFile.exists()) {
	      dirFile.mkdirs();
	    }
	    File file = new File(dir + "/" +fileName);
	    try {
	      file.createNewFile();
	      OutputStream outputStream = new FileOutputStream(file);
	      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
	      outputStream.flush();
	      outputStream.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	   
	  /**
	   * ����sd���ϵ�ʣ��ռ�
	   * @return
	   */
	  private int freeSpaceOnSd() {
	    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    double sdFreeMB = ((double)statFs.getAvailableBlocks() * (double)statFs.getBlockSize()) / MB;
	    Log.i("test", "ʣ��ռ�Ϊ��"+sdFreeMB);
	    return (int)sdFreeMB;
	  }
	   
	  /**
	   * ��urlת���ļ���
	   * @param url
	   * @return
	   */
	  private String convertUrlToFileName(String url) {
	    String[] strs = url.split("/");
	    return strs[strs.length - 1] + WHOLESALE_CONV;
	  }
	   
	  /**
	   * ����洢Ŀ¼�µ��ļ���С
	   * ���ļ��ܴ�С���ڹ涨�Ĵ�С����sd��ʣ��ռ�С��FREE_SD_SPACE_NEEDED_TO_CACHE�Ĺ涨ʱ
	   * ����ôɾ��40%���û�б�ʹ�õ��ļ�
	   * @param dirPath
	   * @return
	   */
	  private boolean removeCache(String dirPath){
	    File dirFile = new File(dirPath);
	    File[] files = dirFile.listFiles();
	    if (files == null || files.length <= 0) {
	      return true;
	    }
	    //���sd��û�й���
	    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	      return false;
	    }
	    int dirSize = 0;
	    for (int i = 0; i < files.length; i++) {
	      if (files[i].getName().contains(WHOLESALE_CONV)) {
	        dirSize += files[i].length();
	      }
	    }
	    if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
	      int removeFactor = (int)((0.4 * files.length) + 1);
	      Arrays.sort(files, new FileLastModifySoft());
	      for (int i = 0; i < removeFactor; i++) {
	        if (files[i].getName().contains(WHOLESALE_CONV)) {
	          files[i].delete();
	        }
	      }
	    }
	    if (freeSpaceOnSd() <= CACHE_SIZE) {
	      return false;
	    }
	    return true;
	    }
	 
	  //�Ƚ�����
	  private class FileLastModifySoft implements Comparator<File>{
	    @Override
	    public int compare(File arg0, File arg1) {
	      if (arg0.lastModified() > arg1.lastModified()) {
	        return 1;
	      }else if (arg0.lastModified() == arg1.lastModified()) {
	        return 0;
	      }else {
	        return -1;
	      }
	    }
	  }
}
