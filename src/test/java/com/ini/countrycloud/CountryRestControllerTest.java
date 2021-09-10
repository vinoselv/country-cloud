package com.ini.countrycloud;

import com.ini.countrycloud.model.CountryResponse;
import com.ini.countrycloud.model.CountryDetailsResponse;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

/*
 * Tests the REST APIs exposed by the application
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryRestControllerTest {

    private static final String GET_COUNTRIES_URL = "/countries";
    private static final String GET_COUNTRY_DETAILS_URL = "countries/{country}";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getCountries() {
        webTestClient
                .get().uri(GET_COUNTRIES_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CountryResponse.class)
                .value(cr -> cr, CoreMatchers.notNullValue())
                .value(cr -> cr, CoreMatchers.hasItems(
                        new CountryResponse("Finland", "FI"),
                        new CountryResponse("Poland", "PL"),
                        new CountryResponse("United States of America", "US")));
    }

    @Test
    public void getCountryByName() {
        getCountryByName(new CountryDetailsResponse("Finland", "FI", "Helsinki", "5491817",  "https://restcountries.eu/data/fin.svg"));
        getCountryByName(new CountryDetailsResponse("India", "IN", "New Delhi", "1295210000",  "https://restcountries.eu/data/ind.svg"));
        getCountryByName(new CountryDetailsResponse("Denmark", "DK", "Copenhagen", "5717014",  "https://restcountries.eu/data/dnk.svg"));
        getCountryByName(new CountryDetailsResponse("Germany", "DE", "Berlin", "81770900",  "https://restcountries.eu/data/deu.svg"));
    }

    public void getCountryByName(CountryDetailsResponse expectedCountry) {
        webTestClient
                .get().uri(GET_COUNTRY_DETAILS_URL.replace("{country}", expectedCountry.name))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CountryDetailsResponse.class)
                .value(cdr -> cdr, CoreMatchers.notNullValue())
                .value(List::size, CoreMatchers.equalTo(1))
                .value(cdr -> cdr.get(0).name, CoreMatchers.equalTo(expectedCountry.name))
                .value(cdr -> cdr.get(0).countryCode, CoreMatchers.equalTo(expectedCountry.countryCode))
                .value(cdr -> cdr.get(0).capital, CoreMatchers.equalTo(expectedCountry.capital))
                // population value cannot be static
                .value(cdr -> Long.parseLong(cdr.get(0).population) >= Long.parseLong(expectedCountry.population),  CoreMatchers.equalTo(true))
                .value(cdr -> cdr.get(0).flagFileUrl, CoreMatchers.equalTo(expectedCountry.flagFileUrl));
    }

    @Test
    public void getCountryByInvalidName() {
        webTestClient
                .get().uri(GET_COUNTRY_DETAILS_URL.replace("{country}", "invalid"))
                .exchange()
                .expectStatus().isNotFound();
    }
}
