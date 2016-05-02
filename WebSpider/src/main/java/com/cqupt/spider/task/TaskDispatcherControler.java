package com.cqupt.spider.task;

import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.JedisOperatorUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.ObjectAndByteArrayConvertUtil;
import com.cqupt.spider.pojos.CrawlTaskPojo;

/**
 * 任务种类控制器
 * 
 */
public class TaskDispatcherControler {
	// 日志
	public static MyLogger logger = new MyLogger(TaskDispatcherControler.class);

	private static JedisOperatorUtil jedisOperatorUtil = new JedisOperatorUtil(
			SystemParasSpider.redis_host, SystemParasSpider.redis_port,
			SystemParasSpider.redis_password);

	private static String to_do_task_key_circle_keyword = StaticValue.redis_task_todo_key_name_circle_keyword;
	private static String to_do_task_key_circle = StaticValue.redis_task_todo_key_name_circle;
	private static String task_todo_level_2 = StaticValue.redis_task_todo_list_key_name_level_2;
	private static String todo_key_name = StaticValue.redis_task_todo_list_key_name;

	/**
	 * 获取cirlce task 4 keyword
	 */
	public static CrawlTaskPojo getTaskCircleKeyword() {
		CrawlTaskPojo taskPojo = null;
		try {
			byte[] byteArray = jedisOperatorUtil
					.rpop(StaticValue.redis_task_circle_keyword_queue_key_cache
							.getBytes(StaticValue.default_encoding));
			
			if (byteArray != null && byteArray.length > 0) {
				taskPojo = (CrawlTaskPojo) ObjectAndByteArrayConvertUtil
						.ByteArrayToObject(jedisOperatorUtil.HGet(
								to_do_task_key_circle_keyword
										.getBytes(StaticValue.default_encoding),
								byteArray));
				// 删除掉已经取出来的循环任务中的key
				long delTag = jedisOperatorUtil.HDel(
						to_do_task_key_circle_keyword
								.getBytes(StaticValue.default_encoding),
						byteArray);
				if (delTag > 0) {
					logger.info("success to delete circle keyword to do key!");
				} else {
					logger.info("fail to delete circle keyword to do key!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return taskPojo;
	}

	/**
	 * 获取 cirlce task 4 normal 
	 * 
	 * @return
	 */
	public static CrawlTaskPojo getTaskCircleNormal() {
		CrawlTaskPojo taskPojo = null;
		try {
			byte[] byteArray = jedisOperatorUtil
					.rpop(StaticValue.redis_task_circle_queue_key_cache
							.getBytes(StaticValue.default_encoding));
			
			if (byteArray != null && byteArray.length > 0) {
				taskPojo = (CrawlTaskPojo) ObjectAndByteArrayConvertUtil
						.ByteArrayToObject(jedisOperatorUtil.HGet(
								to_do_task_key_circle
										.getBytes(StaticValue.default_encoding),
								byteArray));
				// 删除掉已经取出来的循环任务中的key
				long delTag = jedisOperatorUtil.HDel(to_do_task_key_circle
						.getBytes(StaticValue.default_encoding), byteArray);
				if (delTag > 0) {
					logger.info("success to delete circle url to do key!");
				} else {
					logger.info("fail to delete circle url to do key!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskPojo;
	}

	/**
	 * 从二级队列中得到一个任务
	 * 
	 * @return
	 */
	public static CrawlTaskPojo getTaskSecondQueue() {
		CrawlTaskPojo taskPojo = null;
		try {
			taskPojo = (CrawlTaskPojo) ObjectAndByteArrayConvertUtil
					.ByteArrayToObject(jedisOperatorUtil.rpop(task_todo_level_2
							.getBytes(StaticValue.default_encoding)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskPojo;
	}

	/**
	 * 得到第一队列的任务
	 * 
	 * @return
	 */
	public static CrawlTaskPojo getTaskFirstQueue() {
		CrawlTaskPojo taskPojo = null;
		try {
			taskPojo = (CrawlTaskPojo) ObjectAndByteArrayConvertUtil
					.ByteArrayToObject(jedisOperatorUtil.rpop(todo_key_name
							.getBytes(StaticValue.default_encoding)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskPojo;
	}

	public static void main(String[] args) {
		getTaskCircleKeyword();
		getTaskCircleNormal();
	}
}
