package com.raj.worldgdp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.raj.worldgdp.dao.mapper.CountryLanguageRowMapper;
import com.raj.worldgdp.model.CountryLanguage;

import lombok.Setter;

@Service
@Setter
public class CountryLanguageDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

//	private static final Integer PAGE_SIZE = 20;

	public List<CountryLanguage> getCountryLanguage(String countryCode){
		Map<String, Object> params = new HashMap<>();
		params.put("code", countryCode);
//		Integer offset = (pageNo - 1) * PAGE_SIZE;
//		params.put("offset", offset);
//		params.put("size", PAGE_SIZE);
		return namedParameterJdbcTemplate.query(
				"SELECT * from countrylanguage "
				+ " WHERE countrycode = :code "
				+ " ORDER BY percentage DESC "
//				+ " LIMIT :size OFFSET :offset "
				, params, new CountryLanguageRowMapper());
	}

	public boolean languageExist(String countryCode, String language) {
		Map<String, Object> params = new HashMap<>();
		params.put("code", countryCode);
		params.put("language", language);
		Integer rowCount = namedParameterJdbcTemplate.queryForObject(
				"SELECT COUNT(*) from countrylanguage "
				+ " WHERE countrycode = :code "
				+ " AND language = :language "
				, params, Integer.class);
		return rowCount > 0;
	}

	public void addLanguage(String countryCode, CountryLanguage countryLanguage) {
		Map<String, Object> params = new HashMap<>();
		params.put("code", countryCode);
		params.put("language", countryLanguage.getLanguage());
		params.put("isOfficial", countryLanguage.getIsOfficial());
		params.put("percentage", countryLanguage.getPercentage());

		namedParameterJdbcTemplate.update(
				"INSERT INTO countrylanguage (countrycode, language, isofficial, percentage) "
				+ " VALUES ( :code, :language, :isOfficial, :percentage) "
				, params);
	}

	public void deleteLanguage(String countryCode, String language) {
		Map<String, Object> params = new HashMap<>();
		params.put("code", countryCode);
		params.put("language", language);
		namedParameterJdbcTemplate.update(
				"DELETE from countrylanguage "
				+ " WHERE countrycode = :code "
				+ " AND language = :language "
				, params);
	}

}
