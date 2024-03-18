package com.ruppyrup.javapoet.integrationtest;

import com.ruppyrup.javapoet.generated.Person;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class DummyTest {

    @Test
    void sanityCheck() {
        Dummy.run(System.getProperty("user.dir") + "/src/test/resources/nested_schema2.json");
        Person person = Person.builder().build();
        assertThat(person.getName()).isEqualTo("Rupert");
        assertThat(person.getAddress().getCity()).isEqualTo("Wokingham");
        assertThat(person.getAddress().getPostalCode().getCodePart1()).isEqualTo("RG10");
        assertThat(person.getHobbies()).contains("fishing", "triathlon");
        assertThat(person.getLuckyNumbers()).contains(39, 77);

    }

}