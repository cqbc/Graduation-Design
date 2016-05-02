package com.cqupt.spider.test;

import com.cqupt.common.enums.CrawlTypeEnum;
import com.cqupt.spider.crawl.Crawl4Phantomjs;
import com.cqupt.spider.pojos.CrawlResultPojo;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.ParserResultPojo;
import com.cqupt.spider.task.parser.HtmlSourceParser;

public class AddRule {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		
		/**
		 * 	String url = taskPojo.getValue();
		String host = UrlOperatorUtil.getHost(url);
		 */
		boolean isTest=true;
		String url="http://news.hexun.com/2015-10-31/180263803.html";
		CrawlTaskPojo taskPojo=new CrawlTaskPojo();
		taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
		taskPojo.setTitle("sina");
		taskPojo.setValue(url);
		CrawlResultPojo resultPojo = Crawl4Phantomjs.crawlHtmlSourceByRandomUrl(url);
//		CrawlResultPojo resultPojo = ControlerManager.crawlHtmlSourceByRandomUrl4HttpClient(url, null);
//		System.out.println(resultPojo);
		if (resultPojo != null && resultPojo.isNormal()) {
			ParserResultPojo parser_resultPojo =HtmlSourceParser.parserHtmlSource(taskPojo,
					resultPojo.getHtmlSource(), isTest);
			if (parser_resultPojo == null) {
				System.out.println(url + ",解析出现问题，请检查!");
				
			}
			else
			System.out.println(parser_resultPojo.toString());
	}
	}
}
