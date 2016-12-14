package com.tch.test.elasticsearch.test;

import java.util.Date;
import java.util.List;

public class OrgSearchReq {

	private String[] keyword;
	private List<Integer> types;
	private List<Integer> industrialModeCodes;
	private Date[] updatedAt;
	private Integer page;
	private Integer size;
	private String sortField;
	private String sortType;
	private static final int FIRST_PAGE = 1;
	private static final int PAGE_SIZE = 10;

	public String[] getKeyword() {
		return keyword;
	}

	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public List<Integer> getIndustrialModeCodes() {
		return industrialModeCodes;
	}

	public void setIndustrialModeCodes(List<Integer> industrialModeCodes) {
		this.industrialModeCodes = industrialModeCodes;
	}

	public Date[] getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date[] updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getPage() {
		if (page == null || page < 0) {
			return FIRST_PAGE;
		}
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		if (size == null || size < 0) {
			return PAGE_SIZE;
		}
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getOffset() {
		return (getPage() - 1) * getSize();
	}

}
