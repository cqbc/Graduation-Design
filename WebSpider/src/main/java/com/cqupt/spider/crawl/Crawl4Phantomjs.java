package com.cqupt.spider.crawl;

import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.FileOperatorUtil;
import com.cqupt.common.utils.IOUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.spider.manager.PhantomManager;
import com.cqupt.spider.manager.ProxyManager;
import com.cqupt.spider.pojos.CrawlConfigParaPojo;
import com.cqupt.spider.pojos.CrawlResultPojo;
import com.cqupt.spider.pojos.ProxyPojo;

public class Crawl4Phantomjs {
	// 日志
	public static MyLogger logger = new MyLogger(Crawl4Phantomjs.class);

	// 抓取任意URL的内容或截图
	public static CrawlResultPojo crawlHtmlSourceByRandomUrl(String pageUrl) {
		CrawlResultPojo resultPojo = new CrawlResultPojo();
		if (StringOperatorUtil.isBlank(pageUrl)) {
			resultPojo.setNormal(false);
			return resultPojo;
		}
		String txt_data_file_path = null;
		String txtFileString = null;
		ProxyPojo proxyPojo = null;
		// 遇到http请求错误，则重复请求http_req_error_repeat次
		for (int i = 0; i < SystemParasSpider.http_req_error_repeat_number; i++) {
			try {
				if (SystemParasSpider.proxy_open) {
					proxyPojo = ProxyManager.getOneProxy();
					System.out.println(proxyPojo);
				}

				CrawlConfigParaPojo crawlConfigParaPojo = PhantomManager
						.crawlBody4MetaSearch(pageUrl, proxyPojo,null);

				txt_data_file_path = crawlConfigParaPojo
						.getAbsolute_body_file_path();

				if (StringOperatorUtil.isNotBlank(txt_data_file_path)
						&& FileOperatorUtil.existFile(txt_data_file_path)) {
					// 读取出其下的每个txtFile
					txtFileString = IOUtil.readDirOrFile(txt_data_file_path,
							StaticValue.default_encoding);
					resultPojo.setNormal(true);
					resultPojo.setHtmlSource(txtFileString);
					return resultPojo;
				}
			} catch (Exception e) {
				resultPojo.setNormal(false);
				resultPojo.setDesc(e.getLocalizedMessage());
				e.printStackTrace();
				logger.info("phantomjs在请求过程中出现异常，将重新请求!");
			}
		}
		return resultPojo;
	}

	// 对单pageUrl参数的重载，解决指定文件路径的存储问题
	public static CrawlResultPojo crawlHtmlSourceByRandomUrl(String pageUrl,
			String aidPicFilePathString) {
		CrawlResultPojo resultPojo = new CrawlResultPojo();
		if (StringOperatorUtil.isBlank(pageUrl)) {
			resultPojo.setNormal(false);
			return resultPojo;
		}
		String txt_data_file_path = null;
		String txtFileString = null;
		ProxyPojo proxyPojo = null;
		// 遇到http请求错误，则重复请求http_req_error_repeat次
		for (int i = 0; i < SystemParasSpider.http_req_error_repeat_number; i++) {
			try {
				if (SystemParasSpider.proxy_open) {
					proxyPojo = ProxyManager.getOneProxy();
					System.out.println(proxyPojo);
				}

				CrawlConfigParaPojo crawlConfigParaPojo = PhantomManager
						.crawlBody4MetaSearch(pageUrl, proxyPojo,aidPicFilePathString);
				
				return null;
			} catch (Exception e) {
				resultPojo.setNormal(false);
				resultPojo.setDesc(e.getLocalizedMessage());
				e.printStackTrace();
				logger.info("phantomjs在请求过程中出现异常，将重新请求!");
			}
		}
		return resultPojo;
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://news.hexun.com/2015-05-21/176024720_4.html";
		CrawlResultPojo crawlResultPojo = crawlHtmlSourceByRandomUrl(url);

     System.out.println(crawlResultPojo.getHtmlSource());

		System.out.println("执行完成!");
	}
}
