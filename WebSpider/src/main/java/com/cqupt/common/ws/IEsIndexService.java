package com.cqupt.common.ws;

import java.util.List;

import javax.jws.WebService;

import com.cqupt.es.dao.ESIndexDao;
import com.cqupt.spider.pojos.CrawlData4PortalSite;

/**
 * ES索引服务
 * 
 * @author zel
 * 
 */
@WebService
public interface IEsIndexService {
	 
	 void createIndex1(String indexName);
	
	/**
	 * 以indexName,indexType为依据创建索引库
	 * 
	 * @param indexName
	 *            索引名称
	 * @param indexType
	 *            索引类型
	 */
	 void createIndex(String indexName, String indexType);
	 
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
	 void addBatchIndex4PortalWeb(String indexName, String indexType,
				List<CrawlData4PortalSite> pojoList);
	 
	 void deleteIndexByIndexOrType(String indexName, String indexType);
		
	 void deleteByIndexName(String indexName);
		
	 void dropIndexType(String indexName,String typeName);
		
	 String test();

	 

}
