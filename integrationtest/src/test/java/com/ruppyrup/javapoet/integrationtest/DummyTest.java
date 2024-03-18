package com.ruppyrup.javapoet.integrationtest;

import com.ruppyrup.javapoet.generated.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DummyTest {

    @Test
    void sanityCheck() {
        Person person = Person.builder().build();
        System.out.println(person);
    }

}