package com.raj.worldgdp.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.raj.worldgdp.dao.CityDao;
import com.raj.worldgdp.model.City;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/city")
public class CityApiController {

	@Autowired
	CityDao cityDao = new CityDao();

	// URL: http://localhost:8080/worldgdp/city/IND
	@GetMapping("/{countryCode}")
	public ResponseEntity<?> getCities(@PathVariable(name = "countryCode", required = false) String countryCode,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer PageNo) {
		try {
			System.out.println("City scanning");
			List<City> cities = cityDao.getCities(countryCode, PageNo);

			System.out.println("City found : " + cities);
			if (cities != null) {
				return ResponseEntity.ok(cities);
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong...");
		}
	}

	@PostMapping("/{countryCode}/cities")
	public ResponseEntity<?> addCity(@PathVariable(name = "countryCode") String countryCode, @RequestBody City city) {
		try {
			Long addedCityId = cityDao.addCity(countryCode, city);
			City cityDetails = cityDao.getCityDetails(addedCityId);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(addedCityId)
					.toUri();
			return ResponseEntity.created(location).body(cityDetails);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went wrong at server.");
		}
	}

	@DeleteMapping("/{cityId}")
	public ResponseEntity<?> deleteCity(@PathVariable(name = "cityId") Long cityId) {
		try {
			
			int deletedCity = cityDao.deleteCity(cityId);
			
			if (deletedCity == 0) {
				return ResponseEntity.notFound().build();
			}
			System.out.println("Deleted");
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			log.error("Error occured with city id: {}", cityId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An Error Occurred While Deleting the city by id: " + cityId);
		}
	}

}
