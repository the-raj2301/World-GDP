package com.raj.worldgdp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String countryDetail(@PathVariable(name = "code") String code, Model model)
			throws JsonMappingException, JsonProcessingException {
		Country country = countryDao.getCountryDetail(code);
		City capital = null;
		if (country.getCapital() == null) {
			capital = new City();
			capital.setName("Not Found");
		} else {
			capital = cityDao.getCityDetails(country.getCapital().getId());
		}

		List<City> cities = cityDao.getCities(code);

		List<CountryLanguage> countryLanguage = languageDao.getCountryLanguage(code);

		List<CountryGDP> gdp = worldBankApiClient.getGDP(code);

		if (gdp == null) {
			gdp = new ArrayList<CountryGDP>();
		} else {
			for (CountryGDP g : gdp) {
				if (g.getValue() != null) {
					g.setValue(g.getValue() / 1_000_000_000.0);
				}
			}
		}

		model.addAttribute("gdpValue", gdp);
		model.addAttribute("languages", countryLanguage);
		model.addAttribute("cities", cities);
		model.addAttribute("country", country);
		model.addAttribute("capital", capital);
		model.addAttribute("title", "Country : " + country.getName());
		return "country";
	}

	@GetMapping("/countries/{code}/cities")
	public String loadMoreCities(@PathVariable(name = "code") String code, Model model,
			@RequestParam(name = "pageNo", defaultValue = "2") Integer pageNo) {

		System.out.println("Loading more cities");
		List<City> cities = cityDao.getCities(code, pageNo);
		model.addAttribute("cities", cities);
		model.addAttribute("hasMore", cities.size() == 20);

		return "fragments/city-list :: cityItems";
	}

	@GetMapping("countries/{code}/form")
	public String editCountry(@PathVariable(name = "code") String code, Model model) {
		City capital = null;
		Country country = countryDao.getCountryDetail(code);
		if (country.getCapital() == null) {
			capital = new City();
		} else {
			capital = cityDao.getCityDetails(country.getCapital().getId());
		}
		
		List<String> continents = lookupDao.getContinents();
		List<String> regions = lookupDao.getRegions();
		model.addAttribute("continents", continents);
		model.addAttribute("regions", regions);
		model.addAttribute("capital", capital);
		model.addAttribute("country", country);
		return "country-form";
	}

	@PostMapping("countries/{countryCode}")
	public String editCountry(@PathVariable(name = "countryCode") String countryCode, Country country,
			RedirectAttributes ra) {
		try {
			countryDao.editCountryDetail(countryCode, country);

			ra.addFlashAttribute("toastMessage", "Country updated successfully!");
			ra.addFlashAttribute("toastType", "bg-success");
		} catch (Exception e) {
			ra.addFlashAttribute("toastMessage", "Failed to update country.");
			ra.addFlashAttribute("toastType", "bg-danger");
		}
		return "redirect:/countries/" + countryCode;
	}

}
