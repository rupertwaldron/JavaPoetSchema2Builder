package com.ruppyrup.javapoet.maker.makers;

import com.ruppyrup.javapoet.app.FieldType;
import com.ruppyrup.javapoet.app.SchemaField;
import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.factories.ClassMakerFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChildObjectMaker {
    public void makeChild(SchemaField<?> schemaField, String dir, String packageName, String classMakerType) {
        String name = schemaField.name();
        List<SchemaField<?>> initialFields = null;
        if (FieldType.OBJECT_ARRAY.typeIdentifier.equals(schemaField.clazz.getName())) {
            name = StringUtils.chop(name);
            initialFields = (ArrayList<SchemaField<?>>)schemaField.initialValue();
//            Object[] bob = ((Object[]) schemaField.initialValue());
//            initialFields = initialValue.stream()
//                            .map(obj -> (SchemaField<?>) obj)
//                                    .collect(Collectors.toList());
        } else {
            initialFields = (List<SchemaField<?>>) schemaField.initialValue();
        }

        name = StringUtils.capitalize(name);

        ClassGenerationBuilder.GenerationBuilder generationBuilder = ClassGenerationBuilder.builder()
                .withDir(dir)
                .withPackageName(packageName)
                .withClassName(name);

        if (schemaField.initialValue() == null) return;

        initialFields.forEach(generationBuilder::withField);

        ClassMaker classMaker = ClassMakerFactory.getClassMakerOfType(classMakerType, generationBuilder.build());
        try {
            classMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}