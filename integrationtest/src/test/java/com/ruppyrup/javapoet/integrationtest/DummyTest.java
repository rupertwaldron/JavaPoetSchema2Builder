package com.ruppyrup.javapoet.integrationtest;

import com.ruppyrup.javapoet.generated.Person;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class DummyTest {

    @Test
    void sanityCheck() throws IOException {
//        FileUtils.cleanDirectory(new File("/Users/rupertwaldron/Documents/JavaDevelopment/Gradle/Json2Pojo/JavaPoet/integrationtest/generated"));
        Dummy.run(System.getProperty("user.dir") + "/src/test/resources/schema1/nested_schema1.json");
        Person person = Person.builder().build();
        assertThat(person.getName()).isEqualTo("Rupert");
        assertThat(person.getAddress().getCity()).isEqualTo("Wokingham");
        assertThat(person.getAddress().getPostalCode().getCodePart1()).isEqualTo("RG10");
        assertThat(person.getHobbies()).contains("fishing", "triathlon");
        assertThat(person.getLuckyNumbers()).contains(39, 77);

    }

}