package com.raj.worldgdp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.raj.worldgdp.config.TestDBConfiguration;
import com.raj.worldgdp.model.Country;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(classes = TestDBConfiguration.class)
class CountryDaoTest {

	@Autowired
	CountryDao countryDao;

	@Autowired
	@Qualifier("testTemplate")
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@BeforeEach
	public void setup() {
		countryDao.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
	}

	@Test
	public void testGetCountries() {
		List<Country> countries = countryDao.getCountries(new HashMap<>());
		assertThat(countries).hasSize(20);

		Country c = countries.get(0);
		assertThat(c.getCode()).isEqualTo("ABW");
		assertThat(c.getName()).isEqualTo("Aruba");
		assertThat(c.getContinent()).isEqualTo("North America");
		assertThat(c.getRegion()).isEqualTo("Caribbean");
		assertThat(c.getSurfaceArea()).isEqualTo(193.0);
		assertThat(c.getIndepYear()).isNull();
		assertThat(c.getPopulation()).isEqualTo(103000);
		assertThat(c.getLifeExpectancy()).isCloseTo(78.4, within(0.01));
		assertThat(c.getGnp()).isEqualTo(828.00);
		assertThat(c.getLocalName()).isEqualTo("Aruba");
		assertThat(c.getGovernmentForm()).isEqualTo("Nonmetropolitan Territory of The Netherlands");
		assertThat(c.getHeadOfState()).isEqualTo("Beatrix");
		assertThat(c.getCapital().getId()).isEqualTo(129);
		assertThat(c.getCode2()).isEqualTo("AW");
	}

	@Test
	public void testGetCountriesCount() {
		int count = countryDao.getCountriesCount(Collections.emptyMap());
		assertThat(count).isEqualTo(239);

	}

	@Test
	public void testGetCountriesBySearchingName() {
		Map<String, Object> params = new HashMap<>();
		params.put("search", "India");
		List<Country> c = countryDao.getCountries(params);
//		assertThat(c).hasSize(2);
		assertThat(c).extracting(Country::getCode).contains("IND");
	}

	@Test
	public void testGetCountriesBySearchingContinent() {
		Map<String, Object> params = new HashMap<>();
		params.put("continent", "Asia");
		List<Country> c = countryDao.getCountries(params);
		assertThat(c).hasSize(20);
	}

	@Test
	public void testGetCountryDetail() {
		Country c = countryDao.getCountryDetail("IND");
		assertThat(c.getCode()).isEqualTo("IND");
		assertThat(c.getName()).isEqualTo("India");
		assertThat(c.getContinent()).isEqualTo("Asia");
		assertThat(c.getRegion()).isEqualTo("Southern and Central Asia");
		assertThat(c.getSurfaceArea()).isEqualTo(3287263);
		assertThat(c.getIndepYear()).isEqualByComparingTo((short) 1947);
		assertThat(c.getPopulation()).isEqualTo(1013662000);
		assertThat(c.getLifeExpectancy()).isCloseTo(62.5, within(0.01));
		assertThat(c.getGnp()).isEqualTo(447114.00);
		assertThat(c.getLocalName()).isEqualTo("Bharat/India");
		assertThat(c.getGovernmentForm()).isEqualTo("Federal Republic");
		assertThat(c.getHeadOfState()).isEqualTo("Kocheril Raman Narayanan");
		assertThat(c.getCapital().getId()).isEqualTo(1109);
		assertThat(c.getCode2()).isEqualTo("IN");
	}

	@Transactional
	@Test
	public void testEditCountryDetail() {
		Country c = countryDao.getCountryDetail("IND");
		c.setHeadOfState("Smt. Draupadi Murmu");
		c.setPopulation(1400000000L);
		countryDao.editCountryDetail("IND", c);
		c = countryDao.getCountryDetail("IND");
		assertThat(c.getName()).isEqualTo("India");
		assertThat(c.getHeadOfState()).isEqualTo("Smt. Draupadi Murmu");
		assertThat(c.getPopulation()).isEqualTo(1400000000L);
	}

}
