package com.ks.tests.appium;

import java.io.File;

import java.net.URL;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.testng.Reporter;


import com.ks.tests.dao.ConfigDao;
import com.ks.tests.utils.SpringBeanFactoryUtils;
import com.mysql.jdbc.log.LogFactory;

import io.appium.java_client.android.AndroidDriver;

public class POSActionUtils {

	private AndroidDriver driver;
	private DesiredCapabilities capabilities;
	private static File classpathRoot = new File(System.getProperty("user.dir"));
	private static File appDir = new File(classpathRoot, "apps");
	
	
//	private static final Log logger=LogFactory.getLogger(arg0, arg1)
	
	private ConfigDao configDao=(ConfigDao) SpringBeanFactoryUtils.getBean("config");
	
	public AndroidDriver getDriver() {
		return driver;
	}

	public POSActionUtils(String sn) {

		Map<String, String> map;
		try {

				Reporter.log("init AppiumDriver ");
				map = configDao.getConfig(sn);
				if (map == null) {
					throw new Exception("Parse MySQL Fail");
				} else {
					capabilities = new DesiredCapabilities();
					capabilities.setCapability("deviceName", map.get("deviceName"));
					capabilities.setCapability("platformName", map.get("platformName"));
					capabilities.setCapability("platformVersion", map.get("platformVersion"));
					// 配置时安装获取短信app

					// File app = new File(appDir, "smsCode.apk");
					// capabilities.setCapability("app", app.getAbsolutePath());
					capabilities.setCapability("noReset", "true");
					capabilities.setCapability("fullReset", "false");
					capabilities.setCapability("unicodeKeyboard", "True");
					capabilities.setCapability("resetKeyboard", "True");
					capabilities.setCapability("appPackage", map.get("appPackage"));
					capabilities.setCapability("appActivity", map.get("appActivity"));
					Reporter.log("Appium Server:" + map.get("deviceUrl"));
					driver = new AndroidDriver(new URL(map.get("deviceUrl")), capabilities);
					goSleep(30);

				}
//			} else {
//				System.out.println("sn号未成功获取");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param pkgName
	 */
	public void installApp(String pkgName, String name) {
		if (driver.isAppInstalled(pkgName)) {
			driver.removeApp(pkgName);
		}
		File app = new File(appDir, name);
		System.out.println(app.getAbsolutePath());
		driver.installApp(app.getAbsolutePath());
	}

	/**
	 * 根据AndroidDriver的findElementByAndroidUIAutomator获取元素
	 * 
	 * @param driver
	 * @param value
	 * @return
	 */
	public WebElement findElementByAndroidUIAutomator(String value) {
		WebElement element = driver.findElementByAndroidUIAutomator(value);
		return element;
	}

	/**
	 * 根据resuourceId获取
	 * 
	 * @param driver
	 * @param value
	 * @return
	 */
	public WebElement findElementById(String value) {
		WebElement element = driver.findElementById(value);
		return element;
	}

	/**
	 * 根据xpth定位
	 * 
	 * @param driver
	 * @param value
	 * @return
	 */
	public WebElement findElementByXpath(String value) {
		WebElement element = driver.findElementByXPath(value);
		return element;
	}

	/**
	 * 根据类名遍历元素
	 * 
	 * @param driver
	 * @param value
	 * @return
	 */
	public List<WebElement> findElementsByClassName(String value) {
		List<WebElement> lists = driver.findElementsByClassName(value);
		return lists;
	}

	public WebElement findElementByClassName(String value) {
		WebElement element = driver.findElementByClassName(value);
		return element;
	}

	/**
	 * 向左滑动
	 * 
	 * @param value
	 * @param pageIndex
	 */
	public void swipeToLeft(String value, int pageIndex) {
		int x, y;
		if (value.equals("")) {
			x = driver.manage().window().getSize().getWidth();
			y = driver.manage().window().getSize().getHeight();
		} else {
			x = driver.findElementByClassName(value).getSize().getWidth();
			y = driver.findElementByClassName(value).getSize().getHeight();
		}
		for (int i = 0; i < pageIndex; i++) {
			driver.swipe(x * 3 / 4, y * 2 / 3, x / 4, y * 2 / 3, 550);
		}
	}

	/**
	 * 设置等待时间
	 * 
	 * @param mill
	 *            秒
	 */
	public void goSleep(int mill) {
		driver.manage().timeouts().implicitlyWait(mill, TimeUnit.SECONDS);
	}

	/**
	 * 线程等待
	 * 
	 * @param mill
	 *            秒
	 */
	public void threadSleep(int mill) {
		try {
			Thread.sleep(mill * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 向右滑动
	 * 
	 * @param value
	 * @param pageIndex
	 */
	public void swipeToRight(String value, int pageIndex) {
		int x, y;
		if (value.equals("")) {
			x = driver.manage().window().getSize().getWidth();
			y = driver.manage().window().getSize().getHeight();
		} else {
			x = driver.findElementByClassName(value).getSize().getWidth();
			y = driver.findElementByClassName(value).getSize().getHeight();
		}
		for (int i = 0; i < pageIndex; i++) {
			driver.swipe(x / 4, y * 2 / 3, x * 3 / 4, y * 2 / 3, 550);
		}
	}

	/**
	 * 向上滑动
	 * 
	 * @param value
	 * @param pageIndex
	 */
	public void swipeToUp(String value, int pageIndex) {
		int x, y;
		if (value.equals("")) {
			x = driver.manage().window().getSize().getWidth();
			y = driver.manage().window().getSize().getHeight();
		} else {
			x = driver.findElementByClassName(value).getSize().getWidth();
			y = driver.findElementByClassName(value).getSize().getHeight();
		}
		for (int i = 0; i < pageIndex; i++) {
			driver.swipe(x / 4, y * 3 / 4, x / 4, y / 4, 600);
		}
	}

	/**
	 * 向下
	 * 
	 * @param value
	 * @param pageIndex
	 */
	public void swipeToDown(String value, int pageIndex) {
		int x, y;
		if (value.equals("")) {
			x = driver.manage().window().getSize().getWidth();
			y = driver.manage().window().getSize().getHeight();
		} else {
			x = driver.findElementByClassName(value).getSize().getWidth();
			y = driver.findElementByClassName(value).getSize().getHeight();
		}
		for (int i = 0; i < pageIndex; i++) {
			driver.swipe(x / 4, y / 4, x / 4, y * 3 / 4, 600);
		}
	}

	/**
	 * 某个元素是否存在 获取页面内容
	 * 
	 * @return
	 */
	public boolean getContent(String str) {
		return driver.getPageSource().contains(str);
	}

	/**
	 * 第三方应用停止
	 */
	public boolean isStop() {
		return getContent("很抱歉");
	}

	public void doReSign() {
		if (isStop()) {
			driver.findElementByClassName("android.widget.Button").click();
			if (getContent("更多功能")) {
				swipeToLeft("android.support.v4.view.ViewPager", 3);
			} else if (getContent("常用功能")) {
				swipeToLeft("android.support.v4.view.ViewPager", 2);
			}

			if (getContent("辅助功能")) {

				driver.findElementById("ll_trans_login").click();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			}
		}
	}
	// public static void main(String[] args) {
	// POSActionUtils posAndroidDriver = new POSActionUtils();
	//
	// AndroidDriver<WebElement> wd = posAndroidDriver.getDriver();
	// System.out.println(wd.getPageSource());
	// System.out.println(wd.currentActivity());
	// wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	//
	// int x =
	// wd.findElementByClassName("android.support.v4.view.ViewPager").getSize().getWidth();
	// int y =
	// wd.findElementByClassName("android.support.v4.view.ViewPager").getSize().getHeight();
	// for (int i = 0; i < 3; i++) {
	// wd.swipe(x * 3 / 4, y * 2 / 3, x / 4, y * 2 / 3, 600);
	//
	// }
	// System.out.println(x + "===" + y);
	// }
}
