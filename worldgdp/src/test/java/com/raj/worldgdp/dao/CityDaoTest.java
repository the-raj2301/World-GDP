package com.raj.worldgdp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.raj.worldgdp.config.TestDBConfiguration;
import com.raj.worldgdp.model.City;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(classes = TestDBConfiguration.class)
@ActiveProfiles("test")
class CityDaoTest {

	@Autowired
	CityDao cityDao;

	@Autowired
	@Qualifier("testTemplate")
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@BeforeEach
	public void setUp() {
		cityDao.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
	}

	@Test
	public void testGetCities() {
		List<City> cities = cityDao.getCities("IND", 1);
		assertThat(cities).hasSize(20);
	}

	@Test
	public void testGetCityDetails() {
		City cityDetails = cityDao.getCityDetails(1024L);
		assertThat(cityDetails.getName()).isEqualTo("Mumbai (Bombay)");
		assertThat(cityDetails.getCountryCode()).isEqualTo("IND");
		assertThat(cityDetails.getDistrict()).isEqualTo("Maharashtra");
		assertThat(cityDetails.getPopulation()).isEqualByComparingTo(10500000L);
	}

	@Test
	public void testAddCity() {

		String countryCode = "IND";

		City c = new City();
		c.setName("City Name");
		c.setDistrict("City District");
		c.setPopulation(1000L);

		Long cityId = cityDao.addCity(countryCode, c);
		assertThat(cityId).isNotNull();

		City cityDetails = cityDao.getCityDetails(cityId);
		assertThat(cityDetails.getId()).isNotNull();
		assertThat(cityDetails.getName()).isEqualTo(c.getName());
		assertThat(cityDetails.getDistrict()).isEqualTo(c.getDistrict());
		assertThat(cityDetails.getPopulation()).isEqualTo(c.getPopulation());
	}

	@Test
	public void testDeleteCity() {
		Long cityId = addCity();
		cityDao.deleteCity(cityId);
//		assertThat(cityDao.getCityDetails(cityId)).isNull();
		assertThatThrownBy(() -> cityDao.getCityDetails(cityId)).isInstanceOf(EmptyResultDataAccessException.class);
	}

	private Long addCity() {
		City c = new City();
		c.setName("City");
		c.setDistrict("District");
		c.setPopulation(101010L);
		Long cityId = cityDao.addCity("IND", c);
		return cityId;
	}

}
