package com.ks.tests.manager;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;


import org.testng.IResultMap;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;






public class ReporterData {
	// 测试结果Set<ITestResult>转为list，再按执行时间排序 ，返回list
	public List<ITestResult> sortByTime(Set<ITestResult> str) {
		List<ITestResult> list = new ArrayList<ITestResult>();
		for (ITestResult r : str) {
			list.add(r);
		}
		Collections.sort(list);
		return list;


	}
	
	public DataBean testContext(ITestContext context) {
		// 测试结果汇总数据
		DataBean data = new DataBean();
		ReportUnits units = new ReportUnits();
		IResultMap passedTests = context.getPassedTests();
		IResultMap failedTests= context.getFailedTests();
		IResultMap skipedTests = context.getSkippedTests();	
		//全部测试周期方法，包括beforetest,beforeclass,beforemethod,aftertest,afterclass,aftermethod
		//IResultMap passedConfigurations =context.getPassedConfigurations(); 
		//IResultMap failedConfigurations =context.getFailedConfigurations();
		//IResultMap skipedConfigurations =context.getSkippedConfigurations();
		Collection<ITestNGMethod> excludeTests = context.getExcludedMethods();		
		
		int passedTestsSize = passedTests.size();
		int failedTestsSize = failedTests.size();
		int skipedTestsSize = skipedTests.size();
		int excludeTestsSize = excludeTests.size();
		//所有测试结果的数量＝测试pass+fail+skip的和，因为数据驱动一个测试方法有多次执行的可能，导致方法总数并不等于测试总数
		int allTestsSize= passedTestsSize+failedTestsSize+skipedTestsSize;
		data.setAllTestsSize(allTestsSize);
		data.setPassedTestsSize(passedTestsSize);
		data.setFailedTestsSize(failedTestsSize);
		data.setSkippedTestsSize(skipedTestsSize);
		data.setExcludeTestsSize(excludeTestsSize);
		data.setTestsTime(units.getTestDuration(context));		
		data.setPassPercent(units.formatPercentage(passedTestsSize, allTestsSize));		
		data.setAllTestsMethod(context.getAllTestMethods());
		data.setExcludeTestsMethod(context.getExcludedMethods());
	
		return data;


	}


	public List<DataBean> testResults(IResultMap map, int status) {
		// 测试结果详细数据
		List<DataBean> list = new ArrayList<DataBean>();
		ReportUnits units = new ReportUnits();
		map.getAllResults().size();
		if(map.getAllResults().size()==0){
			DataBean data = new DataBean();	
			data.setCaseId(0);
			data.setCaseName("nothing");
			data.setCaseExpect("nothing");
			data.setThrowable(new Throwable("nothing"));
			data.setCaseTester("nothing");
			list.add(data);
		}
		for (ITestResult result : sortByTime(map.getAllResults())) {
			String param=units.getParams(result);//测试参数
			DataBean data = new DataBean();
			data.setTestName(result.getName());
			data.setClassName(result.getTestClass().getName());
			data.setDuration(units.formatDuration(result.getEndMillis()
					- result.getStartMillis()));
			data.setParams(param);
			data.setDescription(result.getMethod().getDescription());
			data.setOutput(Reporter.getOutput(result));
			data.setDependMethod(units.getDependMethods(result));
			data.setThrowable(result.getThrowable());
			if (result.getThrowable() != null) {
				data.setStackTrace(result.getThrowable().getStackTrace());
			}
			String [] array=param.split(";");
			for (String str : array) {
				if(str.contains("caseId")){
					data.setCaseId(Integer.parseInt(str.split("=")[1]));
				}else if(str.contains("caseName")){
					data.setCaseName(str.split("=")[1]);
				}else if(str.contains("caseExpect")){
					data.setCaseExpect(str.split("=")[1]);
				}else if(str.contains("caseTester")){
					data.setCaseTester(str.split("=")[1]);
				}
			}
		
			
			list.add(data);
		}
		return list;
	}
	
//	public List<TestCases> testResultsBySQL(IResultMap map, int status){
//		List<TestCases> lists = DBUtils.instance.getAllTestCase();	
//		return lists;
//	}

}