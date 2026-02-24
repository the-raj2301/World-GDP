package com.raj.worldgdp.model;

import lombok.Data;

@Data
public class CountryLanguage {
	private Country country;
	private String countryCode;
	private String Language;
	private String isOfficial;
	private Double percentage;
	
}
