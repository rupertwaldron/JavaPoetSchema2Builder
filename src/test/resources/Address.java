package com.ruppyrup.javapoet.generated;

public class Address {
  public String streetName = "Rances Lane";

  public Integer houseNumber = 63;

  public String name = null;



  private Address(AddressBuilder addressBuilder) {
    this.streetName = addressBuilder.streetName;
    this.houseNumber = addressBuilder.houseNumber;
    this.name = addressBuilder.name;

  }

  public static AddressBuilder builder() {
    return new AddressBuilder();
  }

  public static class AddressBuilder {
    public String streetName = "Rances Lane";

    public Integer houseNumber = 63;

    public String name = null;


    public Address build() {
      return new Address(this);
    }

    public AddressBuilder withStreetName(String streetName) {
      this.streetName = streetName;
      return this;
    }

    public AddressBuilder withHouseNumber(Integer houseNumber) {
      this.houseNumber = houseNumber;
      return this;
    }

    public AddressBuilder withName(String name) {
      this.name = name;
      return this;
    }


  }
}
