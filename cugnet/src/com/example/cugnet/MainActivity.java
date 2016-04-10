package com.example.cugnet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Button btn_connect_once;
	private EditText et_username;
	private EditText et_password;
	private int select;
	private SharedPreferences sp;
	private ProgressDialog progressDialog;
	private RadioGroup rg_group;
	 private static Boolean isQuit = false;  
	    private long mExitTime = 0;  
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getOverflowMenu();

		sp = this.getSharedPreferences("userInfo", MainActivity.this.MODE_PRIVATE);
		
		btn_connect_once = (Button) findViewById(R.id.btnLogin_once);
		et_username = (EditText) findViewById(R.id.username);
		et_password = (EditText) findViewById(R.id.pwd);
		rg_group = (RadioGroup) findViewById(R.id.radioGroup);
		et_username.setText(sp.getString("USER_NAME", ""));
		et_password.setText(sp.getString("PASSWORD", ""));
		et_username.setSelection(et_username.getText().toString().length());

		// 弹出要给ProgressDialog
		progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setTitle("提示信息");
		progressDialog.setMessage("正在登录，请稍后......");
		// 设置setCancelable(false); 表示我们不能取消这个弹出框，等下载完成之后再让弹出框消失
		// progressDialog.setCancelable(false);
		// 设置ProgressDialog样式为圆圈的形式
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_dianxin:
					select = 1;
					break;
				case R.id.rb_jiaoyu:
					select = 0;
					break;
				}
			}
		});
		btn_connect_once.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String ip = new getIP().getWIFILocalIpAdress(MainActivity.this);
					String username = et_username.getText().toString();
					String password = et_password.getText().toString();

					Bundle bundle = new Bundle();
					bundle.putString("username", username);
					bundle.putInt("select", select);
					bundle.putString("password", password);
					//bundle.putString("ip", ip);
					new MyAsyncTask().execute(bundle);
					Editor editor = sp.edit();
					editor.putString("USER_NAME", username);
					editor.putString("PASSWORD", password);
					editor.putString("ip", ip);
					editor.putInt("SELECT", select);
					editor.commit();
					// ToastOwn.Short("登录成功！");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();

				}
			}
		});
		
			
		

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
			progressDialog.show();
		}

		@Override
		protected String doInBackground(Bundle... params) {
			Bundle bundle = params[0];
			//String ip = bundle.getString("ip");
			//Pattern pattern2 = Pattern.compile("\\d+");
			//Matcher matcher2 = pattern2.matcher(ip);
		//	ArrayList<String> IP = new ArrayList<String>();
		//	while (matcher2.find()) {
		//		IP.add(matcher2.group(0));
		//	}
		//	if (IP.get(0).equals("172") && IP.get(1).equals("31")) {
				int count = 0;
				while (true) {
					try {
						count++;
						if (count == 3) {
							return "0";
						}
						String surl = "http://192.168.168.46/?LanmanUserURL=http://m.baidu.com/?cachebust=20160321";

						/**
						 * 首先要和URL下的URLConnection对话。
						 * URLConnection可以很容易的从URL得到。比如： // Using java.net.URL
						 * and //java.net.URLConnection
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
						 * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如：
						 * ...
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
							String str = "登录成功！" + "余额" + num.get(1) + "." + num.get(2) + "元" + "," + "已用流量"
									+ num.get(3) + "." + num.get(4) + "M";

							return str;

						}

					} catch (Exception e) {

					}
				}
			//} else {
			//	return "1";
			//}

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			progressDialog.dismiss();
			if (result.equals("0")) {
				ToastOwn.Long("连接失败，请重试");
			}else if(result.equals("1")) {
				ToastOwn.Long("请连接CUG");
			}
			else {
				ToastOwn.Long(result);
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK) {  
	          if ((System.currentTimeMillis() - mExitTime) > 2000) {//  
	              // 如果两次按键时间间隔大于2000毫秒，则不退出  
	              Toast.makeText(this, "如果想CUG自动登录的话\n请按HOME键哦", Toast.LENGTH_LONG).show();  
	              mExitTime = System.currentTimeMillis();// 更新mExitTime  
	          } else {  
	              System.exit(0);// 否则退出程序  
	          }  
	          return true;  
	      }  
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.tell:
			Intent intent = new Intent(MainActivity.this, TellActivity.class);
			startActivity(intent);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	// force to show overflow menu in actionbar
	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
