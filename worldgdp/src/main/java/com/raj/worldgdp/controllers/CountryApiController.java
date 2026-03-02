package com.raj.worldgdp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raj.worldgdp.dao.CountryDao;
import com.raj.worldgdp.external.WorldBankApiClient;
import com.raj.worldgdp.model.Country;

@RestController
public class CountryApiController {

	@Autowired
	CountryDao countryDao;

	@Autowired
	WorldBankApiClient worldBankApiClient;

	@GetMapping("/countries")
	public ResponseEntity<?> getCountries(@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "continent", required = false) String continent, @RequestParam(name = "region", required = false) String region,
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
}
