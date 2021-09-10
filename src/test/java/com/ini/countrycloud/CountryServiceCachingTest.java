package com.ini.countrycloud;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;

import com.google.common.cache.CacheBuilder;
import com.ini.countrycloud.model.CountryDetailsResponse;
import com.ini.countrycloud.model.CountryResponse;
import com.ini.countrycloud.rest.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * Tests the country service caching functionality
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class CountryServiceCachingTest {

    private static final String GET_COUNTRIES_URL = "/countries";
    private static final String GET_COUNTRY_DETAILS_URL = "countries/{country}";

    @Autowired
    private CountryService countryService;

    private CountryService countryServiceMock;

    @EnableCaching
    @Configuration
    public static class CachingTestConfig {

        @Bean
        public CountryService countryServiceMock() {
            return mock(CountryService.class);
        }

        @Bean
        public CacheManager cacheManager() {
            ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {

                @Override
                protected Cache createConcurrentMapCache(final String name) {
                    return new ConcurrentMapCache(name, CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS)
                            .maximumSize(100).build().asMap(), false);
                }
            };

            cacheManager.setCacheNames(Collections.singletonList("countries"));
            return cacheManager;
        }

    }

    @BeforeEach
    void setUp() {
        countryServiceMock = AopTestUtils.getTargetObject(countryService);

        reset(countryServiceMock);
    }

    @Test
    public void getCountries() throws InterruptedException {
        List<CountryResponse> countryList = Arrays.asList(new CountryResponse("Finland", "FI"),
                new CountryResponse("Poland", "PL"),
                new CountryResponse("United States of America", "US"));

        Flux<CountryResponse> countryFlux = Flux.fromIterable(countryList);
        Mockito.when(countryServiceMock.getCountries()).thenReturn(countryFlux);

        getCountriesFromService();
        Mockito.verify(countryServiceMock, times(1)).getCountries();

        // call country service to get the countries
        getCountriesFromService();
        getCountriesFromService();

        // verify that the mock is not called again due to caching
        Mockito.verify(countryServiceMock, times(1)).getCountries();

        // wait for the cache to expire
        Thread.sleep(6000);

        // call country service to get the countries and the mock will be called again as the cache is expired
        getCountriesFromService();
        Mockito.verify(countryServiceMock, times(2)).getCountries();
    }

    private void getCountriesFromService() {
        countryService.getCountries();
    }

    @Test
    public void getCountryByName() throws InterruptedException {
        Mockito.when(countryServiceMock.getCountryByName("Finland")).thenReturn(
                Mono.just(new CountryDetailsResponse("Finland", "FI", "Helsinki", "5491817",  "https://restcountries.eu/data/fin.svg")));

        String countryName = "Finland";

        getCountryByNameFromService(countryName);
        Mockito.verify(countryServiceMock, times(1)).getCountryByName(countryName);

        // call country service to get the country
        getCountryByNameFromService(countryName);
        getCountryByNameFromService(countryName);

        // verify that the mock is not called again due to caching
        Mockito.verify(countryServiceMock, times(1)).getCountryByName(countryName);

        // wait for the cache to expire
        Thread.sleep(6000);

        // call country service to get the country and the mock will be called again as the cache is expired
        getCountryByNameFromService(countryName);
        Mockito.verify(countryServiceMock, times(2)).getCountryByName(countryName);
    }

    private void getCountryByNameFromService(String name) {
        countryService.getCountryByName(name);
    }
}
