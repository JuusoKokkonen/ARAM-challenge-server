package com.example.ARAM.web;

import java.util.ArrayList;

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
import com.example.ARAM.domain.ChampionList;
import com.example.ARAM.domain.ChampionListRepository;
import com.example.ARAM.domain.Test;
import com.example.ARAM.domain.TestRepository;
import com.example.ARAM.domain.User;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
	
	@Value("${RIOT_API_KEY}")
    private String apiKey;
	
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private ChallengeRepository challengeRepository;
	
	@Autowired
	private ChampionListRepository championListRepository;
	
	// Return all tests from database as json
	@GetMapping
	@CrossOrigin(origins = "http://localhost:3000")
	private ResponseEntity<?> getAllTests() {
		return ResponseEntity.ok(this.testRepository.findAll());
	}
	
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
	@GetMapping(value = "/challenge/{uid}")
	private ResponseEntity<?> getChallengeById(@PathVariable String uid) {
		return ResponseEntity.ok(this.challengeRepository.findById(uid));
	}
	
	// Create a new test object
	@GetMapping("/newtest/{var}")
	@CrossOrigin(origins = "http://localhost:3000")
	public Test addTest(@PathVariable String var) {
		Test newTest = new Test(var);
		System.out.println(newTest);
		return testRepository.save(newTest);

	}	
	
	// Get Champion list
	@GetMapping(value = "/champions")
	public ChampionList getChampions() {
		String uri = "http://ddragon.leagueoflegends.com/cdn/13.7.1/data/en_US/champion.json";
		RestTemplate restTemplate = new RestTemplate();
		ChampionList newChampionList = restTemplate.getForObject(uri, ChampionList.class);
		championListRepository.save(newChampionList);
//		String jsonResponse = restTemplate.getForObject(uri, String.class);
//		ArrayList<Champion> champs = new ArrayList<Champion>();
//		newChampionList.setId((long) 2);
//		Challenge challenge = new Challenge("ambaal", newChampionList);
//		challengeRepository.save(challenge);
//		championListRepository.save(newChampionList);
		return newChampionList;
		
	}
	
	// Get list of matches username
	@GetMapping(value = "/user/{username}")
	public User getUserByUsername(@PathVariable String username) {
		String uri = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+ username + "?api_key=" + apiKey;
		RestTemplate restTemplate = new RestTemplate();
		User answer = restTemplate.getForObject(uri, User.class);
		return answer;
	}
	
	// Create a new challenge
	@GetMapping(value = "/newchallenge/{username}")
	public Challenge createChallenge() {
		User user = getUserByUsername("spili");
		ChampionList championList = getChampions();
		Challenge challenge = new Challenge(user, championList);
		challengeRepository.save(challenge);
		return challenge;
	}
	
	@GetMapping(value = "testings") 
	public ChampionList testenings() {
		ChampionList test = getChampions(); 
		System.out.println(test.getId());
		return test;
	}

	

}
