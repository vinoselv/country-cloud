package com.ini.countrycloud.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Objects;
import java.util.StringJoiner;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy .class)
public class CountryDetailsResponse {
    public String name;
    public String countryCode;
    public String capital;
    public String population;
    public String flagFileUrl;

    public CountryDetailsResponse(String name, String countryCode, String capital, String population, String flagFileUrl) {
        this.name = name;
        this.countryCode = countryCode;
        this.capital = capital;
        this.population = population;
        this.flagFileUrl = flagFileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryDetailsResponse that = (CountryDetailsResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(countryCode, that.countryCode) && Objects.equals(capital, that.capital) && Objects.equals(population, that.population) && Objects.equals(flagFileUrl, that.flagFileUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, countryCode, capital, population, flagFileUrl);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CountryDetailsResponse.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("countryCode='" + countryCode + "'")
                .add("capital='" + capital + "'")
                .add("population='" + population + "'")
                .add("flagFileUrl='" + flagFileUrl + "'")
                .toString();
    }
}
