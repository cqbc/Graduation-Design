
package com.cqupt.spider.pojos;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cqupt.common.enums.CrawlEngineEnum;
import com.cqupt.common.statics.StaticValueOfRule;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.spider.pojos.pattern.MatchContentPojo;
import com.cqupt.spider.pojos.pattern.MatchContentRulsListPojo;
import com.cqupt.spider.pojos.pattern.MatchCrawlPojo;
import com.cqupt.spider.pojos.pattern.MatchPatternPojo;
import com.cqupt.spider.task.extractor.detail.FormatExtractorImpl;
import com.cqupt.spider.task.extractor.detail.IExtractorContentRule;

/**
 * 当一个host对应多个url过滤器时用该pojo类
 * 
 */
public class UrlFilterPojo4OneHostToMultiPattern implements Serializable {

	private String host;
	private String name;

	private List<String> regexList;
	private List<MatchPatternPojo> patternList;

	// 以不同的host下的url为基本的匹配单位

	public UrlFilterPojo4OneHostToMultiPattern(String name, String host) {
		this.host = host;
		this.name = name;
		this.regexList = new LinkedList<String>();
		this.patternList = new LinkedList<MatchPatternPojo>();
		// 以不同的host下的url为基本的匹配单位
	}

	public void addPattern(String regex, String crawlEngine) {
		this.regexList.add(regex);
		// 将regex加上一个(),用来作为后边的group(1)操作
		Pattern pattern = Pattern.compile(StaticValueOfRule.separator_left_bracket
				+ regex + StaticValueOfRule.separator_right_bracket);
		MatchPatternPojo matchPatternPojo = new MatchPatternPojo(pattern,
				crawlEngine);
		this.patternList.add(matchPatternPojo);
	}

	public boolean isMatch(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return false;
		}
		for (MatchPatternPojo matchPatternPojo : patternList) {
			Matcher matcher = matchPatternPojo.getPattern().matcher(url);
			if (matcher.find()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从url中匹配出正则中匹配的值来，主要是能去掉一些无用参数
	 * 
	 * @param url
	 * @return
	 */
	public String getMatchValue(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		for (MatchPatternPojo matchPatternPojo : patternList) {
			Matcher matcher = matchPatternPojo.getPattern().matcher(url);
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		return null;
	}

	// 得到匹配上的engine pojo
	public MatchCrawlPojo getMatchCrawlPojo(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		MatchCrawlPojo matchCrawlPojo = null;
		for (MatchPatternPojo matchPatternPojo : patternList) {
			// System.out.println(pattern.toString());
			Matcher matcher = matchPatternPojo.getPattern().matcher(url);
			if (matcher.find()) {
				if (matchCrawlPojo != null) {
					if (matchPatternPojo.getCrawlEngineEnum() == CrawlEngineEnum.Phantomjs) {
						matchCrawlPojo
								.setCrawlEngineEnum(CrawlEngineEnum.Phantomjs);
					}
				} else {
					matchCrawlPojo = new MatchCrawlPojo(true, matcher.group(1),
							matchPatternPojo.getCrawlEngineEnum());
				}
			}
		}
		return matchCrawlPojo;
	}


	

	/**
	 * 一次性传递多个规则串,针对一个host下的一个url的 isOver代表是否加截完毕
	 * 
	 * @param ruleLine
	 */
	public void addIExtContentRuleList(String ruleName, List<String> ruleList,
			boolean isOver) {
		if (StringOperatorUtil.isBlankCollection(ruleList)) {
			return;
		}
		List<IExtractorContentRule> temp_extContentRuleList = new LinkedList<IExtractorContentRule>();
		for (String ruleLine : ruleList) {
			IExtractorContentRule iRule = new FormatExtractorImpl(ruleLine);
			temp_extContentRuleList.add(iRule);
		}
		MatchContentRulsListPojo matchContentRulsListPojo = new MatchContentRulsListPojo();
		matchContentRulsListPojo.setRuleName(ruleName);
		matchContentRulsListPojo.setExtContentRuleList(temp_extContentRuleList);
		// 根据初始化的加载策略来对应关系
		for (MatchPatternPojo matchPatternPojo : this.patternList) {
			if (matchPatternPojo.getMatchContentRulsListPojo() == null) {
				matchPatternPojo
						.setMatchContentRulsListPojo(matchContentRulsListPojo);
				break;
			}
		}
	}

	// 解析出具体的字段内容
	public MatchContentPojo getMatchContentList(String url, String htmlsource) {
		if (StringOperatorUtil.isNotBlankCollection(this.patternList)) {
			List<MatchResultKeyValue> resultKVlist = null;
			// 防止会有多个匹配的正则项，那个的匹配item最多，要哪个
			List<MatchResultKeyValue> finalResultList = null;
			List<MatchResultKeyValue> tempResultList = null;
			MatchContentPojo matchContentPojo = null;
			int match_rule_times = 0;
			
			for (MatchPatternPojo matchPatternPojo : this.patternList) {
				if (matchPatternPojo.isMatch(url)) {
					// 得到与之匹配的规则
					MatchContentRulsListPojo matchContentRulsListPojo = matchPatternPojo
							.getMatchContentRulsListPojo();
					// 这是以防为空的规则列表
					if (matchContentRulsListPojo == null) {
						continue;
					}
					
					List<IExtractorContentRule> iRuleList = matchContentRulsListPojo
							.getExtContentRuleList();
					tempResultList = new LinkedList<MatchResultKeyValue>();
					for (IExtractorContentRule iRule : iRuleList) {
						resultKVlist = iRule.getContent(htmlsource);
						if (StringOperatorUtil
								.isNotBlankCollection(resultKVlist)) {
							match_rule_times++;
							tempResultList.addAll(resultKVlist);
						} else {
							break;
						}
					}
					if (match_rule_times == iRuleList.size()
							&& StringOperatorUtil
									.isNotBlankCollection(tempResultList)) {
						if (finalResultList == null) {
							// 在为空时进行一次初始化，之后就不需要了!
							matchContentPojo = new MatchContentPojo();
							finalResultList = tempResultList;
							matchContentPojo
									.setRule_name(matchContentRulsListPojo
											.getRuleName());
						} else if (tempResultList.size() > finalResultList
								.size()) {
							finalResultList = tempResultList;
							matchContentPojo
									.setRule_name(matchContentRulsListPojo
											.getRuleName());
						}
						match_rule_times = 0;
						matchContentPojo.setNormal(true);
						matchContentPojo.setMatchContentList(finalResultList);
					} else {
						match_rule_times = 0;
					}
				}
			}
			return matchContentPojo;
		}
		return null;
	}


	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
