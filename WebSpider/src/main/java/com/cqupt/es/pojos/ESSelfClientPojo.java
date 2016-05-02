package com.cqupt.es.pojos;

import org.elasticsearch.client.Client;

/**
 * es self client的pojo类
 * 
 */
public class ESSelfClientPojo {
	private ESIndexServerConfigPojo esIndexServerConfigPojo;
	private Client esClient;
	private String prefix_sign;

	public String getPrefix_sign() {
		return prefix_sign;
	}

	public void setPrefix_sign(String prefix_sign) {
		this.prefix_sign = prefix_sign;
	}

	public Client getEsClient() {
		return esClient;
	}

	public void setEsClient(Client esClient) {
		this.esClient = esClient;
	}

	public ESIndexServerConfigPojo getEsIndexServerConfigPojo() {
		return esIndexServerConfigPojo;
	}

	public void setEsIndexServerConfigPojo(
			ESIndexServerConfigPojo esIndexServerConfigPojo) {
		this.prefix_sign = esIndexServerConfigPojo.getIp() + ":"
				+ esIndexServerConfigPojo.getData_port() + ":"
				+ esIndexServerConfigPojo.getAdmin_port() + "#";
		this.esIndexServerConfigPojo = esIndexServerConfigPojo;
	}

}
