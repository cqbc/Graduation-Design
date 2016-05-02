package com.cqupt.spider.task.parser;

import java.util.Date;
import java.util.Set;

import com.cqupt.common.statics.StaticValueOfRule;
import com.cqupt.common.utils.DateUtil;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.spider.manager.SystemControlerManager;
import com.cqupt.spider.pojos.CrawlData4PortalSite;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.MatchResultKeyValue;
import com.cqupt.spider.pojos.ParserResultPojo;
import com.cqupt.spider.pojos.pattern.MatchContentPojo;
import com.cqupt.spider.task.extractor.detail.UrlExtractor;
import com.vaolan.extkey.utils.UrlOperatorUtil;

public class HtmlSourceParser {
	// 日期处理工具类
	public static DateUtil dateUtil = new DateUtil();
	
	public static ParserResultPojo parserHtmlSource(CrawlTaskPojo taskPojo,
			String htmlSource, boolean isTest) {
		
		String url = taskPojo.getValue();
		String host = UrlOperatorUtil.getHost(url);
		MatchContentPojo matchContentPojo = SystemControlerManager.extractorContentManager4SimpleCrawler
				.getMatchContentList(url, host, htmlSource);
		
		ParserResultPojo parserResultPojo = new ParserResultPojo();
		if (matchContentPojo != null && matchContentPojo.isNormal()) {
			
			parserResultPojo.setRuleName(matchContentPojo.getRule_name());
			parserResultPojo.setFromUrl(url);
			// 代表完全匹配上规则库了
			parserResultPojo.setMatchRegex(true);
			// 要解析出来的数据封装对象
			CrawlData4PortalSite crawlData4PortalSite = new CrawlData4PortalSite();
			crawlData4PortalSite.setUrl(url);
			String fieldKey = null;
			String fieldValue = null;
			for (MatchResultKeyValue matchKeyValue : matchContentPojo
					.getMatchContentList()) {
				fieldKey = matchKeyValue.getFieldKey();
				fieldValue = matchKeyValue.getValue();
				if (fieldKey.equals("标题")) {
					crawlData4PortalSite.setTitle(fieldValue);
				} else if (fieldKey.equals("作者")) {
					crawlData4PortalSite.setAuthor(fieldValue);
				} else if (fieldKey.equals("正文")) {
					if (crawlData4PortalSite.getBody() != null) {
						crawlData4PortalSite.setBody(crawlData4PortalSite
								.getBody()
								+ StaticValueOfRule.separator_next_line
								+ fieldValue);
					} else {
						crawlData4PortalSite.setBody(fieldValue);
					}
				} else if (fieldKey.equals("评论数")) {
					try {
						crawlData4PortalSite
								.setDiscuss_number(StringOperatorUtil
										.getNumber(fieldValue));
					} catch (Exception e) {
						crawlData4PortalSite.setDiscuss_number(0);
					}
				} else if (fieldKey.equals("转发数")) {
					try {
						crawlData4PortalSite
								.setTransmit_number(StringOperatorUtil
										.getNumber(fieldValue));
					} catch (Exception e) {
						crawlData4PortalSite.setTransmit_number(0);
					}
				} else if (fieldKey.equals("发布时间")) {
					crawlData4PortalSite.setPublish_time_string(fieldValue);
					// 在这里做时间的格式化
					try {
						Date date = dateUtil
								.getDateByNoneStructure4News(crawlData4PortalSite
										.getPublish_time_string());
						if (date != null) {
							crawlData4PortalSite.setPublish_time_long(date
									.getTime());
						} else {
							// 遇到不转化，也暂定为0
							crawlData4PortalSite.setPublish_time_long(0);
						}
					} catch (Exception e) {
						// 如果发现date无法解析，则将日期暂定上为0
						crawlData4PortalSite.setPublish_time_long(0);
					}
				} else if (fieldKey.equals("摘要")) {
					crawlData4PortalSite.setSummary(fieldValue);
				} else if (fieldKey.equals("关键词")) {
					crawlData4PortalSite.setKeyword(fieldValue);
				} else if (fieldKey.equals("点赞数")) {
					try {
						crawlData4PortalSite.setLikes_number(StringOperatorUtil
								.getNumber(fieldValue));
					} catch (Exception e) {
						crawlData4PortalSite.setLikes_number(0);
					}
				} else if (fieldKey.equals("回复数")) {
					try {
						crawlData4PortalSite.setReply_number(StringOperatorUtil
								.getNumber(fieldValue));
					} catch (Exception e) {
						crawlData4PortalSite.setReply_number(0);
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

			}

			
			// 设置parserResultPojo的信息
			parserResultPojo.setCrawlData4PortalSite(crawlData4PortalSite);
		}


		// 提取新产生的url
		if (taskPojo.isContinue()) {
			Set<String> urlSet = UrlExtractor.getNewUrls(
					taskPojo.getValue(), host, htmlSource, taskPojo.getTopN());
			parserResultPojo.setNewUrlSet(urlSet);
		}

		
		
	/*	//如果为null 代表是抓url的首页
		if(parserResultPojo.getCrawlData4PortalSite()==null)
		parserResultPojo.setCrawlData4PortalSite(new CrawlData4PortalSite());*/
		
		parserResultPojo.setNormal(true);
		parserResultPojo.setSource_title(taskPojo.getSource_title());
		parserResultPojo.setCurrent_depth(taskPojo.getCurrent_depth() + 1);
		parserResultPojo.setOwnToCrawlTaskPojo(taskPojo);
		return parserResultPojo;
	}
}
