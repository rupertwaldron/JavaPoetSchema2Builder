package com.ruppyrup.javapoet;

import com.ruppyrup.javapoent.generated.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateBuilderTest {

    @Test
    void canBuildAddressWithDefaults() {
        Address address = Address.builder().build();
        Assertions.assertNotNull(address);
    }

}