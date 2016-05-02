package com.cqupt.spider.pojos.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cqupt.common.enums.CrawlEngineEnum;

/**
 * 匹配正则时会有该url相对应的抓取引擎，默认为httpclient或是phantomjs
 * 现在将其设置为匹配和解析的中间类
 * 
 */
public  class MatchPatternPojo {
	public MatchPatternPojo(Pattern pattern, String crawlEngine) {
		this.pattern = pattern;
		if (crawlEngine.toLowerCase().equals("phantomjs")) {
			this.crawlEngineEnum = CrawlEngineEnum.Phantomjs;
		} else if (crawlEngine.toLowerCase().equals("httpclient")) {
			this.crawlEngineEnum = CrawlEngineEnum.HttpClient;
		} else {
			try {
				throw new Exception("crawl engine is error,please check!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 该url是否适配该pattern
	public boolean isMatch(String url) {
		Matcher matcher = this.getPattern().matcher(url);
		return matcher.find();
	}

	private Pattern pattern;
	private CrawlEngineEnum crawlEngineEnum;
	// 与该规则对应的匹配的字段匹配对应关系
	private MatchContentRulsListPojo matchContentRulsListPojo;

	public MatchContentRulsListPojo getMatchContentRulsListPojo() {
		return matchContentRulsListPojo;
	}

	public void setMatchContentRulsListPojo(
			MatchContentRulsListPojo matchContentRulsListPojo) {
		this.matchContentRulsListPojo = matchContentRulsListPojo;
	}

	public CrawlEngineEnum getCrawlEngineEnum() {
		return crawlEngineEnum;
	}

	public void setCrawlEngineEnum(CrawlEngineEnum crawlEngineEnum) {
		this.crawlEngineEnum = crawlEngineEnum;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

}
