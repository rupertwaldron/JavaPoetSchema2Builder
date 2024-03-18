package com.ruppyrup.javapoet.schema2;

import java.lang.Integer;
import java.lang.String;

public class Child {
  private String name = "Sam";

  private Integer age = 15;

  private Address address = Address.builder().build();

  private String[] hobbies = new String[] {"xbox","more xbox"};

  private Integer[] luckyNumbers = new Integer[] {3,29};

  private Child(ChildBuilder childBuilder) {
    this.name = childBuilder.name;
    this.age = childBuilder.age;
    this.address = childBuilder.address;
    this.hobbies = childBuilder.hobbies;
    this.luckyNumbers = childBuilder.luckyNumbers;
  }

  public static ChildBuilder builder() {
    return new ChildBuilder();
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

  public static class ChildBuilder {
    private String name = "Sam";

    private Integer age = 15;

    private Address address = Address.builder().build();

    private String[] hobbies = new String[] {"xbox","more xbox"};

    private Integer[] luckyNumbers = new Integer[] {3,29};

    public Child build() {
      return new Child(this);
    }

    public ChildBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public ChildBuilder withAge(Integer age) {
      this.age = age;
      return this;
    }

    public ChildBuilder withAddress(Address address) {
      this.address = address;
      return this;
    }

    public ChildBuilder withHobbies(String[] hobbies) {
      this.hobbies = hobbies;
      return this;
    }

    public ChildBuilder withLuckyNumbers(Integer[] luckyNumbers) {
      this.luckyNumbers = luckyNumbers;
      return this;
    }
  }
}
