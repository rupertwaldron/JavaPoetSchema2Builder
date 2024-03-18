package com.ruppyrup.javapoet.schema1;

import java.lang.Integer;
import java.lang.String;

public class Parent {
  private String name = "Rupert";

  private Integer age = 33;

  private Address address = Address.builder().build();

  private String[] hobbies = new String[] {"fishing","triathlon"};

  private Integer[] luckyNumbers = new Integer[] {77,39};

  private Parent(ParentBuilder parentBuilder) {
    this.name = parentBuilder.name;
    this.age = parentBuilder.age;
    this.address = parentBuilder.address;
    this.hobbies = parentBuilder.hobbies;
    this.luckyNumbers = parentBuilder.luckyNumbers;
  }

  public static ParentBuilder builder() {
    return new ParentBuilder();
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

  public static class ParentBuilder {
    private String name = "Rupert";

    private Integer age = 33;

    private Address address = Address.builder().build();

    private String[] hobbies = new String[] {"fishing","triathlon"};

    private Integer[] luckyNumbers = new Integer[] {77,39};

    public Parent build() {
      return new Parent(this);
    }

    public ParentBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public ParentBuilder withAge(Integer age) {
      this.age = age;
      return this;
    }

    public ParentBuilder withAddress(Address address) {
      this.address = address;
      return this;
    }

    public ParentBuilder withHobbies(String[] hobbies) {
      this.hobbies = hobbies;
      return this;
    }

    public ParentBuilder withLuckyNumbers(Integer[] luckyNumbers) {
      this.luckyNumbers = luckyNumbers;
      return this;
    }
  }
}
