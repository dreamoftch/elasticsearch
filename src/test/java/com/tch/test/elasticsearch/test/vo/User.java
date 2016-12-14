package com.tch.test.elasticsearch.test.vo;

public class User {

	private String name;
	private String address;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
