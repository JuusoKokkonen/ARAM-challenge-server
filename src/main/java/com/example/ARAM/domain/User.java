package com.example.ARAM.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public class User {
	private String id;
	private String puuid;
	private String name;
	

	public User() {
		super();
	}
	
	@JsonCreator
	public User(String id, String puuid, String name) {
		this.id = id;
		this.puuid = puuid;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPuuid() {
		return puuid;
	}
	
	public String getName() {
		return name;
	}
}
