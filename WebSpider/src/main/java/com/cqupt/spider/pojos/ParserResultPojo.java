
package com.cqupt.spider.pojos;

import com.cqupt.common.statics.StaticValueOfRule;

/**
 * 解析对象的返回结果
 * 
 */
public class ParserResultPojo extends IResultPojo {

	private String ruleName;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getSource_title() + StaticValueOfRule.separator_tab);
		sb.append(this.getCrawlEngine() + StaticValueOfRule.separator_tab);
		sb.append(ruleName + StaticValueOfRule.separator_tab);
		sb.append(this.getFromUrl() + StaticValueOfRule.separator_next_line);
		// sb.append(content);
		// sb.append("real data---"+this.getCrawlData4PortalSite());
		if (newUrlSet != null) {
			sb.append("new url set length " + this.newUrlSet.size()
					+ StaticValueOfRule.separator_next_line);
		} else {
			sb.append("new url set is empty " + StaticValueOfRule.separator_next_line);
		}
		sb.append("real parser data \n" + this.getCrawlData4PortalSite());

		return sb.toString();
	}

	public String toErrorString() {
		// return "ParserResultPojo [fromUrl=" + fromUrl + ", isNormal="
		// + isNormal + ", content=" + content + "]";
		StringBuilder sb = new StringBuilder();
		sb.append(this.getCrawlEngine() + StaticValueOfRule.separator_tab);
		// sb.append(this.getFileName() + StaticValueOfRule.separator_tab);
		sb.append(this.getFromUrl() + StaticValueOfRule.separator_tab);

		return sb.toString();
	}

}
