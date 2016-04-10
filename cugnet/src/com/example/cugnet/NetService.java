package com.example.cugnet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.cugnet.MainActivity.MyAsyncTask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

public class NetService extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		try{
		String username = intent.getStringExtra("username");
		String password = intent.getStringExtra("password");
		String ip = intent.getStringExtra("ip");
		int select = intent.getIntExtra("select", 0);
		Bundle bundle = new Bundle();
		bundle.putString("username", username);
		bundle.putInt("select", select);
		bundle.putString("password", password);
		bundle.putString("ip", ip);
		new MyAsyncTask().execute(bundle);
		}catch(Exception e){
			
		}

	}

	/**
	 * 定义一个类，让其继承AsyncTask这个类 Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径
	 * Progress: Integer类型，进度条的单位通常都是Integer类型 Result：byte[]类型，表示我们下载好的图片以字节数组返回
	 * 
	 * @author xiaoluo
	 *
	 */
	public class MyAsyncTask extends AsyncTask<Bundle, Integer, String> {
	

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// 在onPreExecute()中我们让ProgressDialog显示出来
			
		}

		@Override
		protected String doInBackground(Bundle... params) {
			
			Bundle bundle = params[0];
			 int count=0;
			while (true) {
				try {
					 count++;
					 if (count == 5) {
					 return "0";
					 }
					String surl = "http://192.168.168.46/?LanmanUserURL=http://m.baidu.com/?cachebust=20160321";

					/**
					 * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如：
					 * // Using java.net.URL and //java.net.URLConnection
					 */
					URL url = new URL(surl);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					/**
					 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
					 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
					 */
					connection.setDoOutput(true);

					connection.setDoInput(true);
					/**
					 * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...
					 */
					// connection.setUseCaches(false);
					// OutputStreamWriter out = new
					// OutputStreamWriter(connection.getOutputStream(),
					// "GBK");
					// 其中的memberName和password也是阅读html代码得知的，即为表单中对应的参数名称
					// out.write("username=20141002570&password=0&rmbUser=1&B1=");
					// //
					// post的关键所在！

					String username = bundle.getString("username");
					int select = bundle.getInt("select");
					String password = bundle.getString("password");
					String data2 = "username=" + username + "&password=" + password + "&rmbUser=1&B1=";
					// 这里可以写一些请求头的东东...
					// 获取输出流
					OutputStream out = connection.getOutputStream();
					out.write(data2.getBytes());
					// remember to clean up
					out.flush();
					out.close();

					// 取得cookie，相当于记录了身份，供下次访问时使用
					String cookieVal = connection.getHeaderField("Set-Cookie");

					String s = "http://192.168.168.46:9999/?LanmanUserURL=$USERURL";
					// 重新打开一个连接
					url = new URL(s);
					HttpURLConnection resumeConnection = (HttpURLConnection) url.openConnection();
					if (cookieVal != null) {
						// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
						resumeConnection.setRequestProperty("Cookie", cookieVal);
					}
					resumeConnection.connect();
					InputStream urlStream = resumeConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
					String ss = null;
					String total = "";
					while ((ss = bufferedReader.readLine()) != null) {
						total += ss;
					}

					Document doc = (Document) Jsoup.parse(total);

					String[] Lan = new String[3];
					int i = 0;
					Elements links = doc.getElementsByTag("a");
					Pattern r = Pattern.compile("(\\d{5}|\\d{4})");
					for (Element link : links) {
						String linkHref = link.attr("onclick");
						Matcher m = r.matcher(linkHref);
						if (m.find() && m.group().length() != 0) {

							Lan[i] = m.group();
							i++;
						}

					}

					String LOGIN_URL = "http://192.168.168.46:9999/";
					String msg = "";

					HttpURLConnection conn = (HttpURLConnection) new URL(LOGIN_URL).openConnection();
					// 设置请求方式,请求超时信息
					conn.setRequestMethod("POST");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					if (cookieVal != null) {
						// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
						conn.setRequestProperty("Cookie", cookieVal);
					}
					// 设置运行输入,输出:
					conn.setDoOutput(true);
					conn.setDoInput(true);
					// Post方式不能缓存,需手动设置为false
					conn.setUseCaches(false);
					// 我们请求的数据:
					String data = "POSTLanmanUserURL&Lanmannumber=" + Lan[select];
					// 这里可以写一些请求头的东东...
					// 获取输出流
					OutputStream out1 = conn.getOutputStream();
					out1.write(data.getBytes());
					out1.flush();
					if (conn.getResponseCode() == 200) {
						// 获取响应的输入流对象
						InputStream is = conn.getInputStream();
						// 创建字节输出流对象
						ByteArrayOutputStream message = new ByteArrayOutputStream();
						// 定义读取的长度
						int len = 0;
						// 定义缓冲区
						byte buffer[] = new byte[1024];
						// 按照缓冲区的大小，循环读取
						while ((len = is.read(buffer)) != -1) {
							// 根据读取的长度写入到os对象中
							message.write(buffer, 0, len);
						}
						// 释放资源
						is.close();
						message.close();
						// 返回字符串

						msg = new String(message.toByteArray());
						msg = String.copyValueOf(msg.toCharArray(), 0, msg.length());
						Pattern pattern = Pattern.compile("\\d+");
						Matcher matcher = pattern.matcher(msg);
						ArrayList<String> num = new ArrayList<String>();
						while (matcher.find()) {
							num.add(matcher.group(0));
						}
						String str = "登录成功！" + "余额" + num.get(1) + "." + num.get(2) + "元" + "," + "已用流量" + num.get(3)
								+ "." + num.get(4) + "M";
						
						return str;

					}

				} catch (Exception e) {

				}
			}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(result.equals("0")){
				ToastOwn.Long("对不起，连接失败，请手动登录");
			}else{
				ToastOwn.Long(result);
			}
				
			

			stopSelf();

		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
