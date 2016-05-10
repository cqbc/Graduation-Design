
package com.cqupt.spider.task;

import com.cqupt.common.utils.MyLogger;
import com.cqupt.spider.pojos.CrawlTaskPojo;

/**
 * 任务协调管理器
 * 
 */
public class TaskDispacherManager {
	// 日志
	public static MyLogger logger = new MyLogger(TaskDispacherManager.class);

	// 为循环队列而添加
	private static long task_count = 0;

	public static CrawlTaskPojo getCrawlTask() {
		/**
		 * 先判定几个要填充的redis中key的名称，就不用在后边用if...else去每次去判定了
		 */
		CrawlTaskPojo taskPojo = null;
		try {
			taskPojo = TaskDispatcherControler.getTaskFirstQueue();
			// 如果一级队列为空，则从二级队取值，再为空，则不再抓取
			if (taskPojo == null) {
			
					taskPojo = TaskDispatcherControler.getTaskCircleNormal();
					
					if (taskPojo != null) {
						System.out.println("从普通循环队列取出一个任务,"+ taskPojo);
					} else {
						taskPojo = TaskDispatcherControler.getTaskCircleKeyword();
						if (taskPojo != null) {
							System.out.println("从元搜索循环队列取出一个任务," + taskPojo);
						} else {
							taskPojo = TaskDispatcherControler.getTaskSecondQueue();
							if (taskPojo != null) {
								System.out.println("从第二队列取出一个任务," + taskPojo);
							} 
						}
					}
				 
			
				if (task_count > 10000000) {
					task_count = 0;
					logger.info("counter reset,task_task_count > 10000000 will set task_task_count=0");
				}
				
				task_count++;
			} else {
				System.out.println("从第一队列取出一个任务," + taskPojo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskPojo;
	}
}
