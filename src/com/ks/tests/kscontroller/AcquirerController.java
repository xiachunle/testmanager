package com.ks.tests.kscontroller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ks.tests.utils.HttpUtils;



public class AcquirerController {
	public static AcquirerController getInstance() {
		return new AcquirerController();
	}
	
	private HttpUtils httpUtils=new HttpUtils();
	public JSONArray getBankAcquirer(){
		String result="";
		String url = "http://backend.test.kashuo.net/v1/backend/dropdown/acquirer";
		result=httpUtils.setUrl(url).setCharset("UTF-8").get();
		JSONObject json=JSON.parseObject(result);
		if(!json.getString("code").equals("0")){
//			System.out.println("获取银行收单机构失败,请检查网络通信");
			return null;
		}else{
			JSONArray dataArray=json.getJSONArray("dataList");
			return dataArray;
		}
	
	}
	//根据acuireName获取相应的acquireCode
	public String getAcuireId(String acquireName){
		JSONArray array =getBankAcquirer();
		String result="";
		for (int i = 0; i < array.size(); i++) {
			JSONObject info=array.getJSONObject(i);
//			System.out.println(info.getString("key")+"--"+info.getString("label")+"--"+info.getString("desc"));
			if(info.getString("label").equals(acquireName)){
				result=info.getString("key");
				break;
			}else{
				result="无效收单机构";
			}
		}
//		System.out.println("解析出的名称key为:"+result );
		return result;
	}
	//移动端收单通道
	public JSONArray getChannelList(){
		String url = "http://backend.test.kashuo.net/v1/backend/channel/list";
		String result=httpUtils.setUrl(url).setCharset("UTF-8").get();
		JSONObject json=JSON.parseObject(result);
		if(!json.getString("code").equals("0")){
//			System.out.println("获取移动端收单机构失败,请检查网络通信");
			return null;
		}else{
			JSONArray dataArray=json.getJSONArray("dataList");
			return dataArray;
		}
		
	
	}
}
