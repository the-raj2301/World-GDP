package com.raj.worldgdp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raj.worldgdp.dao.CountryDao;
import com.raj.worldgdp.external.WorldBankApiClient;
import com.raj.worldgdp.model.Country;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/countries")
public class CountryApiController {

	@Autowired
	CountryDao countryDao;

	@Autowired
	WorldBankApiClient worldBankApiClient;

	// http://localhost:8080/worldgdp/countries
	@GetMapping
	public ResponseEntity<?> getCountries(@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "continent", required = false) String continent,
			@RequestParam(name = "region", required = false) String region,
			@RequestParam(name = "pageNo", required = false) Integer pageNo) {

		try {
			Map<String, Object> params = new HashMap<>();
			params.put("search", search);
			params.put("continent", continent);
			params.put("region", region);

			if (pageNo != null) {
				params.put("pageNo", pageNo.toString());
			}

			List<Country> countries = countryDao.getCountries(params);

			Map<String, Object> response = new HashMap<>();

			response.put("list", countries);
			response.put("count", countryDao.getCountriesCount(params));

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			System.out.println("Something went wrong while getting countries : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong while getting countries...");
		}

	}

	@GetMapping("/{countryCode}")
	public ResponseEntity<Country> getCountryByCountryCode(@PathVariable("countryCode") String countryCode) {

		Country country = countryDao.getCountryDetail(countryCode);

		if (country == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(country);
	}
	
	@GetMapping("{countryCode}/gdp")
	public ResponseEntity<?> getGdp(@PathVariable("countryCode") String countryCode) {
			
		try {
			return ResponseEntity.ok(worldBankApiClient.getGDP(countryCode));
		} catch (Exception e) {
			System.out.println("Fitching gdp failed");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fetching gdp failed");
		}
	}

	@PostMapping("/{countryCode}")
	public ResponseEntity<?> editCountry(@PathVariable String countryCode, @RequestBody Country country) {
		try {
			countryDao.editCountryDetail(countryCode, country);
			Country countryDetail = countryDao.getCountryDetail(countryCode);

			return ResponseEntity.ok(countryDetail);
		} catch (Exception e) {
			log.error("Internal Server Error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
		}
	}

}
