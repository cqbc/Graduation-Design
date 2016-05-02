package com.cqupt.spider.threads;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.cqupt.common.enums.CrawlTypeEnum;
import com.cqupt.common.enums.TaskQueueType;
import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.IOUtil;
import com.cqupt.common.utils.JedisOperatorUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.ObjectAndByteArrayConvertUtil;
import com.cqupt.es.manager.ESIndexWsManager;
import com.cqupt.spider.manager.BloomFilterManager;
import com.cqupt.spider.manager.TaskQueueManager;
import com.cqupt.spider.pojos.CrawlData4PortalSite;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.ParserResultPojo;
import com.vaolan.extkey.utils.UrlOperatorUtil;

/**
 * 守护线程，负责抓取任务入队和处理
 * 
 * 
 */
public class DaemonThread4SaveCrawlData implements Runnable {
	public static MyLogger logger = new MyLogger(
			DaemonThread4SaveCrawlData.class);

	public ParserResultPojo resultPojo_normal = null;
	public ParserResultPojo resultPojo_error = null;
	private int thread_id;
	private boolean runnable_able = false;
	private static JedisOperatorUtil jedisOperatorUtil = null;

	
	
	public DaemonThread4SaveCrawlData(int thread_id, boolean runnable_able) {
		this.thread_id = thread_id;
		this.runnable_able = runnable_able;
		this.jedisOperatorUtil = new JedisOperatorUtil(SystemParasSpider.redis_host,
				SystemParasSpider.redis_port, SystemParasSpider.redis_password);
	}

	public static String msg_fail_meta_search_repeat = "meta search data find repeat,abandon send index to server!";
	public static String msg_success_meta_search = "index metadata search data success~";

	public static String msg_fail_web_page_repeat = "web page index find repeat,will abandon!";
	public static String msg_success_web_page = "web page index portal web data success~";

