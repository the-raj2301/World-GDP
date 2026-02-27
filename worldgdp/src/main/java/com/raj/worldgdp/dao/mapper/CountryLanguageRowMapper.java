package com.raj.worldgdp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raj.worldgdp.model.CountryLanguage;

public class CountryLanguageRowMapper implements RowMapper<CountryLanguage>{

	@Override
	public CountryLanguage mapRow(ResultSet rs, int rowNum) throws SQLException {
		CountryLanguage countryLanguage = new CountryLanguage();
		countryLanguage.setCountryCode(rs.getString("countrycode"));
		countryLanguage.setLanguage(rs.getString("language"));
		countryLanguage.setIsOfficial(rs.getString("isofficial"));
		countryLanguage.setPercentage(rs.getDouble("percentage"));
		return countryLanguage;
	}

}
