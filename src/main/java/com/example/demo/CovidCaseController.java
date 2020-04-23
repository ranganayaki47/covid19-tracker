package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/covidcases")
public class CovidCaseController {
	
	private ResponseEntity responseEntity;
	//private CovidCaseService covidCaseService;
	
	@GetMapping("/getcovidcases")
	public ResponseEntity getCasesStatewise() {
		System.err.println("-----GET COVID CASES API--------");
		return responseEntity;
		
	}
	
	

}
