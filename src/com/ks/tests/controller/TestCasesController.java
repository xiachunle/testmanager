package com.ks.tests.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jboss.logging.Logger;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ks.tests.dao.CasesDao;
import com.ks.tests.dao.ConfigDao;
import com.ks.tests.entries.DeviceInfo;
import com.ks.tests.entries.TestCases;

@Controller
public class TestCasesController {
	protected static Logger logger = Logger.getLogger("TestCasesController");

	@Resource(name = "testcase")
	private CasesDao casesDao;
	@Resource(name = "config")
	private ConfigDao configDao;

	@RequestMapping(value = "/casers",method=RequestMethod.GET)
	public String list(Map<String, Object> map) {
		logger.debug("Recevice  request to list ");
		List<TestCases> lists = casesDao.queryAll();
		List<DeviceInfo> deviceLists=configDao.getAllDeviceInfos();
		List<String> snLists=new ArrayList<String>();
		for (DeviceInfo deviceInfo : deviceLists) {
			snLists.add(deviceInfo.getDeviceSn());
		}
		map.put("deviceInfo", snLists);
		map.put("testcase", lists);
		return "/WEB-INF/views/poslist.jsp";
	}

	@RequestMapping(value = "/caser", method = RequestMethod.GET)
	public String input(Map<String, Object> map) {
		logger.debug("Receiver add request");
		map.put("testcase", new TestCases());
		return "/WEB-INF/views/input.jsp";
	}

	@RequestMapping(value = "/caser", method = RequestMethod.POST)
	public String save(TestCases caser) {
		try {
			logger.debug(caser.toString());
			casesDao.addTest(caser);
			
		} catch (Exception e) {
			return "redirect:/caser";
		}

		return "redirect:/casers";
	}

	@RequestMapping(value = "/caser/delete/{caseId}", method = RequestMethod.GET)
	public String delete(@PathVariable("caseId") Integer caseId) {
		System.out.println("Delete 请求:" + caseId);
		casesDao.deleterTest(caseId);
		return "redirect:/casers";
	}

	@RequestMapping(value = "/caser/{caseId}", method = RequestMethod.GET)
	public String modify(@PathVariable("caseId") int caseId, Map<String, Object> map) {

		map.put("testcase", casesDao.getTest(caseId));
		return "/WEB-INF/views/edit.jsp";
	}

	@RequestMapping(value = "/caser/edit", method = RequestMethod.POST)
	public String update(TestCases caser) {

		casesDao.updateTest(caser);
		return "redirect:/casers";
	}
}
