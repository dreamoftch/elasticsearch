package com.tch.test.elasticsearch.test;

import java.util.Date;

public class ESOrganization {

	private Long id;
	private String name;
	private Integer type;
	private Integer[] industrialModeCodes;
	private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer[] getIndustrialModeCodes() {
		return industrialModeCodes;
	}

	public void setIndustrialModeCodes(Integer[] industrialModeCodes) {
		this.industrialModeCodes = industrialModeCodes;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
