package com.ks.tests.utils;

import java.io.File;
import java.io.InputStream;

import java.nio.charset.Charset;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;
import com.ks.tests.entries.SignParams;


public class HttpUtils {
	private String charset = "UTF-8";// 默认编码
	private List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); // get请求参数
	private String url;
	private HttpClient httpClient;

	public HttpUtils setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public HttpUtils setParam(String name, String value) {
		this.params.add(new BasicNameValuePair(name, value));
		return this;
	}

	public HttpUtils setParam(Map<String, String> params) {
		for (Map.Entry<String, String> entry : params.entrySet()) {
			this.params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return this;
	}

	public HttpUtils() {
		httpClient = HttpClientBuilder.create().build();
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public HttpUtils setUrl(String url) {
		this.url = url;
		return this;
	}

	// get请求
	public String get() {
		if (params != null) {
			url = url + "?" + URLEncodedUtils.format(this.params, this.charset);
		}
		System.out.println(url);
		HttpGet httpGet = new HttpGet(url);
		try {

			HttpResponse httpResponse = this.getHttpClient().execute(httpGet);
			System.out.println(httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				return convertStreamToString(httpEntity.getContent(), this.charset);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public String get(String cookies) {
		if (params != null) {
			url = url + "?" + URLEncodedUtils.format(this.params, this.charset);
		}
		System.out.println(url);
		HttpGet httpGet = new HttpGet(url);
		try {
			httpGet.setHeader("Cookie", cookies);
			HttpResponse httpResponse = this.getHttpClient().execute(httpGet);
			System.out.println(httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				return convertStreamToString(httpEntity.getContent(), this.charset);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 权限系统接口 POST
	 * 
	 * @param method
	 *            具体方法名
	 * @param json
	 *            json数据或者解析json文件后的字符串
	 * @return
	 */
	public String doAuthPost(String method, String json, boolean isLoginCookie) {
		String cookies = "";
		url = "http://auth2.test.kashuo.net" + method;
		try {
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/json;charset=UTF-8");
			post.setHeader("Accept", "application/json");
			post.setHeader("Referer", "http://auth2.test.kashuo.net/auth/swagger-ui.html");
			StringEntity entity = new StringEntity(json, Charset.forName("UTF-8"));
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			Header[] allHeaders = response.getAllHeaders();
			String temp = "";

			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity httpEntity = response.getEntity();
				if (cookies.equals("")) {
					for (Header header : allHeaders) {
						if (header.getName().equals("Set-Cookie")) {
							temp += header.getValue().substring(0, header.getValue().indexOf(";") > -1
									? header.getValue().indexOf(";") : header.getValue().length() - 1) + ";";

						}
					}
					cookies = temp;
				}
				if (isLoginCookie) {
					return cookies;
				} else {
					return convertStreamToString(httpEntity.getContent(), "UTF-8");
				}
			} else {
				return response.toString();
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 大后台POST
	 * 
	 * @param method
	 *            具体方法名
	 * @param jsonFile
	 *            json数据或者解析json文件后的字符串
	 * @return
	 */
	public String doBackendPost(String method, String jsonData, String cookies) {
		try {
			url = "http://backend.test.kashuo.net" + method;
			// System.out.println("==="+url);
			HttpPost post = new HttpPost(url);
			// 构造消息头
			post.setHeader("Content-Type", "application/json;charset=UTF-8");
			post.setHeader("Accept", "application/json");
			post.setHeader("Referer", "http://backend.test.kashuo.net/swagger-ui.html");
			if (!cookies.equals("")) {
				post.setHeader("Cookie", cookies);
			} else {
				return "登陆大后台失败";
			}
			// 构建消息实体
			StringEntity entity = new StringEntity(jsonData, Charset.forName("UTF-8"));
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == 200) {
				HttpEntity httpEntity = response.getEntity();
				return convertStreamToString(httpEntity.getContent(), "UTF-8");
			} else {
				return "请求返回" + statusCode;
			}
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	// 解析请求返回结果
	private String convertStreamToString(InputStream is, String charset) {
		StringBuilder sb = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;
		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, charset);
				sb.append(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 开放平台POST请求
	 * 
	 * @param params
	 * @return
	 */
	public String doOpenPost(List<NameValuePair> params) {
		try {
			url = "http://posopen.test.kashuo.net/terminal.aspx";

			HttpPost post = new HttpPost(url);
			// 构造消息头
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			// 构建消息实体
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			// entity.setContentEncoding("UTF-8");
			// entity.setContentType("application/x-www-form-urlencoded");
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("=====" + statusCode + "=====");
			if (statusCode == 200) {
				HttpEntity httpEntity = response.getEntity();
				return convertStreamToString(httpEntity.getContent(), "UTF-8");
			} else {
				return "请求返回" + statusCode;
			}
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	/**
	 * 获取登陆cookie
	 * 
	 * @param login_name
	 * @param password
	 * @return
	 */
	public String getCookies(SignParams params) {
		HttpUtils httpUtils = new HttpUtils();
		JSONObject json = new JSONObject();
		try {
			json.put("login_name", params.getUsername());
			json.put("password", params.getPassword());
			json.put("sys_id", "0");
			return httpUtils.doAuthPost("/auth/login", json.toJSONString(), true);
		} catch (Exception e) {
			e.getMessage();
		}
		return "";
	}

	public String uploadFile(String url, File file) {
		try {
			url=url+"/testmanager/upload";
			System.out.println("上传地址");
			HttpPost postMethod = new HttpPost(url);

			FileBody bin = new FileBody(file);

			HttpEntity entity = MultipartEntityBuilder.create().addPart("file", bin).build();

			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000)
					.build();// 设置请求和传输超时时间
			postMethod.setConfig(requestConfig);
			postMethod.setEntity(entity);
			HttpResponse response = httpClient.execute(postMethod);
			// 检验返回码
			return String.valueOf(response.getStatusLine().getStatusCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	//
	// public static void main(String[] args) {
	// String biz_content = "{"
	// +
	// "\"total_amount\":\"1\",\"trans_channel\":\"KS\",\"device_sn\":\"0820023544\",\"acq_channel\":\"HSHI\","
	// +
	// "\"card_enc\":\"26201cd88a7ac7bdaeccac5d73e4a0036a92d921a0a9054a49612f7f0ae27eb2\","
	// + "\"card_no\":\"62170020****3688\","
	// + "\"discount_amount\":\"1\","
	// + "\"payment_type\":\"BANK_QK\"}";
	// String app_id="06953ff5-f450-40ed-becb-1624d3e56f50";
	// String key="24797d82-8772-4e20-8db9-dabd67eac01d";
	// String charset="UTF-8";
	// String method="trans_query";
	// String sign_type="MD5";
	// String timestamp=new SimpleDateFormat("yyyyMMddHHmmSS").format(new
	// Date());
	// String verson="1.00.09";
	// String beforeSign="app_id="+app_id+"&biz_content="+biz_content+
	// "&charset="+charset+"&method="+method+"&sign_type="+sign_type+"&timestamp="+timestamp+"&version="+verson+
	// "&secret_key="+key;
	//
	// String sign=EntryUtils.instance.encryptMD5(beforeSign);
	//
	//
	// List<NameValuePair> lists=new ArrayList<NameValuePair>();
	// lists.add(new BasicNameValuePair("app_id", app_id));
	// lists.add(new BasicNameValuePair("biz_content", biz_content));
	// lists.add(new BasicNameValuePair("charset", charset));
	// lists.add(new BasicNameValuePair("method", method));
	// lists.add(new BasicNameValuePair("sign", sign));
	// lists.add(new BasicNameValuePair("sign_type", sign_type));
	// lists.add(new BasicNameValuePair("timestamp", timestamp));
	// lists.add(new BasicNameValuePair("version", verson));
	//
	//
	// System.out.println(lists.toString());
	// HttpUtils httpUtils=new HttpUtils();
	// System.out.println(httpUtils.doOpenPost(lists));
	// }
}
