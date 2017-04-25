package com.ks.tests.kscontroller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ks.tests.entries.SignParams;
import com.ks.tests.utils.FileUtils;
import com.ks.tests.utils.HttpUtils;



public class MerchantController {
	private SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
	private SimpleDateFormat sfSecond = new SimpleDateFormat("HH时mm分SSSS");
	Date date = new Date();
	private String merChantName = sf.format(date) + "POS终端测试在" + sfSecond.format(date);

	public static MerchantController getInstance() {
		return new MerchantController();
	}

	private HttpUtils httpUtils = new HttpUtils();

	public String InsertTradingArea(SignParams params, String cookies) {
		String result = "";

		try {
			String mechantStr = FileUtils.instance.readFromJson("mechant.json");
			
			JSONObject mechantJson = JSON.parseObject(mechantStr);
			// 修改文件
			mechantJson.put("name", merChantName);
			mechantJson.put("shortSelling", merChantName);
			mechantJson.put("contactPerson",params.getMerchantConnectName());
			mechantJson.put("phoneNumber",params.getMerChantConnectPhone());


			result = httpUtils.doBackendPost("/v1/backend/merchant/insertTradingArea", mechantJson.toJSONString(),
					cookies);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	
	//根据ID获取商户信息
	public String  getMerInfoById(String id,String cookies){
		String result="";
		String url = "http://backend.test.kashuo.net/v1/backend/dropdown/acquirer";
		result=httpUtils.setUrl(url).setParam("id", id).setCharset("UTF-8").get();
		
		return result;
	}

}
