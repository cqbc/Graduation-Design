package com.cqupt.spider.threads;

import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.JedisOperatorUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.spider.manager.SystemControlerManager;

/**
 * 守护线程，负责守护工作 ;1、周期性更新规则库
 * 
 */
public class DaemonThread4UpdateRuleKeyClient implements Runnable {
	public static MyLogger logger = new MyLogger(
			DaemonThread4UpdateRuleKeyClient.class);

	private int thread_id;
	private boolean runnable_able = true;
	private JedisOperatorUtil jedisOperatorUtil = null;

	public DaemonThread4UpdateRuleKeyClient(int thread_id, boolean runnable_able) {
		this.thread_id = thread_id;
		this.runnable_able = runnable_able;
		this.jedisOperatorUtil = new JedisOperatorUtil(SystemParasSpider.redis_host,
				SystemParasSpider.redis_port, SystemParasSpider.redis_password);
	}

	public void run() {
		String status = null;
		while (this.runnable_able) {
			// 首先进行sleep
			try {
				// 休息一个SystemParas.ext_content_rule_key_syn_circle就检查是否有更新
				Thread.sleep(SystemParasSpider.ext_content_rule_key_syn_circle);
				status = jedisOperatorUtil
						.getObj(StaticValue.ext_content_rule_key_is_changed);
				if ("1".equals(status)) {
					String rule_string = jedisOperatorUtil
							.getObj(StaticValue.ext_content_rule_key);
					
					synchronized (SystemControlerManager.class) {
						SystemControlerManager.extractorContentManager4SimpleCrawler
								.resetRule(rule_string, false);
					}
					logger.info("规则库有更新，更新完成!");
				} else {
//					logger.info("规则库没有更新!");
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		logger.info("规则库更新列表的守护线程被结束了，请检查程序的正确性!");
	}
}
