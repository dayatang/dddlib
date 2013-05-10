package com.dayatang.domain;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class DataPage<T> {
	private List<T> pageData = new ArrayList<T>();
	private int pageIndex;
	private int pageSize;
	private long resultCount;

	public DataPage(List<T> pageData, int pageIndex, int pageSize, long resultCount) {
		super();
		this.pageData = pageData;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.resultCount = resultCount;
	}

	public List<T> getPageData() {
		return pageData;
	}

	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return (int) Math.ceil(resultCount / (pageSize * 1.0));
	}

	public long getResultCount() {
		return resultCount;
	}

	public void setResultCount(long resultCount) {
		this.resultCount = resultCount;
	}

}
