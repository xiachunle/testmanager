package com.ks.tests.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@Controller
public class FileHandler {

	@RequestMapping(value="/upload")
	public String springUpload(HttpServletRequest request) throws IllegalStateException, IOException {
		System.out.println("upload********");
		long startTime = System.currentTimeMillis();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					
					String path = System.getProperty("evan.webapp")+ file.getOriginalFilename();
					System.out.println("文件上传路径:"+path);
					// 上传
					file.transferTo(new File(path));
				}
			}

		}
		long endTime = System.currentTimeMillis();
		System.out.println("上传时间：" + String.valueOf(endTime - startTime) + "ms");
		return "/WEB-INF/views/success.jsp";
	}
}
