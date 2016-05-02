package com.cqupt.common.statics;

import java.util.HashMap;
import java.util.Map;

/**
 * 静态变量定义
 * 
 * @author zel
 * 
 */
public class StaticValue {
	
	/**
	 * index name 集中定义处
	 */
	public static String index_name_yuqing = "yuqing_news";
	public static String type_name_portals_web_data = "portals_web_data";
	

	public static final String gb2312_encoding = "gb2312";
	public static String gbk_encoding = "gbk";
	public static String default_encoding = "utf-8";

	public static String default_refer = "http://www.baidu.com/";
	public static String baidu_index = "http://www.baidu.com";

	/**
	 * 一些为搜索相关的静态url的定义
	 */
	public static String meta_search_root_url_baidu = "http://news.baidu.com/";
	public static String meta_search_root_url_sogou = "http://news.sogou.com/";
	public static String meta_search_root_url_360 = "http://sh.qihoo.com/";
	public static String meta_search_root_url_sina_news = "http://search.sina.com.cn/";
	
	/**
	 * 为服务器与客户数据交互新增的key
	 */
	public static String ext_content_rule_key = "ext_content_rule_key";
	public static String ext_content_rule_key_is_changed = "ext_content_rule_key_is_changed";
	// 以这个key去redis中去获取数据，并做为下一步去存储
	public static String ext_content_to_save_list_key = "ext_content_to_save_list_key";
	public static String ext_content_error_list_key = "ext_content_error_list_key";

	/**
	 * bloom filter to save key 4 redis
	 */
	public static String bloom_to_do_task_key = "bloom_to_do_task_key";
	public static String bloom_done_task_key = "bloom_done_task_key";

	/**
	 * redis相关默认变量
	 */
	// 正常的todo和finish任务队列
	public static String redis_task_todo_list_key_name = "task_todo";
	public static String redis_task_finished_key_name = "task_finish";
	// 循环任务的hash set，兼管去重
	public static String redis_task_set_key_name_circle = "task_circle";
	public static String redis_task_circle_queue_key_cache = "task_circle_key_cache";
	public static String redis_task_circle_keyword_queue_key_cache = "task_circle_keyword_key_cache";

	public static String redis_task_todo_key_name_circle = "task_circle_todo";
	public static String redis_task_todo_key_name_circle_keyword = "task_circle_keyword_todo";

	public static String redis_task_todo_list_key_name_level_2 = "task_todo_level_2";

	

	/**
	 * 关于crawl config 4 phantomjs
	 */
	public static boolean is_inject_jquery_default = SystemParasSpider.is_inject_jquery_default;
	public static boolean is_capture_pic_default_seg = SystemParasSpider.is_capture_pic_default_seg;
	public static boolean is_capture_pic_default_body = SystemParasSpider.is_capture_pic_default_body;
	public static boolean is_capture_pic_default_random_page = SystemParasSpider.is_capture_pic_default_random_page;

	public static String pic_file_prefix_name_default = "capture-";
	public static String pic_file_suffix_name_default = ".png";
	public static int max_page_number_default = SystemParasSpider.crawl_max_page_number;
	public static boolean is_data_write_to_file_default = SystemParasSpider.is_data_write_to_file_default;
	public static String data_write_to_file_root_path_default = ".png";
	public static String data_file_prefix_name_default = "page-";
	public static String data_file_suffix_name_default = ".txt";

	/**
	 * 为任意url定义子路径
	 */
	public static String random_url_capture_dir_name = "/capture/";
	public static String random_url_body_data_dir_name = "/body/";
	public static String phantomjs_config_file_name_default = "config_phantomjs.json";
	/**
	 * phantomjs默认参数
	 */
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2";
	public static String userAgent_360 = "Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.27 Safari/525.13";

	/**
	 * phantomjs的执行的js脚本文件名称
	 */
	public static String phantomjs_js_crawl_4_baidu_news = "baidu_news_crawl_search_result.js";
	public static String phantomjs_js_crawl_4_baidu_webpage = "baidu_webpage_crawl_search_result.js";
	public static String phantomjs_js_crawl_4_baidu_random_page = "baidu_crawl_body_random_page.js";
	public static String phantomjs_js_crawl_4_baidu_body = "baidu_crawl_body.js";

	public static String phantomjs_js_crawl_4_360_news = "360_news_crawl_search_result.js";
	public static String phantomjs_js_crawl_4_sogou_news = "sogou_news_crawl_search_result.js";

	public static String phantomjs_js_crawl_4_weixin_sogou = "weixin_crawl_search_result.js";
	
	//新浪新闻
	public static String phantomjs_js_crawl_4_sina_news = "sina_news_crawl_search_result.js";
	
	
	
	public static String qihu360_news_search_url_format = "http://news.haosou.com/ns?q=${query}&src=newhome";
	
	//新浪
	public static String sina_news_search_url_format = "http://search.sina.com.cn/?q=${query}&range=all&c=news&sort=time";
	
	/**
	 * 关于phantomjs config
	 */
	public static String output_encoding_default = default_encoding;
	public static String script_encoding_default = default_encoding;

	// -----------------phantomjs end-------------------

	// 默认的参数设置
	// UA默认设置
	public static Map<String, Object> headerMap = null;
	static {
		headerMap = new HashMap<String, Object>();
		headerMap
				.put("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
	}

	// 关于统计信息的静态变量定义
	public static String statistic_key_template = "template_useful";
	public static String statistic_key_task = "task_counter";

}
