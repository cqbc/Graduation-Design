package com.cqupt.es.dao;

import io.searchbox.core.Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.es.dao.impl.ESIndexDaoImpl;
import com.cqupt.spider.pojos.CrawlData4PortalSite;

/**
 * es搜索管理器
 * 
 */
public class ESIndexDao {
	private static MyLogger logger = new MyLogger(ESIndexDao.class);
	private static ESIndexDaoImpl esIndexDaoImpl = new ESIndexDaoImpl();

	

	// 将所有ESOperatorUtil工具类的所有操作均通过ESIndexManager来完成，不直接引用ESOperatorUtil来搞
	public void addIndexToES(String indexName, String typeName,
			List<Index> indexList) {
		esIndexDaoImpl.addIndexToES(indexName, typeName, indexList);
	}

	
	public static void addBatchIndexByObj(String indexName, String indexType,
			List pojoList) {
		if (StringOperatorUtil.isBlankCollection(pojoList)) {
			return;
		}
		List<Index> indexList = new ArrayList<Index>();
		Index index = null;
		for (Object pojo : pojoList) {
			index = new Index.Builder(pojo).build();
			indexList.add(index);
		}
		esIndexDaoImpl.addIndexToES(indexName, indexType, indexList);
	}
	
	
	public static void deleteByIndexName(String indexName) {
		esIndexDaoImpl.deleteByIndexName(indexName);
	}

	public static void deleteIndexByIndexOrType(String indexName,
			String typeName) {
		esIndexDaoImpl.deleteIndexByIndexOrType(indexName, typeName);
	}
	
	
	public static void dropIndexType(String indexName,
			String typeName) {
		esIndexDaoImpl.dropIndexType(indexName, typeName);
	}
	
	

	public static void createIndex(String indexName) {
		esIndexDaoImpl.createIndex(indexName);
	}


	// 通过一批一批非阻塞式的去添加文本行达到多线程添加索引数据的目的

	public static void createIndex(String indexName, String indexType) {
		esIndexDaoImpl.createIndex(indexName);
	}

	
	// 将任意对象要索引的数据对象，转化为可索引地象
		private static List<Index> convertToIndexList(List pojoList) {
			List<Index> indexList = new ArrayList<Index>();
			Index index = null;
			for (Object obj : pojoList) {
				index = new Index.Builder(obj).build();
				indexList.add(index);
			}
			return indexList;
		}
	public static void main(String[] args) {

		String indexName = StaticValue.index_name_yuqing;
		String typeName = "test";

		

		Map<String, Object> newFieldsMap = new HashMap<String, Object>();
		newFieldsMap.put("is_new", 1);

		CrawlData4PortalSite crawlData4PortalSite = new CrawlData4PortalSite();
		crawlData4PortalSite.setUrl("http://www.baidu.com/3");
		crawlData4PortalSite.setMedia_type(2);
		crawlData4PortalSite.setTransmit_number(3);
		crawlData4PortalSite.setIs_new(0);
		List<CrawlData4PortalSite> list = new LinkedList<CrawlData4PortalSite>();
		list.add(crawlData4PortalSite);

	

		System.out.println("执行完成!");
	}
}
