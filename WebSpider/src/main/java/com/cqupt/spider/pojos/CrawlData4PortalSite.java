

package com.cqupt.spider.pojos;

import java.io.Serializable;

import com.cqupt.common.utils.StringOperatorUtil;

public class CrawlData4PortalSite implements Serializable {

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("                            title=" + title + "\n");
		sb.append("                            author=" + author + "\n");
		sb.append("                            url=" + url + "\n");
		sb.append("                            publishtime="
				+ publish_time_string + "\n");
		sb.append("                            body=" + body + "\n");
		sb.append("                            discuss_number="
				+ discuss_number+ "\n");
		sb.append("                            summary=" + summary+ "\n");
		sb.append("                            keyword=" + keyword+ "\n");
		sb.append("                            likes_number=" + likes_number+ "\n");
		sb.append("                            reply_number=" + reply_number);

		return sb.toString();
	}

	// 需要映射的字段
	private String title;
	// title字段，不进行分词的，用于group by操作
	private String title_not_analyzer;
	// 媒体类型
	private int media_type;

	public String getTitle_not_analyzer() {
		// return title_not_analyzer;
		return this.title;
	}

	public void setTitle_not_analyzer(String title_not_analyzer) {
		// this.title_not_analyzer = title_not_analyzer;
		this.title_not_analyzer = this.title;
	}

	public int getMedia_type() {
		return media_type;
	}

	public void setMedia_type(int media_type) {
		this.media_type = media_type;
	}

	// @Override
	// public String toString() {
	// return "CrawlData4PortalSite [title=" + title + ", theme_word="
	// + theme_word + "]";
	// }

	public String toTestString() {
		return "CrawlData4PortalSite [title=" + title + ", author=" + author
				+ ", publish_time_string=" + publish_time_string + ", url="
				+ url + ", source_title=" + source_title
				+ ", publish_time_long=" + publish_time_long + "]";
	}

	private String author;
	private int discuss_number;
	private int transmit_number;
	private String publish_time_string;

	private String url;
	private int site_id;
	private int item_type;
	private String body;
	private String summary;
	private String theme_word;
	private String keyword;
	private String emotion_polar;
	private String description;
	private long insert_time;
	// 点赞数
	private int likes_number;
	// 回复数
	private int reply_number;

	public int getReply_number() {
		return reply_number;
	}

	public void setReply_number(int reply_number) {
		this.reply_number = reply_number;
	}

	public int getLikes_number() {
		return likes_number;
	}

	public void setLikes_number(int likes_number) {
		this.likes_number = likes_number;
	}

	// 原始标题
	private String source_title;

	// 新增的字段，2015-03-01
	private long publish_time_long;

	// 新增字段，2015-04-08，标志是否是最新的数据， isnew=0是最新的，！=0则代表是旧数据
	private int is_new;

	public int getIs_new() {
		return is_new;
	}

	public String getUrlFormat() {
		if (StringOperatorUtil.isNotBlank(url)) {
			if (url.endsWith("/")) {
				return url.substring(0, url.length() - 1);
			}
		}
		return url;
	}

	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}

	public long getPublish_time_long() {
		return publish_time_long;
	}

	public void setPublish_time_long(long publish_time_long) {
		this.publish_time_long = publish_time_long;
	}

	public String getSource_title() {
		return source_title;
	}

	public void setSource_title(String source_title) {
		this.source_title = source_title;
	}

	public int getSite_id() {
		return site_id;
	}

	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}

	public int getItem_type() {
		return item_type;
	}

	public void setItem_type(int item_type) {
		this.item_type = item_type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTheme_word() {
		return theme_word;
	}

	public void setTheme_word(String theme_word) {
		this.theme_word = theme_word;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getEmotion_polar() {
		return emotion_polar;
	}

	public void setEmotion_polar(String emotion_polar) {
		this.emotion_polar = emotion_polar;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublish_time_string() {
		return publish_time_string;
	}

	public void setPublish_time_string(String publish_time_string) {
		this.publish_time_string = publish_time_string;
	}

	public long getInsert_time() {
		return insert_time;
	}

	public void setInsert_time(long insert_time) {
		this.insert_time = insert_time;
	}

	public int getDiscuss_number() {
		return discuss_number;
	}

	public void setDiscuss_number(int discuss_number) {
		this.discuss_number = discuss_number;
	}

	public int getTransmit_number() {
		return transmit_number;
	}

	public void setTransmit_number(int transmit_number) {
		this.transmit_number = transmit_number;
	}
}
