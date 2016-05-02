package com.cqupt.es.pool;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * es client pool 类
 * 
 * @author zel
 * 
 */
public class ESClientPool<T> {
	// 存放链接池的集合类
	private LinkedList<T> clientList = new LinkedList<T>();

	public LinkedList<T> getClientList() {
		return clientList;
	}

	public void setClientList(LinkedList<T> clientList) {
		this.clientList = clientList;
	}

	private Lock lock = new ReentrantLock();

	public ESClientPool() {
	}

	public void addClient(T clientClass) {
		clientList.add(clientClass);
	}

	public T popIdleClient() {
		T t = null;
		while (true) {
			lock.lock();
			t = this.clientList.pollFirst();
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
	public void pushToIdleClientPool(T t) {
		lock.lock();
		this.clientList.add(t);
		lock.unlock();
	}

	public void removeElement(T t) {
		lock.lock();
		this.clientList.remove(t);
		lock.unlock();
	}

	public void addClientSync(T t) {
		lock.lock();
		this.clientList.add(t);
		lock.unlock();
	}

	public static void main(String[] args) {

	}
}
