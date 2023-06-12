package com.example.ARAM.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
	
	private String user = null;
	private ArrayList<MatchData> list;
	
	@JsonProperty("info")
	@SuppressWarnings("unchecked")
	private void mapData(Map<String, Object> data) {
		List<MatchData> list = new ArrayList<MatchData>();
		ArrayList<Object> participants = (ArrayList<Object>) data.get("participants");
		for(Object data1 : participants) {
			String championName = (((Map<String, Object>) data1).get("championName").toString());
			String summonerName = (((Map<String, Object>) data1).get("summonerName").toString());
			boolean win = (Boolean.parseBoolean(((Map<String, Object>) data1).get("win").toString()));
			MatchData matchData = new MatchData(summonerName, championName, win);
			list.add(matchData);
		}
		this.list = (ArrayList<MatchData>) list;

	}

	//Getters & Setters
	public ArrayList<MatchData> getList() {
		return list;
	}

	public void setList(ArrayList<MatchData> list) {
		this.list = list;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Match [user=" + user + ", list=" + list + "]";
	}

}
