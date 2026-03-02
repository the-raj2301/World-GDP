package com.raj.worldgdp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raj.worldgdp.model.City;
import com.raj.worldgdp.model.Country;

public class CountryRowMapper implements RowMapper<Country>{

	@Override
	public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
		Country country = new Country();
		country.setCode(rs.getString("code"));
		country.setName(rs.getString("name"));
		country.setContinent(rs.getString("continent"));
		country.setRegion(rs.getString("region"));
		country.setSurfaceArea(rs.getDouble("surface_area"));
		country.setIndepYear(rs.getObject("indep_year", Short.class));
		country.setPopulation(rs.getLong("population"));
		country.setLifeExpectancy(rs.getDouble("life_expectancy"));
		country.setGnp(rs.getDouble("gnp"));
		country.setLocalName(rs.getString("local_name"));
		country.setGovernmentForm(rs.getString("government_form"));
		country.setHeadOfState(rs.getString("head_of_state"));
		long capitalId = rs.getLong("capital");
		if(!rs.wasNull()) {
			City city = new City();
			city.setId(capitalId);
			city.setName(rs.getString("capital_name"));
			country.setCapital(city);
		}

		country.setCode2(rs.getString("code2"));

		return country;
	}



}
