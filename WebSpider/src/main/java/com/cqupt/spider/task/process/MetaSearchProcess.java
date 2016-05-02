package com.cqupt.spider.task.process;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.cqupt.common.enums.SearchEngineEnum;
import com.cqupt.common.utils.IOUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.spider.crawl.metasearch.Crawl4Baidu;
import com.cqupt.spider.crawl.metasearch.Crawl4SinaNews;
import com.cqupt.spider.crawl.metasearch.Crawl4Sogou;
import com.cqupt.spider.crawl.metasearch.Crawl4_360;
import com.cqupt.spider.pojos.CrawlData4PortalSite;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.IResultPojo;
import com.cqupt.spider.pojos.ParserResultPojo;

/**
 * 元搜索抓取引擎管理器
 * 
 */
public class MetaSearchProcess {
	// 日志
	public static MyLogger logger = new MyLogger(MetaSearchProcess.class);
	// 搜索引擎使用计数，为了节省时间，暂时规则性只抓取任选一个搜索引擎的结果
	public static int random_max_int = 50;
	public static Random randomUtil = new Random();

	public static List<IResultPojo> processTaskControler(
			CrawlTaskPojo taskPojo, boolean isTest,
			SearchEngineEnum[] searchEngineEnumList) {
		if (taskPojo == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());
		List<IResultPojo> resultList_all = null;
	
			resultList_all = processTaskMeta(taskPojo, isTest,
					searchEngineEnumList);
		
		try {
			for (IResultPojo u : resultList_all) {

				if (u.getNewUrlSet() == null)
					continue;
				
				Set<String> set = u.getNewUrlSet();
				

				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String url = iterator.next();
//					System.out.println(url);
					IOUtil.writeFile("site/site_" + today + ".txt",
							url.toString() + "\n", true, "utf-8");
//					System.out.println("site/site_" + today + ".txt");
				}

			}
		} catch (Exception e) {
			logger.info("读取新产生种子文件时，出现了问题");
			e.printStackTrace();
		}
		return resultList_all;
	}

	
	public static List<IResultPojo> processTaskMeta(CrawlTaskPojo taskPojo,
			boolean isTest, SearchEngineEnum[] searchEngineEnumList) {
		if (taskPojo == null) {
			return null;
		}
		List<IResultPojo> resultList_all = new LinkedList<IResultPojo>();


//		SearchEngineEnum searchEngineEnum = SearchEngineEnum.Sogou;
		SearchEngineEnum searchEngineEnum = searchEngineEnumList[(randomUtil
				.nextInt(random_max_int) % 2)];

		List<IResultPojo> allHitResultLink4Letv = getAllHitResultLink4Letv(
				taskPojo, searchEngineEnum, isTest);
		if (allHitResultLink4Letv != null) {
			resultList_all.addAll(allHitResultLink4Letv);
		}


		return resultList_all;
	}

	
	public static List<IResultPojo> getAllHitResultLink4Letv(
			CrawlTaskPojo taskPojo, SearchEngineEnum searchEngineEnum,
			boolean isTest) {
		ParserResultPojo parserResultPojo = null;
		String root_url = taskPojo.getSearchRootUrl(searchEngineEnum);

		List<IResultPojo> resultList = new ArrayList<IResultPojo>();

		List<CrawlData4PortalSite> searchResultLinkList = null;

		// 新url set集合
		Set<String> newUrlSet = new HashSet<String>();

		if (SearchEngineEnum.Baidu == searchEngineEnum) {
			if (StringOperatorUtil.isBlank(root_url)) {
				logger.info("meta search task occur a NULL root_url,please check!");
			} else {
				// 暂定以下均为元搜索情况
				searchResultLinkList = Crawl4Baidu.getAllNewsSearchResult(
						taskPojo, root_url, isTest);

				if (StringOperatorUtil
						.isNotBlankCollection(searchResultLinkList)) {
					for (CrawlData4PortalSite linkPojo : searchResultLinkList) {
						// 将抓取的数据封装，并加入url set中
						newUrlSet.add(linkPojo.getUrl());
					}

					parserResultPojo = new ParserResultPojo();
					// 为传递空采集对象，告诉服务器端是有要处理和保存的信息而添加
					parserResultPojo
							.setCrawlData4PortalSite(new CrawlData4PortalSite());
					parserResultPojo.setRuleName("百度元搜索");
					parserResultPojo.setMatchRegex(true);
					parserResultPojo.setNewUrlSet(newUrlSet);
					parserResultPojo.setNormal(true);
					parserResultPojo
							.setSource_title(taskPojo.getSource_title());
					// 因为是元搜索，暂认为此字段无意义
					parserResultPojo.setCurrent_depth(0);

					// 重置下关键词采集时的topn和depth
					taskPojo.setTopN(0);
					taskPojo.setDepth(0);

					parserResultPojo.setOwnToCrawlTaskPojo(taskPojo);
				}
			}
		} else if (SearchEngineEnum.Sogou == searchEngineEnum) {
			if (StringOperatorUtil.isBlank(root_url)) {
				logger.info("meta search task occur a NULL root_url,please check!");
			} else {

				searchResultLinkList = Crawl4Sogou.getAllNewsSearchResult(
						taskPojo, root_url, isTest);

				if (StringOperatorUtil
						.isNotBlankCollection(searchResultLinkList)) {
					// 将抓取的数据封装，并加入result list中
					for (CrawlData4PortalSite crawlData4PortalSite : searchResultLinkList) {
						// 将抓取的数据封装，并加入url set中
						newUrlSet.add(crawlData4PortalSite.getUrl());

					}
					parserResultPojo = new ParserResultPojo();
					// 为传递空采集对象，告诉服务器端是有要处理和保存的信息而添加
					parserResultPojo
							.setCrawlData4PortalSite(new CrawlData4PortalSite());
					parserResultPojo.setRuleName("搜狗元搜索");
					parserResultPojo.setMatchRegex(true);
					parserResultPojo.setNewUrlSet(newUrlSet);
					parserResultPojo.setNormal(true);
					parserResultPojo
							.setSource_title(taskPojo.getSource_title());
					// 因为是元搜索，暂认为此字段无意义
					parserResultPojo.setCurrent_depth(0);

					// 重置下关键词采集时的topn和depth
					taskPojo.setTopN(0);
					taskPojo.setDepth(0);

					parserResultPojo.setOwnToCrawlTaskPojo(taskPojo);
				}
			}
		} else if (SearchEngineEnum.SinaNews == searchEngineEnum) {
			if (StringOperatorUtil.isBlank(root_url)) {
				logger.info("meta search task occur a NULL root_url,please check!");
			} else {
				// 暂定以下均为元搜索情况，暂不包括微博元搜索的情况
				searchResultLinkList = Crawl4SinaNews
						.getAllNewsSearchResult(taskPojo, isTest);

				if (StringOperatorUtil
						.isNotBlankCollection(searchResultLinkList)) {
					// 将抓取的数据封装，并加入result list中
					for (CrawlData4PortalSite crawlData4PortalSite : searchResultLinkList) {
						// 将抓取的数据封装，并加入url set中
						newUrlSet.add(crawlData4PortalSite.getUrl());
					}
					parserResultPojo = new ParserResultPojo();
					// 为传递空采集对象，告诉服务器端是有要处理和保存的信息而添加
					parserResultPojo.setCrawlData4PortalSite(new CrawlData4PortalSite());
					parserResultPojo.setRuleName("sina新闻搜索");
					parserResultPojo.setMatchRegex(true);

					parserResultPojo.setNewUrlSet(newUrlSet);
					parserResultPojo.setNormal(true);
					parserResultPojo
							.setSource_title(taskPojo.getSource_title());
					// 因为是元搜索，暂认为此字段无意义
					parserResultPojo.setCurrent_depth(0);
					// 重置下关键词采集时的topn和depth
					taskPojo.setTopN(0);
					taskPojo.setDepth(0);
					parserResultPojo.setOwnToCrawlTaskPojo(taskPojo);
				}
			}
		}

		else if (SearchEngineEnum.QiHu360 == searchEngineEnum) {
			if (StringOperatorUtil.isBlank(root_url)) {
				logger.info("meta search task occur a NULL root_url,please check!");
			} else {
				// 暂定以下均为元搜索情况
				searchResultLinkList = Crawl4_360.getAllNewsSearchResult(
						taskPojo, isTest);

				if (StringOperatorUtil
						.isNotBlankCollection(searchResultLinkList)) {
					// 将抓取的数据封装，并加入result list中
					for (CrawlData4PortalSite crawlData4PortalSite : searchResultLinkList) {

						// 将抓取的数据封装，并加入url set中
						newUrlSet.add(crawlData4PortalSite.getUrl());
					}

					parserResultPojo = new ParserResultPojo();
					// 为传递空采集对象，告诉服务器端是有要处理和保存的信息而添加
					parserResultPojo.setCrawlData4PortalSite(new CrawlData4PortalSite());
					parserResultPojo.setRuleName("360元搜索");
					parserResultPojo.setMatchRegex(true);
					parserResultPojo.setNewUrlSet(newUrlSet);
					parserResultPojo.setNormal(true);
					parserResultPojo
							.setSource_title(taskPojo.getSource_title());
					// 因为是元搜索，暂认为此字段无意义
					parserResultPojo.setCurrent_depth(0);
					// 重置下关键词采集时的topn和depth
					taskPojo.setTopN(0);
					taskPojo.setDepth(0);
					parserResultPojo.setOwnToCrawlTaskPojo(taskPojo);
				}
			}
		}



		// 该parserResultPojo中保存了元搜索所有提取的连接
		resultList.add(parserResultPojo);
		// 把元搜索提取的所有连接转换成对象
		for (CrawlData4PortalSite crawlData4PortalSite : searchResultLinkList) {
			// 将抓取的数据封装，并加入url set中
			/*
			 * newUrlSet.add(crawlData4PortalSite.getUrl()); }
			 */

			parserResultPojo = new ParserResultPojo();
			parserResultPojo.setCrawlData4PortalSite(crawlData4PortalSite);
			parserResultPojo.setRuleName("元搜索");
			parserResultPojo.setMatchRegex(true);

			parserResultPojo.setNormal(true);
			parserResultPojo.setSource_title(taskPojo.getSource_title());
			// 因为是元搜索，暂认为此字段无意义
			parserResultPojo.setCurrent_depth(0);
			// 此字段也无意义,但要设置，为在数据存储中判断处是哪种类型的任务得来的数据
			parserResultPojo.setOwnToCrawlTaskPojo(taskPojo);

			resultList.add(parserResultPojo);
		}

		return resultList;
	}



	public static void main(String[] args) {

	}

}
