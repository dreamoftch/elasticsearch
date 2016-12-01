package com.tch.test.elasticsearch.vo;


import java.util.Date;
import java.util.List;


public class OrgSearchReq {

  //搜索关键字
  private String[] keyword;
  //公司类型
  private List<Integer> types;
  //地区
  private List<Integer> locationCodes;
  //行业
  private List<Integer> industryCodes;
  //行业模式
  private List<Integer> industrialModeCodes;
  //亮点
  private List<Integer> highlightCodes;
  //公司性质
  private List<Integer> natureCodes;
  //融资轮数
  private List<Integer> financingStatusList;
  //员工规模
  private List<Integer> empScales;
  //注册时间
  private Date[] registedAt;
  //更新时间
  private Date[] updatedAt;
  //排序字段
  private String sortField;
  //排序类型
  private String sortType;
  private Integer page;
  private Integer size;
  private Long ownerId;

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

  public List<Integer> getLocationCodes() {
    return locationCodes;
  }

  public void setLocationCodes(List<Integer> locationCodes) {
    this.locationCodes = locationCodes;
  }

  public List<Integer> getIndustryCodes() {
    return industryCodes;
  }

  public void setIndustryCodes(List<Integer> industryCodes) {
    this.industryCodes = industryCodes;
  }

  public List<Integer> getNatureCodes() {
    return natureCodes;
  }

  public void setNatureCodes(List<Integer> natureCodes) {
    this.natureCodes = natureCodes;
  }

  public List<Integer> getFinancingStatusList() {
    return financingStatusList;
  }

  public void setFinancingStatusList(List<Integer> financingStatusList) {
    this.financingStatusList = financingStatusList;
  }

  public List<Integer> getEmpScales() {
    return empScales;
  }

  public void setEmpScales(List<Integer> empScales) {
    this.empScales = empScales;
  }

  public Date[] getRegistedAt() {
    return registedAt;
  }

  public void setRegistedAt(Date[] registedAt) {
    this.registedAt = registedAt;
  }

  public Date[] getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date[] updatedAt) {
    this.updatedAt = updatedAt;
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

  public Integer getPage() {
    if (page == null || page < 1) {
      return 1;
    }
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    if (size == null || size < 1) {
      return 10;
    }
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public List<Integer> getIndustrialModeCodes() {
    return industrialModeCodes;
  }

  public void setIndustrialModeCodes(List<Integer> industrialModeCodes) {
    this.industrialModeCodes = industrialModeCodes;
  }

  public List<Integer> getHighlightCodes() {
    return highlightCodes;
  }

  public void setHighlightCodes(List<Integer> highlightCodes) {
    this.highlightCodes = highlightCodes;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }
}
