
package com.cqupt.spider.pojos;

import java.io.Serializable;
import java.util.Date;

import com.cqupt.common.enums.CrawlTypeEnum;
import com.cqupt.common.enums.SearchEngineEnum;
import com.cqupt.common.enums.TaskLevelEnum;
import com.cqupt.common.enums.TaskTypeEnum;
import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.StaticValue4RelationMap;
import com.cqupt.common.utils.StringOperatorUtil;

/**
 * 提交的爬取任务的单元类。 </br> CrawlTask属性包括： 抓取任务类型， 采用的抓取引擎， 任务级别， 任务值（关键词或者url）， 标题，
 * 抓取深度（0表示当前url，1表示当前url页包好的url，以此类推） TopN，按TOPN选择解析url 当前深度
 * 
 */
public class CrawlTaskPojo implements Serializable {

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CrawlTaskPojo) {
			CrawlTaskPojo taskPojo2 = (CrawlTaskPojo) obj;
			if (this.value != null && this.value.equals(taskPojo2.getValue())
					&& this.source_title != null
					&& this.source_title.equals(taskPojo2.getSource_title())
					&& this.media_type == taskPojo2.getMedia_type()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "CrawlTaskPojo [type=" + type + ", crawlEngine=" + crawlEngine
				+ ", level=" + level + ", value=" + value + ", title=" + title
				+ ", depth=" + depth + ", topN=" + topN + ", current_depth="
				+ current_depth + ", source_title=" + source_title + "]";
	}

	public String toUniqString() {
		return "CrawlTaskPojo [type=" + type + ", value=" + value + ", depth="
				+ depth + ", topN=" + topN + ", source_title=" + source_title
				+ "]";
	}

	// 为了判重，而新添加的value格式化，主要是把非域名情况的最后一个"/"给去掉，防止"A","A/"情况下的重复
	public String getValueFormat() {
		if (crawlEngine == CrawlTypeEnum.WebPage_Url) {
			if (StringOperatorUtil.isNotBlank(value)) {
				if (value.endsWith("/")) {
					return value.substring(0, value.length() - 1);
				}
			}
		}
		return value;
	}

	// 默认的任务类型
	private TaskTypeEnum type = TaskTypeEnum.Url;
	// 任务所要使用的抓取引擎或方式
	private CrawlTypeEnum crawlEngine = CrawlTypeEnum.WebPage_Url;
	// 默认的任务级别
	private TaskLevelEnum level = StaticValue4RelationMap.taskLevelDefault;
	// 任务的值,可以为关键字或是url值
	private String value;
	// 任务的标题title
	private String title;
	// 该url要抓取的深度,如果元搜索抓取，则代表要翻的页数
	// 0代表自己，1代表该url产生的url集合，依次类推之
	private int depth;
	// 表示该任务被插入的时间，以上为基础再加上TaskLevelEnum对应的ms即可判断是否应该被加入到待抓取的circle中

	private long last_insert_time_long;

	public long getLast_insert_time_long() {
		return last_insert_time_long;
	}

	public void setLast_insert_time_long(long last_insert_time_long) {
		this.last_insert_time_long = last_insert_time_long;
	}

	// 该任务是否应该被添加到实际的redis待抓取的周期任务队列中
	public boolean isShouldToDoThisCircleTask() {
		int minutes = StaticValue4RelationMap.getLevelToCircleTime(level);
		if (new Date().getTime() >= (this.last_insert_time_long + minutes * 60 * 1000)) {
			return true;
		}
		return false;
	}

	public boolean isEnableToCircle() {
		if (TaskLevelEnum.Z == this.level) {
			return false;
		}
		return true;
	}

	/**
	 * 每个url抓取的内容页中抽取前多少个为要抓取的二次产生的页面，去继续抓取 会有一个过滤和排序的过程,最后按排序结果取topN
	 */
	private int topN;
	/**
	 * 以下为不需要客户端来管理的变量
	 */
	/*
	 * 用来标志当前所处的层次，到即定层次后不再往后抓取了 此参数不需要外层传入
	 * current_depth从0开始，当current_depth==depth时，不再进行新url的产生
	 */
	private int current_depth;
	// 原始标题,用来找根的
	private String source_title;
	/**
	 * media type媒体类型
	 * 新闻1,博客2,论坛3,娱乐4,社区5,财经6,未分类7,元搜索8
	 */
	private int media_type;

	public int getMedia_type() {
		return media_type;
	}

	public void setMedia_type(int media_type) {
		this.media_type = media_type;
	}

	/** 获取原始标题 */
	public String getSource_title() {
		return source_title;
	}

	/** 设置原始标题 */
	public void setSource_title(String source_title) {
		this.source_title = source_title;
	}

	/** 获取当前深度 */
	public int getCurrent_depth() {
		return current_depth;
	}

	/** 设置当前深度 */
	public void setCurrent_depth(int current_depth) {
		this.current_depth = current_depth;
	}

	/** 获取任务类型 */
	public TaskTypeEnum getType() {
		return type;
	}
	

	/** 设置任务类型 */
	public void setType(TaskTypeEnum type) {
		this.type = type;
	}

	/** 获取抓取引擎 */
	public CrawlTypeEnum getCrawlEngine() {
		return crawlEngine;
	}

	/** 设置抓取引擎 */
	public void setCrawlEngine(CrawlTypeEnum crawlEngine) {
		this.crawlEngine = crawlEngine;
	}

	/** 获取任务级别 */
	public TaskLevelEnum getLevel() {
		return level;
	}

	/** 设置任务级别 */
	public void setLevel(TaskLevelEnum level) {
		this.level = level;
	}

	/** 获取任务值 */
	public String getValue() {
		return value;
	}

	/** 设置任务值 */
	public void setValue(String value) {
		this.value = value;
	}

	/** 获取标题 */
	public String getTitle() {
		return title;
	}

	/** 设置标题 */
	public void setTitle(String title) {
		this.title = title;
	}

	/** 获取抓取深度 */
	public int getDepth() {
		return depth;
	}

	/** 设置抓取深度 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/** 获取topN值 */
	public int getTopN() {
		return topN;
	}

	/** 设置TopN值 */
	public void setTopN(int topN) {
		this.topN = topN;
	}

	/** 即该任务是否还需要再次取得其下次的新的url，并再次抓取之 ,0代表自己种子本身，不再任何其下的子链接 */
	public boolean isContinue() {
		return this.current_depth < this.depth;
	}

	/** 任务是否结束 */
	public boolean isOver() {
		return this.current_depth >= this.depth;
	}

	public String getSearchRootUrl(SearchEngineEnum searchEngineEnum) {
		String root_url = null;
		if (SearchEngineEnum.Baidu == searchEngineEnum) {
			root_url = StaticValue.meta_search_root_url_baidu;
		} else if (SearchEngineEnum.Sogou == searchEngineEnum) {
			root_url = StaticValue.meta_search_root_url_sogou;
		} else if (SearchEngineEnum.QiHu360 == searchEngineEnum) {
			root_url = StaticValue.meta_search_root_url_360;
		} else if (SearchEngineEnum.SinaNews == searchEngineEnum) {
			root_url = StaticValue.meta_search_root_url_sina_news;
		}

		
		return root_url;
	}

	public void reset4Circle() {
		this.setCurrent_depth(0);
	}

	public static void main(String[] args) {
		CrawlTaskPojo taskPojo = new CrawlTaskPojo();
		taskPojo.setValue("http://www.weibo.com/abc/");
		taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
		// taskPojo.setCrawlEngine(CrawlEngineEnum.MetaSearch_NEWSPage);

		// System.out.println(taskPojo.getValueFormat());
		System.out.println(taskPojo.getValue());
	}
}
