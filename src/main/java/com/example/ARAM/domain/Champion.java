package com.example.ARAM.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Champion {
	
	private String name;
	private int wins;
	private int losses;
	 
	Champion() {
		super();
	}
	
	Champion(String name) {
		this.name = name;
		this.wins = 0;
		this.losses = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	@Override
	public String toString() {
		return "Champion [name=" + name + ", wins=" + wins + ", losses=" + losses + "]";
	}
	
	
	
	
}

	
	

