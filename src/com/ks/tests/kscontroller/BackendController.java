package com.ks.tests.kscontroller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ks.tests.utils.HttpUtils;


public class BackendController {
	public static BackendController getInstance() {
		return new BackendController();
	}

	private HttpUtils httpUtils = new HttpUtils();

	/**
	 * 普通交易查询
	 * 
	 * @param deviceSn
	 * @param cookies
	 * @return
	 */
	public String doNormalQuery(String deviceSn, String method, String cookies) {
		JSONObject json = new JSONObject();
		json.put("deviceSn", deviceSn);
		json.put("method", method);
		json.put("pageIndex", 1);
		return httpUtils.doBackendPost("/v1/backend/realtime/list", json.toJSONString(), cookies);
	}

	public String getTimeStamp(String sn, String method, String cookies) {
		String result = doNormalQuery(sn, method, cookies);
		JSONArray data = JSON.parseObject(result).getJSONArray("dataList");
		String request = data.getJSONObject(0).getString("requestContent");
		return JSON.parseObject(request).getString("timestamp");
	}

	public int getPayMount(String sn, String method, String cookies) {
		String result = doNormalQuery(sn, method, cookies);
		JSONArray data = JSON.parseObject(result).getJSONArray("dataList");

		String request = data.getJSONObject(0).getString("requestContent");
		String re = JSON.parseObject(request).getString("biz_content");
		return Integer.parseInt(JSON.parseObject(re).getString("pay_amount"));
	}

	public Double getPayDisMount(String sn, String method, String cookies) {
		String result = doNormalQuery(sn, method, cookies);
		// System.out.println(result);
		JSONArray data = JSON.parseObject(result).getJSONArray("dataList");

		String respone = data.getJSONObject(0).getString("responseContent");

		String re = JSON.parseObject(respone).getJSONObject("trans_query_response").getString("rec_remark");
		return Double.parseDouble((re.split("实付金额:           ")[1].split("元")[0]));
	}

//	public static void main(String[] args) {
//		SignParams params = new SignParams();
//		params.setUsername("夏春乐");
//		params.setPassword("123456");
//
//		System.out.println(Float.parseFloat(BackendController.getInstance().getPayMount("0820023544", "normal_trans",
//				BackendController.getInstance().httpUtils.getCookies(params))*0.01+""));
//	}
}
