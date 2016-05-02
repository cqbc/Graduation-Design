	
package com.cqupt.spider.pojos;


/**
 * 抓取结果的返回对象
 * 
 */
public class CrawlResultPojo extends IResultPojo{
	@Override
	public String toString() {
		return "CrawlResultPojo [isNormal=" + isNormal + ", desc=" + desc
				+ ", content=" + htmlSource + "]";
	}
	
}
