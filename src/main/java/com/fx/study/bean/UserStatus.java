package com.fx.study.bean;

public class UserStatus extends User {
	

	private Boolean online;

	public Boolean isOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}
	
	public UserStatus() {
		super();
	}
	
	public UserStatus(User user) {
		super(user);
	}

	public UserStatus(User user,Boolean online) {
		super(user);
		this.online = online;
	}
	
}
