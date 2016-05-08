package com.cqupt.spider.task.process;

import com.cqupt.common.enums.CrawlEngineEnum;
import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.HtmlParserUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.spider.crawl.Crawl4HttpClient;
import com.cqupt.spider.crawl.Crawl4Phantomjs;
import com.cqupt.spider.manager.SystemControlerManager;
import com.cqupt.spider.pojos.CrawlResultPojo;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.HttpRequestPojo;
import com.cqupt.spider.pojos.IResultPojo;
import com.cqupt.spider.pojos.ParserResultPojo;
import com.cqupt.spider.pojos.ProxyPojo;
import com.cqupt.spider.pojos.pattern.MatchCrawlPojo;
import com.cqupt.spider.task.parser.HtmlSourceParser;
import com.vaolan.extkey.utils.UrlOperatorUtil;

public class WebUrlProcess {
	
	public static MyLogger logger = new MyLogger(SystemControlerManager.class);
	
	public static HtmlParserUtil htmlParserUtil = new HtmlParserUtil();

	// 处理一个网页的开始
		public static IResultPojo processTask(CrawlTaskPojo taskPojo, boolean isTest) {
			String url = taskPojo.getValue();

		
			IResultPojo resultPojo = null;
			ParserResultPojo parser_resultPojo = null;
			
			MatchCrawlPojo matchCrawlPojo = SystemControlerManager.extractorContentManager4SimpleCrawler
					.getMatchCrawlPojo(url);
			
			
			
			if (matchCrawlPojo != null && matchCrawlPojo.isMatch()) {
				// 在这里将格式化后的format_url值更新url
				url = matchCrawlPojo.getFormat_url();
				if (matchCrawlPojo.getCrawlEngineEnum() == CrawlEngineEnum.Phantomjs) {
					resultPojo = Crawl4Phantomjs.crawlHtmlSourceByRandomUrl(url);
				} else {
					
					resultPojo = crawlHtmlSourceByRandomUrl4HttpClient(url, null);
					
					if (resultPojo != null
							&& StringOperatorUtil.isNotBlank(resultPojo
									.getFromUrl())) {
						url = resultPojo.getFromUrl();
						taskPojo.setValue(url);
					}

				}

				
				if (resultPojo != null && resultPojo.isNormal()) {
					parser_resultPojo = HtmlSourceParser.parserHtmlSource(taskPojo,
							resultPojo.getHtmlSource(), isTest);
					
					if (parser_resultPojo == null) {
						logger.info(url + ",解析出现问题，请检查!");
						System.out.println(parser_resultPojo);
						return parser_resultPojo;
					} else if (parser_resultPojo.isNormal()) {
						// 说明是处理过程没出现异常!
						if (parser_resultPojo.isMatchRegex()) {
							// 说明规则库匹配上了
							parser_resultPojo.setCrawlEngine(matchCrawlPojo
									.getCrawlEngineEnum());
							System.out.println(parser_resultPojo.toString());
							System.out.println("Match Rule Sucess!");

							// 在这里将有效模板的统计次数上报到redis缓存中
							String field = parser_resultPojo.getRuleName() + "#"
									+ parser_resultPojo.getSource_title();

							
								boolean is_exists = SystemControlerManager.jedisOperatorUtil
										.HContainsFields(
												StaticValue.statistic_key_template,
												field);
								
								if (is_exists) {
									String value = SystemControlerManager.jedisOperatorUtil.HGet(
											StaticValue.statistic_key_template,
											field);
									if (StringOperatorUtil.isNotBlank(value)) {
										SystemControlerManager.jedisOperatorUtil.HSet(
												StaticValue.statistic_key_template,
												field,
												(Integer.parseInt(value) + 1) + "");
										// System.out.println("value---"+value);
										logger.info("update template statistic successful");
									} else {
										logger.info("模板有效性统计出现value非数值异常，请检查!");
									}
								} else {
									SystemControlerManager.jedisOperatorUtil.HSet(
											StaticValue.statistic_key_template,
											field, "1");
									logger.info("update template statistic successful");
								}
							
						} else {
							// 说明规则库没有匹配上
							System.out.println("Match Rule Fail!");
						}
					}
				} else {
					logger.info(url + ",下载失败或处理出现异常,请检查!");
				}
			} else {
				
				
				
				System.out.println("任务url中提取的host不在规则库中，请检查!");
			}
			return parser_resultPojo;
		}
		
		// 抓取任意URL的内容或截图
		public static CrawlResultPojo crawlHtmlSourceByRandomUrl4HttpClient(
				String pageUrl, ProxyPojo proxyPojo) {
			CrawlResultPojo resultPojo = new CrawlResultPojo();
			if (StringOperatorUtil.isBlank(pageUrl)) {
				resultPojo.setNormal(false);
				return resultPojo;
			}
			HttpRequestPojo requestPojo = new HttpRequestPojo(pageUrl);

			if (requestPojo.getHeaderMap() == null) {
				String host = UrlOperatorUtil.getHost(pageUrl);
				StaticValue.headerMap.put("Host", host);
				StaticValue.headerMap.put("Connection", "Keep-Alive");
				StaticValue.headerMap.put("Accept",
						"text/html, application/xhtml+xml, */*");
				StaticValue.headerMap.put("Accept-Encoding", "gzip, deflate");
				StaticValue.headerMap.put("Accept-Language", "zh-CN");

				requestPojo.setHeaderMap(StaticValue.headerMap);
			}
			int refresh_time = 0;
			// 如果遇到http请求错误，则重复请求http_req_error_repeat次来确定是否能得到内容
			for (int i = 0; i < SystemParasSpider.http_req_error_repeat_number; i++) {
				try {
					String htmlSource = Crawl4HttpClient.crawlWebPage(requestPojo);
					if (htmlSource == null || htmlSource.isEmpty()) {
						continue;
					}
					
					// 在这里做页面内是否有内容跳转
					String refresh_location = htmlParserUtil.getRefreshLocationUrl(
							requestPojo.getUrl(), htmlSource);
					if (StringOperatorUtil.isNotBlank(refresh_location)) {
						if (refresh_time <= 3) {
							requestPojo.setUrl(refresh_location);
							logger.info("find webpage refresh location,will trace continue the jump,"
									+ refresh_location);
							i = 0;
							// 此时的url产生了变化
							resultPojo.setFromUrl(refresh_location);

							refresh_time++;
							continue;
						} else {
							logger.info("refresh location超过最大次数，将跳过该url--"
									+ pageUrl);
						}
					}
					resultPojo.setNormal(true);
					resultPojo.setHtmlSource(htmlSource);
					return resultPojo;
				} catch (Exception e) {
					resultPojo.setNormal(false);
					resultPojo.setDesc(e.getLocalizedMessage());
					e.printStackTrace();
					logger.info("httpclient请求过程中出现问题，请检查!");
					try {
						Thread.sleep(SystemParasSpider.http_req_once_wait_time);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			return resultPojo;
		}

		

		

		
}
