package com.ruppyrup.javapoet.maker.makers;

import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.models.SchemaField;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

public class ChildObjectMaker {
    public void makeChild(SchemaField<?> schemaField) {
        ClassGenerationBuilder.GenerationBuilder generationBuilder = ClassGenerationBuilder.builder()
                .withDir("build/generated")
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName(StringUtils.capitalize(schemaField.name()));

        ((List<SchemaField<?>>) schemaField.initialValue()).forEach(generationBuilder::withField);

        ClassGenerationBuilder builder = generationBuilder.build();
        try {
            ClassMaker classMaker = new ClassMaker(builder);
            classMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
