
package com.cqupt.spider.threads;

import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.spider.manager.BloomFilterManager;
import com.cqupt.spider.manager.TaskQueueManager;

/**
 * 守护线程，负责周期性保存某些数据
 * 
 */
public class DaemonThread4SaveCacheData implements Runnable {
	public static MyLogger logger = new MyLogger(
			DaemonThread4SaveCacheData.class);
	private boolean runnable_able = false;

	public DaemonThread4SaveCacheData(boolean runnable_able) {
		this.runnable_able = runnable_able;
	}

	public void run() {
		while (runnable_able) {
			try {
				Thread.sleep(SystemParasSpider.cache_data_circle_save_interval);
				BloomFilterManager.saveToRedis();
				
				//定期存储circle set
				TaskQueueManager.saveToRedis();
			} catch (Exception e) {
				logger.info("save cache data occur exception,please check!");
				e.printStackTrace();
			}
		}
	}
}
