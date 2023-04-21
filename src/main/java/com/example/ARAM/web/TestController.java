package com.example.ARAM.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.ARAM.domain.Champion;
import com.example.ARAM.domain.ChampionList;
import com.example.ARAM.domain.Test;
import com.example.ARAM.domain.TestRepository;
import com.example.ARAM.domain.User;
import com.example.ARAM.domain.UserTest;

@RestController
@RequestMapping("/dbtest")
public class TestController {
	
	@Value("${RIOT_API_KEY}")
    private String apiKey;
	
	@Autowired
	private TestRepository testRepository;
	
	// Return all tests from database as json
	@GetMapping
	@CrossOrigin(origins = "http://localhost:3000")
	private ResponseEntity<?> getAllTests() {
		return ResponseEntity.ok(this.testRepository.findAll());
	}
	
	// Create a new test object
	@GetMapping("/newtest/{var}")
	@CrossOrigin(origins = "http://localhost:3000")
	public Test addTest(@PathVariable String var) {
		Test newTest = new Test(var);
		System.out.println(newTest);
		return testRepository.save(newTest);

	}	
	
	// Test Riot api
	@GetMapping(value = "/riotapi/{userName}")
	private String riotApiCall(@PathVariable String userName) {
		String uri = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+ userName + "?api_key=" + apiKey;
		try {
			RestTemplate restTemplate = new RestTemplate();
			UserTest user = restTemplate.getForObject(uri, UserTest.class);
			System.out.println(user);
			return user.id;
			
		} catch(Exception e) {
			System.out.println(e);
			return "Username not found";
		}
	}
	
	// Get Champion list
	@GetMapping(value = "/champions")
	private ChampionList getChampions() {
		String uri = "http://ddragon.leagueoflegends.com/cdn/13.7.1/data/en_US/champion.json";
		RestTemplate restTemplate = new RestTemplate();
;		ChampionList newChampionList = restTemplate.getForObject(uri, ChampionList.class);
//		ResponseEntity<Champion> response = restTemplate.exchange(uri, HttpMethod.GET, null, Champion.class);
//		System.out.println(response);
		return newChampionList;
		
	}
	
	
	
	
	
	
	

}
