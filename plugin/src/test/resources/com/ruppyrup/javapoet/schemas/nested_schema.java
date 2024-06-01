package com.ruppyrup.javapoet.schemas;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Number;
import java.lang.String;
import java.util.List;

public class nested_schema {
  private String name = "Rupert";

  private Boolean male = true;

  private Integer age = 33;

  private Address address = Address.builder().build();

  private String[] hobbies = new String[] {"fishing","triathlon"};

  private Integer[] luckyNumbers = new Integer[] {77,39};

  private Integer[] emptyInts = new Integer[] {};

  private Boolean[] coinToss = new Boolean[] {true,false,true};

  private Number yearsInHouse = 16.9;

  private Number[] meterReadings = new Number[] {16.9,120.9,200.64};

  private List<Book> books = List.of(Book.builder().build());

  private nested_schema(nested_schemaBuilder nested_schemaBuilder) {
    this.name = nested_schemaBuilder.name;
    this.male = nested_schemaBuilder.male;
    this.age = nested_schemaBuilder.age;
    this.address = nested_schemaBuilder.address;
    this.hobbies = nested_schemaBuilder.hobbies;
    this.luckyNumbers = nested_schemaBuilder.luckyNumbers;
    this.emptyInts = nested_schemaBuilder.emptyInts;
    this.coinToss = nested_schemaBuilder.coinToss;
    this.yearsInHouse = nested_schemaBuilder.yearsInHouse;
    this.meterReadings = nested_schemaBuilder.meterReadings;
    this.books = nested_schemaBuilder.books;
  }

  public static nested_schemaBuilder builder() {
    return new nested_schemaBuilder();
  }

  public String getName() {
    return this.name;
  }

  public Boolean getMale() {
    return this.male;
  }

  public Integer getAge() {
    return this.age;
  }

  public Address getAddress() {
    return this.address;
  }

  public String[] getHobbies() {
    return this.hobbies;
  }

  public Integer[] getLuckyNumbers() {
    return this.luckyNumbers;
  }

  public Integer[] getEmptyInts() {
    return this.emptyInts;
  }

  public Boolean[] getCoinToss() {
    return this.coinToss;
  }

  public Number getYearsInHouse() {
    return this.yearsInHouse;
  }

  public Number[] getMeterReadings() {
    return this.meterReadings;
  }

  public List<Book> getBooks() {
    return this.books;
  }

  public static class nested_schemaBuilder {
    private String name = "Rupert";

    private Boolean male = true;

    private Integer age = 33;

    private Address address = Address.builder().build();

    private String[] hobbies = new String[] {"fishing","triathlon"};

    private Integer[] luckyNumbers = new Integer[] {77,39};

    private Integer[] emptyInts = new Integer[] {};

    private Boolean[] coinToss = new Boolean[] {true,false,true};

    private Number yearsInHouse = 16.9;

    private Number[] meterReadings = new Number[] {16.9,120.9,200.64};

    private List<Book> books = List.of(Book.builder().build());

    public nested_schema build() {
      return new nested_schema(this);
    }

    public nested_schemaBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public nested_schemaBuilder withMale(Boolean male) {
      this.male = male;
      return this;
    }

    public nested_schemaBuilder withAge(Integer age) {
      this.age = age;
      return this;
    }

    public nested_schemaBuilder withAddress(Address address) {
      this.address = address;
      return this;
    }

    public nested_schemaBuilder withHobbies(String[] hobbies) {
      this.hobbies = hobbies;
      return this;
    }

    public nested_schemaBuilder withLuckyNumbers(Integer[] luckyNumbers) {
      this.luckyNumbers = luckyNumbers;
      return this;
    }

    public nested_schemaBuilder withEmptyInts(Integer[] emptyInts) {
      this.emptyInts = emptyInts;
      return this;
    }

    public nested_schemaBuilder withCoinToss(Boolean[] coinToss) {
      this.coinToss = coinToss;
      return this;
    }

    public nested_schemaBuilder withYearsInHouse(Number yearsInHouse) {
      this.yearsInHouse = yearsInHouse;
      return this;
    }

    public nested_schemaBuilder withMeterReadings(Number[] meterReadings) {
      this.meterReadings = meterReadings;
      return this;
    }

    public nested_schemaBuilder withBooks(List<Book> books) {
      this.books = books;
      return this;
    }
  }
}
