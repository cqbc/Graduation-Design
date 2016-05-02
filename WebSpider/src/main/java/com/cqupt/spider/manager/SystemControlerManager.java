package com.cqupt.spider.manager;

import java.util.List;

import com.cqupt.common.enums.CrawlTypeEnum;
import com.cqupt.common.enums.SearchEngineEnum;
import com.cqupt.common.enums.TaskQueueType;
import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.FileOperatorUtil;
import com.cqupt.common.utils.IOUtil;
import com.cqupt.common.utils.JedisOperatorUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.ObjectAndByteArrayConvertUtil;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.common.utils.TaskTxt2ObjectUtil;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.IResultPojo;
import com.cqupt.spider.pojos.ParserResultPojo;
import com.cqupt.spider.pojos.RetStatus;
import com.cqupt.spider.pojos.pattern.MatchCrawlPojo;
import com.cqupt.spider.task.TaskDispacherManager;
import com.cqupt.spider.task.extractor.ExtSimpleContentRuleManager;
import com.cqupt.spider.task.process.MetaSearchProcess;
import com.cqupt.spider.task.process.WebUrlProcess;
import com.cqupt.spider.threads.DaemonThread4SaveCacheData;
import com.cqupt.spider.threads.DaemonThread4UpdateRuleKey;

/**
 * 系统启动官理器
 * 
 */
public class SystemControlerManager {
	// 日志
	public static MyLogger logger = new MyLogger(SystemControlerManager.class);

	public static JedisOperatorUtil jedisOperatorUtil = null;
	public static ExtSimpleContentRuleManager extractorContentManager4SimpleCrawler = null;
	static {
		
		jedisOperatorUtil = new JedisOperatorUtil(SystemParasSpider.redis_host,
				SystemParasSpider.redis_port, SystemParasSpider.redis_password);
		
		if (SystemParasSpider.node_is_master) {
			// 说明是主节点
			// 将拿到的最新的规则串放到redis一份
			// 新的获取规则已改，由init中的独立线程去维护规则库，master和slave均是直接从redis中取值更新自己的manager
			extractorContentManager4SimpleCrawler = new ExtSimpleContentRuleManager(
					jedisOperatorUtil.getObj(StaticValue.ext_content_rule_key),
					false);

		} else {// 说明是子节点
			extractorContentManager4SimpleCrawler = new ExtSimpleContentRuleManager(
					jedisOperatorUtil.getObj(StaticValue.ext_content_rule_key),
					false);
		}
	}

