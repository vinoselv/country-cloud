# Country Cloud ![country-cloud workflow](https://github.com/vinoselv/country-cloud/actions/workflows/gradle.yml/badge.svg) ![Coverage](.github/badges/jacoco.svg)
Country cloud is a RESTful web service provides country details, developed using the Spring Boot framework that 
consumes / retrieves the country data from the [Rest countries](https://restcountries.eu/) project.

## What you will need
- [Java 8](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)
- [maven >= 3.0](http://maven.apache.org)
- [maven wrapper](https://github.com/takari/maven-wrapper)

## APIs
Get all the country names and the country code.
```bash
GET /countries
[
   {
      "name":"Afghanistan",
      "country_code":"AF"
   },
   {
      "name":"Åland Islands",
      "country_code":"AX"
   },
   {
      "name":"Albania",
      "country_code":"AL"
   },
   {
      "name":"Algeria",
      "country_code":"DZ"
   },
   {
      "name":"American Samoa",
      "country_code":"AS"
   }....
]
```

Get specific country details by name.
```bash
GET /countries/Finland
{
   "name":"Finland",
   "country_code":"FI",
   "capital":"Helsinki",
   "population":"5491817",
   "flag_file_url":"https://restcountries.eu/data/fin.svg"
}
```