package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.generated.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateClassTest {

    @Test
    void canFindClass(){
        Person person = new Person();
        Assertions.assertNotNull(person);
    }
}
