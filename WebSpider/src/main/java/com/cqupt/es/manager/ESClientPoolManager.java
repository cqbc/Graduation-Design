package com.cqupt.es.manager;

import com.cqupt.common.statics.SystemParasES;
import com.cqupt.common.utils.ESCommonOperatorUtil;
import com.cqupt.es.pojos.ESIndexServerConfigPojo;
import com.cqupt.es.pojos.ESJestClientPojo;
import com.cqupt.es.pojos.ESSelfClientPojo;
import com.cqupt.es.pool.ESClientPool;

/**
 * es client manager es client管理器，当太多的并发一起时，会导致object太多，故在此进行client
 * pool管理，防止内存溢处
 * 
 */
public class ESClientPoolManager {
	// jest client pool
	private ESClientPool<ESJestClientPojo> esJestClientPool = new ESClientPool<ESJestClientPojo>();

	// es client pool, es client的客户端保持目前即可
	private ESClientPool<ESSelfClientPojo> esClientPool = new ESClientPool<ESSelfClientPojo>();

	// 初始化连接池

	// 初始化各jest client
	public ESClientPoolManager() {
		// 先初始化es index server pool
		ESJestClientPojo esJestClientPojo = null;
		ESSelfClientPojo esSelfClientPojo = null;
		for (ESIndexServerConfigPojo esIndexServerConfigPojo : SystemParasES.esIndexServerConfigPojoList) {
			ESCommonOperatorUtil esCommonOperatorUtil = new ESCommonOperatorUtil(
					esIndexServerConfigPojo.getIp(),
					esIndexServerConfigPojo.getData_port(),
					esIndexServerConfigPojo.getAdmin_port());
			// init es jest client
			for (int i = 0; i < SystemParasES.jest_client_pool_size; i++) {
				esJestClientPojo = new ESJestClientPojo();
				esJestClientPojo.setEsIndexServerConfigPojo(esIndexServerConfigPojo);
				esJestClientPojo.setJestClient(esCommonOperatorUtil
						.getJestClient());

				esJestClientPool.addClient(esJestClientPojo);
			}
		}
		ESCommonOperatorUtil esCommonOperatorUtil = new ESCommonOperatorUtil(
				SystemParasES.es_index_server_ip,
				SystemParasES.es_index_server_data_port,
				SystemParasES.es_index_server_admin_port);
		
		ESIndexServerConfigPojo esIndexServerConfigPojo = new ESIndexServerConfigPojo(
				SystemParasES.es_index_server_ip,
				SystemParasES.es_index_server_data_port,
				SystemParasES.es_index_server_admin_port);
		// init es self client for port admin port
		for (int i = 0; i < SystemParasES.es_client_pool_size; i++) {
			esSelfClientPojo = new ESSelfClientPojo();
			esSelfClientPojo.setEsIndexServerConfigPojo(esIndexServerConfigPojo);
			esSelfClientPojo.setEsClient(esCommonOperatorUtil.getEsClient());
			esClientPool.addClient(esSelfClientPojo);
		}

	}

	// 删除一个可能的错误的jest client
	public void removeJestClient(ESJestClientPojo esJestClientPojo) {
		esJestClientPool.removeElement(esJestClientPojo);
	}

	// 异步添加新元素到链接池中
	public void addElementSync(ESJestClientPojo esJestClientPojo_bad) {
		ESJestClientPojo esJestClientPojo_new = new ESJestClientPojo();
		esJestClientPojo_new.setEsIndexServerConfigPojo(esJestClientPojo_bad
				.getEsIndexServerConfigPojo());
		ESCommonOperatorUtil esCommonOperatorUtil = new ESCommonOperatorUtil(
				esJestClientPojo_new.getEsIndexServerConfigPojo().getIp(),
				esJestClientPojo_new.getEsIndexServerConfigPojo()
						.getData_port(), esJestClientPojo_new
						.getEsIndexServerConfigPojo().getAdmin_port());
		esJestClientPojo_new
				.setJestClient(esCommonOperatorUtil.getJestClient());

		esJestClientPool.addClientSync(esJestClientPojo_new);
	}

	public ESJestClientPojo getJestClient() {
		return esJestClientPool.popIdleClient();
	}

	public void pushJestClient(ESJestClientPojo esJestClientPojo) {
		esJestClientPool.pushToIdleClientPool(esJestClientPojo);
	}

	public ESSelfClientPojo getESClient() {
		return esClientPool.popIdleClient();
	}

	public void pushESClient(ESSelfClientPojo client) {
		esClientPool.pushToIdleClientPool(client);
	}

	// 销毁传入的旧的，返回一个新的
	public ESJestClientPojo getNewClient(ESJestClientPojo esJestClientPojo) {
		/**
		 * 进行相关es client的替换
		 */
		this.removeJestClient(esJestClientPojo);
		this.addElementSync(esJestClientPojo);

		esJestClientPojo = this.getJestClient();

		return esJestClientPojo;
	}

	// 关掉所有链接池的链接，不然会在es server端报异常关闭port
	public void closeAllClient() {
		if (esJestClientPool != null
				&& esJestClientPool.getClientList() != null) {
			for (ESJestClientPojo esJestClientPojo : esJestClientPool
					.getClientList()) {
				esJestClientPojo.getJestClient().shutdownClient();
			}
		}
		if (esClientPool != null && esClientPool.getClientList() != null) {
			for (ESSelfClientPojo esSelfClientPojo : esClientPool
					.getClientList()) {
				esSelfClientPojo.getEsClient().close();
			}
		}
	}

	public static void main(String[] args) {

	}
}
