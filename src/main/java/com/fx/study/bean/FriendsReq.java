package com.fx.study.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "friendsreq")
public class FriendsReq {

	@Id
	@Column
	private int id;
	
	@Column
	private int userid;
	
	@Column
	private int destid;
	
	@Column
	private Date time;
	
	@Column
	private int status;
	
	@Column
	private String groupname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getDestid() {
		return destid;
	}

	public void setDestid(int destid) {
		this.destid = destid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public FriendsReq(int userid, int destid, Date time, int status, String groupname) {
		super();
		this.userid = userid;
		this.destid = destid;
		this.time = time;
		this.status = status;
		this.groupname = groupname;
	}

	public FriendsReq() {
		super();
	}
	
}
