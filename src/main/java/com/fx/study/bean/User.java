package com.fx.study.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column
	private String account;
	
	@Column
	private String password;
	
	@Column
	private String name;
	
	@Column
	private Date birthday;
	
	@Column
	private String headimage;
	
	@Column
	private Character sex;
	
	@Column
	private String friends;
	
	@Column
	private String groups;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public Character getSex() {
		return sex;
	}

	public void setSex(Character sex) {
		this.sex = sex;
	}

	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}
	
	

	public User() {
		super();
	}
	
	public User(User user)
	{
		this.id = user.id;
		this.account = user.account;
		this.password = user.password;
		this.name = user.name;
		this.birthday = user.birthday;
		this.headimage = user.headimage;
		this.sex = user.sex;
		this.friends = user.friends;
		this.groups = user.groups;
	}

	public User(Integer id, String account, String password, String name, Date birthday, String headimage,
			Character sex, String friends, String groups) {
		super();
		this.id = id;
		this.account = account;
		this.password = password;
		this.name = name;
		this.birthday = birthday;
		this.headimage = headimage;
		this.sex = sex;
		this.friends = friends;
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", password=" + password + ", name=" + name + ", birthday="
				+ birthday + ", headimage=" + headimage + ", sex=" + sex + ", friends=" + friends + ", groups=" + groups;
	}
	
	
	                     
}
