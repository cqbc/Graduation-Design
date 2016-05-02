package com.cqupt.spider.crawl.metasearch;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.cqupt.common.enums.CrawlTypeEnum;
import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.DateUtil;
import com.cqupt.common.utils.IOUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.RegexPaserUtil;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.spider.manager.PhantomManager;
import com.cqupt.spider.manager.ProxyManager;
import com.cqupt.spider.pojos.CrawlConfigParaPojo;
import com.cqupt.spider.pojos.CrawlData4PortalSite;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.ProxyPojo;
import com.vaolan.extkey.utils.UrlOperatorUtil;
import com.vaolan.parser.JsoupHtmlParser;
import com.vaolan.status.DataFormatStatus;

public class Crawl4Baidu {
	// 日志
	public static MyLogger logger = new MyLogger(Crawl4Baidu.class);
	/*
	 * 得到搜索结果第一条的content block
	 */
	public static String block_beginTag = "class=\"result";
	public static String block_endTag = "class=\"f13\"";

	/**
	 * 在block截取到相应的分词结果
	 */
	public static String content_beginTag = "<em>";
	public static String content_endTag = "</em>";

	/**
	 * 为得到每为搜索结果的链接块
	 */
	public static String block_beginTag_link = "<h3 class=\"t\"";
	public static String block_endTag_link = "</h3>";

	/**
	 * 提取链接块的链接地址
	 */
	public static String content_beginTag_link = "href=\"";
	public static String content_endTag_link = "\"";

	public static String blockSelector = "div";
	public static List<String> removeSelector = new LinkedList<String>();
	public static List<String> itemSelector = new LinkedList<String>();
	public static List<String> titleSelector = new LinkedList<String>();
	public static List<String> authorSelector = new LinkedList<String>();
	public static List<String> bodySelector = new LinkedList<String>();
	public static List<String> bodySelector_2 = new LinkedList<String>();

	public static List<String> remove_selecot_4_body = new LinkedList<String>();

	// 日期处理类
	private static DateUtil dateUtil = new DateUtil();

	static {
		init();
	}

	public static void init() {
		blockSelector = "div";

		removeSelector.add("p");
		removeSelector.add("span");

		itemSelector.add("div#content_left>div>div.result");

		titleSelector.add("h3.c-title>a");

		authorSelector.add("div>p.c-author");

		bodySelector.add("div.c-summary>div.c-span18");

		bodySelector_2.add("div.c-summary");

		remove_selecot_4_body.add("p");
		remove_selecot_4_body.add("span");
	}

