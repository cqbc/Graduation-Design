package com.cqupt.spider.pojos.pattern;

import java.io.Serializable;

import com.cqupt.common.enums.CrawlEngineEnum;

/**
 * 得到某个url的设定的抓取引擎的结果pojo类
 * 
 */
public class MatchCrawlPojo implements Serializable {

	public MatchCrawlPojo(boolean isMatch, String format_url,
			CrawlEngineEnum crawlEngineEnum) {
		this.isMatch = isMatch;
		this.format_url = format_url;
		this.crawlEngineEnum = crawlEngineEnum;
	}

	private boolean isMatch;
	// 通过正则格式化好后的url值，原始的url就没什么意义了
	private String format_url;

	public String getFormat_url() {
		return format_url;
	}

	public void setFormat_url(String format_url) {
		this.format_url = format_url;
	}

	public boolean isMatch() {
		return isMatch;
	}

	public void setMatch(boolean isMatch) {
		this.isMatch = isMatch;
	}

	private CrawlEngineEnum crawlEngineEnum;

	public CrawlEngineEnum getCrawlEngineEnum() {
		return crawlEngineEnum;
	}

	public void setCrawlEngineEnum(CrawlEngineEnum crawlEngineEnum) {
		this.crawlEngineEnum = crawlEngineEnum;
	}

}