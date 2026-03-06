package com.raj.worldgdp.model;

import lombok.Data;

@Data
public class CountryLanguage {
	private Country country;
	private String countryCode;
	private String language;
	private String isOfficial;
	private Double percentage;

}
