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

		// ����Ҫ��ProgressDialog
		progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setTitle("��ʾ��Ϣ");
		progressDialog.setMessage("���ڵ�¼�����Ժ�......");
		// ����setCancelable(false); ��ʾ���ǲ���ȡ����������򣬵��������֮�����õ�������ʧ
		// progressDialog.setCancelable(false);
		// ����ProgressDialog��ʽΪԲȦ����ʽ
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
					// ToastOwn.Short("��¼�ɹ���");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(MainActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();

				}
			}
		});
		
			
		

	}

	/**
	 * ����һ���࣬����̳�AsyncTask����� Params: String���ͣ���ʾ���ݸ��첽����Ĳ���������String��ͨ��ָ������URL·��
	 * Progress: Integer���ͣ��������ĵ�λͨ������Integer���� Result��byte[]���ͣ���ʾ�������غõ�ͼƬ���ֽ����鷵��
	 * 
	 * @author xiaoluo
	 *
	 */
	public class MyAsyncTask extends AsyncTask<Bundle, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// ��onPreExecute()��������ProgressDialog��ʾ����
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
						 * ����Ҫ��URL�µ�URLConnection�Ի���
						 * URLConnection���Ժ����׵Ĵ�URL�õ������磺 // Using java.net.URL
						 * and //java.net.URLConnection
						 */
						URL url = new URL(surl);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();

						/**
						 * Ȼ���������Ϊ���ģʽ��URLConnectionͨ����Ϊ������ʹ�ã���������һ��Webҳ��
						 * ͨ����URLConnection��Ϊ���������԰����������Webҳ���͡��������������
						 */
						connection.setDoOutput(true);

						connection.setDoInput(true);
						/**
						 * ���Ϊ�˵õ�OutputStream�������������Լ����Writer���ҷ���POST��Ϣ�У����磺
						 * ...
						 */
						// connection.setUseCaches(false);
						// OutputStreamWriter out = new
						// OutputStreamWriter(connection.getOutputStream(),
						// "GBK");
						// ���е�memberName��passwordҲ���Ķ�html�����֪�ģ���Ϊ���ж�Ӧ�Ĳ�������
						// out.write("username=20141002570&password=0&rmbUser=1&B1=");
						// //
						// post�Ĺؼ����ڣ�

						String username = bundle.getString("username");
						int select = bundle.getInt("select");
						String password = bundle.getString("password");
						String data2 = "username=" + username + "&password=" + password + "&rmbUser=1&B1=";
						// �������дһЩ����ͷ�Ķ���...
						// ��ȡ�����
						OutputStream out = connection.getOutputStream();
						out.write(data2.getBytes());
						// remember to clean up
						out.flush();
						out.close();

						// ȡ��cookie���൱�ڼ�¼����ݣ����´η���ʱʹ��
						String cookieVal = connection.getHeaderField("Set-Cookie");

						String s = "http://192.168.168.46:9999/?LanmanUserURL=$USERURL";
						// ���´�һ������
						url = new URL(s);
						HttpURLConnection resumeConnection = (HttpURLConnection) url.openConnection();
						if (cookieVal != null) {
							// ����cookie��Ϣ��ȥ���Ա����Լ�����ݣ�����ᱻ��Ϊû��Ȩ��
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
						// ��������ʽ,����ʱ��Ϣ
						conn.setRequestMethod("POST");
						conn.setReadTimeout(5000);
						conn.setConnectTimeout(5000);
						if (cookieVal != null) {
							// ����cookie��Ϣ��ȥ���Ա����Լ�����ݣ�����ᱻ��Ϊû��Ȩ��
							conn.setRequestProperty("Cookie", cookieVal);
						}
						// ������������,���:
						conn.setDoOutput(true);
						conn.setDoInput(true);
						// Post��ʽ���ܻ���,���ֶ�����Ϊfalse
						conn.setUseCaches(false);
						// �������������:
						String data = "POSTLanmanUserURL&Lanmannumber=" + Lan[select];
						// �������дһЩ����ͷ�Ķ���...
						// ��ȡ�����
						OutputStream out1 = conn.getOutputStream();
						out1.write(data.getBytes());
						out1.flush();
						if (conn.getResponseCode() == 200) {
							// ��ȡ��Ӧ������������
							InputStream is = conn.getInputStream();
							// �����ֽ����������
							ByteArrayOutputStream message = new ByteArrayOutputStream();
							// �����ȡ�ĳ���
							int len = 0;
							// ���建����
							byte buffer[] = new byte[1024];
							// ���ջ������Ĵ�С��ѭ����ȡ
							while ((len = is.read(buffer)) != -1) {
								// ���ݶ�ȡ�ĳ���д�뵽os������
								message.write(buffer, 0, len);
							}
							// �ͷ���Դ
							is.close();
							message.close();
							// �����ַ���

							msg = new String(message.toByteArray());
							msg = String.copyValueOf(msg.toCharArray(), 0, msg.length());
							Pattern pattern = Pattern.compile("\\d+");
							Matcher matcher = pattern.matcher(msg);
							ArrayList<String> num = new ArrayList<String>();
							while (matcher.find()) {
								num.add(matcher.group(0));
							}
							String str = "��¼�ɹ���" + "���" + num.get(1) + "." + num.get(2) + "Ԫ" + "," + "��������"
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
				ToastOwn.Long("����ʧ�ܣ�������");
			}else if(result.equals("1")) {
				ToastOwn.Long("������CUG");
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
	              // ������ΰ���ʱ��������2000���룬���˳�  
	              Toast.makeText(this, "�����CUG�Զ���¼�Ļ�\n�밴HOME��Ŷ", Toast.LENGTH_LONG).show();  
	              mExitTime = System.currentTimeMillis();// ����mExitTime  
	          } else {  
	              System.exit(0);// �����˳�����  
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
