package com.cqupt.common.ws.impl;

import java.util.List;

import javax.jws.WebService;

import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.ws.IEsIndexService;
import com.cqupt.es.dao.ESIndexDao;

/**
 * 任务服务实现类
 * 
 */
@WebService(endpointInterface = "com.cqupt.common.ws.IEsIndexService", serviceName = "EsIndexService")
public class EsIndexServiceImpl implements IEsIndexService {
	
	MyLogger logger = new MyLogger(EsIndexServiceImpl.class);

	public void createIndex1(String indexName) {
		// TODO Auto-generated method stub
		ESIndexDao.createIndex(indexName);
	}
	
	public void createIndex(String indexName, String indexType) {
		// TODO Auto-generated method stub
		ESIndexDao.createIndex(indexName, indexType);
	}

	
	public void addBatchIndex4PortalWeb(String indexName, String indexType,
			List pojoList) {
		// TODO Auto-generated method stub
		ESIndexDao.addBatchIndexByObj(indexName, indexType, pojoList);
	}

	public void deleteIndexByIndexOrType(String indexName, String indexType){
		
		ESIndexDao.deleteIndexByIndexOrType(indexName, indexType);
	}
	
	public void deleteByIndexName(String indexName){
		
		ESIndexDao.deleteByIndexName(indexName);
	}
	
	public void dropIndexType(String indexName,String typeName){
		
		ESIndexDao.dropIndexType(indexName, typeName);
	}
	
	

	public String test() {
		// TODO Auto-generated method stub
		return null;
	}
}

