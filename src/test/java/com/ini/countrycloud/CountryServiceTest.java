package com.ini.countrycloud;

import com.ini.countrycloud.model.CountryDetailsResponse;
import com.ini.countrycloud.model.CountryResponse;
import com.ini.countrycloud.rest.CountryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.ArrayList;
/*
 * Tests the country service using the restcountries.eu web service
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Test
    public void getCountries() {
        StepVerifier.create(countryService.getCountries().log())
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .consumeRecordedWith(result -> {
                    Assertions.assertThat(result)
                            .contains(new CountryResponse("Finland", "FI"),
                                    new CountryResponse("Poland", "PL"),
                                    new CountryResponse("United States of America", "US"));
                })
                .verifyComplete();
    }

    @Test
    public void getCountryByName() {
        getCountryByName(new CountryDetailsResponse("Finland", "FI", "Helsinki", "5491817",  "https://restcountries.eu/data/fin.svg"));
        getCountryByName(new CountryDetailsResponse("India", "IN", "New Delhi", "1295210000",  "https://restcountries.eu/data/ind.svg"));
        getCountryByName(new CountryDetailsResponse("Denmark", "DK", "Copenhagen", "5717014",  "https://restcountries.eu/data/dnk.svg"));
        getCountryByName(new CountryDetailsResponse("Germany", "DE", "Berlin", "81770900",  "https://restcountries.eu/data/deu.svg"));
    }

    public void getCountryByName(CountryDetailsResponse expectedCountry) {
        System.out.println(expectedCountry.name + " -> " + countryService.getCountryByName(expectedCountry.name));
        StepVerifier.create(countryService.getCountryByName(expectedCountry.name).log())
                .expectNext(expectedCountry)
                .verifyComplete();
    }
}
