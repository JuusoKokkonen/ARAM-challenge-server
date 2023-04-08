package com.example.ARAM.web;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.ARAM.domain.Test;
import com.example.ARAM.domain.TestRepository;

@RestController
@RequestMapping("/dbtest")
public class TestController {
	
	@Autowired
	private TestRepository testRepository;
	
	@GetMapping
	@CrossOrigin(origins = "http://localhost:3000")
	ResponseEntity<?> getAllTests() {
		return ResponseEntity.ok(this.testRepository.findAll());
	}
	
	// create a new test object
	@GetMapping("/newtest/{var}")
	public Test addTest(@PathVariable String var) {
		Test newTest = new Test(var);
		System.out.println(newTest);
		return testRepository.save(newTest);

	}	
	

}
