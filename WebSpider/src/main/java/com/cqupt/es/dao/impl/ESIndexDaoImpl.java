package com.cqupt.es.dao.impl;

import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;

import java.net.SocketTimeoutException;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;

import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasES;
import com.cqupt.common.utils.IOUtil;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.es.manager.ESClientPoolManager;
import com.cqupt.es.pojos.ESJestClientPojo;
import com.cqupt.es.pojos.ESSelfClientPojo;

/**
 * ES索此操作工具类
 * 
 */
public class ESIndexDaoImpl {
	private static MyLogger logger = new MyLogger(ESIndexDaoImpl.class);

	public ESIndexDaoImpl() {

	}

	private ESClientPoolManager esClientPoolManager = new ESClientPoolManager();

	/**
	 * 根据indexName创建索引
	 * 
	 * @param indexName
	 * @throws Exception
	 */
	public void createIndex(String indexName) {
		ESJestClientPojo esJestClientPojo = esClientPoolManager.getJestClient();
		try {
			// new Index.Builder(source)
			esJestClientPojo.getJestClient().execute(
					new CreateIndex.Builder(indexName).build());
			logger.info(esJestClientPojo.getPrefix_sign()
					+ "create index successful!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			esClientPoolManager.pushJestClient(esJestClientPojo);
		}
	}

	public void createIndex(String indexName, String indexType) {
		ESJestClientPojo esJestClientPojo = esClientPoolManager.getJestClient();
		try {
			esJestClientPojo.getJestClient().execute(
					new CreateIndex.Builder(indexName).build());
			logger.info(esJestClientPojo.getPrefix_sign() + "index ok!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			esClientPoolManager.pushJestClient(esJestClientPojo);
		}
	}

	public void createIndexWithESClient(String indexName, String indexType,
			String mappingJsonSource) {
		ESSelfClientPojo esClientPojo = esClientPoolManager.getESClient();
		try {
			esClientPojo.getEsClient().admin().indices()
					.preparePutMapping(indexName).setType(indexType)
					.setSource(mappingJsonSource).execute().actionGet();
			logger.info(esClientPojo.getPrefix_sign()
					+ "create index and type by es client!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			esClientPoolManager.pushESClient(esClientPojo);
		}
	}

	// 增加单一对象数据加进ES索引中
	public void addIndexToES(Index index) {
		ESJestClientPojo esJestClientPojo = esClientPoolManager.getJestClient();
		// 遇到time out时，进行重新请求
		int repeat_time = 0;
		while (repeat_time < SystemParasES.es_index_fail_max_time) {
			try {
				esJestClientPojo.getJestClient().execute(index);
				logger.info(esJestClientPojo.getPrefix_sign() + "index ok!");
				break;
			} catch (SocketTimeoutException timeout) {
				repeat_time++;
				logger.info(esJestClientPojo.getPrefix_sign()
						+ "occur timeout when indexing,will index try again!");
				try {
					Thread.sleep(SystemParasES.es_index_fail_waitting_time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				repeat_time++;
				logger.info(esJestClientPojo.getPrefix_sign()
						+ "occur exception when indexing,will index try again!");
				e.printStackTrace();
				try {
					Thread.sleep(SystemParasES.es_index_fail_waitting_time);
				} catch (Exception sleepException) {
					sleepException.printStackTrace();
				}
			}
		}
		esClientPoolManager.pushJestClient(esJestClientPojo);
	}

	// 批量增加对象集合数据加进ES索引中,indexName为索引名称，typeName为索引类型，indexList为批量可索引对象集合
	public void addIndexToES(String indexName, String typeName,
			List<Index> indexList) {
		if (indexList == null || indexList.isEmpty()) {
			return;
		}
		// 通过manager类得到jest client
		ESJestClientPojo esJestClientPojo = esClientPoolManager.getJestClient();
		// 构建索引名称、索引类型的builder，此为加入索引数据的前提条件
		Bulk.Builder builder = new Bulk.Builder().defaultIndex(indexName)
				.defaultType(typeName);
		// 每个index就是一个可索引对象，将其批量构建成json串数据
		builder.addAction(indexList).build();
		// 构造进可执行实质索引操作的批量操作工具类中
		Bulk bulk2 = new Bulk(builder);

		// 遇到time out时，进行重新请求
		int repeat_time = 0;
		// int continious_exception_count = 0;
		while (repeat_time <= SystemParasES.es_index_fail_max_time) {
			try {
				// 实质执行将数据转化为成索引的索引操作
				esJestClientPojo.getJestClient().execute(bulk2);
				logger.info(esJestClientPojo.getPrefix_sign() + "index ok!");
				break;
			} catch (SocketTimeoutException timeout) {
				timeout.printStackTrace();
				repeat_time++;
				logger.info(esJestClientPojo.getPrefix_sign()
						+ "occur timeout when indexing,will do it try again!");
				try {
					Thread.sleep(SystemParasES.es_index_fail_waitting_time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (repeat_time == SystemParasES.es_index_fail_max_time) {
					esClientPoolManager.removeJestClient(esJestClientPojo);
					logger.info("when indexing occur error,remove a jest client");
					esJestClientPojo = esClientPoolManager.getJestClient();
					repeat_time = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				repeat_time++;
				logger.info(esJestClientPojo.getPrefix_sign()
						+ "occur unknown error when indexing,will do it try again!");
				e.printStackTrace();
				try {
					Thread.sleep(SystemParasES.es_index_fail_waitting_time);
				} catch (Exception sleepException) {
					sleepException.printStackTrace();
				}
				if (repeat_time == SystemParasES.es_index_fail_max_time) {
					esClientPoolManager.removeJestClient(esJestClientPojo);
					logger.info("when indexing occur error,remove a jest client");
					esJestClientPojo = esClientPoolManager.getJestClient();
					repeat_time = 0;
				}
			}
		}
		esClientPoolManager.pushJestClient(esJestClientPojo);
	}

	// 直接删除指定索引库
	public void deleteByIndexName(String indexName) {
		ESJestClientPojo esJestClientPojo = esClientPoolManager.getJestClient();
		DeleteIndex dIndex = new DeleteIndex(new DeleteIndex.Builder(indexName));
		// 遇到time out时，进行重新请求
		int repeat_time = 0;
		while (repeat_time < SystemParasES.es_index_fail_max_time) {
			try {
				esJestClientPojo.getJestClient().execute(dIndex);
				logger.info(esJestClientPojo.getPrefix_sign()
						+ "delete index by indexName successful!");
				break;
			} catch (SocketTimeoutException timeout) {
				repeat_time++;
				logger.info(esJestClientPojo.getPrefix_sign()
						+ "occur timeout when deleteByIndexName,will do it try again!");
				try {
					Thread.sleep(SystemParasES.es_index_fail_waitting_time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(esJestClientPojo.getPrefix_sign()
						+ "to add one item index occur error,will jump the item!");
				break;
			}
		}
		esClientPoolManager.pushJestClient(esJestClientPojo);
	}

	

	/**
	 * 以下是ES端提供的client来操作索引端，删除指定的indexname或是indexname下的名称为type的表数据
	 * 
	 * @param args
	 */
	public void deleteIndexByIndexOrType(String indexName, String typeName) {
		ESSelfClientPojo esClientPojo = esClientPoolManager.getESClient();

		if (StringOperatorUtil.isBlank(indexName)) {
			System.out.println("indexName不可为空!");
		}

		// 遇到time out时，进行重新请求
		int repeat_time = 0;
		while (repeat_time < SystemParasES.es_index_fail_max_time) {
			try {
				if (StringOperatorUtil.isNotBlank(typeName)) {
					esClientPojo.getEsClient().prepareDeleteByQuery(indexName)
							.setTypes(typeName)
							.setQuery(QueryBuilders.matchAllQuery()).execute()
							.actionGet();
				} else {
					this.deleteByIndexName(indexName);
				}

				logger.info(esClientPojo.getPrefix_sign()
						+ "delete index by query successful!");

				break;
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(esClientPojo.getPrefix_sign()
						+ "delete index fail,please try again!");
			}
			repeat_time++;
		}
		esClientPoolManager.pushESClient(esClientPojo);
	}

	// 直接删除索引中的某type
	public void dropIndexType(String indexName, String typeName) {
		ESSelfClientPojo esClientPojo = esClientPoolManager.getESClient();

		if (StringOperatorUtil.isBlank(indexName)
				|| StringOperatorUtil.isBlank(typeName)) {
			System.out.println("indexName或typeName不可为空!");
		}

		// 遇到time out时，进行重新请求
		int repeat_time = 0;
		while (repeat_time < SystemParasES.es_index_fail_max_time) {
			try {
				esClientPojo.getEsClient().admin().indices()
						.prepareDeleteMapping(indexName).setType(typeName)
						.execute().actionGet();
				logger.info(esClientPojo.getPrefix_sign()
						+ "delete indexType successful!");
				break;
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(esClientPojo.getPrefix_sign()
						+ "delete indexType fail,please try again!");
			}
			repeat_time++;
		}
		esClientPoolManager.pushESClient(esClientPojo);
	}

	public static void main(String[] args) {
		
		ESIndexDaoImpl esIndexDaoImpl = new ESIndexDaoImpl();
		String indexName = "yuqing_news";
		String indexType = "portals_web_data";
		String jsonFilePath = "table_schema/portals_web_data.json";

		String mappingJsonSource = IOUtil.readFile(jsonFilePath,
				StaticValue.default_encoding);

		 
		// 删除索引
		// esIndexDaoImpl.dropIndexType(indexName, indexType);
		
//		esIndexDaoImpl.deleteByIndexName(indexName);
		// 创建索引
		esIndexDaoImpl.createIndexWithESClient(indexName, indexType,
				mappingJsonSource);
	}
}
