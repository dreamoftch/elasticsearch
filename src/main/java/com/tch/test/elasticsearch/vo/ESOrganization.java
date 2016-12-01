package com.tch.test.elasticsearch.vo;

import java.util.Date;

public class ESOrganization {

  private Long ownerId;
  private Long id;
  private String name;
  private Integer type;
  //仅包含公司的location code，用于前端展示
  private Long[] locationCodes;
  //公司的location code以及parent的code递归一直到最上层，用于搜索
  private Long[] allLocationCodes;
  private Integer[] industryCodes;
  private Integer natureCode;
  private Integer[] industrialModeCodes;
  private Integer empScale;
  private Integer financingStatus;
  private Integer[] highlightCodes;
  private Date registedAt;
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

  public Long[] getLocationCodes() {
    return locationCodes;
  }

  public void setLocationCodes(Long[] locationCodes) {
    this.locationCodes = locationCodes;
  }

  public Integer[] getIndustryCodes() {
    return industryCodes;
  }

  public void setIndustryCodes(Integer[] industryCodes) {
    this.industryCodes = industryCodes;
  }

  public Integer getNatureCode() {
    return natureCode;
  }

  public void setNatureCode(Integer natureCode) {
    this.natureCode = natureCode;
  }

  public Integer[] getIndustrialModeCodes() {
    return industrialModeCodes;
  }

  public void setIndustrialModeCodes(Integer[] industrialModeCodes) {
    this.industrialModeCodes = industrialModeCodes;
  }

  public Integer getEmpScale() {
    return empScale;
  }

  public void setEmpScale(Integer empScale) {
    this.empScale = empScale;
  }

  public Integer getFinancingStatus() {
    return financingStatus;
  }

  public void setFinancingStatus(Integer financingStatus) {
    this.financingStatus = financingStatus;
  }

  public Integer[] getHighlightCodes() {
    return highlightCodes;
  }

  public void setHighlightCodes(Integer[] highlightCodes) {
    this.highlightCodes = highlightCodes;
  }

  public Date getRegistedAt() {
    return registedAt;
  }

  public void setRegistedAt(Date registedAt) {
    this.registedAt = registedAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public Long[] getAllLocationCodes() {
    return allLocationCodes;
  }

  public void setAllLocationCodes(Long[] allLocationCodes) {
    this.allLocationCodes = allLocationCodes;
  }
}
