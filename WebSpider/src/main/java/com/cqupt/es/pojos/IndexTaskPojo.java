package com.cqupt.es.pojos;

import io.searchbox.core.Index;

import java.util.List;

/**
 * 生产者消费者多线程模式中的pojo类
 * 
 */
public class IndexTaskPojo {
	private String indexName;
	private String indexType;
	private List<Index> todoList;

	public IndexTaskPojo(String indexName, String indexType,
			List<Index> todoList) {
		this.indexName = indexName;
		this.indexType = indexType;
		this.todoList = todoList;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public List<Index> getTodoList() {
		return todoList;
	}

	public void setTodoList(List<Index> todoList) {
		this.todoList = todoList;
	}
}
