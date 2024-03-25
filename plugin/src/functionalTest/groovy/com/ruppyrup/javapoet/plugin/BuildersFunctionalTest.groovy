/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package com.ruppyrup.javapoet.plugin

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;
import org.gradle.internal.impldep.org.apache.commons.io.IOUtils

import java.nio.charset.Charset;
import java.nio.file.Path

/**
 * A simple functional test for the 'tasktimer.greeting' plugin.
 */
class BuildersFunctionalTest extends Specification {
    @TempDir
    private File projectDir

    private getBuildFile() {
        new File(projectDir, "build.gradle")
    }

    private getSettingsFile() {
        new File(projectDir, "settings.gradle")
    }

    def "can run task"() {
        given:
        settingsFile << ""
        buildFile << """
plugins {
    id 'java-library'
    id 'com.ruppyrup.javapoet.plugin.poetBuilder'
}


println System.getProperty("user.dir")


poetBuilder {
    schemaDir = '/src/functionalTest/resources'
    outputDir = "${projectDir.getPath()}"
}
"""

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("generateBuilders")
        runner.withProjectDir(projectDir)
        def result = runner.build()

        then:
        def path = projectDir.toPath().resolve("com/ruppyrup/javapoet/schemas/Parent.java")
        String person = IOUtils.toString(path.toUri(), Charset.defaultCharset());
        def expected =  "package com.ruppyrup.javapoet.schemas;\n" +
                "\n" +
                "import java.lang.Integer;\n" +
                "import java.lang.String;\n" +
                "\n" +
                "public class Parent {\n" +
                "  private String name = \"Rupert\";\n" +
                "\n" +
                "  private Integer age = 33;\n" +
                "\n" +
                "  private Address address = Address.builder().build();\n" +
                "\n" +
                "  private String[] hobbies = new String[] {\"fishing\",\"triathlon\"};\n" +
                "\n" +
                "  private Integer[] luckyNumbers = new Integer[] {77,39};\n" +
                "\n" +
                "  private Parent(ParentBuilder parentBuilder) {\n" +
                "    this.name = parentBuilder.name;\n" +
                "    this.age = parentBuilder.age;\n" +
                "    this.address = parentBuilder.address;\n" +
                "    this.hobbies = parentBuilder.hobbies;\n" +
                "    this.luckyNumbers = parentBuilder.luckyNumbers;\n" +
                "  }\n" +
                "\n" +
                "  public static ParentBuilder builder() {\n" +
                "    return new ParentBuilder();\n" +
                "  }\n" +
                "\n" +
                "  public String getName() {\n" +
                "    return this.name;\n" +
                "  }\n" +
                "\n" +
                "  public Integer getAge() {\n" +
                "    return this.age;\n" +
                "  }\n" +
                "\n" +
                "  public Address getAddress() {\n" +
                "    return this.address;\n" +
                "  }\n" +
                "\n" +
                "  public String[] getHobbies() {\n" +
                "    return this.hobbies;\n" +
                "  }\n" +
                "\n" +
                "  public Integer[] getLuckyNumbers() {\n" +
                "    return this.luckyNumbers;\n" +
                "  }\n" +
                "\n" +
                "  public static class ParentBuilder {\n" +
                "    private String name = \"Rupert\";\n" +
                "\n" +
                "    private Integer age = 33;\n" +
                "\n" +
                "    private Address address = Address.builder().build();\n" +
                "\n" +
                "    private String[] hobbies = new String[] {\"fishing\",\"triathlon\"};\n" +
                "\n" +
                "    private Integer[] luckyNumbers = new Integer[] {77,39};\n" +
                "\n" +
                "    public Parent build() {\n" +
                "      return new Parent(this);\n" +
                "    }\n" +
                "\n" +
                "    public ParentBuilder withName(String name) {\n" +
                "      this.name = name;\n" +
                "      return this;\n" +
                "    }\n" +
                "\n" +
                "    public ParentBuilder withAge(Integer age) {\n" +
                "      this.age = age;\n" +
                "      return this;\n" +
                "    }\n" +
                "\n" +
                "    public ParentBuilder withAddress(Address address) {\n" +
                "      this.address = address;\n" +
                "      return this;\n" +
                "    }\n" +
                "\n" +
                "    public ParentBuilder withHobbies(String[] hobbies) {\n" +
                "      this.hobbies = hobbies;\n" +
                "      return this;\n" +
                "    }\n" +
                "\n" +
                "    public ParentBuilder withLuckyNumbers(Integer[] luckyNumbers) {\n" +
                "      this.luckyNumbers = luckyNumbers;\n" +
                "      return this;\n" +
                "    }\n" +
                "  }\n" +
                "}\n"

        person == expected
//        result.output.contains("""
//package com.ruppyrup.javapoet.schemas;
//
//import java.lang.String;
//
//public class PostalCode {
//  private String codePart1 = "RG10";
//
//  private String codePart2 = "1LG";
//
//  private PostalCode(PostalCodeBuilder postalcodeBuilder) {
//    this.codePart1 = postalcodeBuilder.codePart1;
//    this.codePart2 = postalcodeBuilder.codePart2;
//  }
//
//  public static PostalCodeBuilder builder() {
//    return new PostalCodeBuilder();
//  }
//
//  public String getCodePart1() {
//    return this.codePart1;
//  }
//
//  public String getCodePart2() {
//    return this.codePart2;
//  }
//
//  public static class PostalCodeBuilder {
//    private String codePart1 = "RG10";
//
//    private String codePart2 = "1LG";
//
//    public PostalCode build() {
//      return new PostalCode(this);
//    }
//
//    public PostalCodeBuilder withCodePart1(String codePart1) {
//      this.codePart1 = codePart1;
//      return this;
//    }
//
//    public PostalCodeBuilder withCodePart2(String codePart2) {
//      this.codePart2 = codePart2;
//      return this;
//    }
//  }
//}
//package com.ruppyrup.javapoet.schemas;
//
//import java.lang.String;
//
//public class Address {
//  private String street = "Rances Lane";
//
//  private String city = "Wokingham";
//
//  private String state = "Berkshire";
//
//  private PostalCode postalCode = PostalCode.builder().build();
//
//  private Address(AddressBuilder addressBuilder) {
//    this.street = addressBuilder.street;
//    this.city = addressBuilder.city;
//    this.state = addressBuilder.state;
//    this.postalCode = addressBuilder.postalCode;
//  }
//
//  public static AddressBuilder builder() {
//    return new AddressBuilder();
//  }
//
//  public String getStreet() {
//    return this.street;
//  }
//
//  public String getCity() {
//    return this.city;
//  }
//
//  public String getState() {
//    return this.state;
//  }
//
//  public PostalCode getPostalCode() {
//    return this.postalCode;
//  }
//
//  public static class AddressBuilder {
//    private String street = "Rances Lane";
//
//    private String city = "Wokingham";
//
//    private String state = "Berkshire";
//
//    private PostalCode postalCode = PostalCode.builder().build();
//
//    public Address build() {
//      return new Address(this);
//    }
//
//    public AddressBuilder withStreet(String street) {
//      this.street = street;
//      return this;
//    }
//
//    public AddressBuilder withCity(String city) {
//      this.city = city;
//      return this;
//    }
//
//    public AddressBuilder withState(String state) {
//      this.state = state;
//      return this;
//    }
//
//    public AddressBuilder withPostalCode(PostalCode postalCode) {
//      this.postalCode = postalCode;
//      return this;
//    }
//  }
//}
//package com.ruppyrup.javapoet.schemas;
//
//import java.lang.Integer;
//import java.lang.String;
//
//public class Parent {
//  private String name = "Rupert";
//
//  private Integer age = 33;
//
//  private Address address = Address.builder().build();
//
//  private String[] hobbies = new String[] {"fishing","triathlon"};
//
//  private Integer[] luckyNumbers = new Integer[] {77,39};
//
//  private Parent(ParentBuilder parentBuilder) {
//    this.name = parentBuilder.name;
//    this.age = parentBuilder.age;
//    this.address = parentBuilder.address;
//    this.hobbies = parentBuilder.hobbies;
//    this.luckyNumbers = parentBuilder.luckyNumbers;
//  }
//
//  public static ParentBuilder builder() {
//    return new ParentBuilder();
//  }
//
//  public String getName() {
//    return this.name;
//  }
//
//  public Integer getAge() {
//    return this.age;
//  }
//
//  public Address getAddress() {
//    return this.address;
//  }
//
//  public String[] getHobbies() {
//    return this.hobbies;
//  }
//
//  public Integer[] getLuckyNumbers() {
//    return this.luckyNumbers;
//  }
//
//  public static class ParentBuilder {
//    private String name = "Rupert";
//
//    private Integer age = 33;
//
//    private Address address = Address.builder().build();
//
//    private String[] hobbies = new String[] {"fishing","triathlon"};
//
//    private Integer[] luckyNumbers = new Integer[] {77,39};
//
//    public Parent build() {
//      return new Parent(this);
//    }
//
//    public ParentBuilder withName(String name) {
//      this.name = name;
//      return this;
//    }
//
//    public ParentBuilder withAge(Integer age) {
//      this.age = age;
//      return this;
//    }
//
//    public ParentBuilder withAddress(Address address) {
//      this.address = address;
//      return this;
//    }
//
//    public ParentBuilder withHobbies(String[] hobbies) {
//      this.hobbies = hobbies;
//      return this;
//    }
//
//    public ParentBuilder withLuckyNumbers(Integer[] luckyNumbers) {
//      this.luckyNumbers = luckyNumbers;
//      return this;
//    }
//  }
//}
//""")
    }
}
