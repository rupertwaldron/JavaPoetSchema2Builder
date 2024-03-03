package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.demo.CreateClass;
import com.ruppyrup.javapoet.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.makers.ClassMaker;
import com.ruppyrup.javapoet.models.SchemaField;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<SchemaField<?>> postCode = List.of(
                new SchemaField<>("firstPart", String.class, "RG40"),
                new SchemaField<>("secondPart", String.class, "2LG")
        );

        List<SchemaField<?>> countyFields = List.of(
                new SchemaField<>("countyName", String.class, "Berks"),
                new SchemaField<>("postCode", Object.class, postCode)
                );

        ClassGenerationBuilder classGenerationBuilder = ClassGenerationBuilder.builder()
                .withDir("build/generated")
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName("Address")
                .withField(new SchemaField<>("streetName", String.class, "Rances Lane"))
                .withField(new SchemaField<>("name", String.class, null))
                .withField(new SchemaField<>("houseNumber", Integer.class, 63))
                .withField(new SchemaField<>("family", String[].class, new String[]{"Ben", "Sam", "Joe"}))
                .withField(new SchemaField<>("county", Object.class, countyFields))
                .build();

        ClassMaker classMaker = new ClassMaker(classGenerationBuilder);
        classMaker.makeBuilder();

        //demo
        CreateClass.create("build/generated");
    }
}
