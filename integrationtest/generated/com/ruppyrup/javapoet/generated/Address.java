package com.ruppyrup.javapoet.generated;

import java.lang.String;

public class Address {
  private String street = "Rances Lane";

  private String city = "Wokingham";

  private String state = "Berkshire";

  private PostalCode postalCode = PostalCode.builder().build();

  private Address(AddressBuilder addressBuilder) {
    this.street = addressBuilder.street;
    this.city = addressBuilder.city;
    this.state = addressBuilder.state;
    this.postalCode = addressBuilder.postalCode;
  }

  public static AddressBuilder builder() {
    return new AddressBuilder();
  }

  public String getStreet() {
    return this.street;
  }

  public String getCity() {
    return this.city;
  }

  public String getState() {
    return this.state;
  }

  public PostalCode getPostalCode() {
    return this.postalCode;
  }

  public static class AddressBuilder {
    private String street = "Rances Lane";

    private String city = "Wokingham";

    private String state = "Berkshire";

    private PostalCode postalCode = PostalCode.builder().build();

    public Address build() {
      return new Address(this);
    }

    public AddressBuilder withStreet(String street) {
      this.street = street;
      return this;
    }

    public AddressBuilder withCity(String city) {
      this.city = city;
      return this;
    }

    public AddressBuilder withState(String state) {
      this.state = state;
      return this;
    }

    public AddressBuilder withPostalCode(PostalCode postalCode) {
      this.postalCode = postalCode;
      return this;
    }
  }
}
