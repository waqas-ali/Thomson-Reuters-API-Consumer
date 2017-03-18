package com.fx.model;

import java.util.Date;

public class EconomicHeadlinesRequestInfo {
	private int id;
	private Date fromDate;
	private Date toDate;
	private int pageNo;
	private int recordsPerPage;
	private int totalRecords;
	private int totalPages;
	private int recordsOnLastPage;

	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getRecordsPerPage() {
		return recordsPerPage;
	}
	public void setRecordsPerPage(int recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getRecordsOnLastPage() {
		return recordsOnLastPage;
	}
	public void setRecordsOnLastPage(int recordsOnLastPage) {
		this.recordsOnLastPage = recordsOnLastPage;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
