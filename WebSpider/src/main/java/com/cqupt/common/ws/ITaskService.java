package com.cqupt.common.ws;

/**
 * spider task service
 * 主要用于爬虫方面的任务管理接口
 */
import javax.jws.WebService;

import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.RetStatus;

@WebService
public interface ITaskService {
	 String test();

	 RetStatus addTask(CrawlTaskPojo taskTaskPojo);
	
	 RetStatus removeTask(CrawlTaskPojo taskTaskPojo);

}
