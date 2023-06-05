package com.example.ARAM.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
	@JsonProperty("id")
	public String id;
	
	@JsonProperty("puuid")
	public String puuid;
	
	@JsonProperty("name")
	public String name;
	
	@JsonProperty("summonerLevel")
	public Long summonerLevel;

	//Getters & Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPuuid() {
		return puuid;
	}

	public void setPuuid(String puuid) {
		this.puuid = puuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSummonerLevel() {
		return summonerLevel;
	}

	public void setSummonerLevel(Long summonerLevel) {
		this.summonerLevel = summonerLevel;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", puuid=" + puuid + ", name=" + name + ", summonerLevel=" + summonerLevel + "]";
	}

}
