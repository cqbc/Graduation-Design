package com.cqupt.spider.pojos.pattern;

import java.io.Serializable;
import java.util.List;

import com.cqupt.spider.pojos.MatchResultKeyValue;

/**
 * 对象抽取结果的封装
 */
public  class MatchContentPojo implements Serializable {
	@Override
	public String toString() {
		return "MatchContentPojo [isNormal=" + isNormal + ", rule_name="
				+ rule_name + ", matchContentList=" + matchContentList
				+ "]";
	}

	public MatchContentPojo() {

	}

	public MatchContentPojo(String rule_name,
			List<MatchResultKeyValue> matchContentList) {
		this.rule_name = rule_name;
		this.matchContentList = matchContentList;
	}

	// 解析是否正常
	private boolean isNormal;

	public boolean isNormal() {
		return isNormal;
	}

	public void setNormal(boolean isNormal) {
		this.isNormal = isNormal;
	}

	/**
	 * 匹配时候所属的标题
	 */
	private String rule_name;

	public String getRule_name() {
		return rule_name;
	}

	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}

	/**
	 * 匹配所属的内容
	 */
	private List<MatchResultKeyValue> matchContentList;

	public List<MatchResultKeyValue> getMatchContentList() {
		return matchContentList;
	}

	public void setMatchContentList(
			List<MatchResultKeyValue> matchContentList) {
		this.matchContentList = matchContentList;
	}
}