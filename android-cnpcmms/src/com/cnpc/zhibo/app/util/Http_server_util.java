package com.cnpc.zhibo.app.util;
//网络服务器的操作类+
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class Http_server_util {
	

	//通过get请求拿到String类型的数据
	public static String Get_Stringhttp(String urlstr){
		HttpClient client=new DefaultHttpClient();//创建客户端对象
		HttpGet get=new HttpGet(urlstr);//创建一个Get请求
		try {
			HttpResponse response=client.execute(get);//执行一次请求
			int code=response.getStatusLine().getStatusCode();
			System.out.println("Get_Stringhttp方法返回的响应吗："+code);
			if(code==200){
				HttpEntity entity=response.getEntity();//实体类
				return EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	//通过get请求拿到inputstream类型的数据
	public static InputStream Get_inptu_http(String urlstr){
			
		HttpClient client=new DefaultHttpClient();
		HttpParams params=client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 2000);
		HttpConnectionParams.setSoTimeout(params, 3000);
		HttpGet get=new HttpGet(urlstr);
		try {
			HttpResponse response=client.execute(get);
			int code=response.getStatusLine().getStatusCode();
			if(code==200){
				HttpEntity entity=response.getEntity();
				return entity.getContent();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null ;
		
	}
	//通过post请求拿到String类型的数据
		public static String Post_string_http(String urlstr,Map<String, Object> value){
			try {
				//1.创建客户端
				HttpClient client = new DefaultHttpClient();
				//3.创建一个post请求
				HttpPost post = new HttpPost(urlstr);
				//5.创建内容的LIST集合
				List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
				//6.遍历Map集合内的数据，放入list集合
				Set<String> key = value.keySet();
				for (String keys : key) {
					BasicNameValuePair pair = new BasicNameValuePair(keys, value
							.get(keys).toString());
					parameters.add(pair);
				}
				//4.给post请求设置内容(提交给服务器的LIST集合和数据的编码)
				HttpEntity entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
				post.setEntity(entity);
				//2.执行一次请求
				HttpResponse response=client.execute(post);
				int code=response.getStatusLine().getStatusCode();
				if(code==200){
					HttpEntity httpentity=response.getEntity();
					return EntityUtils.toString(httpentity);
				}
				
			} catch (IOException e) {
				// TODO: handle exception
			}
			return null;
			
		}
		//通过post请求拿到inputstream类型的数据
		public static InputStream Post_input_http(String urlstr,Map<String, Object> map){
			try {
				HttpClient client1 = new DefaultHttpClient();
				HttpPost post1 = new HttpPost(urlstr);
				List<BasicNameValuePair> pair1 = new ArrayList<BasicNameValuePair>();
				Set<String> key = map.keySet();
				for (String keys : key) {
					BasicNameValuePair basi = new BasicNameValuePair(keys, map.get(
							keys).toString());
					pair1.add(basi);
				}
				HttpEntity entity1 = new UrlEncodedFormEntity(pair1, HTTP.UTF_8);
				post1.setEntity(entity1);
				HttpResponse response = client1.execute(post1);
				int code=response.getStatusLine().getStatusCode();
				if(code==200){
					HttpEntity httpentity=response.getEntity();
					return httpentity.getContent();
					
				}
			} catch (IOException e) {
				// TODO: handle exception
			}
			return null;
			
		}
		
		//post传递文件返回string数据
		public static String postFileToString(String urlStr,Map<String, Object> map){
			try {
				HttpClient client=new DefaultHttpClient();
				HttpPost request=new HttpPost(urlStr);
				MultipartEntity entity=new MultipartEntity();
				Set<String> keys=map.keySet();
				for (String key : keys) {
					Object value=map.get(key);
					if(value instanceof File){
						entity.addPart("file", new FileBody((File) value));
					}else{
						entity.addPart(key, new StringBody(value.toString()));
						
					}
				}
				
				request.setEntity(entity);
				HttpResponse response=client.execute(request);
				if(response.getStatusLine().getStatusCode()==200){
					HttpEntity entity2=response.getEntity();
					return EntityUtils.toString(entity2);
				}else {
					System.out.println("上传失败："+response.getStatusLine().getStatusCode());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	
	
	


}
