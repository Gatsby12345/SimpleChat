package com.fx.study.bean;
/**
 * 好友
 * @author fx
 *
 */
public class Friends {

	private String name;
	
	private String members;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public Friends(String name, String members) {
		super();
		this.name = name;
		this.members = members;
	}

	public Friends() {
		super();
	}
	
	
}
