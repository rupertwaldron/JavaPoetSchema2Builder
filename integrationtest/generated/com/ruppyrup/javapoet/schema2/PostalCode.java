package com.ruppyrup.javapoet.schema2;

import java.lang.String;

public class PostalCode {
  private String codePart1 = "B94";

  private String codePart2 = "2PX";

  private PostalCode(PostalCodeBuilder postalcodeBuilder) {
    this.codePart1 = postalcodeBuilder.codePart1;
    this.codePart2 = postalcodeBuilder.codePart2;
  }

  public static PostalCodeBuilder builder() {
    return new PostalCodeBuilder();
  }

  public String getCodePart1() {
    return this.codePart1;
  }

  public String getCodePart2() {
    return this.codePart2;
  }

  public static class PostalCodeBuilder {
    private String codePart1 = "B94";

    private String codePart2 = "2PX";

    public PostalCode build() {
      return new PostalCode(this);
    }

    public PostalCodeBuilder withCodePart1(String codePart1) {
      this.codePart1 = codePart1;
      return this;
    }

    public PostalCodeBuilder withCodePart2(String codePart2) {
      this.codePart2 = codePart2;
      return this;
    }
  }
}
