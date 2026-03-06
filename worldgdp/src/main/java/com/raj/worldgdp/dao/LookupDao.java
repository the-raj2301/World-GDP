package com.raj.worldgdp.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LookupDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<String> getContinents() {
		return namedParameterJdbcTemplate.queryForList("SELECT DISTINCT continent FROM country ORDER BY continent ",
				new MapSqlParameterSource(), String.class);
	}
	public List<String> getRegions() {
		return namedParameterJdbcTemplate.queryForList("SELECT DISTINCT region FROM country ORDER BY region ",
				new MapSqlParameterSource(), String.class);
	}
	public List<String> getGovernmentFroms() {
		return namedParameterJdbcTemplate.queryForList("SELECT DISTINCT governementForm FROM country ORDER BY governementForm ",
				new MapSqlParameterSource(), String.class);
	}
	public List<String> getHeadOfStates() {
		return namedParameterJdbcTemplate.queryForList("SELECT DISTINCT headOfState FROM country ORDER BY headOfState ",
				new MapSqlParameterSource(), String.class);
	}
}
