package com.cqupt.es.pool;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ws service client pool 类
 * 实现一个服务客户端类池
 */
public class WsClientPool<T> {
	// 存放链接池的集合类
	private LinkedList<T> serviceClientList = new LinkedList<T>();
	private Lock lock = new ReentrantLock();

	public WsClientPool() {
	}

	public void addServiceClient(T serviceClientClass) {
		serviceClientList.add(serviceClientClass);
	}

	public T popIdleServiceClient() {
		T t = null;
		while (true) {
			lock.lock();
			t = this.serviceClientList.pollFirst();
			if (t != null) {
				// 释放锁后返回
				lock.unlock();
				return t;
			} else {
				try {
					// 释放锁,再sleep
					lock.unlock();
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 将用它的service client放入到pool中
	public void pushToIdleServicePool(T t) {
		lock.lock();
		this.serviceClientList.add(t);
		lock.unlock();
	}

	public static void main(String[] args) {
		
	}
}
