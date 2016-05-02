
package com.cqupt.spider.task.extractor.detail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cqupt.common.utils.StringOperatorUtil;
import com.vaolan.extkey.utils.UrlOperatorUtil;
import com.vaolan.parser.JsoupHtmlParser;
import com.vaolan.utils.IOUtil;
import com.vaolan.utils.StaticValue;
  
public class UrlExtractor  {
	// 暂时以host完全相同为唯一获取新的url依据，后续会添加新的规则
	public static Set<String> getNewUrls(String fromUrl, String host,
			String htmlSource, int topN) {
		List<String> urlList = JsoupHtmlParser.getAllHref4AddHost(fromUrl,
				host, htmlSource);
		
		if (StringOperatorUtil.isNotBlankCollection(urlList)) {
			int count = 0;
			Set<String> finalUrlSet = new HashSet<String>();
			for (String url : urlList) {
				
				
				//防止抓和自己相等的连接地址
				if(fromUrl.contains(url))
					continue;
				
				// 去掉可能的链接可能的锚点
				url = UrlOperatorUtil.delAnchor(url);
				if (UrlOperatorUtil.isSameHost(host, url)
						&& (!finalUrlSet.contains(url))) {
					if (count >= topN) {
						return finalUrlSet;
					}
					//取出那些非html的网页 
					if(url.contains("html"))
					{
					finalUrlSet.add(url);
					count++;
					}
					
				}
			}
			return finalUrlSet;
		}
		return null;
	}

	public static void main(String[] args) {
		String fileName = "d:/test.txt";
		String htmlSource = IOUtil.readFile(fileName,
				StaticValue.default_encoding);
//		String fromUrl = "http://epaper.jinghua.cn/html/2015-04/09/node_100.htm";
		String fromUrl = "http://zhidao.baidu.com/";
		String host = UrlOperatorUtil.getHost(fromUrl);
		Set<String> set = getNewUrls(fromUrl, host, htmlSource, 200);
		// String url =
		// "http://bbs.hefei.cc/forum.php?mod=viewthread&tid=14198003&page=1";
		for (String url : set) {
			System.out.println(UrlOperatorUtil.delAnchor(url));
		}

	}
}
