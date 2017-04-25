package com.ks.tests.casers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import com.alibaba.fastjson.JSONObject;
import com.ks.tests.appium.AppiumUtils;
import com.ks.tests.appium.POSAction;
import com.ks.tests.dao.CasesDao;
import com.ks.tests.entries.SignParams;
import com.ks.tests.entries.TestCases;
import com.ks.tests.kscontroller.BackendController;
import com.ks.tests.kscontroller.POSController;
import com.ks.tests.utils.HttpUtils;
import com.ks.tests.utils.SpringBeanFactoryUtils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({ com.ks.tests.manager.GenerateReporter.class })
public class CommandTests {
	private AppiumUtils actionUtils;
	private HttpUtils httpUtils;
	private String cookies;
	private POSController posController;
	private BackendController backendController;
	private WebDriverWait wait;
	private SimpleDateFormat sf;

	private String deviceSn;
	private List<TestCases> allCases;
	
	private CasesDao caseDao=(CasesDao) SpringBeanFactoryUtils.getBean("testcase");
	/**
	 * 正常刷卡测试用例数据源
	 * 
	 * @return
	 */
	@DataProvider(name = "cardDB")
	public Iterator<Object[]> createCardStageData() {
		Set<Object[]> set = new HashSet<Object[]>();
		for (TestCases testCases : allCases) {
			if (testCases.getCaseName().contains("快速-银行卡")) {
				set.add(new TestCases[] { testCases });
			}
		}
		Iterator<Object[]> iterator = set.iterator();
		return iterator;
	}

	/**
	 * 银行活动(折扣)相关测试用例数据源
	 */
	@DataProvider(name = "DPDisCount")
	public Iterator<Object[]> createDisCountData() {
		Set<Object[]> set = new HashSet<Object[]>();
		// for (TestCases testCases : allCases) {
		// if (testCases.getCaseTitle().contains("银行活动")) {
		// set.add(new TestCases[] { testCases });
		// }
		// }
		Iterator<Object[]> iterator = set.iterator();
		return iterator;
	}

	/**
	 * 分期判定数据源
	 * 
	 * @return
	 */
	@DataProvider(name = "DPNoStage")
	public Iterator<Object[]> createCardNoStageData() {
		Set<Object[]> set = new HashSet<Object[]>();
		// for (TestCases testCases : allCases) {
		// if (testCases.getCaseTitle().contains("分期商户")) {
		// set.add(new TestCases[] { testCases });
		// }
		// }
		Iterator<Object[]> iterator = set.iterator();
		return iterator;
	}

