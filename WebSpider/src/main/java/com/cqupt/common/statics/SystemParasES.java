package com.cqupt.common.statics;


import java.util.LinkedList;
import java.util.List;

import com.cqupt.common.utils.IOUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.ReadConfigUtil;
import com.cqupt.es.pojos.ESIndexServerConfigPojo;

/**
 * 系统参数配置
 * 
 */
public class SystemParasES {
	private static MyLogger logger = new MyLogger(SystemParasSpider.class);
	public static ReadConfigUtil systemConfigReadUtil = new ReadConfigUtil(
			"es_config.properties", true);

	/**
	 * es相关配置
	 */
	public static String ws_es_search_server_ip = systemConfigReadUtil
			.getValue("ws_es_search_server_ip");
	public static int ws_es_search_server_port = Integer
			.parseInt(systemConfigReadUtil.getValue("ws_es_search_server_port"));
    /**
     * 关于ws spider client config
     */
	public static String ws_spider_server_ip = systemConfigReadUtil
			.getValue("ws_spider_server_ip");
	public static int ws_spider_server_port = Integer
			.parseInt(systemConfigReadUtil.getValue("ws_spider_server_port"));
	public static String ws_spider_task_service_name = systemConfigReadUtil
			.getValue("ws_spider_task_service_name");
	
	
	/**
	 * es索此操作相关配置
	 */
	public static int add_index_batch_size = Integer
			.parseInt(systemConfigReadUtil.getValue("add_index_batch_size"));
	// 最大可以添加的索引的数量
	public static int add_index_total_max_size = Integer
			.parseInt(systemConfigReadUtil.getValue("add_index_total_max_size"));
	

	
	/**
	 * web service 配置
	 * 
	 * @param args
	 */

	public static String ws_index_service_name = systemConfigReadUtil
			.getValue("ws_index_service_name");

	public static int ws_pool_default_size = Integer
			.parseInt(systemConfigReadUtil.getValue("ws_pool_default_size"));
	public static int ws_service_connection_timeout = Integer
			.parseInt(systemConfigReadUtil.getValue("ws_service_connection_timeout"));
	public static int ws_service_receive_timeout = Integer
			.parseInt(systemConfigReadUtil.getValue("ws_service_receive_timeout"));
	
	
	/**
	 * es相关配置
	 */
	public static String es_cluster_name = systemConfigReadUtil
			.getValue("es_cluster_name");
	public static String es_search_server_ip = systemConfigReadUtil
			.getValue("es_search_server_ip");
	public static int es_search_server_port = Integer
			.parseInt(systemConfigReadUtil.getValue("es_search_server_port"));

	public static String es_index_server_ip = systemConfigReadUtil
			.getValue("es_index_server_ip");
	public static int es_index_server_data_port = Integer
			.parseInt(systemConfigReadUtil
					.getValue("es_index_server_data_port"));
	public static int es_index_server_admin_port = Integer
			.parseInt(systemConfigReadUtil
					.getValue("es_index_server_admin_port"));

	public static int es_index_fail_max_time = Integer
			.parseInt(systemConfigReadUtil.getValue("es_index_fail_max_time"));
	/**
	 * es client去搜索或向es server发相关请求时，如果达到下边指定的数值时，则要进行重新的客户端对象的申请!
	 */
	public static int es_client_continious_exception_count_max = Integer
			.parseInt(systemConfigReadUtil
					.getValue("es_client_continious_exception_count_max"));

	public static int es_index_fail_waitting_time = Integer
			.parseInt(systemConfigReadUtil
					.getValue("es_index_fail_waitting_time"));
	// public static String ad_crowd_base_data_root = systemConfigReadUtil
	// .getValue("ad_crowd_base_data_root");

	/**
	 * 链接池的大小配置,包括jest client和es client
	 */
	public static int jest_client_pool_size = Integer
			.parseInt(systemConfigReadUtil.getValue("jest_client_pool_size"));
	public static int jest_client_timeout = Integer
			.parseInt(systemConfigReadUtil.getValue("jest_client_timeout")) * 1000;
	public static int es_client_pool_size = Integer
			.parseInt(systemConfigReadUtil.getValue("es_client_pool_size"));

	/**
	 * es索此操作相关配置
	 */
	
	public static int add_index_sleep_interval_time = Integer
			.parseInt(systemConfigReadUtil
					.getValue("add_index_sleep_interval_time"));
	/**
	 * 定义添加索引时候的多线程相关操作
	 */
	// 定义是否开启多线程去添加索引
	public static boolean add_index_by_multi_threads = Boolean
			.parseBoolean(systemConfigReadUtil
					.getValue("add_index_by_multi_threads"));
	// 添加索引时的多线程操作的方式，是直接按文件个数启动多线程，还是按文本的行数每一批启动一个线程来运行,其下的参数，0代表按一个文件启动一个线程，1代表按文本行去
	// 取一批量的文本来启动一个多线程
	public static int add_index_by_multi_threads_setup_by_file_or_txtLines = Integer
			.parseInt(systemConfigReadUtil
					.getValue("add_index_by_multi_threads_setup_by_file_or_txtLines"));
	// 启动多少个消费者线程来进行真正的将数据提交到索引服务端
	public static int add_index_multi_threads_consumer_number = Integer
			.parseInt(systemConfigReadUtil
					.getValue("add_index_multi_threads_consumer_number"));
	// 启动多少个生产者线程来产生索引前的数据，该值为最大的阀值
	public static int add_index_multi_threads_producer_max_number = Integer
			.parseInt(systemConfigReadUtil
					.getValue("add_index_multi_threads_producer_max_number"));

	// 生产者-消费者模式中，资源池中的数据为多少条时停止添加，待小于该条数时再去添加,即阀值
	public static int add_index_multi_threads_resource_number_threshold = Integer
			.parseInt(systemConfigReadUtil
					.getValue("add_index_multi_threads_resource_number_threshold"));


	/**
	 * 读取es client相关参数，包括索引服务和搜索服务的ip、port等
	 * 
	 * @param args
	 */
	public static List<ESIndexServerConfigPojo> esIndexServerConfigPojoList = new LinkedList<ESIndexServerConfigPojo>();
	static {
		// 读取index server config list
		ReadConfigUtil esIndexConfigFilePath = new ReadConfigUtil(
				"es_index_server.txt", false);
		String es_index_server_string = esIndexConfigFilePath.getTextLines();
		List<String> lineList = IOUtil
				.tranBatchLineToList(es_index_server_string);
		String[] colArray = null;
		ESIndexServerConfigPojo esIndexServerConfigPojo = null;
		for (String line : lineList) {
			// logger.info("line---" + line);
			colArray = line.split(StaticValueOfRule.separator_tab);
			if (colArray.length == 3) {
				esIndexServerConfigPojo = new ESIndexServerConfigPojo(
						colArray[0], Integer.parseInt(colArray[1]),
						Integer.parseInt(colArray[2]));
				esIndexServerConfigPojoList.add(esIndexServerConfigPojo);
			}
		}
		
	}

	public static void main(String[] args) {
		
		String s = SystemParasES.es_search_server_ip;
		System.out.println(s);
	}
}

