package com.dongnaoedu.mall.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dongnaoedu.mall.common.uploadfile.FastDFSClient;
import com.dongnaoedu.mall.common.uploadfile.FileAccessUtils;
import com.dongnaoedu.mall.common.uploadfile.FileManagerUtils;
import com.dongnaoedu.mall.pojo.TbBase;
import com.dongnaoedu.mall.service.SystemService;

public class ApplicationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger log = LoggerFactory.getLogger(ApplicationServlet.class);
	
	ServletContext application;
	
	public void init(ServletConfig config) throws ServletException {
		/**
		 * 此处是系统启动时候执行的，一些读取配置文件，监听端口，初始化连接之类的可以在此处做
		 */
		
		//D:\tomcat64-bit\apache-tomcat-7.0.64\webapps\caterers\
		String realPath = config.getServletContext().getRealPath("/");
		log.info(realPath);
		log.info("===========================================================================");
		FileAccessUtils fileUtils = SpringContextHolder.getBean(FileAccessUtils.class);
		
		application = config.getServletContext();
		
		// 通过ApplicationContext.getBean获取bean
		SystemService systemService = SpringContextHolder.getBean(SystemService.class);
		
		TbBase base = systemService.getBase();
		application.setAttribute("base", base);
		
		try {
			application.setAttribute("fdfsUrl", FastDFSClient.getTrackerUrl());
			//application.setAttribute("fdfsUrl", fileUtils.init(realPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("===========================================================================");
	}
	
	public void destroy() {
		log.info("开始停止应用");
		application.removeAttribute("ctx");
		super.destroy();
	}
	
}
