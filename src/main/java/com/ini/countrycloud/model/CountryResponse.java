package com.ini.countrycloud.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Objects;
import java.util.StringJoiner;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy .class)
public class CountryResponse {
    public String name;
    public String countryCode;

    public CountryResponse(String name, String countryCode) {
        this.name = name;
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryResponse that = (CountryResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(countryCode, that.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, countryCode);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CountryResponse.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("countryCode='" + countryCode + "'")
                .toString();
    }
}
