package com.ruppyrup.javapoet.schemas;

import java.lang.String;

public class Country {
  private String countryName = "England";

  private Country(CountryBuilder countryBuilder) {
    this.countryName = countryBuilder.countryName;
  }

  public static CountryBuilder builder() {
    return new CountryBuilder();
  }

  public String getCountryName() {
    return this.countryName;
  }

  public static class CountryBuilder {
    private String countryName = "England";

    public Country build() {
      return new Country(this);
    }

    public CountryBuilder withCountryName(String countryName) {
      this.countryName = countryName;
      return this;
    }
  }
}
