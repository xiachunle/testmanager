package com.ks.tests.manager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.TestNG;




@Service("testmanager")
@Transactional
public class TestManager {
	private TestNG testNG;
	
	
	public TestManager(){
		testNG=new TestNG();
	}
	
	

	public void runTests(){
		testNG.setTestClasses(new Class[]{com.ks.tests.casers.SampleTest.class});
		
		testNG.run();
	}
	
	public static void main(String[] args) {
		TestManager testManager=new TestManager();
		testManager.runTests();
	}
	
	

//	
//	public static void main(String[] args) throws ClassNotFoundException {
//		TestManager testManager=new TestManager();
//		testManager.runTests();
//	}
//	
}
