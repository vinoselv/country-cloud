package com.ini.countrycloud.rest;

import com.ini.countrycloud.model.Country;
import com.ini.countrycloud.model.CountryDetails;
import com.ini.countrycloud.model.CountryDetailsResponse;
import com.ini.countrycloud.model.CountryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    private static final String GET_COUNTRIES_URL = "https://restcountries.eu/rest/v2/all";
    private static final String GET_COUNTRY_DETAILS_URL = "https://restcountries.eu/rest/v2/name/{country}?fullText=true";

    @Cacheable(value = "countries")
    public Flux<CountryResponse> getCountries() {
        return WebClient.builder()
                .filter(errorHandlingFilter())
                .build()
                .get()
                .uri(GET_COUNTRIES_URL)
                .retrieve()
                .bodyToFlux(Country.class)
                .map(this::convertToResponse);
    }

    @Cacheable(value = "countries", key = "#country")
    public Mono<CountryDetailsResponse> getCountryByName(String country) {
        return WebClient.builder()
                .filter(errorHandlingFilter())
                .build()
                .get()
                .uri(GET_COUNTRY_DETAILS_URL.replace("{country}", country))
                .retrieve()
                .bodyToFlux(CountryDetails.class)
                .map(this::convertToResponse).next();
    }

    private CountryResponse convertToResponse(Country c) {
        return new CountryResponse(c.name, c.alpha2Code);
    }

    private CountryDetailsResponse convertToResponse(CountryDetails cd) {
        return new CountryDetailsResponse(cd.name, cd.alpha2Code, cd.capital, cd.population, cd.flag);
    }

    public static ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if(clientResponse.statusCode().is5xxServerError() || clientResponse.statusCode().is4xxClientError()) {
                logger.warn("Error on calling the restcountries.eu : " + clientResponse.statusCode());
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), errorBody)));
            }else {
                return Mono.just(clientResponse);
            }
        });
    }

}
