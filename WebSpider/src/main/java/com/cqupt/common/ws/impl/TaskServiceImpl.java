package com.cqupt.common.ws.impl;

import javax.jws.WebService;

import com.cqupt.common.enums.RetCodeEnum;
import com.cqupt.common.enums.RetDescEnum;
import com.cqupt.common.enums.TaskQueueType;
import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.ws.ITaskService;
import com.cqupt.spider.manager.TaskQueueManager;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.RetStatus;

/**
 * 任务服务实现类,通过该web service接口，外部可以通过程序接口对任务进行管理
 * 
 */
@WebService(endpointInterface = "com.cqupt.common.ws.ITaskService", serviceName = "TaskService")
public class TaskServiceImpl implements ITaskService {

	public String test() {
		// TODO Auto-generated method stub
		return null;
	}

	public RetStatus addTask(CrawlTaskPojo taskTaskPojo) {
		// TaskQueueManager.addTask(taskTaskPojo, TaskQueueType.To_Visit);
		TaskQueueManager.addTaskToDoQueue(
				StaticValue.redis_task_todo_list_key_name, taskTaskPojo, null,
				false);
		// 只有从这个途径过添加的任务都是原始任务,根据条件加入循环队列
		if (SystemParasSpider.task_circle_enable && taskTaskPojo.isEnableToCircle()) {
			TaskQueueManager.addTask(taskTaskPojo, TaskQueueType.Circle_Visit);
		}

		System.out.println("ws client add task operator!");
		return new RetStatus(RetCodeEnum.Ok, RetDescEnum.Success);
	}

	public RetStatus removeTask(CrawlTaskPojo taskTaskPojo) {
		boolean remove_status = TaskQueueManager.removeCircleTask(taskTaskPojo);
		if (remove_status) {
			System.out.println("ws client remove task success!");
			return new RetStatus(RetCodeEnum.Ok, RetDescEnum.Success);
		} else {
			System.out.println("ws client remove task fail!");
			return new RetStatus(RetCodeEnum.Error, RetDescEnum.Fail);
		}

	}

}
