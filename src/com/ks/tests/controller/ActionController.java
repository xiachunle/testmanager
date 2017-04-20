package com.ks.tests.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ks.tests.manager.TestManager;
import com.ks.tests.utils.FileUtils;

@Controller
public class ActionController {
	
	@Resource(name="testmanager")
	private TestManager testManager;
	
	@RequestMapping(value="/action",method=RequestMethod.GET)
	public ModelAndView  doPOSAction(){
		testManager.runTests();
		 ModelAndView modelAndView = new ModelAndView();  
		 modelAndView.setViewName("report.html");
		return modelAndView;
	}
	
	@RequestMapping(value="/appium",method=RequestMethod.GET)
	public String startServer(){
		String  appiumStr="appium -p 52025 --log-level debug --log-timestamp --command-timeout 600000 ";
		FileUtils.instance.execCMD(appiumStr);
		return "redirect:/casers";
	}
}