	/**
	 * 得到的root_url下输入query后的搜索结果的第一页的所有标红词条
	 * 
	 * @param root_url
	 * @param query
	 * @param proxyPojo
	 * @return
	 */
	public static Set<String> getSplitResult(String root_url, String query,
			ProxyPojo proxyPojo) {
		if (StringOperatorUtil.isBlank(root_url)
				|| StringOperatorUtil.isBlank(query)) {
			return null;
		}
		String txt_data_sub_dir = null;
		String txtFileString = null;

		// 提取每个网页中的单条搜索出来的记录的正则
		RegexPaserUtil regexPaserUtil = new RegexPaserUtil(block_beginTag,
				block_endTag, RegexPaserUtil.TEXTEGEXANDNRT);
		// 每条搜索结果中的标红提取
		RegexPaserUtil itemRegexPaserUtil = new RegexPaserUtil(
				content_beginTag, content_endTag, RegexPaserUtil.TEXTEGEXANDNRT);
		// 取得第一个搜索结果串的html block,从而得到最合理的分词结果
		String first_item_block_html = null;
		// 字符串暂存变量
		String temp_key = null;

		// 提取出来的搜索结果集合存储变量
		Set<String> splitSet = new HashSet<String>();
		// 遇到http请求错误，则重复请求http_req_error_repeat次
		for (int i = 0; i < SystemParasSpider.http_req_error_repeat_number; i++) {
			try {
				CrawlConfigParaPojo crawlConfigParaPojo = PhantomManager
						.crawlKeywordSeg4MetaSearch(root_url, query, proxyPojo);

				txt_data_sub_dir = crawlConfigParaPojo
						.getData_write_to_file_root_path()
						+ crawlConfigParaPojo.getData_write_to_file_sub_path();
				File[] fileArray = new File(txt_data_sub_dir).listFiles();

				// 在这里加入对代理无效问题的解决，在上层或下层都不太合适，暂置于此
				if (proxyPojo != null
						&& (fileArray == null || fileArray.length == 0)) {
					// 此时没有抓取到任务数据，认为是代理
					ProxyManager.removeProxy(proxyPojo);
					proxyPojo.reset(ProxyManager.getOneProxy());
					logger.info("此时认为暂理无效，更换代理试之");
					continue;
				}

				if (fileArray != null) {
					// 读取出其下的每个txtFile
					for (File txtFile : fileArray) {
						txtFileString = IOUtil.readDirOrFile(
								txtFile.getAbsolutePath(),
								StaticValue.default_encoding);

						regexPaserUtil.reset(txtFileString);

						while (regexPaserUtil.hasNext()) {
							first_item_block_html = regexPaserUtil.getNext();

							if (first_item_block_html != null) {
								itemRegexPaserUtil.reset(first_item_block_html);
								while (itemRegexPaserUtil.hasNext()) {
									temp_key = itemRegexPaserUtil.getNextText();
									splitSet.add(temp_key);
								}
							}
						}
					}

				}
				// System.out.println("txt_data_sub_dir---"+txt_data_sub_dir);
				// 请求一次后，休息一下
				Thread.sleep(SystemParasSpider.http_req_once_wait_time);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("phantomjs请求过程中出现问题，请检查!");
				try {
					Thread.sleep(SystemParasSpider.http_req_once_wait_time);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		return splitSet;
	}

	/**
	 * 得到指定搜索结果页的对应的各正文内容的链接的url,存储到set中，中间即去重
	 * 
	 * @param root_url
	 * @param query
	 * @param proxyPojo
	 * @return
	 */
	public static Set<String> getAllSearchLink(String root_url, String query,
			ProxyPojo proxyPojo) {
		if (StringOperatorUtil.isBlank(root_url)
				|| StringOperatorUtil.isBlank(query)) {
			return null;
		}
		String txt_data_sub_dir = null;
		String txtFileString = null;

		// 提取每个网页中的单条搜索出来的记录的正则
		RegexPaserUtil regexPaserUtil = new RegexPaserUtil(block_beginTag_link,
				block_endTag_link, RegexPaserUtil.TEXTEGEXANDNRT);
		// 每条搜索结果中的标红提取
		RegexPaserUtil itemRegexPaserUtil = new RegexPaserUtil(
				content_beginTag_link, content_endTag_link,
				RegexPaserUtil.TEXTEGEXANDNRT);
		// 取得第一个搜索结果串的html block,从而得到最合理的分词结果
		String first_item_block_html = null;
		// 字符串暂存变量
		String temp_key = null;

		// 提取出来的搜索结果集合存储变量
		Set<String> splitSet = new HashSet<String>();
		// 遇到http请求错误，则重复请求http_req_error_repeat次
		for (int i = 0; i < SystemParasSpider.http_req_error_repeat_number; i++) {
			try {
				CrawlConfigParaPojo crawlConfigParaPojo = PhantomManager
						.crawlKeywordSeg4MetaSearch(root_url, query, proxyPojo);

				txt_data_sub_dir = crawlConfigParaPojo
						.getData_write_to_file_root_path()
						+ crawlConfigParaPojo.getData_write_to_file_sub_path();
				File[] fileArray = new File(txt_data_sub_dir).listFiles();

				// 在这里加入对代理无效问题的解决，在上层或下层都不太合适，暂置于此
				if (proxyPojo != null
						&& (fileArray == null || fileArray.length == 0)) {
					// 此时没有抓取到任务数据，认为是代理的问题
					logger.info("此时认为暂理无效，更换代理试之");
					ProxyManager.removeProxy(proxyPojo);

					proxyPojo.reset(ProxyManager.getOneProxy());
					continue;
				}

				// 读取出其下的每个txtFile
				if (fileArray != null) {
					for (File txtFile : fileArray) {
						txtFileString = IOUtil.readDirOrFile(
								txtFile.getAbsolutePath(),
								StaticValue.default_encoding);

						regexPaserUtil.reset(txtFileString);

						while (regexPaserUtil.hasNext()) {
							first_item_block_html = regexPaserUtil.getNext();

							if (first_item_block_html != null) {
								itemRegexPaserUtil.reset(first_item_block_html);
								while (itemRegexPaserUtil.hasNext()) {
									temp_key = itemRegexPaserUtil.getNextText();
									splitSet.add(temp_key);
								}
							}
						}
					}
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("phantomjs请求过程中出现问题，请检查!");
				try {
					Thread.sleep(SystemParasSpider.http_req_once_wait_time);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		return splitSet;
	}

	/**
	 * 根据一系列的url抓取其对应的web页面
	 * 
	 * @param root_url
	 * @param query
	 * @param proxyPojo
	 */
	public static void crawlBodyByUrlSet(String root_url, String query,
			ProxyPojo proxyPojo) {
		if (StringOperatorUtil.isBlank(root_url)
				|| StringOperatorUtil.isBlank(query)) {
			return;
		}
		Set<String> urlSet = getAllSearchLink(root_url, query, proxyPojo);
		int i = 1;
		for (String linkUrl : urlSet) {
			PhantomManager.crawlBody4MetaSearch(linkUrl, query, i, proxyPojo);
			i++;
		}
	}

	// 抓取任意URL的内容或截图
	public static void crawlBodyByRandomUrl(String pageUrl, ProxyPojo proxyPojo) {
		if (StringOperatorUtil.isBlank(pageUrl)) {
			return;
		}
		PhantomManager.crawlBody4MetaSearch(pageUrl, proxyPojo, null);
	}

	/**
	 * 组合所有的百度新闻的搜索结果
	 * 
	 * @param root_url
	 * @param query
	 * @return
	 */
	public static List<CrawlData4PortalSite> getAllNewsSearchResult(
			CrawlTaskPojo taskPojo, String root_url, boolean isTest) {
		if (taskPojo == null) {
			return null;
		}
		// taskPojo.get
		String query = taskPojo.getValue();

		String txt_data_sub_dir = null;
		String txtFileString = null;

		// 提取出来的搜索结果集合存储变量
		LinkedList<CrawlData4PortalSite> searchResultList = new LinkedList<CrawlData4PortalSite>();
		// 遇到http请求错误，则重复请求http_req_error_repeat次
		for (int i = 0; i < SystemParasSpider.http_req_error_repeat_number; i++) {
			try {
				ProxyPojo proxyPojo = null;
				if (SystemParasSpider.proxy_open) {
					proxyPojo = ProxyManager.getOneProxy();
					System.out.println(proxyPojo);
				}
				CrawlConfigParaPojo crawlConfigParaPojo = PhantomManager
						.crawlKeywordSeg4NewsSearchResult(root_url, query,
								proxyPojo);

				txt_data_sub_dir = crawlConfigParaPojo
						.getData_write_to_file_root_path()
						+ crawlConfigParaPojo.getData_write_to_file_sub_path();
				File[] fileArray = new File(txt_data_sub_dir).listFiles();

				// 读取出其下的每个txtFile
				if (fileArray != null) {
					for (File txtFile : fileArray) {
						// 读出单个文件的文本内容
						txtFileString = IOUtil.readDirOrFile(
								txtFile.getAbsolutePath(),
								StaticValue.default_encoding);

						// 均采用jsoup+正则合力解析
						// 取得每个条目的完整html块
						List<String> itemList = JsoupHtmlParser
								.getNodeContentBySelector(txtFileString,
										itemSelector,
										DataFormatStatus.TagAllContent, false);

						if (StringOperatorUtil.isNotBlankCollection(itemList)) {
							for (String itemBlock : itemList) {
								// 逐条进行解析
								// 首先取得title部分
								List<String> titleList = JsoupHtmlParser
										.getNodeContentBySelector(itemBlock,
												titleSelector,
												DataFormatStatus.TagAllContent,
												false);
								CrawlData4PortalSite crawlData4PortalSite = null;
								if (StringOperatorUtil
										.isNotBlankCollection(titleList)) {
									crawlData4PortalSite = new CrawlData4PortalSite();

									String title_block = titleList.get(0);
									crawlData4PortalSite
											.setTitle(JsoupHtmlParser
													.getCleanTxt(title_block));

									// 取得链接
									String href = JsoupHtmlParser
											.getAttributeValue(title_block,
													"href");
									if (UrlOperatorUtil.isValidUrl(href)) {
										crawlData4PortalSite.setUrl(href);
									} else {
										logger.info("百度元搜索时，提取的链接有问题，请检查!");
									}

									// 取得作者和发布时间
									List<String> authorList = JsoupHtmlParser
											.getNodeContentBySelector(
													itemBlock, authorSelector,
													DataFormatStatus.CleanTxt,
													false);
									if (StringOperatorUtil
											.isNotBlankCollection(authorList)) {
										String author_and_time = authorList
												.get(0);
										if (StringOperatorUtil
												.isNotBlank(author_and_time)) {
											String[] arr = author_and_time
													.split("&nbsp;&nbsp;");
											if (arr.length == 2) {
												crawlData4PortalSite
														.setAuthor(arr[0]);
												crawlData4PortalSite
														.setPublish_time_string(arr[1]);
												Date publish_date = dateUtil
														.getDateByNoneStructure4News(crawlData4PortalSite
																.getPublish_time_string());
												if (publish_date != null) {
													crawlData4PortalSite
															.setPublish_time_long(publish_date
																	.getTime());
												}
											}
										}
									}

									// 取得摘要，并将摘要作为正文
									List<String> bodyList = JsoupHtmlParser
											.getNodeContentBySelector(
													itemBlock,
													bodySelector,
													DataFormatStatus.TagAllContent,
													false);
									if (StringOperatorUtil
											.isNotBlankCollection(bodyList)) {
										String body_content = bodyList.get(0);
										body_content = JsoupHtmlParser
												.removeInnerContent(
														body_content, "div",
														remove_selecot_4_body);
										crawlData4PortalSite
												.setBody(body_content);
										crawlData4PortalSite
												.setSummary(body_content);
									} else {
										bodyList = JsoupHtmlParser
												.getNodeContentBySelector(
														itemBlock,
														bodySelector_2,
														DataFormatStatus.TagAllContent,
														false);
										if (StringOperatorUtil
												.isNotBlankCollection(bodyList)) {
											String body_content = bodyList
													.get(0);
											body_content = JsoupHtmlParser
													.removeInnerContent(
															body_content,
															"div",
															remove_selecot_4_body);
											crawlData4PortalSite
													.setBody(body_content);
											crawlData4PortalSite
													.setSummary(body_content);
										}
									}
								
									// 加入一些非处理字段
									crawlData4PortalSite
											.setInsert_time(DateUtil
													.getLongByDate());
									// 加入source_title字段
									crawlData4PortalSite
											.setSource_title(taskPojo
													.getSource_title());
									crawlData4PortalSite.setMedia_type(taskPojo
											.getMedia_type());

									searchResultList.add(crawlData4PortalSite);
								}
							}
						}
					}
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("phantomjs请求过程中出现问题，请检查!");
			}
		}
		return searchResultList;
	}



	public static void main(String[] args) throws Exception {
		CrawlTaskPojo taskPojo = new CrawlTaskPojo();
		String root_url = "http://news.baidu.com/";
		String keyword = "捉妖记";
		taskPojo.setCrawlEngine(CrawlTypeEnum.MetaSearch_NEWSPage);
		taskPojo.setValue(keyword);
		

		List<CrawlData4PortalSite> searchResultLinkList = getAllNewsSearchResult(
				taskPojo, root_url, true);
		for (CrawlData4PortalSite crawlData4PortalSite : searchResultLinkList) {
			System.out.println("title=" + crawlData4PortalSite.getTitle());
			System.out.println("url=" + crawlData4PortalSite.getUrl());
		}

		System.out.println("执行完成!");
	}
}
