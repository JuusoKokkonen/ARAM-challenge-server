package com.example.ARAM.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Champion implements Serializable {
	private static final long serialVersionUID = 123;
	
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

	// Adds wins and losses to challenge object from matchData object
	public void handleMatch(MatchData matchData, Challenge challenge) {
		if (matchData.isWin() == true) {
			this.wins = (this.wins + 1);
			challenge.setTotalGames(challenge.getTotalGames() + 1);
			challenge.setTotalWins(challenge.getTotalWins() + 1);
			System.out.println("Added a win for: " + matchData.getChampion());
		} else {
			this.losses = (this.losses + 1);
			challenge.setTotalGames(challenge.getTotalGames() + 1);
			challenge.setTotalLosses(challenge.getTotalLosses() + 1);
			System.out.println("Added a loss for: " + matchData.getChampion());
		}
	}

	// Getters & Setters
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
