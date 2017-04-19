package com.ks.tests.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;



public class GenerateReporter implements IReporter {
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		// TODO Auto-generated method stub
		try {
			// 初始化并取得Velocity引擎
			VelocityEngine ve = new VelocityEngine();
			Properties p = new Properties();
			
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
			p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
			p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
			ve.init(p);
			Template t = ve.getTemplate("com/ks/tests/overview.vm");
			t.setEncoding("UTF-8");
			VelocityContext context = new VelocityContext();

			for (ISuite suite : suites) {
				Map<String, ISuiteResult> suiteResults = suite.getResults();
				for (ISuiteResult suiteResult : suiteResults.values()) {
					ReporterData data = new ReporterData();
					ITestContext testContext = suiteResult.getTestContext();
					// 把数据填入上下文
					context.put("overView", data.testContext(testContext));// 测试结果汇总信息
					IResultMap passedTests = testContext.getPassedTests();// 测试通过的测试方法
					IResultMap failedTests = testContext.getFailedTests();// 测试失败的测试方法
					IResultMap skippedTests = testContext.getSkippedTests();// 测试跳过的测试方法
					
					context.put("pass", data.testResults(passedTests, ITestResult.SUCCESS));
					context.put("fail", data.testResults(failedTests, ITestResult.FAILURE));
					context.put("skip", data.testResults(skippedTests, ITestResult.SKIP));

				}
			}
			
			//制定位置输出测试报告  
			URL url=GenerateReporter.class.getProtectionDomain().getCodeSource().getLocation();
			String filePath=URLDecoder.decode(url.getPath(),"UTF-8");
			if(filePath.endsWith(".jar")){
				filePath=filePath.substring(0, filePath.lastIndexOf("/")+1);
			}
			File file=new File(filePath);
			String path = System.getProperty("evan.webapp");
			File reportFile=new File(System.getProperty("evan.webapp")+"report.html");
			System.out.println("测试报告路径"+reportFile.getAbsolutePath());
			OutputStream out = new FileOutputStream(reportFile.getAbsolutePath());
			Writer writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));// 解决乱码问题
			// 转换输出
			t.merge(context, writer);
			writer.flush();
//			if(reportFile.exists()){
//				HttpUtils httpUtils=new HttpUtils();
//				httpUtils.uploadFile(ToolSwing.RESULTURL, reportFile);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
