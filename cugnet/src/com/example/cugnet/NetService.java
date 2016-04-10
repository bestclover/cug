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
					 * ����Ҫ��URL�µ�URLConnection�Ի��� URLConnection���Ժ����׵Ĵ�URL�õ������磺
					 * // Using java.net.URL and //java.net.URLConnection
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
					 * ���Ϊ�˵õ�OutputStream�������������Լ����Writer���ҷ���POST��Ϣ�У����磺 ...
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
						String str = "��¼�ɹ���" + "���" + num.get(1) + "." + num.get(2) + "Ԫ" + "," + "��������" + num.get(3)
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
				ToastOwn.Long("�Բ�������ʧ�ܣ����ֶ���¼");
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
