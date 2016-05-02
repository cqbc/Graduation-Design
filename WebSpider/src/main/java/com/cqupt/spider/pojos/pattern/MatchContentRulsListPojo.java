package com.cqupt.spider.pojos.pattern;

import java.io.Serializable;
import java.util.List;

import com.cqupt.spider.task.extractor.detail.IExtractorContentRule;

public class MatchContentRulsListPojo implements Serializable {
	private String ruleName;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	private List<IExtractorContentRule> extContentRuleList;

	public List<IExtractorContentRule> getExtContentRuleList() {
		return extContentRuleList;
	}

	public void setExtContentRuleList(
			List<IExtractorContentRule> extContentRuleList) {
		this.extContentRuleList = extContentRuleList;
	}

}
