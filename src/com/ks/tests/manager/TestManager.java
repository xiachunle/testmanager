package com.ks.tests.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

@Service("testmanager")
@Transactional
public class TestManager {
	private TestNG testNG;

	public TestManager() {
		testNG = new TestNG();
	}

	public void runTests(String sn) {
		System.out.println("执行测试用例");
		
		
		List<String> suites=new ArrayList<String>();
		suites.add("D:\\workpace\\webService\\testmanager\\src\\com\\ks\\tests\\ testng.xml");
		testNG.setTestSuites(suites);		
		testNG.run();
	}

	//
	// public static void main(String[] args) throws ClassNotFoundException {
	// TestManager testManager=new TestManager();
	// testManager.runTests();
	// }
	//
}
