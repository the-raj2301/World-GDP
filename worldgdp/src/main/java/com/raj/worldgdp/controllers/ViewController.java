package com.raj.worldgdp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.raj.worldgdp.dao.CityDao;
import com.raj.worldgdp.dao.CountryDao;
import com.raj.worldgdp.dao.CountryLanguageDao;
import com.raj.worldgdp.dao.LookupDao;
import com.raj.worldgdp.external.WorldBankApiClient;
import com.raj.worldgdp.model.City;
import com.raj.worldgdp.model.Country;
import com.raj.worldgdp.model.CountryGDP;
import com.raj.worldgdp.model.CountryLanguage;

@Controller
@RequestMapping("/")
public class ViewController {

	@Autowired
	LookupDao lookupDao;

	@Autowired
	CountryDao countryDao;

	@Autowired
	CityDao cityDao;
	
	@Autowired
	CountryLanguageDao languageDao;
	
	@Autowired
	WorldBankApiClient worldBankApiClient;

	@GetMapping({ "/countries", "/" })
	public String counties(Model model, @RequestParam Map<String, Object> params) {
		Integer pageNo = 1;
		if (params.containsKey("pageNo")) {
			pageNo = Integer.parseInt(params.get("pageNo").toString());
		}

		model.addAttribute("continents", lookupDao.getContinents());
		model.addAttribute("regions", lookupDao.getRegions());
		model.addAttribute("countries", countryDao.getCountries(params));
		model.addAttribute("count", countryDao.getCountriesCount(params));
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("title", "Earth : Countries");
		return "countries";
	}

	@GetMapping("/countries/{code}")
	public String countryDetail(@PathVariable(name = "code") String code, Model model) throws JsonMappingException, JsonProcessingException {
		Country country = countryDao.getCountryDetail(code);
		City capital = null;
		if (country.getCapital() == null) {
			capital = new City();
			capital.setName("Not Found");
		} else {
			capital = cityDao.getCityDetails(country.getCapital().getId());
		}
		
		List<City> cities = cityDao.getCities(code, 1);
		
		List<CountryLanguage> countryLanguage = languageDao.getCountryLanguage(code, 1);
		
		List<CountryGDP> gdp = worldBankApiClient.getGDP(code);
		
		model.addAttribute("gdp", gdp);
		model.addAttribute("languages", countryLanguage);
		model.addAttribute("cities", cities);
		model.addAttribute("country", country);
		model.addAttribute("capital", capital);
		model.addAttribute("title", "Country : " + country.getName());
		return "country";
	}

	@GetMapping("countries/{code}/form")
	public String editCountry(@PathVariable(name = "code") String code, Model model) {
		return "country-form";
	}
}
