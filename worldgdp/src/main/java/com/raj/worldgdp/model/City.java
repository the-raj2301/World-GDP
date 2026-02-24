package com.raj.worldgdp.model;

import lombok.Data;

@Data
public class City{
	private Long id;
	private String name;
	private String countryCode;
	private Country country;
	private String district;
	private Long population;
}