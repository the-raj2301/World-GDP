package com.raj.worldgdp.external;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raj.worldgdp.model.CountryGDP;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorldBankApiClient {
	String GDP_URL = "https://api.worldbank.org/v2/countries/%s/indicators/NY.GDP.MKTP.CD?"
			+ "format=json&date=2015:2024";

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper mapper = new ObjectMapper();

	public List<CountryGDP> getGDP(String countryCode) {
		
		List<CountryGDP> data = new ArrayList<>();

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(String.format(GDP_URL, countryCode),
					String.class);

			JsonNode tree = mapper.readTree(response.getBody()).get(1);

			for (JsonNode node : tree) {

				CountryGDP countryGDP = new CountryGDP();

				if (!node.get("value").isNull()) {
					countryGDP.setValue(node.get("value").asDouble());
				}

				countryGDP.setYear(Short.valueOf(node.get("date").asText()));
				data.add(countryGDP);
			}

		} catch (Exception e) {
			log.error("Something went wrong! " + e);
		}

		return data;
	}
}
