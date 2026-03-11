package com.raj.worldgdp.controllers;

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

import com.raj.worldgdp.dao.CountryLanguageDao;
import com.raj.worldgdp.model.CountryLanguage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/language")
public class CountryLanguageApiController {

	@Autowired
	CountryLanguageDao languageDao;

	@GetMapping("/{countryCode}")
	public ResponseEntity<?> getLanguage(@PathVariable(name = "countryCode") String countryCode) {
		try {
			List<CountryLanguage> languages = languageDao.getCountryLanguage(countryCode);
			if (languages.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(languages);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong..");
		}
	}

	@PostMapping("/{countryCode}")
	public ResponseEntity<?> addLanguage(@PathVariable(name = "countryCode") String countryCode,
			@RequestBody CountryLanguage language) {
		try {
			if (languageDao.languageExist(countryCode, language.getLanguage())) {
				return ResponseEntity.badRequest().body("The language " + language.getLanguage() + " already exists!");
			}
			languageDao.addLanguage(countryCode, language);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("Something went wrong : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong..");
		}
	}

	@DeleteMapping("/{countryCode}/{language}")
	public ResponseEntity<?> deleteLanguage(
			@PathVariable(name = "countryCode") String countryCode,
			@PathVariable(name = "language") String language) {
		try {
			if (languageDao.languageExist(countryCode, language)) {
				languageDao.deleteLanguage(countryCode, language);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.badRequest().body("No language found : " + language);
			
		} catch (Exception e) {
			log.error("Something went wrong! ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
