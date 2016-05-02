package com.cqupt.spider.start;

import com.cqupt.common.manager.WebServiceManager;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.spider.manager.SystemControlerManager;

/**
 * 系统运行的控制器类
 * 
 */
public class SystemControler {
	// 日志
	public static MyLogger logger = new MyLogger(SystemControler.class);
	
	public static void main(String[] args) {
		
			/**
			 * 启动系统web service服务
			 */
			WebServiceManager webServiceManager = new WebServiceManager();
			webServiceManager.startWebService();
			
//			webServiceManager.stopWebService();
			
			SystemControlerManager controlerManager = new SystemControlerManager();
			controlerManager.startServer();
			
		
		System.out.println("done!");
	}
}
