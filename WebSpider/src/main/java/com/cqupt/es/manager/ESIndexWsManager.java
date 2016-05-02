package com.cqupt.es.manager;

import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.cqupt.common.statics.StaticValueOfRule;
import com.cqupt.common.statics.SystemParasES;
import com.cqupt.common.utils.CxfTimeoutOperatorUtil;
import com.cqupt.common.ws.IEsIndexService;
import com.cqupt.es.pool.WsClientPool;
import com.cqupt.spider.pojos.CrawlData4PortalSite;

/**
 * 对索引操作调用的管理类
 * 
 */
public class ESIndexWsManager {

	private static WsClientPool<IEsIndexService> wsClientPool = new WsClientPool<IEsIndexService>();;

	// 初始化连接池
	static {
		for (int i = 0; i < SystemParasES.ws_pool_default_size; i++) {
			wsClientPool.addServiceClient(createIndexService(IEsIndexService.class));
		}
	}

	private static IEsIndexService createIndexService(Class serviceClass) {
		JaxWsProxyFactoryBean soapFactoryBean = new JaxWsProxyFactoryBean();
		soapFactoryBean.setAddress(StaticValueOfRule.prefix_http
				+ SystemParasES.ws_es_search_server_ip + ":"
				+ SystemParasES.ws_es_search_server_port + "/"
				+ SystemParasES.ws_index_service_name);
		soapFactoryBean.setServiceClass(serviceClass);

		IEsIndexService esIndexService = (IEsIndexService) soapFactoryBean
				.create();
		CxfTimeoutOperatorUtil.setTimeout(esIndexService,
				SystemParasES.ws_service_connection_timeout,
				SystemParasES.ws_service_receive_timeout);
		return esIndexService;
	}


	/**
	 * 为网页添加索引
	 * <p>
	 * 使用方法：给出索引的名称，索引的类型和CrawlData4PortalSite的列表。
	 * 在使用前准备好包含CrawlData4PortalSite对象的list
	 * </p>
	 * <p>
	 * 作用：为list中的CrawlData4PortalSite批量添加索引
	 * </p>
	 * 
	 * @param indexName
	 *            索引名称
	 * @param indexType
	 *            索引类型
	 * @param pojoList
	 *            存储CrawlData4PortalSite对象的list
	 */
	public static void addBatchIndex4PortalWeb(String indexName,
			String indexType, List<CrawlData4PortalSite> pojoList) {
		IEsIndexService indexService = wsClientPool.popIdleServiceClient();
		try {
			indexService.addBatchIndex4PortalWeb(indexName, indexType, pojoList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 将用完的ws search client放回连接池
			wsClientPool.pushToIdleServicePool(indexService);
		}
		return;
	}

	/**
	 * 以indexName,indexType为依据创建索引库
	 * 
	 * @param indexName
	 *            索引名称
	 * @param indexType
	 *            索引类型
	 */
	public static void createIndex(String indexName, String indexType) {
		IEsIndexService indexService = wsClientPool.popIdleServiceClient();

		try {
			indexService.createIndex(indexName, indexType);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 将用完的ws search client放回连接池
			wsClientPool.pushToIdleServicePool(indexService);
		}
		return;
	}
	
	public static void deleteIndexByIndexOrType(String indexName,
			String indexType) {
		
		IEsIndexService indexService = wsClientPool.popIdleServiceClient();
		
		try {
			indexService.deleteIndexByIndexOrType(indexName, indexType);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 将用完的ws search client放回连接池
			wsClientPool.pushToIdleServicePool(indexService);
		}
		return;
	}
	
	
	public static void deleteByIndexName(String indexName) {
		
		IEsIndexService indexService = wsClientPool.popIdleServiceClient();
		
		try {
			indexService.deleteByIndexName(indexName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 将用完的ws search client放回连接池
			wsClientPool.pushToIdleServicePool(indexService);
		}
		return;
	}
	
	public static void dropIndexType(String indexName,String indexType) {
		
		IEsIndexService indexService = wsClientPool.popIdleServiceClient();
		
		try {
			indexService.dropIndexType(indexName,indexType);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 将用完的ws search client放回连接池
			wsClientPool.pushToIdleServicePool(indexService);
		}
		return;
	}

	public static void main(String[] args) {
		
	}


}
