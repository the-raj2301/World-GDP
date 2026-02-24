package com.raj.worldgdp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.raj.worldgdp.dao.mapper.CountryRowMapper;
import com.raj.worldgdp.model.Country;

import lombok.Setter;

@Service
@Setter
public class CountryDao {
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final String SELECT_CLAUSE = " SELECT "
			+ " c.code, "
			+ " c.name, "
			+ " c.continent, "
			+ " c.region, "
			+ " c.surfaceArea surface_area, "
			+ " c.indepYear, "
			+ " c.population, "
			+ " c.lifeExpectancy life_expectancy, "
			+ " c.gnp, "
			+ " c.localName local_name, "
			+ " c.governmentForm government_form, "
			+ " c.headOfState head_of_state, "
			+ " c.code2, "
			+ " c.capital, "
			+ " cy.name capital_name "
			+ " FROM Country c "
			+ " LEFT JOIN City cy ON cy.id = c.capital ";
	
	
	private static final String SEARCH_WHERE_CLAUSE = " AND (LOWER(c.name) " 
			+ " LIKE CONCAT('%', LOWER(:search), '%') )";
	
	private static final String CONTINENT_WHERE_CLAUSE = " AND c.continent = :continent ";

	private static final String REGION_WHERE_CLAUSE = " AND c.region = :region ";
	
	private static final String PAGINATION_CLAUSE = " ORDER BY c.code "
			+ " LIMIT :size OFFSET :offset ";
	
	private static final Integer PAGE_SIZE = 20;
	
//	Get Countries By Searching With Keywords
	
	public List<Country> getCountries(Map<String, Object> params){
		
		int pageNo = 1;
		
		if (params.containsKey("pageNo")) {
			pageNo = Integer.parseInt(params.get("pageNo").toString());
		}
		
		Integer offset = (pageNo - 1) * PAGE_SIZE;
		
		params.put("offset", offset);
		params.put("size", PAGE_SIZE);
		
		return namedParameterJdbcTemplate.query(SELECT_CLAUSE
				+ " WHERE 1 = 1 "
				+ (StringUtils.hasText((String)params.get("size")) ? SEARCH_WHERE_CLAUSE : "")
				+ (StringUtils.hasText((String)params.get("region")) ? REGION_WHERE_CLAUSE : "")
				+ (StringUtils.hasText((String)params.get("continent")) ? CONTINENT_WHERE_CLAUSE : "")
				+ PAGINATION_CLAUSE, 
				params, 
				new CountryRowMapper());
		
		
	}
	 
	public int getCountriesCount(Map<String, Object> params) {
		
		return namedParameterJdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM Country c "
				+ " WHERE 1 =1 "
				+ (StringUtils.hasText((String)params.get("continent")) ? CONTINENT_WHERE_CLAUSE : "")
				+ (StringUtils.hasText((String)params.get("region")) ? REGION_WHERE_CLAUSE : ""), 
				params, Integer.class);
	}
	
	public Country getCountryDetail(String code) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		return namedParameterJdbcTemplate.queryForObject(
				SELECT_CLAUSE 
				+ " WHERE c.code = :code ", 
				params, new CountryRowMapper());
	}
	
	public void editCountryDetail(String code, Country country) {
		namedParameterJdbcTemplate.update(
				"UPDATE country SET "
				+ " name = :name, "
				+ " continent = :continent "
				+ " region = :region "
				+ " surfaceArea = :surfaceArea "
				+ " indepYear = :indepYear "
				+ " population = :population "
				+ " lifeExpectancy = :lifeExpectancy "
				+ " localName = :localName "
				+ " governmentForm = :governmentForm "
				+ " headOfState = :headOfState "
				+ " capital = :capital "
				+ " WHERE c.code = :code ", 
				getCountryAsMap(code, country));
	}

	private Map<String, Object> getCountryAsMap(String code, Country country) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", country.getName());
		params.put("continent", country.getContinent());
		params.put("region", country.getRegion());
		params.put("surfaceArea", country.getSurfaceArea());
		params.put("indepYear", country.getIndepYear());
		params.put("population", country.getPopulation());
		params.put("lifeExpectancy", country.getLifeExpectancy());
		params.put("localName", country.getLocalName());
		params.put("governmentForm", country.getGovernmentForm());
		params.put("headOfState", country.getHeadOfState());
		params.put("capital", country.getCapital());
		params.put("code", code);
		return params;
	}
	
}