	public void run() {
		// 取得保存数据时候的根目录
		String final_normal_file = SystemParasSpider.spider_data_dir + "/normal.txt";
		String final_error_file = SystemParasSpider.spider_data_dir + "/error.txt";

		boolean is_occur_save = false;
		

		

		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String  dt = sdf.format(new Date());
		
		//得到最新的规则库
		String cache_rule_value = jedisOperatorUtil
				.getObj(StaticValue.ext_content_rule_key);
		
		
		while (this.runnable_able) {
			is_occur_save = false;
			byte[] byteArray = null;
			// 从redis中取得要保存的数据，没有取得，则sleep一段时间
			try {
				byteArray = jedisOperatorUtil
						.rpop(StaticValue.ext_content_to_save_list_key
								.getBytes(StaticValue.default_encoding));
				resultPojo_normal = null;
				
				if (byteArray != null) {
					resultPojo_normal = (ParserResultPojo) ObjectAndByteArrayConvertUtil
							.ByteArrayToObject(byteArray);
					/**
					 * 解析返回过来的结果集
					 */
					if (resultPojo_normal != null) {
						
						addNewUrlSetToCrawl(resultPojo_normal,cache_rule_value);
						
						
						// 首先将抓回来的数据索引
						CrawlData4PortalSite crawlData4PortalSite = resultPojo_normal
								.getCrawlData4PortalSite();
						

						if (crawlData4PortalSite != null) {
							// 如果是元搜索任务，则不进行如下的增量url的处理
							CrawlTypeEnum crawlEngineEnum = resultPojo_normal
									.getOwnToCrawlTaskPojo().getCrawlEngine();
							if (CrawlTypeEnum.MetaSearch == crawlEngineEnum
									|| CrawlTypeEnum.MetaSearch_NEWSPage == crawlEngineEnum) {
								
								
								String portal_url=null;
								try{
									portal_url = crawlData4PortalSite.getUrl();
								}catch(Exception e){
									e.printStackTrace();
									logger.info("新产生对象的url___======null");
									continue;
								}

								
								   //元搜索产生的结果中的url不再规则库中 则提取summary保存 
									String summary = crawlData4PortalSite.getSummary();
									if(summary==null)
										continue;
							
									
									String url = crawlData4PortalSite.getUrl();
									String  host = UrlOperatorUtil.getHost(url);
									if(host==null)
										continue;
									
									//如果规则库中包含改连接就跳过 
									if(cache_rule_value.contains(host))
										continue;
									
									
										// 更新uniq key,将url类的key去掉最后的"/"，防止A与"A/"类的重复
									String meta_search_uniq = crawlData4PortalSite.getUrlFormat();
							
//									crawlData4PortalSite.setBody("");
									IOUtil.writeFile("url_not_in_rulekey/url_"+dt, host+"\n",true,"utf-8");
									
									
										
										// 索引该条数据
										indexOneRecord(crawlData4PortalSite,
												meta_search_uniq,
												msg_success_meta_search,
												msg_fail_meta_search_repeat);
										
							} else {
								
								//url抓取 例如： http://ent.sina.com.cn/star/  首页的内容无法和规则匹配
								if(crawlData4PortalSite.getBody()!=null)
								{
									
									
								IOUtil.writeFile("save_url/url_"+dt,crawlData4PortalSite.getUrlFormat()+"\n" , true,"utf-8");
								indexOneRecord(crawlData4PortalSite,
										crawlData4PortalSite.getUrlFormat(),
										msg_success_web_page,
										msg_fail_web_page_repeat);
								}

							}
						}
						
						//记录抓取保存失败的url
						else{
							
							String url = resultPojo_normal.getFromUrl();
							logger.info("fail_url_"+url+"_"+resultPojo_normal.toErrorString());
							if(url!=null){
								IOUtil.writeFile("fail_url/url_"+dt,url+"\n" , true,"utf-8");
							}
							
						}
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			// 对抓取错误的url进行处理
			try {
				byteArray = jedisOperatorUtil
						.rpop(StaticValue.ext_content_error_list_key
								.getBytes(StaticValue.default_encoding));
				resultPojo_error = null;
				if (byteArray != null) {
					resultPojo_error = (ParserResultPojo) ObjectAndByteArrayConvertUtil
							.ByteArrayToObject(byteArray);

					if (resultPojo_error != null) {
						IOUtil.writeFile(final_error_file,
								resultPojo_error.toErrorString() + "\n", true,
								StaticValue.default_encoding);
						is_occur_save = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!is_occur_save) {
				try {
//					logger.info("没有要存储的数据，存储线程将休息一下");
					Thread.sleep(SystemParasSpider.ext_content_save_thread_sleep_time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}




	

	
	// 将待抓的url加入待抓取集合
		public static void addNewUrlSetToCrawl(ParserResultPojo resultPojo_normal,String cache_rule_value) {
			
		
			
			// 将待抓的url加入待抓取集合
			Set<String> urlSet = resultPojo_normal.getNewUrlSet();
			if (urlSet != null && (!urlSet.isEmpty())) {
				CrawlTaskPojo taskPojo = null;
				for (String url : urlSet) {
					
  				String host = UrlOperatorUtil.getHost(url);
					if(host==null)
						continue;
					
					//如果url在规则库当中   则在去抓取具体的详情页
					if(cache_rule_value.contains(host))
					{
					
					taskPojo = new CrawlTaskPojo();
					taskPojo.setValue(url);
					taskPojo.setMedia_type(resultPojo_normal
							.getOwnToCrawlTaskPojo().getMedia_type());
					taskPojo.setSource_title(resultPojo_normal
							.getOwnToCrawlTaskPojo().getSource_title());
					taskPojo.setDepth(resultPojo_normal.getOwnToCrawlTaskPojo()
							.getDepth());
					taskPojo.setCurrent_depth(resultPojo_normal.getCurrent_depth());
					taskPojo.setTopN(resultPojo_normal.getOwnToCrawlTaskPojo()
							.getTopN());
					taskPojo.setLevel(resultPojo_normal.getOwnToCrawlTaskPojo()
							.getLevel());
					taskPojo.setType(resultPojo_normal.getOwnToCrawlTaskPojo()
							.getType());
					
					TaskQueueManager.addTask(taskPojo, TaskQueueType.To_Visit);
					
					}
				}
			}
		}
		
	public static void indexOneRecord(
			CrawlData4PortalSite crawlData4PortalSite, String uniq_string,
			String successMsg, String failMsg) {
		if (BloomFilterManager.containsTaskInVisitedBloom(uniq_string)) {
			logger.info(failMsg);
			return;
		}
		List<CrawlData4PortalSite> pojoList = new LinkedList<CrawlData4PortalSite>();
		pojoList.add(crawlData4PortalSite);
		ESIndexWsManager.addBatchIndex4PortalWeb(
				StaticValue.index_name_yuqing,
				StaticValue.type_name_portals_web_data,
				pojoList);

		// 将已抓取完成的任务，放置到done task bloom filter一份
		BloomFilterManager.addToBloom(TaskQueueType.Visited, uniq_string);
		logger.info(successMsg);
	}

}