	/**
	 * 是否从签到开始
	 */
	@BeforeClass
	public void beforeClass() {
		if (actionUtils.getContent("签到")) {
			actionUtils.findElementById("login_btn").click();
		}
		assertTrue(wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				return actionUtils.getContent("商户版") || actionUtils.getContent("商圈版");
			}
		}));
	}

	/**
	 * 修改为分期 参数后续配置到xml文件中
	 */
	@BeforeGroups(groups = "快速刷卡")
	public void beforeStateGroup() {
		JSONObject json = posController.getUsedPOSDetails(deviceSn, cookies);
		if (Integer.parseInt(json.getString("isStage")) != 1) {
			System.out.println("银行刷卡用例执行");
			SignParams params = new SignParams();
			params.setIsStage(1);
			params.setCardBinNumber("621700,622848");
			params.setStatgeNumber("600");
			posController.mobile(deviceSn, cookies, params);
		}
		json = posController.getUsedPOSDetails(deviceSn, cookies);
		assertEquals(Integer.parseInt(json.getString("isStage")), 1);
	}

	@BeforeMethod
	public void beforeMethod() {

		if (!actionUtils.getDriver().currentActivity().equals("com.kashuo.kashuoposlauncher.MainActivity1")) {
			actionUtils.getDriver().startActivity("com.kashuo.kashuoposlauncher",
					"com.kashuo.kashuoposlauncher.MainActivity1");
		}
	}

	// 每个测试用例结束后 退回主桌面
	@AfterMethod
	public void afterMethod() {
		actionUtils.getDriver().sendKeyEvent(3);
		System.out.println("测试用例执行完成,不要撕毁小票,凭票撤销消费！！!");
	}
	
	@Parameters({"snnumber","authname","authpassword"})
	@BeforeSuite
	public void beforeSuite(String snnumber,String authname,String authpassword) {
		actionUtils = new AppiumUtils(snnumber);
		httpUtils = new HttpUtils();
		posController = POSController.getInstance();
		backendController = BackendController.getInstance();

		SignParams params = new SignParams();
		params.setUsername(authname);
		params.setPassword(authpassword);
		cookies = httpUtils.getCookies(params);
		wait = new WebDriverWait(actionUtils.getDriver(), 75);

		sf = new SimpleDateFormat("yyyyMMddHHmmss");
		allCases = caseDao.queryAll();
		deviceSn = "0820023544";
	}

	@AfterSuite
	public void afterSuite() {
		actionUtils.getDriver().quit();
	}

	/**
	 * 商户分期 快速刷卡 交易
	 * 
	 * @throws ParseException
	 */
	@Test(groups = "快速刷卡", dataProvider = "cardDB")
	public void card_Test01(TestCases cases) throws ParseException {
		System.out.println("执行测试用例"+cases.getKeyNumber());
		POSAction.instance.cardPay(Integer.parseInt(cases.getKeyNumber()), actionUtils);
		System.out.println("即将刷卡");

		actionUtils.threadSleep(10);
		System.out.println("等待刷卡结束");

		List<WebElement> texts = actionUtils.findElementsByClassName("android.widget.TextView");
		System.out.println("刷卡是否成功:" + actionUtils.getContent("卡号确认") + "银行卡号:" + texts.get(1).getText());

		actionUtils.goSleep(60);

		actionUtils.findElementByXpath("//*[@text='确定']").click();// 确认
		long expectedTime = Long.parseLong(sf.format(new Date()));
		System.out.println("输入密码后的时间戳:" + expectedTime);

		actionUtils.goSleep(60);
		// 确认签名
		actionUtils.findElementByXpath("//*[@text='确认签名']").click();

		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				return actionUtils.getContent("商户版") || actionUtils.getContent("商圈版");
			}
		});
		long actualTime = Long.parseLong(backendController.getTimeStamp(deviceSn, "normal_trans", cookies));
		double paymount = (backendController.getPayMount(deviceSn, "normal_trans", cookies)) * 0.01;
		System.out.println("大后台获取的时间戳:" + actualTime);
		long re = sf.parse(String.valueOf(actualTime)).getTime() - sf.parse(String.valueOf(expectedTime)).getTime();

		assertTrue(re / 1000 < 60);
		// System.out.println("实际的支付金额:" + paymount + ";预期的支付金额:" +
		// Double.parseDouble(cases.getExpectedResult()));
		// assertEquals(paymount,
		// Double.parseDouble(cases.getExpectedResult()));
	}

	/**
	 * 分期商户 分期是否满足
	 */
	// @Test(groups = "分期", dataProvider = "DPNoStage")
	public void stage_Test01(TestCases cases) {
		System.out.println("***分期测试用例 begin**** ");
		System.out.println("\\n");
		System.out.println("***分期测试用例 end****");
	}

	/**
	 * 银行活动
	 */
	// @Test(groups = "银行活动", dataProvider = "DPDisCount")
	public void bank_Test01(TestCases cases) {
		// disountPay((int) (Double.parseDouble(cases.getBankAmount()) * 100),
		// (int) (Double.parseDouble(cases.getNoDisountAmount()) * 100));
		System.out.println("将进行刷卡查询优惠");
		actionUtils.goSleep(10);
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {

				return actionUtils.findElementsByClassName("android.widget.TextView").get(0).getText().equals("银行卡");
			}
		});
		// 输入手机号
		actionUtils.findElementByClassName("android.widget.EditText").clear();
		actionUtils.findElementByClassName("android.widget.EditText").sendKeys("15151619579");

		int y = actionUtils.getDriver().manage().window().getSize().getHeight();

		actionUtils.getDriver().tap(1, 10, y - 5, 0);

		actionUtils.findElementByClassName("android.widget.Button").click();
		System.out.println("将刷卡消费");
		actionUtils.threadSleep(10);

		List<WebElement> texts = actionUtils.findElementsByClassName("android.widget.TextView");
		System.out.println("刷卡是否成功:" + actionUtils.getContent("卡号确认") + "银行卡号:" + texts.get(1).getText());
		actionUtils.goSleep(60);

		actionUtils.findElementByXpath("//*[@text='确定']").click();// 确认
		long expectedTime = Long.parseLong(sf.format(new Date()));
		System.out.println("输入密码后的时间戳" + expectedTime);

		actionUtils.goSleep(60);
		// 确认签名
		actionUtils.findElementByXpath("//*[@text='确认签名']").click();

		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				return actionUtils.getContent("商户版") || actionUtils.getContent("商圈版");
			}
		});
		long actualTime = Long.parseLong(backendController.getTimeStamp(deviceSn, "trans_query", cookies));
		double paymount = (backendController.getPayDisMount(deviceSn, "trans_query", cookies));
		System.out.println("大后台获取的时间戳:" + actualTime);
		//
		// System.out.println("实际的支付金额:" + paymount + ";预期的支付金额:" +
		// Double.parseDouble(cases.getExpectedResult()));
		// assertEquals(paymount,
		// Double.parseDouble(cases.getExpectedResult()));
		// assertEquals(String.valueOf(paymount), cases.getExpectedResult());
	}
	

}
