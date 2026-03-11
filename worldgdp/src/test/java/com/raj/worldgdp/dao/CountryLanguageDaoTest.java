package com.raj.worldgdp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import com.raj.worldgdp.model.CountryLanguage;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(classes = TestDBConfiguration.class)
class CountryLanguageDaoTest {

	@Autowired
	@Qualifier("testTemplate")
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	CountryLanguageDao countryLanguageDao;

	@BeforeEach
	public void setup() {
		countryLanguageDao.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
	}

	@Test
	public void testGetCountryLanguage() {
		List<CountryLanguage> countryLanguage = countryLanguageDao.getCountryLanguage("IND");
		assertThat(countryLanguage).hasSize(10);
	}

	@Test
	public void testAddLanguage() {
		String code = "IND";
		CountryLanguage cl = createLanguage(code);
		countryLanguageDao.addLanguage(code, cl);
		assertThat(countryLanguageDao.languageExist(code, cl.getLanguage())).isTrue();
	}

	@Test
	public void testLanguageExist() {
		String code = "IND";
		CountryLanguage cl = createLanguage(code);
		countryLanguageDao.addLanguage(code, cl);
		assertThat(countryLanguageDao.languageExist(code, cl.getLanguage())).isTrue();
	}

	@Test
	public void testDeleteLanguage() {
		String code = "IND";

		CountryLanguage language = createLanguage(code);
		countryLanguageDao.addLanguage(code, language);

		boolean languageExist = countryLanguageDao.languageExist(code, language.getLanguage());
		assertThat(languageExist).isTrue();

		countryLanguageDao.deleteLanguage(code, language.getLanguage());

		assertThat(countryLanguageDao.languageExist(code, language.getLanguage())).isFalse();
		}

	private CountryLanguage createLanguage(String countryCode) {
		CountryLanguage cl = new CountryLanguage();
		cl.setCountryCode(countryCode);
		cl.setLanguage("Test");
		cl.setIsOfficial("T");
		cl.setPercentage(15.5);
		return cl;
	}

}
