package com.ruppyrup.javapoet;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<SchemaField<?>> countyFields = List.of(
                new SchemaField<>("countyName", String.class, "Berks"),
                new SchemaField<>("postCode", String.class, "RG40 2LG")
                );


        BuilderMaker builderMaker = BuilderMaker.builder()
                .withDir("build/generated")
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName("Address")
                .withField(new SchemaField<>("streetName", String.class, "Rances Lane"))
                .withField(new SchemaField<>("name", String.class, null))
                .withField(new SchemaField<>("houseNumber", Integer.class, 63))
                .withField(new SchemaField<>("family", String[].class, new String[]{"Ben", "Sam", "Joe"}))
                .withField(new SchemaField<>("county", Object.class, countyFields))
                .build();
        builderMaker.makeBuilder();
        CreateClass.create("build/generated");
    }
}
