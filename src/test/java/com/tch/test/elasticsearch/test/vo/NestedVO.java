package com.tch.test.elasticsearch.test.vo;

import java.util.List;

public class NestedVO {

	private String group;
	private List<User> users;
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
