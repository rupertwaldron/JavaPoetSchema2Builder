package com.ruppyrup.javapoet;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

public class ChildObjectMaker {
    public void makeChild(SchemaField<?> schemaField) {
        BuilderMaker.BuilderMakerBuilder builderMakerBuilder = BuilderMaker.builder()
                .withDir("build/generated")
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName(StringUtils.capitalize(schemaField.name()));

        ((List<SchemaField<?>>) schemaField.initialValue()).forEach(builderMakerBuilder::withField);

        BuilderMaker builderMaker = builderMakerBuilder.build();
        try {
            builderMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
