package com.tch.test.elasticsearch.vo;

import java.util.List;

/**
 * Created by tianch on 2017/1/19.
 */
public class ESObject {

  private Long id;

  private List<Long> refIds;

  private User teacher;

  private List<User> students;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<Long> getRefIds() {
    return refIds;
  }

  public void setRefIds(List<Long> refIds) {
    this.refIds = refIds;
  }

  public User getTeacher() {
    return teacher;
  }

  public void setTeacher(User teacher) {
    this.teacher = teacher;
  }

  public List<User> getStudents() {
    return students;
  }

  public void setStudents(List<User> students) {
    this.students = students;
  }
}
