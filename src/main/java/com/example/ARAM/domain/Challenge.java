package com.example.ARAM.domain;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "challenges")
public class Challenge {

	@Id
	private String challenge_id;
	
	private String username;
	private String user_puuid;
	private Long startDate;
	private Long lastRefresh;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "championlistId", referencedColumnName = "id")
	private ChampionList championlist;
	
	public Challenge(User user, ChampionList championListId) {
		this.challenge_id = UUID.randomUUID().toString();
		this.username = user.getName();
		this.user_puuid = user.getPuuid();
		this.startDate = (long)System.currentTimeMillis();
		this.lastRefresh = (long)System.currentTimeMillis();
		this.championlist = championListId;
	}
	
	public Challenge() {
		super();
	}

	public String getChallenge_id() {
		return challenge_id;
	}
	
	public void setChallenge_id(String challenge_id) {
		this.challenge_id = challenge_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getLastRefresh() {
		return lastRefresh;
	}

	public void setLastRefresh(Long lastRefresh) {
		this.lastRefresh = lastRefresh;
	}

	public ChampionList getChampionlist() {
		return championlist;
	}

	public void setChampionlist(ChampionList championlist) {
		this.championlist = championlist;
	}

	public String getUser_puuid() {
		return user_puuid;
	}

	public void setUser_puuid(String user_puuid) {
		this.user_puuid = user_puuid;
	}

	@Override
	public String toString() {
		return "Challenge [uuid=" + challenge_id + ", username=" + username + ", user_puuid=" + user_puuid + ", startDate="
				+ startDate + ", lastRefresh=" + lastRefresh + ", championlist=" + championlist + "]";
	}


	
	
	

}
