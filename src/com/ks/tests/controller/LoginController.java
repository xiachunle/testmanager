package com.ks.tests.controller;


import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.ks.tests.utils.HttpUtils;

@Controller
public class LoginController {
	private HttpUtils httpUtils=new HttpUtils();
	@RequestMapping("login")
	public String login(@RequestParam(value="username",required=true) String username,
			@RequestParam(value="password",required=true)   String password){
		JSONObject loginJSON=new JSONObject();
		loginJSON.put("login_name",username);
		loginJSON.put("password",password);
		String result=httpUtils.doAuthPost("/auth/login", loginJSON.toString(), false);
//		System.out.println(result);
		String response =JSON.parseObject(result).getString("code");
		if(response.equals("200")){
			return "/WEB-INF/views/systemlist.jsp";
		}else{
			return "/WEB-INF/views/failure.jsp";
		}
	}

}
