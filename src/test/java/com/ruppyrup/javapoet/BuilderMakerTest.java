package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.generated.Address;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

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
                .withField(new SchemaField<>("name", String.class, null))
                .withField(new SchemaField<>("family", String[].class, new String[]{"Ben", "Sam", "Joe"}))
                .build();
        builderMaker.makeBuilder();
        Address address = Address.builder()
                .withName("Rupert")
                .withHouseNumber(7)
                .build();
        assertThat(address.streetName).isEqualTo("Rances Lane");
        assertThat(address.name).isEqualTo("Rupert");
        assertThat(address.houseNumber).isEqualTo(7);
    }
}