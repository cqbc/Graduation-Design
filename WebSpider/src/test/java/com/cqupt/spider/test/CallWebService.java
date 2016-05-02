package com.cqupt.spider.test;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.cqupt.common.enums.CrawlTypeEnum;
import com.cqupt.common.enums.TaskTypeEnum;
import com.cqupt.common.statics.StaticValue4RelationMap;
import com.cqupt.common.ws.ITaskService;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.RetStatus;

public class CallWebService {

	public static void main(String[] args) throws Exception {

		addUrl("http://ent.sina.com.cn/film/");
		
		
		
	}
	
	public static void deleteSeeds(String keyword) throws Exception {
		

		       URL wsdlUrl = new URL("http://127.0.0.1:9999/TaskService?wsdl");
			//  URL wsdlUrl = new URL("http://127.0.0.1:9990/TaskService?wsdl");
		        Service s = Service.create(wsdlUrl, new QName("http://task.ws.impl.zel.com/","TaskService"));
		        ITaskService hs = s.getPort(new QName("http://task.ws.impl.zel.com/","TaskServiceImplPort"), ITaskService.class);
		      
		        
				CrawlTaskPojo	taskPojo = new CrawlTaskPojo();
				taskPojo.setTitle("元搜索");
				taskPojo.setValue(keyword);
				taskPojo.setMedia_type(8);
				taskPojo.setSource_title(taskPojo.getTitle());
				taskPojo.setType(TaskTypeEnum.Keyword);
				taskPojo.setCrawlEngine(CrawlTypeEnum.MetaSearch_NEWSPage);
				taskPojo.setLevel(StaticValue4RelationMap
						.getTaskLevelEnumByString("B"));
				taskPojo.setDepth(4);
				taskPojo.setCurrent_depth(0);
				taskPojo.setTopN(100);
				RetStatus rs = hs.removeTask(taskPojo);
				//添加
			
				System.out.println("done!");
		
		
	}
	
	
	//source_title表示如下 
	/*
	 * 如果是keyword  query_+keyword
	 * 如果是url      url_+url
	 */
	
	public static void addSeeds(String keyWord) throws Exception {
		

				
		  URL wsdlUrl = new URL("http://127.0.0.1:9999/TaskService?wsdl");
	        Service s = Service.create(wsdlUrl, new QName("http://task.ws.impl.zel.com/","TaskService"));
	        ITaskService hs = s.getPort(new QName("http://task.ws.impl.zel.com/","TaskServiceImplPort"), ITaskService.class);
			CrawlTaskPojo	taskPojo = new CrawlTaskPojo();
			taskPojo.setTitle("元搜索");
			taskPojo.setValue(keyWord);
			taskPojo.setMedia_type(8);
			taskPojo.setSource_title("query_"+keyWord);
			taskPojo.setType(TaskTypeEnum.Keyword);
			taskPojo.setCrawlEngine(CrawlTypeEnum.MetaSearch_NEWSPage);
			taskPojo.setLevel(StaticValue4RelationMap
					.getTaskLevelEnumByString("B"));
			taskPojo.setDepth(0);
			taskPojo.setCurrent_depth(0);
			taskPojo.setTopN(0);
			RetStatus rs = hs.addTask(taskPojo);
			System.out.println("done!");
			
			}
	
	
	
	
	//网页media_type=6
	public static void addUrl(String url) throws Exception {
		
					
			  URL wsdlUrl = new URL("http://127.0.0.1:9999/TaskService?wsdl");
		        Service s = Service.create(wsdlUrl, new QName("http://task.ws.impl.zel.com/","TaskService"));
		        ITaskService hs = s.getPort(new QName("http://task.ws.impl.zel.com/","TaskServiceImplPort"), ITaskService.class);
				CrawlTaskPojo	taskPojo = new CrawlTaskPojo();
				taskPojo.setTitle("url");
				taskPojo.setValue(url);
				taskPojo.setMedia_type(6);
				taskPojo.setSource_title("url_"+url);
				taskPojo.setType(TaskTypeEnum.Url);
				taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
				taskPojo.setLevel(StaticValue4RelationMap
						.getTaskLevelEnumByString("B"));
				taskPojo.setDepth(4);
				taskPojo.setCurrent_depth(0);
				taskPojo.setTopN(100);
				RetStatus rs = hs.addTask(taskPojo);
				System.out.println("done!");
				
				}
	
	
	
public static void deleteUrl(String url) throws Exception {
		
		//	String[] movies={"寻龙诀","港囧","九层妖塔","小王子","蚁人","三体","山河故人","一个勺子"};
			//String []sites={"site:chinadaily.com.cn","site:people.com.cn","site:huanqiu.com","site:china.com.cn","site:youth.cn","site:chinanews.com","site:cnr.cn","site:hexun.com","site:xinhuanet.com","site:news.cn"};
		
				
					//String keyWord="小飞侠";
					
			  URL wsdlUrl = new URL("http://127.0.0.1:9999/TaskService?wsdl");
			//  URL wsdlUrl = new URL("http://127.0.0.1:9990/TaskService?wsdl");
		        Service s = Service.create(wsdlUrl, new QName("http://task.ws.impl.zel.com/","TaskService"));
		        ITaskService hs = s.getPort(new QName("http://task.ws.impl.zel.com/","TaskServiceImplPort"), ITaskService.class);
				CrawlTaskPojo	taskPojo = new CrawlTaskPojo();
				taskPojo.setTitle("url");
				taskPojo.setValue(url);
				taskPojo.setMedia_type(6);
				taskPojo.setSource_title("url_"+url);
				taskPojo.setType(TaskTypeEnum.Url);
				taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
				taskPojo.setLevel(StaticValue4RelationMap
						.getTaskLevelEnumByString("B"));
				taskPojo.setDepth(4);
				taskPojo.setCurrent_depth(0);
				taskPojo.setTopN(100);
				RetStatus rs = hs.removeTask(taskPojo);
				System.out.println("done!");
				
				}

	
}
