package com.cqupt.es.pojos;

import io.searchbox.client.JestClient;

/**
 * es jest client的pojo类
 * 
 */
public class ESJestClientPojo {
	private ESIndexServerConfigPojo esIndexServerConfigPojo;
	private JestClient jestClient;
	private String prefix_sign;

	public String getPrefix_sign() {
		return prefix_sign;
	}

	public void setPrefix_sign(String prefix_sign) {
		this.prefix_sign = prefix_sign;
	}

	public ESJestClientPojo() {
		
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

	public JestClient getJestClient() {
		return jestClient;
	}

	public void setJestClient(JestClient jestClient) {
		this.jestClient = jestClient;
	}
}
