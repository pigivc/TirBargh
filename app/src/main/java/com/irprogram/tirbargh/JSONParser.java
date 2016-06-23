/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.irprogram.tirbargh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class JSONParser {

	InputStream is = null;
	JSONObject jObj = null;
	JSONArray jArray = null;
	String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	public String getJSONStringFromUrl(String url, List<NameValuePair> params) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object

		// return JSON String
		return json;

	}

	public JSONArray getJSONArrayFromUrl(String url, JSONArray jArray) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			StringEntity strEntity = new StringEntity(jArray.toString());
			httpPost.setEntity(strEntity);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			return new JSONArray(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return null;

	}

	public String getJsonStringFromUrl(String url) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			
			
			/*String[] keyValueSets = CookieManager.getInstance()
					.getCookie("172.30.12.19").split(";");
			for (String cookie : keyValueSets) {
				String[] keyValue = cookie.split("=");
				String key = keyValue[0];
				String value = "";
				if (keyValue.length > 1)
					value = keyValue[1];
				client.getCookieStore().addCookie(
						new BasicClientCookie(key, value));
			}*/

			// connect
			HttpResponse response = client.execute(httpget);

			// get response
			HttpEntity entity = response.getEntity();

			if (entity == null) {
				// msg = "No response from server";
				return null;
			}

			// get response content and convert it to json string
			is = entity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object

		// return JSON String
		return json;

	}
	
	public String getJsonStringFromUrlWithAuth(String url,Context context) throws Exception {

		// Making HTTP request
		try {
			// defaultHttpClient
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			
			DefaultHttpClient client = new DefaultHttpClient(httpParams);
			HttpGet httpget = new HttpGet(url);
			
			/*if(!Authentication(context, httpget))
				return GlobalVariables.NO_CREDENTIALS;*/

			// connect
			HttpResponse response = client.execute(httpget);

			// get response
			HttpEntity entity = response.getEntity();

			if (entity == null) {
				// msg = "No response from server";
				return null;
			}
			// get response content and convert it to json string
			is = entity.getContent();
		}catch(SocketTimeoutException e)
		{
			e.printStackTrace();
			throw new Exception("Connection failure");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new Exception("Connection failure");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new Exception("Connection failure");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Connection failure");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Connection failure");
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object

		// return JSON String
		return json;

	}
	
	
	public String getJsonStringUTF8FromUrlWithAuth(String url,Context context) throws Exception {

		// Making HTTP request
		try {
			// defaultHttpClient
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			
			DefaultHttpClient client = new DefaultHttpClient(httpParams);
			HttpGet httpget = new HttpGet(url);
			
			/*if(!Authentication(context, httpget))
				return GlobalVariables.NO_CREDENTIALS;*/

			// connect
			HttpResponse response = client.execute(httpget);

			// get response
			HttpEntity entity = response.getEntity();

			if (entity == null) {
				// msg = "No response from server";
				return null;
			}


			return EntityUtils.toString(entity, HTTP.UTF_8);
			// get response content and convert it to json string
		}catch(SocketTimeoutException e)
		{
			e.printStackTrace();
			throw new Exception("Connection failure");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new Exception("Connection failure");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new Exception("Connection failure");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Connection failure");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Connection failure");
		}

		

		// try parse the string to a JSON object

		// return JSON String
		

	}

	public String login(String url, Context context) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);

			
			// connect
			HttpResponse response = client.execute(httpget);

			// get response
			HttpEntity entity = response.getEntity();

		/*	List<Cookie> cookies = client.getCookieStore().getCookies();
			try {
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						String cookieString = cookie.getName() + "="
								+ cookie.getValue() + "; domain="
								+ cookie.getDomain();
						CookieSyncManager.createInstance(context);
						CookieManager.getInstance().setCookie(
								cookie.getDomain(), cookieString);

					}
					CookieSyncManager.createInstance(context);
					CookieSyncManager.getInstance().sync();
				}

			} catch (Exception e) {
				// TODO: handle exception
				Exception ee = e;
			}*/
			if (entity == null) {
				// msg = "No response from server";
				return null;
			}

			// get response content and convert it to json string
			is = entity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object

		// return JSON String
		return json;

	}

	public boolean Authentication(Context context, HttpGet httpget) {
		/*SharedPreferences pref = context.getSharedPreferences(
				GlobalVariables.AppSharedPreferenced, context.MODE_PRIVATE);
		String username = pref.getString("username", null);
		String password = pref.getString("password", null);
		if (Utility.IsNullOrEmpty(username) || Utility.IsNullOrEmpty(password) )
			return false;
		String B64Auth = getB64Auth(username, password);
		httpget.setHeader("Authorization", B64Auth);*/
		return true;
	}

	private String getB64Auth(String login, String pass) {
		String source = login + ":" + pass;
		String ret = "Basic "
				+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE
						| Base64.NO_WRAP);
		return ret;
	}
}