	// 规则库守护线程
	public void init() {
		// 规则库守护线程开启
		DaemonThread4UpdateRuleKey daemonThread4UpdateRuleKey = new DaemonThread4UpdateRuleKey(
				1, true);
		Thread update_rule_key_thread = new Thread(daemonThread4UpdateRuleKey);
		update_rule_key_thread.start();

		
		// 开启缓存数据保存线程
		DaemonThread4SaveCacheData daemonThread4SaveCacheData = new DaemonThread4SaveCacheData(
				true);
		Thread cache_data_thread = new Thread(daemonThread4SaveCacheData);
		cache_data_thread.start();

		// 为保证规则库至少一次写入到redis cache中，等待几秒
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("system init occur error,will exit!");
			System.exit(-1);
		}
	}

	private boolean runningFlag = true;

	private void loadSeedDir(String seedDir) {
		List<String> taskFilePathList = FileOperatorUtil.getAllFilePathList(
				seedDir, null);
		
		if (StringOperatorUtil.isNotBlankCollection(taskFilePathList)) {
			for (String filePath : taskFilePathList) {
				// 每处理一个文件，均设置一下该值
				runningFlag = true;
				addFileTaskToQueue(filePath);
				// 认为该文件处理完成，将直接从硬盘删除掉
//				FileOperatorUtil.removeFile(filePath);
				logger.info(filePath + ",数据文件已加载到内存，将删除之!");
			}
			logger.info("once load seed dir,the seeds task have pushed all to the queue");
		}
	}

	public void addFileTaskToQueue(String filePath) {
		/**
		 * 首先对输入的关键词库串或是url串进行去重与计数,即程序的输入就是一个不去重的keyword或是url的文本文件，一行一一个即可
		 */
		/**
		 * 先判定几个要填充的redis中key的名称，就不用在后边用if...else去每次去判定了
		 */
		String todo_key_name = null;
		String finish_key_name = null;

		// 拿到队列在redis所对应的key name
		todo_key_name = StaticValue.redis_task_todo_list_key_name;
		finish_key_name = StaticValue.redis_task_finished_key_name;

		long begin_line_number = 0;
		List<CrawlTaskPojo> taskPojoList = null;
		List<String> txtContent = null;
		long task_total = 0;
		long task_finish_count = 0;

		int url_list_size = 0;
		boolean is_inject_new_url = false;

		String finish_value = null;
		// 取得已完成的任务的数量
		finish_value = jedisOperatorUtil.getObj(finish_key_name);
		
		if (StringOperatorUtil.isNotBlank(finish_value)) {
			task_finish_count = Integer.parseInt(finish_value);
		}
		// 取得现在池子中还有多少todo url list size
		long redis_list_size_current = jedisOperatorUtil.llen(todo_key_name);

		// 已完成行业+todoListSize
		task_total = task_finish_count + redis_list_size_current;

		// 初始化时，总数量即为现在redis中已抓取的数量+todo list size
		begin_line_number = 0;// 只要是开始读取，都从0开始

		// 循环遍历
		while (runningFlag) {
			while (task_total < SystemParasSpider.node_seeds_max_size) {
				if (redis_list_size_current < SystemParasSpider.node_redis_size_threshold) {
					logger.info("redis list size is little to threshold，will inject a batch of "
							+ "tasks!");
					
					
					// 读取出指定的文件中的内容
					txtContent = IOUtil.getLineArrayFromFile(filePath,
							StaticValue.default_encoding, begin_line_number,
							Math.min(SystemParasSpider.node_seeds_inject_batch_size,
									SystemParasSpider.node_seeds_max_size));

					taskPojoList = TaskTxt2ObjectUtil
							.convertTxt2Object(txtContent);

					// 说明已经读取完成，该文件的任务已添加完成
					if (StringOperatorUtil.isBlankCollection(taskPojoList)) {
						runningFlag = false;
						logger.info("the file task list have added to the redis quene all!");
						break;
					}


					try {
						if (StringOperatorUtil
								.isNotBlankCollection(taskPojoList)) {
							for (CrawlTaskPojo taskPojo : taskPojoList) {
								// 添加到正常的待访问队列
								TaskQueueManager.addTaskToDoQueue(
										todo_key_name, taskPojo, null, true);
								// 暂定只有从这个途径过添加的任务都是原始任务,根据条件加入循环队列
								if (SystemParasSpider.task_circle_enable
										&& taskPojo.isEnableToCircle()) {
									
									if(BloomFilterManager.containsTaskInToVisiteBloom(taskPojo.getValueFormat()))
										TaskQueueManager.addTask(taskPojo,TaskQueueType.Circle_Visit);
								}
								
							}
							logger.info("从种子文件中批量添加任务到种子队列中!");
						} else {
							logger.info("种子文件中没有发现新增的任务，不做添加任务操作!");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					url_list_size = taskPojoList.size();
					begin_line_number += url_list_size;
					redis_list_size_current += url_list_size;
					// url总数计算
					task_total = task_total + url_list_size;
					is_inject_new_url = true;

					// 说明文件已经提取结束
					if (url_list_size == 0) {
						logger.info("one file task list have added to the redis quene all,will remove it!");
						runningFlag = false;
					}
				} else {
					break;
				}
			}

			// 写一些守护信息
			redis_list_size_current = jedisOperatorUtil.llen(todo_key_name);

			task_finish_count = task_total - redis_list_size_current;

			// 将已完成任务写入redis中
			jedisOperatorUtil.putObj(finish_key_name, "" + task_finish_count);

			// 输出些守护信息，在此简单处理
			logger.info("current to do task size in redis---"
					+ redis_list_size_current);

			try {
				// 说明文件已经提取结束
				if (is_inject_new_url && url_list_size == 0) {
					runningFlag = false;
				}
				Thread.sleep(SystemParasSpider.node_inject_urls_sleep_time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			is_inject_new_url = false;
		}
	}

	public RetStatus startServer() {
		// 首先进行初始相关
		init();

		// 开始守护线程保存数据
		ThreadManager4DataSave.startDaemon(SystemParasSpider.ext_content_save_threads_numbers);

		if (SystemParasSpider.ext_content_load_seeds_is_circle) {
			while (true) {
				loadSeedDir(SystemParasSpider.spider_seeds_root_path);
				// load完一次种子目录，休息指定时间后再次扫描加载
				try {
					Thread.sleep(SystemParasSpider.ext_content_load_seeds_sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			loadSeedDir(SystemParasSpider.spider_seeds_root_path);
		}

		return null;
	}

	
	
	
	
	public static SearchEngineEnum[] searchEngineEnumArray = {
		SearchEngineEnum.Baidu, SearchEngineEnum.Sogou,
		SearchEngineEnum.SinaNews
	};

	// 从redis端接受任务，暂定为以一个url为单位进行获取
	public void startClient() {
		// 在启动之处启动守护线程,来更新跟踪规则状态是否发生更新,如有更新，则同步更新
		ThreadManager4DataSave.startDaemon4UpdateRuleKey4Client(1);

		while (runningFlag) {
			// 取得一个任务，在方法内调度
			CrawlTaskPojo taskPojo = TaskDispacherManager.getCrawlTask();
			
			
			if (taskPojo != null) {
				// 在这个位置保证下唯一
				IResultPojo resultPojo = null;
				/**
				 * 首先判断任务的类型,看看是元搜索，还是网页搜索
				 */
				if (taskPojo.getCrawlEngine() == CrawlTypeEnum.MetaSearch_NEWSPage) {
					List<IResultPojo> resultPojoList = null;
					synchronized (SystemControlerManager.class) {
						
						resultPojoList = MetaSearchProcess
								.processTaskControler(taskPojo, false,
										searchEngineEnumArray);
					}
					if (StringOperatorUtil.isNotBlankCollection(resultPojoList)) {
						for (IResultPojo resultItem : resultPojoList) {
							if(resultItem==null)
								continue;
							saveResult(taskPojo, resultItem);
							logger.info("metasearch keyword="
									+ taskPojo.getValue()
									+ ",newUrlSet.size()="
									+ (resultItem.getNewUrlSet() == null ? 0
											: resultItem.getNewUrlSet().size()));
						}
					}
				} else {
						// 保证更新规则库跟使用不冲突!
						synchronized (SystemControlerManager.class) {
							resultPojo = WebUrlProcess.processTask(taskPojo, false);
					}
					
					
					if(resultPojo!=null)
					{
						// 保存抓取完成的一个结果
						saveResult(taskPojo, resultPojo);
					}
					
					
				}
				
			} else {
				try {
					Thread.sleep(SystemParasSpider.node_inject_urls_sleep_time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		logger.info("crawl client run finished,this is not right,please check!");
	}

	// 处理一个保存任务
	private static void saveResult(CrawlTaskPojo taskPojo,
			IResultPojo resultPojo) {
		if (resultPojo != null && resultPojo.isNormal) {
			
			try {
				jedisOperatorUtil.lpush(
						StaticValue.ext_content_to_save_list_key
								.getBytes(StaticValue.default_encoding),
						ObjectAndByteArrayConvertUtil
								.ObjectToByteArray(resultPojo));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 说明该url遇到错误,直接加入到redis对列中
			resultPojo = new ParserResultPojo();
			System.out.println("error url pojo---" + taskPojo);
			resultPojo.setNormal(false);
			
			
			
			MatchCrawlPojo matchCrawlPojo = extractorContentManager4SimpleCrawler
					.getMatchCrawlPojo(resultPojo.getFromUrl());
			if (matchCrawlPojo != null) {
				resultPojo.setCrawlEngine(matchCrawlPojo.getCrawlEngineEnum());
			} else {
				resultPojo.setCrawlEngine(null);
			}

			// 当fromUrl为null说明是元搜索，将其值赋于fromUrl属性
			if (resultPojo.getFromUrl() == null) {
				resultPojo.setFromUrl(taskPojo.getValue());
			}
			resultPojo.setDesc("procee_error");
			try {
				jedisOperatorUtil.lpush(StaticValue.ext_content_error_list_key
						.getBytes(StaticValue.default_encoding),
						ObjectAndByteArrayConvertUtil
								.ObjectToByteArray(resultPojo));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 暂用该类作为测试类
	public static void main(String[] args) {
		CrawlTaskPojo taskPojo = new CrawlTaskPojo();
		// taskPojo.setValue("http://news.sina.com.cn/c/2015-03-01/150031554777.shtml");
		// taskPojo.setSource_title("新浪新闻");
		// taskPojo.setValue("http://news.qq.com/a/20150303/049418.htm");
		// taskPojo.setSource_title("腾讯新闻");
		taskPojo.setValue("http://china.chinadaily.com.cn/2015-10/15/content_22199264.htm");
		//taskPojo.setSource_title("新闻中心——国际");
		// taskPojo.setValue(args[0]);
		//taskPojo.setSource_title(args[1]);
		
		
		WebUrlProcess.processTask(taskPojo, true);

		System.out.println("done!");

	}

}
