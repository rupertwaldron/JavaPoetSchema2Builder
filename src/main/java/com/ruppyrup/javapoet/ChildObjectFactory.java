package com.ruppyrup.javapoet;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ChildObjectFactory {
    public void creatFieldSpec(SchemaField<?> schemaField, FieldSpec.Builder builder) {
        String name = StringUtils.capitalize(schemaField.name());
        builder.initializer("$L.builder().build()", name);

        BuilderMaker.BuilderMakerBuilder builderMakerBuilder = BuilderMaker.builder()
                .withDir("build/generated")
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName(name);

        ((List<SchemaField<?>>) schemaField.initialValue()).forEach(builderMakerBuilder::withField);

        BuilderMaker builderMaker = builderMakerBuilder.build();
        try {
            builderMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
