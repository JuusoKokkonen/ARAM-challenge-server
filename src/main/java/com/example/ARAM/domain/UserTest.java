package com.example.ARAM.domain;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTest {
	
	@JsonProperty("id")
	public String id;
	
	@JsonProperty("puuid")
	public String puuid;
	
	@JsonProperty("name")
	public String name;

	@Override
	public String toString() {
		return "UserTest [id=" + id + ", puuid=" + puuid + ", name=" + name + "]";
	}

}
