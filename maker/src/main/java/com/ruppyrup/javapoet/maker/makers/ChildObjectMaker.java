package com.ruppyrup.javapoet.maker.makers;

import com.ruppyrup.javapoet.app.SchemaField;
import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.factories.ClassMakerFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

public class ChildObjectMaker {
    public void makeChild(SchemaField<?> schemaField, String dir, String packageName, String classMakerType) {
        ClassGenerationBuilder.GenerationBuilder generationBuilder = ClassGenerationBuilder.builder()
                .withDir(dir)
                .withPackageName(packageName)
                .withClassName(StringUtils.capitalize(schemaField.name()));

        if (schemaField.initialValue() == null) return;

        ((List<SchemaField<?>>) schemaField.initialValue()).forEach(generationBuilder::withField);

        ClassMaker classMaker = ClassMakerFactory.getClassMakerOfType(classMakerType, generationBuilder.build());
        try {
            classMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}