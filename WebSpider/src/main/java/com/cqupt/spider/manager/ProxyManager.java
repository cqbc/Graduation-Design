package com.cqupt.spider.manager;

import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.MyLogger;
import com.cqupt.spider.pojos.ProxyPojo;

/**
 * 代理管理类，负责指定返回分配的代理配置ip、port、username、password等组成的ProxyPojo对象
 * 
 * @author zel
 * 
 */
public class ProxyManager {
	// 日志
	public static MyLogger logger = new MyLogger(ProxyManager.class);
	public static int pool_current = 0;
	public static int pool_size = 0;

	public static void init() {
		synchronized (ProxyManager.class) {
			if (SystemParasSpider.proxy_open) {
				pool_size = SystemParasSpider.proxyList.size();
			} else {
				pool_size = 1;
			}
		}
	}

	static {
		init();
	}

	public static void removeProxy(ProxyPojo proxyPojo) {
		proxyPojo.setFail_count(proxyPojo.getFail_count() + 1);
		if (proxyPojo.isAbandon()) {
			synchronized (ProxyManager.class) {
				SystemParasSpider.proxyList.remove(proxyPojo);
				pool_size--;
			}
		}
		logger.info("失败超过阀值,丢弃掉," + proxyPojo);
	}

	public static ProxyPojo getOneProxy() {
		synchronized (ProxyManager.class) {
			if (SystemParasSpider.proxy_open) {
				if (pool_current >= pool_size) {
					pool_current = 0;
					if (SystemParasSpider.proxy_self) {
						// 这里是指也用一下当前的IP地址
						return null;
					}
				}
				ProxyPojo proxyPojo = SystemParasSpider.proxyList.get(pool_current);
				pool_current++;

				return proxyPojo;
			}
			return null;
		}
	}

}
