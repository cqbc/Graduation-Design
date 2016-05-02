package com.cqupt.spider.task.extractor.detail;

import java.util.List;

import com.cqupt.spider.pojos.MatchResultKeyValue;

public interface IExtractorContentRule {
	public List<MatchResultKeyValue> getContent(String source);
}
