package com.ks.tests.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class FileUtils {

	public static FileUtils instance = new FileUtils();

	/**
	 * 启动命令行运行
	 * 
	 * @param param
	 *            具体指令
	 */
	public void execCMD(String param) {
		System.out.println("开始执行");
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("cmd /k  start " + param);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FileUtils.instance.execCMD("appium -p 4723 --log-level debug --log-timestamp --command-timeout 600000 ");


	}
}
