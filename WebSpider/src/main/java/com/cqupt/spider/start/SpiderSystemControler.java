
package com.cqupt.spider.start;

import com.cqupt.common.manager.WebServiceManager;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.spider.manager.SystemControlerManager;

/**
 * 系统运行的控制器类
 * 
 */
public class SpiderSystemControler {
	// 日志
	public static MyLogger logger = new MyLogger(SpiderSystemControler.class);
	
	public static void main(String[] args) {
		
		if (SystemParasSpider.node_is_master) {
			SystemControlerManager controlerManager = new SystemControlerManager();
			controlerManager.startServer();
		} else {
			SystemControlerManager controlerManager = new SystemControlerManager();
			controlerManager.startClient();
		}
		
		System.out.println("done!");
	}
	
}