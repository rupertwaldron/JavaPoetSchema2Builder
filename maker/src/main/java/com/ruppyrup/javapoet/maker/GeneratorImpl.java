package com.ruppyrup.javapoet.maker;

import com.ruppyrup.javapoet.app.IGenerator;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.makers.ClassMaker;

import java.io.IOException;

public class GeneratorImpl implements IGenerator {
    public void generate(PoetNode poetNode) {
        String className = poetNode.getSchemaField().name();
        ClassGenerationBuilder.GenerationBuilder classGenerationBuilder = ClassGenerationBuilder.builder()
                .withDir("integrationtest/build/generated")
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName(className);

        poetNode.getChildren().forEach(child -> {
            classGenerationBuilder.withField(child.getSchemaField());
        });

        ClassMaker classMaker = new ClassMaker(classGenerationBuilder.build());

        try {
            classMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException("Error making class " + e.getMessage());
        }
    }

}

//List<SchemaField<?>> postCode = List.of(
//        new SchemaField<>("firstPart", String.class, "RG40"),
//        new SchemaField<>("secondPart", String.class, "2LG")
//);
//
//List<SchemaField<?>> countyFields = List.of(
//        new SchemaField<>("countyName", String.class, "Berks"),
//        new SchemaField<>("postCode", Object.class, postCode)
//);
//
//ClassGenerationBuilder classGenerationBuilder = ClassGenerationBuilder.builder()
//        .withDir("maker/build/generated")
//        .withPackageName("com.ruppyrup.javapoet.generated")
//        .withClassName("Address")
//        .withField(new SchemaField<>("streetName", String.class, "Rances Lane"))
//        .withField(new SchemaField<>("name", String.class, null))
//        .withField(new SchemaField<>("houseNumber", Integer.class, 63))
//        .withField(new SchemaField<>("family", String[].class, new String[]{"Ben", "Sam", "Joe"}))
//        .withField(new SchemaField<>("county", Object.class, countyFields))
//        .build();
//
//ClassMaker classMaker = new ClassMaker(classGenerationBuilder);
//        classMaker.makeBuilder();
