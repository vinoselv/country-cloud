package com.ini.countrycloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class CountryCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CountryCloudApplication.class, args);
    }
}
