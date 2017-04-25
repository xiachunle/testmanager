package com.ks.tests.kscontroller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ks.tests.entries.SignParams;
import com.ks.tests.utils.FileUtils;
import com.ks.tests.utils.HttpUtils;



public class POSController {
	public static POSController getInstance() {
		return new POSController();
	}

	private HttpUtils httpUtils = new HttpUtils();
	private StoreController storeController = StoreController.getInstance();
	private AcquirerController acquirerController = AcquirerController.getInstance();

	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 增加时默认支持分期且分期阀为100,bin值默认为622716
	public String AddPos(SignParams params, String cookies) {
		String result = "";
		String storeResult = storeController.AddStore(params, cookies);
//		System.out.println(storeResult);
		JSONObject storeJson = JSON.parseObject(storeResult);
		if (Integer.parseInt(storeJson.getString("code")) != 0) {
			return storeJson.getString("message");
		} else {
			JSONObject json = storeJson.getJSONObject("data");
			try {
				String posStr = FileUtils.instance.readFromJson("pos.json");
				JSONObject posJson = JSON.parseObject(posStr);
				posJson.put("merchantId", json.getString("merchantId"));
				posJson.put("storeId", json.getString("id"));

				posJson.put("deviceSn", params.getDeviceSn());
				JSONObject  deviceJson = getModelName(params.getDeviceSn(), cookies);
				posJson.put("deviceModel", deviceJson.getString("deviceModel"));
				posJson.put("deviceImei", deviceJson.getString("deviceImei"));
				posJson.put("acquirerCode", acquirerController.getAcuireId(params.getBankAcquireName()));
				posJson.put("acquirerMerchantNumber", params.getBankAcquireNumber());
				posJson.put("acquirerTerminalNumber", params.getBankAcquireSn());

				result = httpUtils.doBackendPost("/v1/backend/storedevice/add", posJson.toJSONString(), cookies);
				if (!storeController.audit(cookies, json.getString("id"))) {
					return "门店审核失败，终端无法启用";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;
	}

	/**
	 * 根据商户和门店ID增加POS
	 * 
	 * @param params
	 * @param cookies
	 * @param mechatantId
	 * @param storeId
	 * @return
	 */
	public String AddPos(SignParams params, String cookies, String mechatantId, String storeId) {
		String result = "";
		try {
			String posStr = FileUtils.instance.readFromJson("pos.json");
			System.out.println("==============");
			System.out.println(posStr);
			System.out.println("==============");
//			JSONObject posJson = JSON.parseObject(URLEncoder.encode(posStr,"UTF-8"));
			org.json.JSONObject posJson=new org.json.JSONObject(posStr);
			posJson.put("merchantId", mechatantId);
			posJson.put("storeId", storeId);
			posJson.put("deviceSn", params.getDeviceSn());
			JSONObject  deviveModels = getModelName(params.getDeviceSn(), cookies);
			posJson.put("deviceModel", deviveModels.getString("deviceModel"));
			posJson.put("deviceImei", deviveModels.getString("deviceImei"));
			posJson.put("acquirerName", params.getBankAcquireName());
			posJson.put("acquirerId", acquirerController.getAcuireId(params.getBankAcquireName()));
			posJson.put("acquirerMerchantNumber", params.getBankAcquireNumber());
			posJson.put("acquirerTerminalNumber", params.getBankAcquireSn());
			System.out.println("Add POS 请求:"+posJson.toString());
			result = httpUtils.doBackendPost("/v1/backend/storedevice/add", posJson.toString(), cookies);
			System.out.println("已审核门店操作:"+result);
			if (!storeController.audit(cookies, storeId)) {
				return "门店审核失败，终端无法启用";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 根据设备号查询设备相关
	 * @param deviceSn
	 * @param cookies
	 * @return
	 */
	public JSONObject getModelName(String deviceSn, String cookies) {
		JSONObject json = new JSONObject();
		json.put("deviceSn", deviceSn);
		String result = httpUtils.doBackendPost("/v1/backend/storedevice/snModel", json.toJSONString(), cookies);
		System.out.println(result);
		JSONObject deviceAll = JSON.parseObject(result);
		if (Integer.parseInt(deviceAll.getString("code")) != 0) {
//			System.out.println("获取终端设备型号失败");
			return null;
		} else {
			JSONObject info = deviceAll.getJSONObject("data");

			return info;

		}

	}
	
	/**
	 * 获取带有编号的device信息
	 * @param deviceSn
	 * @param coookies
	 * @return
	 */
	public JSONObject getStoreDeivesList(String deviceSn,String cookies){
		JSONObject json=new JSONObject();
		json.put("deviceSn", deviceSn);
		String result = httpUtils.doBackendPost("/v1/backend/storedevice/list", json.toJSONString(), cookies);
		JSONObject deviceListInfos = JSON.parseObject(result);
//		System.out.println("====="+deviceListInfos);
		if (Integer.parseInt(deviceListInfos.getString("code")) != 0) {
//			System.out.println("获取的 device带有交易设置信息失败");
			return null;
		} else {
			JSONArray array= deviceListInfos.getJSONArray("dataList");
			if(array.size()==0){
				return null;
			}
			return array.getJSONObject(0);

		}
	}
	
	
	// 终端注册后查看信息
	public JSONObject getUsedPOSDetails(String deviceSn, String cookies) {

		JSONObject deviceJSON = getStoreDeivesList(deviceSn, cookies);

		JSONObject json = new JSONObject();

		json.put("id", deviceJSON.getString("id"));
		String result = httpUtils.doBackendPost("/v1/backend/storedevice/getOne", json.toJSONString(), cookies);
		JSONObject details = JSON.parseObject(result);
		if (Integer.parseInt(details.getString("code")) != 0) {
//			System.out.println("获取详情失败");
			return null;
		} else {
			JSONObject info = details.getJSONObject("data");
			return info;
		}
	}

	// 修改POS机属性
	public String mobile(String deviceSn, String cookies, SignParams params) {
		JSONObject detailsJSON = getUsedPOSDetails(deviceSn, cookies);
		if (detailsJSON != null) {
			detailsJSON.put("acquirerCode", params.getBankAcquireName());
			detailsJSON.put("acquirerMerchantNumber", params.getBankAcquireNumber());
			detailsJSON.put("acquirerTerminalNumber", params.getBankAcquireSn());
			detailsJSON.put("isStage", params.getIsStage());
			detailsJSON.put("cardBin", params.getCardBinNumber());
			detailsJSON.put("stageBudget", params.getStatgeNumber());
			detailsJSON.put("isMobileSupport", params.getIsMobileSupport());
			detailsJSON.put("mobileAlipayChannelCode", params.getAlPayAcquires());
			detailsJSON.put("mobileAlipayMerchantNo", params.getAlPayAcquireNumber());
			detailsJSON.put("mobileAlipayTerminalNo", params.getAlPayAcquireSn());
			detailsJSON.put("mobileWechatChannelCode", params.getWeChatAcquires());
			detailsJSON.put("mobileWechatTerminalNo", params.getWeChatAcquireSn());
			detailsJSON.put("mobileWechatMerchantNo", params.getWeChatAcquireNumber());
			detailsJSON.put("updateAt", sf.format(new Date()));
			String result = httpUtils.doBackendPost("/v1/backend/storedevice/edit", detailsJSON.toJSONString(),
					cookies);
			JSONObject details = JSON.parseObject(result);
			if (Integer.parseInt(details.getString("code")) != 0) {
//				System.out.println("修改失败");
				return details.getString("message");
			} else {
				return "Update Success";
			}
		} else {
			return "SN没有被增加到相应门店";
		}
	}

	// 门店下再增加一台设备
	public String AddAnotherDevice(SignParams params, String cookies) {
		String result = "";

		result = storeController.getStoreDetails(params, cookies);// 得到门店信息
		JSONObject deviceInfos = getModelName(params.getDeviceSn(), cookies);
		try {
			JSONObject json = JSON.parseObject(result);
			String posStr = FileUtils.instance.readFromJson("pos.json");
			JSONObject posJson = JSON.parseObject(posStr);
//			posJson.put("accountName", params.getMerchantConnectName());
			posJson.put("merchantId", json.getString("merchantId"));
			posJson.put("storeId", json.getString("id"));
			posJson.put("deviceSn", params.getDeviceSn());
			posJson.put("deviceModel", deviceInfos.getString("deviceModel"));
			posJson.put("deviceImei", deviceInfos.getString("deviceImei"));
			posJson.put("acquirerCode", acquirerController.getAcuireId(params.getBankAcquireName()));
			posJson.put("acquirerMerchantNumber", params.getBankAcquireNumber());
			posJson.put("acquirerTerminalNumber", params.getBankAcquireSn());
			result = httpUtils.doBackendPost("/v1/backend/storedevice/add", posJson.toJSONString(), cookies);
			if (!storeController.audit(cookies, json.getString("id"))) {
				return "门店审核失败，终端无法启用";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 撤机 根据 id撤机
	 */
	public String doDiscardPOS(SignParams params, String cookies) {
		// 根据sn获取编号
		String result = "";
		JSONObject jsonDevices = getStoreDeivesList(params.getDeviceSn(), cookies);
		if(jsonDevices==null){
			return "设备已经撤机";
		}
		JSONObject discardJSON = new JSONObject();
		discardJSON.put("id", jsonDevices.getString("id"));
		result = httpUtils.doBackendPost("/v1/backend/storedevice/discard", discardJSON.toJSONString(), cookies);

		return result;
	}
		
//	public static void main(String[] args) {
//		HttpUtils httpUtils=new HttpUtils();
//		SignParams params=new SignParams();
//		params.setUsername("夏春乐");
//		params.setPassword("123456");
//		String cookies=httpUtils.getCookies(params);
//		System.out.println(POSController.getInstance().getModelName("0820023544", cookies));
//	}
}
