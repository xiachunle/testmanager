package com.ks.tests.kscontroller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ks.tests.entries.SignParams;
import com.ks.tests.utils.FileUtils;
import com.ks.tests.utils.HttpUtils;



public class StoreController {
	private SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
	private SimpleDateFormat sfSecond = new SimpleDateFormat("HH时mm分");
	Date date = new Date();
	private String storeName = sf.format(date) + "POS终端门店" + sfSecond.format(date);

	public static StoreController getInstance() {
		return new StoreController();
	}

	private HttpUtils httpUtils = new HttpUtils();

	public String AddStore(SignParams params, String cookies) {
		String result = "";
		String mechantResult = MerchantController.getInstance().InsertTradingArea(params, cookies);
		JSONObject jsonMechant = JSON.parseObject(mechantResult);
		if (Integer.parseInt(jsonMechant.getString("code")) != 0) {
			return jsonMechant.getString("message");
		} else {
			JSONObject json = jsonMechant.getJSONObject("data");
			try {
				// store
				String storeStr = FileUtils.instance.readFromJson("store.json");
				JSONObject storeJson = JSON.parseObject(storeStr);
				// 修改文件数据
				storeJson.put("merchantId", json.getString("id"));
				storeJson.put("name", storeName);
				storeJson.put("salesPhone", params.getStoreSalerPhone());
				storeJson.put("salesContact", getSaleName(params.getStoreSalerPhone()));
				// System.out.println(storeJson.toJSONString());
				result = httpUtils.doBackendPost("/v1/backend/store/insert", storeJson.toJSONString(), cookies);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return result;
	}

	/**
	 * 根据商户Id增加门店
	 * 
	 * @param params
	 * @param cookies
	 * @param merchantId
	 * @return
	 */
	public String AddStore(SignParams params, String cookies, String merchantId) {
		String result = "";

		try {
			// store
			String storeStr = FileUtils.instance.readFromJson("store.json");
			JSONObject storeJson = JSON.parseObject(storeStr);
			// 修改文件数据
			storeJson.put("merchantId", merchantId);
			storeJson.put("name", storeName);
			storeJson.put("salesPhone", params.getStoreSalerPhone());
			storeJson.put("salesContact", getSaleName(params.getStoreSalerPhone()));
			// System.out.println(storeJson.toJSONString());
			result = httpUtils.doBackendPost("/v1/backend/store/insert", storeJson.toJSONString(), cookies);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public String getSaleName(String phoneNumber) {
		String result = "";
		String url = "http://backend.test.kashuo.net/v1/backend/store/getSaleNameAndChannel";
		result = httpUtils.setUrl(url).setCharset("UTF-8").setParam("mobilePhone", phoneNumber).get();
		JSONObject json = JSON.parseObject(result);
		if (Integer.parseInt(json.getString("code")) != 0) {
			result = "市场人员手机号无法识别";
		} else {
			JSONObject nameInfo = json.getJSONObject("data");
			result = nameInfo.getString("salesName");
		}
		// System.out.println("-------"+result);
		return result;
	}

	// 审核门店
	public boolean audit(String cookies, String id) {
		String result = "";
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("auditOpinion", "通过");
		result = httpUtils.doBackendPost("/v1/backend/store/audit", json.toJSONString(), cookies);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getString("code").equals("0")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 门店名称获取门店详细信息
	 * 
	 * @param params
	 * @param cookies
	 * @return
	 */
	public String getStoreDetails(SignParams params, String cookies) {
		String result = "";
		JSONObject json = new JSONObject();
		// json.put("merchantShortName", params.getStoreShorName());
		json.put("status", 1);
		json.put("storeName", params.getStoreName());
		result = httpUtils.doBackendPost("/v1/backend/store/list", json.toJSONString(), cookies);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getString("code").equals("0")) {
			JSONArray dataList = resultJson.getJSONArray("dataList");
			if (dataList.size() == 0) {
				result = "已审核" + "状态" + params.getStoreName() + "查无此门店";
			} else {
//				for (int i = 0; i < dataList.size(); i++) {
//					JSONObject info = dataList.getJSONObject(i);
//					if (info.getString("name").equals(params.getStoreName())) {
//						result = info.toJSONString();
//					}
//				}
				result=dataList.getJSONObject(0).toJSONString();
			}

		} else {
			result = "门店不存在或者审核状态不对";
		}

		System.out.println("result:" + result);
		return result;
	}
//
//	public static void main(String[] args) {
//		HttpUtils httpUtils = new HttpUtils();
//		SignParams params = new SignParams();
//		params.setUsername("夏春乐");
//		params.setPassword("123456");
//		String cookies = httpUtils.getCookies(params);
//		params.setStoreName("华势1");
//		StoreController.getInstance().getStoreDetails(params, cookies);
//	}
}
