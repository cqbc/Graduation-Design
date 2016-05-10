package com.cqupt.spider.start;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.cqupt.common.enums.RetCodeEnum;
import com.cqupt.common.enums.RetDescEnum;
import com.cqupt.common.manager.WebServiceManager;
import com.cqupt.common.ws.ITaskService;
import com.cqupt.spider.manager.SystemControlerManager;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.RetStatus;

public class SpiderUIService {
	
	public static void startWebService(){
		
		WebServiceManager webServiceManager = WebServiceManager.getInstance();
		webServiceManager.startWebService();
	}
	
	
	public static void stopWebService(){
		WebServiceManager webServiceManager = WebServiceManager.getInstance();
		webServiceManager.stopWebService();
	}
	
	
	public static void startSpiderServer(){
		SystemControlerManager controlerManager = new SystemControlerManager();
		controlerManager.startServer();
	}
	
	
	
	public static void startSpiderClient(){
		SystemControlerManager controlerManager = new SystemControlerManager();
		controlerManager.startClient();
	}
	
	public static void stopWorld(){
		System.exit(0);
	}
	
	//status  1为添加任务   2为删除任务
	public static RetStatus CallTaskService(CrawlTaskPojo taskPojo,int status){
		try {
			URL wsdlUrl = new URL("http://127.0.0.1:9999/TaskService?wsdl");
			Service s = Service.create(wsdlUrl, new QName("http://impl.ws.common.cqupt.com/","TaskService"));
			ITaskService hs = s.getPort(new QName("http://impl.ws.common.cqupt.com/","TaskServiceImplPort"), ITaskService.class);
			if(status == 1){
				return hs.addTask(taskPojo);
			}else if(status == 2){
				return hs.removeTask(taskPojo);
			}else{
				System.out.println("添加任务异常执行！");
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new RetStatus(RetCodeEnum.Error, RetDescEnum.Fail);
	}
	

	static class SpiderUITempThread extends Thread{
		
		SpiderUITempThread(String sinal){
			setName(sinal);
		}
		
		public void run(){
			
			if(getName().equals("start")){
				SystemControlerManager controlerManager = new SystemControlerManager();
				controlerManager.startServer();
			}else if(getName().equals("stop")){
				SystemControlerManager controlerManager = new SystemControlerManager();
				controlerManager.startClient();
			}
		}
		
	}
	
}
