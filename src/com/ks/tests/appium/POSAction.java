package com.ks.tests.appium;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * POS机上的操作
 * 
 * @author dell
 *
 */
public class POSAction {
	public static POSAction instance = new POSAction();

	// 快速刷卡 --刷卡前
	public void cardPay(int num, final AppiumUtils actionUtils) {
		System.out.println("**********");
		WebDriverWait wait = new WebDriverWait(actionUtils.getDriver(), 60);
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				return actionUtils.getContent("商户版") || actionUtils.getContent("商圈版");
			}
		});
		System.out.println("**********");
		actionUtils.findElementById("ll_bank_card").click();
		actionUtils.goSleep(10);
		if (0 < num && num < 10) {
			actionUtils.findElementById("key_" + num).click();
		} else if (10 <= num && num < 100) {
			int a = num / 10;
			actionUtils.findElementById("key_" + a).click();
			actionUtils.findElementById("key_" + (num - a * 10)).click();
		} else if (100 <= num && num < 1000) {
			int a = num / 100;
			int b = (num - 100 * a) / 10;
			int c = num - a * 100 - b * 10;
			actionUtils.findElementById("key_" + a).click();
			actionUtils.findElementById("key_" + b).click();
			actionUtils.findElementById("key_" + c).click();
		} else if (num >= 1000 && num < 10000) {
			int a = num / 1000;
			int b = (num - 1000 * a) / 100;
			int c = (num - a * 1000 - b * 100) / 10;
			int d = num - a * 1000 - b * 100 - c * 10;
			actionUtils.findElementById("key_" + a).click();
			actionUtils.findElementById("key_" + b).click();
			actionUtils.findElementById("key_" + c).click();
			actionUtils.findElementById("key_" + d).click();
		} else if (num >= 10000 && num < 100000) {
			int a = num / 10000;
			int b = (num - 10000 * a) / 1000;
			int c = (num - a * 10000 - b * 1000) / 100;
			int d = (num - a * 10000 - b * 1000 - c * 100) / 10;
			int e = num - a * 10000 - b * 1000 - c * 100 - d * 10;
			actionUtils.findElementById("key_" + a).click();
			actionUtils.findElementById("key_" + b).click();
			actionUtils.findElementById("key_" + c).click();
			actionUtils.findElementById("key_" + d).click();
			actionUtils.findElementById("key_" + e).click();
		}
		System.out.println("输入的金额为:" + actionUtils.findElementById("et_input_count").getText());
		actionUtils.getDriver().manage().timeouts().implicitlyWait(65, TimeUnit.SECONDS);
		if (actionUtils.getContent("商户版") || actionUtils.getContent("商圈版")) {
			cardPay(num, actionUtils);
		}
		actionUtils.findElementById("btn_true").click();
		// 第三方应用停止运行
		if (actionUtils.isStop()) {
			actionUtils.doReSign();
			cardPay(num, actionUtils);
		}
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver arg0) {
				return actionUtils.getDriver().getPageSource().contains("请使用您的银行卡");
			}
		});

	}

	/**
	 * 银行活动刷卡
	 * 
	 * @param expense
	 *            优惠金额
	 * @param noDisount
	 *            不参与优惠金额
	 * @param wait
	 * @param actionUtils
	 */
	public void bankActivity(int expense, int noDisount, WebDriverWait wait, final AppiumUtils actionUtils) {

		if (actionUtils.getContent("更多功能")) {
			actionUtils.swipeToLeft("android.support.v4.view.ViewPager", 1);
		} else if (actionUtils.getContent("辅助功能")) {
			actionUtils.swipeToRight("android.support.v4.view.ViewPager", 1);
		}
		actionUtils.goSleep(30);
		actionUtils.findElementById("ll_bank_pay").click();
		actionUtils.goSleep(30);
		List<WebElement> lists = actionUtils.findElementsByClassName("android.widget.EditText");

		List<WebElement> btnlists = actionUtils.findElementsByClassName("android.widget.Button");
		lists.get(0).click();
		dokeyAction(expense, btnlists);
		lists.get(1).click();
		dokeyAction(noDisount, btnlists);

		btnlists.get(btnlists.size() - 1).click();
		actionUtils.goSleep(10);
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver arg0) {
				return actionUtils.getDriver().getPageSource().contains("请使用您的银行卡");
			}
		});
	}

	/**
	 * 银行活动键盘输入
	 * 
	 * @param num
	 * @param btnlists
	 */
	private void dokeyAction(int num, List<WebElement> btnlists) {
		if (0 < num && num < 10) {
			btnlists.get(num - 1).click();
		} else if (10 <= num && num < 100) {
			int a = num / 10;
			btnlists.get(a - 1).click();
			if ((num - a * 10) == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(num - a * 10 - 1).click();
			}
		} else if (100 <= num && num < 1000) {
			int a = num / 100;
			int b = (num - 100 * a) / 10;
			int c = num - a * 100 - b * 10;
			btnlists.get(a - 1).click();
			if (b == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(b - 1).click();
			}
			if (c == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(c - 1).click();
			}
		} else if (num >= 1000 && num < 10000) {
			int a = num / 1000;
			int b = (num - 1000 * a) / 100;
			int c = (num - a * 1000 - b * 100) / 10;
			int d = num - a * 1000 - b * 100 - c * 10;
			btnlists.get(a - 1).click();
			if (b == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(b - 1).click();
			}
			if (c == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(c - 1).click();
			}
			if (d == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(d - 1).click();
			}
		} else if (num >= 10000 && num < 100000) {
			int a = num / 10000;
			int b = (num - 10000 * a) / 1000;
			int c = (num - a * 10000 - b * 1000) / 100;
			int d = (num - a * 10000 - b * 1000 - c * 100) / 10;
			int e = num - a * 10000 - b * 1000 - c * 100 - d * 10;
			btnlists.get(a - 1).click();
			if (b == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(b - 1).click();
			}
			if (c == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(c - 1).click();
			}
			if (d == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(d - 1).click();
			}
			if (e == 0) {
				btnlists.get(9).click();
			} else {
				btnlists.get(e - 1).click();
			}
		}
	}

	/**
	 * 
	 * @param acqtsn流水号
	 * @param wait
	 * @param actionUtils
	 * @param revokeFlag撤销类型
	 *            0---银行卡 1--支付宝微信
	 */
	public void doRemoveAction(String acqtsn, WebDriverWait wait, final AppiumUtils actionUtils, int revokeFlag) {
		if (actionUtils.getContent("更多功能")) {
			System.out.println("更多功能");
			actionUtils.swipeToLeft("android.support.v4.view.ViewPager", 2);
		} else if (actionUtils.getContent("常用功能")) {
			System.out.println("常用功能");
			actionUtils.swipeToLeft("android.support.v4.view.ViewPager", 1);
		}

		actionUtils.goSleep(30);
		// 管理
		actionUtils.findElementById("ll_trans_other").click();
		//
		List<WebElement> imagLists = actionUtils.findElementsByClassName("android.widget.ImageView");
		imagLists.get(1).click();

		List<WebElement> checkLists = actionUtils.findElementsByClassName("android.widget.CheckedTextView");
		checkLists.get(revokeFlag).click();
		// 输入流水号

		actionUtils.findElementByClassName("android.widget.EditText").sendKeys(acqtsn);

		actionUtils.findElementByClassName("android.widget.Button").click();
		actionUtils.goSleep(30);
		actionUtils.findElementByClassName("android.widget.EditText").sendKeys("123456");
		List<WebElement> btnList = actionUtils.findElementsByClassName("android.widget.Button");
		System.out.println(btnList.size());
		// 撤销主管密码123456
		// btnList.get(0).click();
		// btnList.get(1).click();
		// btnList.get(2).click();
		// btnList.get(4).click();
		// btnList.get(5).click();
		// btnList.get(6).click();
		//
		btnList.get(btnList.size() - 1).click();
		actionUtils.goSleep(30);

		List<WebElement> editLists = actionUtils.findElementsByClassName("android.widget.TextView");
		System.out.println("流水号为:" + editLists.get(editLists.size() - 3).getText());

		actionUtils.findElementByClassName("android.widget.Button").click();

	}

	/**
	 * 
	 * @param actionUtils
	 * @param wait
	 * @param pwdFlag
	 *            是否需要刷卡
	 */
	public void afterCard(final AppiumUtils actionUtils, WebDriverWait wait, boolean pwdFlag) {
		List<WebElement> texts = actionUtils.findElementsByClassName("android.widget.TextView");
		System.out.println("刷卡是否成功:" + actionUtils.getContent("卡号确认") + "银行卡号:" + texts.get(1).getText());

		actionUtils.goSleep(60);

		actionUtils.findElementByXpath("//*[@text='确定']").click();// 确认
		// long expectedTime = Long.parseLong(sf.format(new Date()));
		// System.out.println("输入密码后的时间戳:" + expectedTime);
		actionUtils.goSleep(60);
		if (!pwdFlag) {
			System.out.println("无需密码");
			actionUtils.goSleep(10);
			int x = actionUtils.getDriver().manage().window().getSize().width;
			int y = actionUtils.getDriver().manage().window().getSize().height;
			actionUtils.doClickByXY(x - 5, y - 5);
		}
		actionUtils.goSleep(60);
		// 确认签名
		actionUtils.findElementByXpath("//*[@text='确认签名']").click();

		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				return actionUtils.getContent("商户版") || actionUtils.getContent("商圈版");
			}
		});
	}

	

	
	
	public static void main(String[] args) {
		AppiumUtils appiumUtils = new AppiumUtils("");
		WebDriverWait wait = new WebDriverWait(appiumUtils.getDriver(), 65);
		// POSAction.instance.doRemoveAction("000215", wait, appiumUtils, 0);
		// System.out.println("即将刷卡");
		//
		// appiumUtils.threadSleep(10);
		// System.out.println("等待刷卡结束");
		//
		// POSAction.instance.afterCard(appiumUtils, wait,false);
		while (!appiumUtils.getContent("辅助功能")) {
			if (appiumUtils.getContent("更多功能")) {
				System.out.println("更多功能");
				appiumUtils.swipeToLeft("android.support.v4.view.ViewPager", 2);
			} else if (appiumUtils.getContent("常用功能")) {
				System.out.println("常用功能");
				appiumUtils.swipeToLeft("android.support.v4.view.ViewPager", 1);
			}
		}
		appiumUtils.goSleep(30);
		while (!appiumUtils.getContent("常用功能")) {
			if (appiumUtils.getContent("更多功能")) {
				System.out.println("更多功能");
				appiumUtils.swipeToLeft("android.support.v4.view.ViewPager", 1);
			} else if (appiumUtils.getContent("辅助功能")) {
		
				appiumUtils.swipeToRight("android.support.v4.view.ViewPager", 1);
			}
		}
	}

}
