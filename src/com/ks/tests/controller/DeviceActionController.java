package com.ks.tests.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ks.tests.dao.ConfigDao;
import com.ks.tests.entries.DeviceInfo;
import com.ks.tests.manager.TestManager;
import com.ks.tests.utils.FileUtils;

@Controller
public class DeviceActionController {

	@Resource(name = "testmanager")
	private TestManager testManager;

	@Resource
	private ConfigDao configDao;

	@RequestMapping(value = "/action", method = RequestMethod.GET)
	public ModelAndView doPOSAction() {
		testManager.runTests();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("report.html");
		return modelAndView;
	}

	@RequestMapping(value = "/appium", method = RequestMethod.GET)
	public String startServer() {
		String appiumStr = "appium -p 52025 --log-level debug --log-timestamp --command-timeout 600000 ";
		FileUtils.instance.execCMD(appiumStr);
		return "redirect:/casers";
	}

	
	
	@RequestMapping(value = "/card", method = RequestMethod.GET)
	public String card() {

		return "redirect:/casers";
	}

	@RequestMapping(value = "/bank", method = RequestMethod.GET)
	public String bank() {

		return "redirect:/casers";
	}

	@RequestMapping(value = "/store", method = RequestMethod.GET)
	public String store() {

		return "redirect:/casers";
	}

	@RequestMapping(value = "/member", method = RequestMethod.GET)
	public String member() {

		return "redirect:/casers";
	}

	@RequestMapping(value = "/revoke", method = RequestMethod.GET)
	public String revoke() {

		return "redirect:/casers";
	}

}
