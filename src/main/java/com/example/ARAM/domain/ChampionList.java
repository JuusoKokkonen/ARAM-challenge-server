package com.example.ARAM.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChampionList {

	private Long id;
	
	private ArrayList<Champion> list;

	public ChampionList(ArrayList<Champion> list) {
		this.list = list;
	}
	
	public ChampionList() {
		super();
	}
	
	@JsonProperty("data")
	@SuppressWarnings("unchecked")
    private void mapData(Map<String, Object> data) {
		List<Champion> list = new ArrayList<Champion>();
		for(String key : data.keySet()) {
			String championName = ((Map<String,String>)data.get(key)).get("name");
			Champion newChampion = new Champion(championName);
			list.add(newChampion);
		}
		this.list = (ArrayList<Champion>) list;
    }

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArrayList<Champion> getList() {
		return list;
	}

	public void setList(ArrayList<Champion> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		return "ChampionList [id=" + id + ", list=" + list + "]";
	}
	
	
}


