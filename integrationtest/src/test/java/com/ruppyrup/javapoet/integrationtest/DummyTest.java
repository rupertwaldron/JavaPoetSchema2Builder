package com.ruppyrup.javapoet.integrationtest;


import com.ruppyrup.javapoet.schema1.Parent;
import com.ruppyrup.javapoet.schema2.Child;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class DummyTest {

    @Test
    void sanityCheck() throws IOException {
//        FileUtils.cleanDirectory(new File("/Users/rupertwaldron/Documents/JavaDevelopment/Gradle/Json2Pojo/JavaPoet/integrationtest/generated"));
        Dummy.run(System.getProperty("user.dir") + "/src/test/resources");
        Parent person = Parent.builder().build();
        assertThat(person.getName()).isEqualTo("Rupert");
        assertThat(person.getAddress().getCity()).isEqualTo("Wokingham");
        assertThat(person.getAddress().getPostalCode().getCodePart1()).isEqualTo("RG10");
        assertThat(person.getHobbies()).contains("fishing", "triathlon");
        assertThat(person.getLuckyNumbers()).contains(39, 77);

        Child child = Child.builder().build();
        assertThat(child.getName()).isEqualTo("Sam");
        assertThat(child.getAge()).isEqualTo(15);
        assertThat(child.getAddress().getCity()).isEqualTo("Solihull");
        assertThat(child.getAddress().getPostalCode().getCodePart1()).isEqualTo("B94");
        assertThat(child.getHobbies()).contains("xbox", "more xbox");
        assertThat(child.getLuckyNumbers()).contains(3, 29);
    }

}