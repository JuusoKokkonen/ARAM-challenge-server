package com.example.ARAM.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Champion implements Serializable {
	
	@Id
	private String id;
	
	private String name;
	private int wins;
	private int losses;
	 
	Champion() {
		super();
	}
	
	Champion(String name) {
		this.id = name;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Champion [id=" + id + ", name=" + name + ", wins=" + wins + ", losses=" + losses + "]";
	}
	
	
	
	
}

	
	
