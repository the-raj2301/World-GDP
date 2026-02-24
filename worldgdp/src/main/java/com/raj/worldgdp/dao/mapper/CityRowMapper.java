package com.raj.worldgdp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raj.worldgdp.model.City;

public class CityRowMapper implements RowMapper<City>{

	@Override
	public City mapRow(ResultSet rs, int rowNum) throws SQLException {
		City city = new City();
		city.setId(rs.getLong("id"));
		city.setName(rs.getString("name"));
		city.setCountryCode(rs.getString("country_code"));
		city.setDistrict(rs.getString("district"));
		city.setPopulation(rs.getLong("population"));
		return city;
	}

}
