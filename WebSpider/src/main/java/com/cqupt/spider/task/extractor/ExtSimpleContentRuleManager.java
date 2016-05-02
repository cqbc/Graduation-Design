package com.cqupt.spider.task.extractor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.cqupt.common.statics.StaticValueOfRule;
import com.cqupt.common.utils.ReadConfigUtil;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.spider.pojos.UrlFilterPojo4OneHostToMultiPattern;
import com.cqupt.spider.pojos.pattern.MatchContentPojo;
import com.cqupt.spider.pojos.pattern.MatchCrawlPojo;
import com.vaolan.extkey.utils.UrlOperatorUtil;

/**
 * 专为采集各种简单网页而设置的url规则管理器
 */
public class ExtSimpleContentRuleManager implements Serializable {

	public Map<String, UrlFilterPojo4OneHostToMultiPattern> urlFilterMap4Finance = null;
	private static final Lock lock_single_instance = new ReentrantLock();

	/**
	 * isSelf代表该rule配置文件是自带，还是外部传入的
	 * 
	 * @param rule_string
	 *        UrlFilterPojo4OneHostToMultiPattern.java * 
	 * @param isSelf
	 */
	public ExtSimpleContentRuleManager(String rule_string,
			boolean isSelf) {
		resetRuleByGlobalLock(rule_string, isSelf);
	}

	public void resetRule(String rule_string, boolean isSelf) {
		
		// 每次用一个新的来存放最新的
		urlFilterMap4Finance = new HashMap<String, UrlFilterPojo4OneHostToMultiPattern>();

		String[] lineArray = null;
		String[] temp_array = null;
		String host = null;
		String name = null;
		String regex = null;
		String crawlEngine = null;

		if (isSelf) {
			ReadConfigUtil readConfigUtil = new ReadConfigUtil(rule_string,
					false);
			rule_string = readConfigUtil.getLineConfigTxt();
		}
		if (StringOperatorUtil.isNotBlank(rule_string)) {
			lineArray = rule_string.split(StaticValueOfRule.separator_next_line);
			List<String> ruleList = new LinkedList<String>();
			String last_host = null;
			String last_rule_name = null;
			for (String line : lineArray) {
				// length==3时，是构造url的过滤规则部分
				if ((temp_array = line.split(StaticValueOfRule.separator_tab)).length == 4) {
					name = temp_array[0].trim();
					host = temp_array[1].trim();
					regex = temp_array[2].trim();
					crawlEngine = temp_array[3].trim();
					if (urlFilterMap4Finance.containsKey(host)) {
						// 说明一个host对应了多个pattern
						urlFilterMap4Finance.get(host).addPattern(regex,
								crawlEngine);
					} else {
						UrlFilterPojo4OneHostToMultiPattern urlFilterPojo4MultiPattern = new UrlFilterPojo4OneHostToMultiPattern(
								name, host);
						urlFilterPojo4MultiPattern.addPattern(regex,
								crawlEngine);
						urlFilterMap4Finance.put(host,
								urlFilterPojo4MultiPattern);
					}

					// 将内容抽取的规则部分加入对象中
					if (StringOperatorUtil.isNotBlankCollection(ruleList)) {
						urlFilterMap4Finance.get(last_host)
								.addIExtContentRuleList(last_rule_name,
										ruleList, false);
						ruleList.clear();
					}
				} else {
					last_host = host;
					last_rule_name = name;
					// 当不等于指定列时，即认为是上一个规则配置的内容抽取部分
					if (StringOperatorUtil.isNotBlank(line)) {
						ruleList.add(line.trim());
					}
				}
			}
			// 对rule list扫尾,即最后一波的规则不在上边的for循环中
			if (StringOperatorUtil.isNotBlankCollection(ruleList)) {
				urlFilterMap4Finance.get(last_host).addIExtContentRuleList(
						last_rule_name, ruleList, true);
				ruleList.clear();
			}
		}
	}

	public boolean isInHostFilter(String host) {
		return urlFilterMap4Finance.containsKey(host);
	}

	public boolean isMatch4AllPattern(String host, String url) {
		UrlFilterPojo4OneHostToMultiPattern urlFilterPojo4OneHostToMultiPattern = urlFilterMap4Finance
				.get(host);
		if (urlFilterPojo4OneHostToMultiPattern == null) {
			return false;
		}
		return urlFilterPojo4OneHostToMultiPattern.isMatch(url);
	}

	public String getMatchValue(String host, String url) {
		UrlFilterPojo4OneHostToMultiPattern urlFilterPojo4OneHostToMultiPattern = urlFilterMap4Finance
				.get(host);
		if (urlFilterPojo4OneHostToMultiPattern == null) {
			return null;
		}
		return urlFilterPojo4OneHostToMultiPattern.getMatchValue(url);
	}

	public MatchCrawlPojo getMatchCrawlPojo(String host, String url) {
		UrlFilterPojo4OneHostToMultiPattern urlFilterPojo4OneHostToMultiPattern = urlFilterMap4Finance
				.get(host);
		if (urlFilterPojo4OneHostToMultiPattern == null) {
			return null;
		}
		return urlFilterPojo4OneHostToMultiPattern.getMatchCrawlPojo(url);
	}

	public MatchCrawlPojo getMatchCrawlPojo(String url) {
		String host = UrlOperatorUtil.getHost(url);
		if (StringOperatorUtil.isNotBlank(host)) {
			UrlFilterPojo4OneHostToMultiPattern urlFilterPojo4OneHostToMultiPattern = urlFilterMap4Finance
					.get(host);
			if (urlFilterPojo4OneHostToMultiPattern == null) {
				return null;
			}
			return urlFilterPojo4OneHostToMultiPattern.getMatchCrawlPojo(url);
		}
		return null;
	}

	public MatchCrawlPojo getMatchCrawlPojoByGlobalLock(String url) {
		try {
			lock_single_instance.lock();
			return getMatchCrawlPojo(url);
		} finally {
			lock_single_instance.unlock();
		}
	}

	public void resetRuleByGlobalLock(String rule_string, boolean isSelf) {
		try {
			lock_single_instance.lock();
			this.resetRule(rule_string, isSelf);
		} finally {
			lock_single_instance.unlock();
		}
	}

	public MatchContentPojo getMatchContentList(String url, String host,
			String htmlSource) {
		
		UrlFilterPojo4OneHostToMultiPattern urlFilterPojo4OneHostToMultiPattern = urlFilterMap4Finance
				.get(host);
		if (urlFilterPojo4OneHostToMultiPattern == null) {
			return null;
		}
		return urlFilterPojo4OneHostToMultiPattern.getMatchContentList(url,
				htmlSource);
	}

	public static void main(String[] args) {
	
	}
}
