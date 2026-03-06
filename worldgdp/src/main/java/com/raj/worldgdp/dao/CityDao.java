package com.raj.worldgdp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.raj.worldgdp.dao.mapper.CityRowMapper;
import com.raj.worldgdp.model.City;

import lombok.Setter;

@Service
@Setter
public class CityDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static final Integer PAGE_SIZE = 5;

	public List<City> getCities(String coutryCode,  Integer pageNo){
		Map<String, Object> params = new HashMap<>();
		params.put("code", coutryCode);

		Integer offset = (pageNo != null) ? (pageNo - 1) * PAGE_SIZE : 1;
		params.put("offset", offset);
		params.put("size", PAGE_SIZE);

		return namedParameterJdbcTemplate.query(
				"SELECT "
				+ " id, name, countryCode country_code, district, population from city "
				+ " WHERE countryCode = :code "
				+ " ORDER BY POPULATION DESC "
				+ ((pageNo != null) ? " LIMIT :size OFFSET :offset " : "")
				, params, new CityRowMapper());
	}

	public List<City> getCities(String country){
		return getCities(country, null);
	}

	public City getCityDetails(Long id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return namedParameterJdbcTemplate.queryForObject(
				" SELECT id, name, countryCode AS country_code, district, population "
				+ " FROM city WHERE id = :id"
				, params, new CityRowMapper());
	}

	public Long addCity(String countryCode, City city) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", city.getName());
		params.put("country_code", countryCode);
		params.put("district", city.getDistrict());
		params.put("population", city.getPopulation());

		SqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);

		KeyHolder keyHolder = new GeneratedKeyHolder();

		namedParameterJdbcTemplate.update(
				" INSERT INTO city (name, countryCode, district, population)"
				+ " VALUES (:name, :country_code, :district, :population) "
				, sqlParameterSource, keyHolder, new String[] {"id"});

		return keyHolder.getKey().longValue();
	}

	public int deleteCity(Long id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);

		return namedParameterJdbcTemplate.update("DELETE FROM CITY WHERE id = :id", params);
	}

}
