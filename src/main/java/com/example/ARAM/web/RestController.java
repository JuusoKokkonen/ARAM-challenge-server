package com.example.ARAM.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.example.ARAM.domain.Challenge;
import com.example.ARAM.domain.ChallengeRepository;
import com.example.ARAM.domain.Champion;
import com.example.ARAM.domain.ChampionList;
import com.example.ARAM.domain.ChampionListRepository;
import com.example.ARAM.domain.Match;
import com.example.ARAM.domain.MatchData;
import com.example.ARAM.domain.User;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
	
	@Value("${RIOT_API_KEY}")
    private String apiKey;
	
	@Autowired
	private ChallengeRepository challengeRepository;
	
	@Autowired
	private ChampionListRepository championListRepository;
	
	
	// Return all champion lists from database as json
	@GetMapping(value = "/championlists")
	@CrossOrigin(origins = "http://localhost:3000")
	private ResponseEntity<?> getAllChampionLists() {
		return ResponseEntity.ok(this.championListRepository.findAll());
	}
	
	// Return all challenges from database as json
	@GetMapping(value = "/challengelists")
	@CrossOrigin(origins = "http://localhost:3000")
	private ResponseEntity<?> getAllChallenges() {
		return ResponseEntity.ok(this.challengeRepository.findAll());
	}
		
	// Return challenge by "uuid" (challengelist code)
	@GetMapping(value = "/challenge/{uuid}")
	private ResponseEntity<?> getChallengeById(@PathVariable String uuid) {
		return ResponseEntity.ok(this.challengeRepository.findById(uuid));
	}
		
	// Create new ChampionList with data from RiotGames api
	@GetMapping(value = "/champions")
	public ChampionList getChampions() {
		String uri = "http://ddragon.leagueoflegends.com/cdn/13.7.1/data/en_US/champion.json";
		RestTemplate restTemplate = new RestTemplate();
		ChampionList newChampionList = restTemplate.getForObject(uri, ChampionList.class);
		championListRepository.save(newChampionList);
		return newChampionList;
	}
	
	// Get user data by username
	@GetMapping(value = "/user/{username}")
	public User getUserByUsername(@PathVariable String username) {
		String uri = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+ username + "?api_key=" + apiKey;
		RestTemplate restTemplate = new RestTemplate();
		User user = restTemplate.getForObject(uri, User.class);
		return user;
	}
	
	// Create a new challenge
	@GetMapping(value = "/newchallenge/{username}")
	public Challenge createChallenge(@PathVariable String username) {
		User user = getUserByUsername(username);
		ChampionList championList = getChampions();
		Challenge challenge = new Challenge(user, championList);
		challengeRepository.save(challenge);
		return challenge;
	}
	
	// Get user match data by user puuid
	// RENAME TEST
	// set match count right
	@GetMapping(value = "/matches/{challenge_id}")
	public HashMap<String, Champion> getMatches(@PathVariable String challenge_id) {
		Challenge challenge = challengeRepository.getById(challenge_id);
		String uri = "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/" + challenge.getUser_puuid() + "/ids?queue=450&start" + challenge.getLastRefresh() + "&count=20&api_key=" + apiKey;
		ChampionList championList = challenge.getChampionlist();
		
		// Create a HashMap for easier data access
		HashMap<String, Champion> mapChampions = new HashMap<String, Champion>();
		for (Champion champion : championList.getList()) {
			if(champion.getName().contains(" ")) {
				String newName1 = champion.getName().replace(" ", "");
				System.out.println("Set: " + champion.getName() + " to " + newName1);
				champion.setName(newName1);
			}
			if(champion.getName().contains("'")) {
				String newName1 = champion.getName().replace("'", "").toLowerCase();
				System.out.println("Set: " + champion.getName() + " to " + newName1);
				String newName2 = newName1.substring(0,1).toUpperCase() + newName1.substring(1);
				System.out.println("Set: " + newName1 + " to " + newName2);
				champion.setName(newName2);
			}
			
			if (champion.getName().equals("Kogmaw")) {
				champion.setName("KogMaw");
				System.out.println("Handled KogMaw exception");
			}
			
			if (champion.getName().equals("Nunu & Willump")) {
				champion.setName("Nunu");
				System.out.println("Handled Nunu exception");
			}
			mapChampions.put(champion.getName(), champion);
		}

		// HANDLE WINS AND LOSSES
		ArrayList<Champion> augh = championList.getList();
		RestTemplate restTemplate = new RestTemplate();
		List<String> test = restTemplate.getForObject(uri, List.class);
		List<MatchData> kekw = parseMatch(test.get(0), challenge.getUsername(), test);
		Champion currentChampion;
		
		for (MatchData entry : kekw) {
			System.out.println(entry.getChampion());
			currentChampion = mapChampions.get(entry.getChampion());
			currentChampion.handleMatch(entry);
		
		}
		championList.setList(augh);
		championListRepository.save(championList);
		return mapChampions;
	}
	
	// Parse individual match data into challenge object
	@GetMapping(value = "/parsematch/{matchId}/{user}")
	public List<MatchData> parseMatch(String matchId, String user, List<String> matchList) {
		String uri = "https://europe.api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + apiKey;
		RestTemplate restTemplate = new RestTemplate();
		
		List<MatchData> response = new ArrayList<MatchData>();
		
		for (String entry : matchList) {
			uri = "https://europe.api.riotgames.com/lol/match/v5/matches/" + entry + "?api_key=" + apiKey;
			Match gameData = restTemplate.getForObject(uri, Match.class);
			gameData.setUser(user);
			for (MatchData lulw : gameData.getList()) {
				if (lulw.getUsername().toLowerCase().equals(user.toLowerCase())) {
					response.add(lulw);
				}
			}

		}
		
		return response;
	}


}
