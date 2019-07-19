package com.fx.study.bean;

public class Message {

	private Integer id;
	
	private Integer desId;
	
	private String content;
	
	private int type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDesId() {
		return desId;
	}

	public void setDesId(Integer desId) {
		this.desId = desId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Message(Integer id, Integer desId, String content, int type) {
		super();
		this.id = id;
		this.desId = desId;
		this.content = content;
		this.type = type;
	}
	
	
	
}
