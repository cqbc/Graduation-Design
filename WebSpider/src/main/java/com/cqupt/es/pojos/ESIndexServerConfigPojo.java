package com.cqupt.es.pojos;

/**
 * es index server config pojo
 * 
 */
public class ESIndexServerConfigPojo {
	@Override
	public String toString() {
		return "ESIndexServerConfigPojo [ip=" + ip + ", data_port=" + data_port
				+ ", admin_port=" + admin_port + "]";
	}

	private String ip;
	public ESIndexServerConfigPojo(String ip, int data_port,
			int admin_port) {
		this.ip = ip;
		this.data_port = data_port;
		this.admin_port = admin_port;
	}

	public int getData_port() {
		return data_port;
	}

	public void setData_port(int data_port) {
		this.data_port = data_port;
	}

	public int getAdmin_port() {
		return admin_port;
	}

	public void setAdmin_port(int admin_port) {
		this.admin_port = admin_port;
	}

	private int data_port;
	private int admin_port;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
