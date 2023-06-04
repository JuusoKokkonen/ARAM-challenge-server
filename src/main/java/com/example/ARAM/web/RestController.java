package com.example.ARAM.web;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

	// Return challenge by "challenge_id"
	@GetMapping(value = "/challenge/{challenge_id}")
	@CrossOrigin(origins = "http://localhost:3000")
	private ResponseEntity<?> getChallengeById(@PathVariable String challenge_id) {
		return ResponseEntity.ok(this.challengeRepository.findById(challenge_id));
	}

	// Create new ChampionList with data from RiotGames api
	@GetMapping(value = "/champions")
	@CrossOrigin(origins = "http://localhost:3000")
	public ChampionList getChampions() {
		String uri = "http://ddragon.leagueoflegends.com/cdn/13.7.1/data/en_US/champion.json";
		RestTemplate restTemplate = new RestTemplate();
		ChampionList newChampionList = restTemplate.getForObject(uri, ChampionList.class);

		// Due to inconsistencies in Riot Games api output some champion (game character) names need to be modified
		for (Champion champion : newChampionList.getList()) {

			// Handle Nunu
			if (champion.getName().equals("Nunu & Willump")) {
				champion.setName("Nunu");
				System.out.println("Handled Nunu exception");
			}

			// Handle Dr.Mundo
			if (champion.getName().equals("Dr. Mundo")) {
				champion.setName("DrMundo");
				System.out.println("Handled Dr.Mundo exception");
			}

			// Removing spaces from champion names
			if (champion.getName().contains(" ")) {
				String newName = champion.getName().replace(" ", "");
				System.out.println("Set: " + champion.getName() + " to " + newName);
				champion.setName(newName);
			}

			// Removing " ' " from champion names
			if (champion.getName().contains("'")) {
				String newName1 = champion.getName().replace("'", "").toLowerCase();
				System.out.println("Set: " + champion.getName() + " to " + newName1);
				String newName2 = newName1.substring(0, 1).toUpperCase() + newName1.substring(1);
				System.out.println("Set: " + newName1 + " to " + newName2);
				champion.setName(newName2);
			}

			// Handle Kog'Maw
			if (champion.getName().equals("Kogmaw")) {
				champion.setName("KogMaw");
				System.out.println("Handled KogMaw exception");
			}

		}

		championListRepository.save(newChampionList);
		return newChampionList;
	}

	// Get user data by username
	@GetMapping(value = "/user/{username}")
	@CrossOrigin(origins = "http://localhost:3000")
	public User getUserByUsername(@PathVariable String username) {
		String uri = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + username + "?api_key=" + apiKey;
		RestTemplate restTemplate = new RestTemplate();
		try {
			User user = restTemplate.getForObject(uri, User.class);
			return user;
		} catch (Exception e) {
			System.out.println("User not found");
			return new User();
		}
	}

	// Create a new challenge
	@GetMapping(value = "/newchallenge/{username}")
	@CrossOrigin(origins = "http://localhost:3000")
	public Challenge createChallenge(@PathVariable String username) {
		User user = getUserByUsername(username);
		ChampionList championList = getChampions();

		// Check that generated challenge_id is unique
		while (true) {
			Challenge challenge = new Challenge(user, championList);
			if (challengeRepository.existsById(challenge.getChallenge_id()) == false) {
				challengeRepository.save(challenge);
				return challenge;
			} else {
				continue;
			}
		}

	}

	// Refreshes given challenge and adds wins and losses to the champions
	// set match count right
	@GetMapping(value = "/refreshchallenge/{challenge_id}")
	@CrossOrigin(origins = "http://localhost:3000")
	public Challenge refreshChallenge(@PathVariable String challenge_id) {
		Challenge challenge = challengeRepository.getById(challenge_id);	
		String uri = "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/" + challenge.getUser_puuid() + "/ids?startTime=" + challenge.getLastRefresh() + "&queue=450&start=0&count=25&api_key=" + apiKey;
		System.out.println(challenge.getLastRefresh());
		System.out.println(uri);
		ChampionList championList = challenge.getChampionlist();

		// Create a HashMap for easier data access
		HashMap<String, Champion> mapChampions = new HashMap<String, Champion>();
		for (Champion champion : championList.getList()) {
			mapChampions.put(champion.getName(), champion);
		}

		// Handle wins and losses
		ArrayList<Champion> championArray = championList.getList();
		RestTemplate restTemplate = new RestTemplate();
		List<String> matchIdList = restTemplate.getForObject(uri, List.class);
		List<MatchData> gameData = getGameData(challenge.getUsername(), matchIdList);
		Champion currentChampion;

		for (MatchData entry : gameData) {
			currentChampion = mapChampions.get(entry.getChampion());
			currentChampion.handleMatch(entry, challenge);

		}
		championList.setList(championArray);
		challenge.setLastRefresh((long) Instant.now().getEpochSecond());
		System.out.println(challenge.getLastRefresh());
		championListRepository.save(championList);
		challengeRepository.save(challenge);
		return challenge;
	}

	// Returns a list of MatchData objects
	public List<MatchData> getGameData(String user, List<String> matchList) {
		String uri;
		RestTemplate restTemplate = new RestTemplate();
		List<MatchData> response = new ArrayList<MatchData>();

		for (String entry : matchList) {
			uri = "https://europe.api.riotgames.com/lol/match/v5/matches/" + entry + "?api_key=" + apiKey;
			Match gameData = restTemplate.getForObject(uri, Match.class);
			gameData.setUser(user);
			for (MatchData match : gameData.getList()) {
				if (match.getUsername().toLowerCase().equals(user.toLowerCase())) {
					response.add(match);
				}
			}

		}

		return response;
	}

}
