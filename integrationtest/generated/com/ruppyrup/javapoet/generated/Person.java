package com.ruppyrup.javapoet.generated;

import java.lang.Integer;
import java.lang.String;

public class Person {
  private String name = "Rupert";

  private Integer age = 33;

  private Address address = Address.builder().build();

  private String[] hobbies = new String[] {"fishing","triathlon"};

  private Integer[] luckyNumbers = new Integer[] {77,39};

  private Person(PersonBuilder personBuilder) {
    this.name = personBuilder.name;
    this.age = personBuilder.age;
    this.address = personBuilder.address;
    this.hobbies = personBuilder.hobbies;
    this.luckyNumbers = personBuilder.luckyNumbers;
  }

  public static PersonBuilder builder() {
    return new PersonBuilder();
  }

  public String getName() {
    return this.name;
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

  public static class PersonBuilder {
    private String name = "Rupert";

    private Integer age = 33;

    private Address address = Address.builder().build();

    private String[] hobbies = new String[] {"fishing","triathlon"};

    private Integer[] luckyNumbers = new Integer[] {77,39};

    public Person build() {
      return new Person(this);
    }

    public PersonBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public PersonBuilder withAge(Integer age) {
      this.age = age;
      return this;
    }

    public PersonBuilder withAddress(Address address) {
      this.address = address;
      return this;
    }

    public PersonBuilder withHobbies(String[] hobbies) {
      this.hobbies = hobbies;
      return this;
    }

    public PersonBuilder withLuckyNumbers(Integer[] luckyNumbers) {
      this.luckyNumbers = luckyNumbers;
      return this;
    }
  }
}
