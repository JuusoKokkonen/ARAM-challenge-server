package com.example.ARAM.domain;

public class MatchData {
	
	private String username;
	private String champion;
	private boolean win;
	
	public MatchData(String username, String champion, boolean win) {
		super();
		this.username = username;
		this.champion = champion;
		this.win = win;
	}
	
	public MatchData() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChampion() {
		return champion;
	}

	public void setChampion(String champion) {
		this.champion = champion;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	@Override
	public String toString() {
		return "MatchData [username=" + username + ", champion=" + champion + ", win=" + win + "]";
	}
	
	
	
	
	
	
}
