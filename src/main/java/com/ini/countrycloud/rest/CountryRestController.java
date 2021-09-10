package com.ini.countrycloud.rest;

import com.ini.countrycloud.model.CountryDetailsResponse;
import com.ini.countrycloud.model.CountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CountryRestController {

    @Autowired
    CountryService cs;

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    @ResponseBody
    public Flux<CountryResponse> getCountries() {
        return cs.getCountries();
    }

    @RequestMapping(value = "/countries/{country}", method = RequestMethod.GET)
    @ResponseBody
    public Mono<CountryDetailsResponse> getCountryByName(@PathVariable String country) {
        return cs.getCountryByName(country);
    }
}