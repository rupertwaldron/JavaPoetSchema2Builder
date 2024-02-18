package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.generated.Address;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class BuilderMakerTest {

//    @Test
//    void canBuildAddressWithDefaults() {
//        Address address = Address.builder().build();
//        assertThat(address.streetName).isEqualTo("Rances Lane");
//        assertThat(address.houseNumber).isEqualTo(63);
//    }

    @Test
    void useBuilderToMakeBuilder() throws IOException {
        BuilderMaker builderMaker = BuilderMaker.builder()
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName("Address")
                .withField(new SchemaField<>("streetName", String.class, "Rances Lane"))
                .withField(new SchemaField<>("houseNumber", Integer.class, 63))
                .build();
        builderMaker.makeBuilder();
//        Address address = Address.builder().build();
//        assertThat(address.streetName).isEqualTo("Rances Lane");
//        assertThat(address.houseNumber).isEqualTo(63);
    }
}