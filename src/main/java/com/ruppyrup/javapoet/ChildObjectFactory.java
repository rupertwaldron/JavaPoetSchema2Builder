package com.ruppyrup.javapoet;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;

public class ChildObjectFactory {
    public FieldSpec.Builder creatFieldSpec(SchemaObject schemaObject) {
        String name = StringUtils.capitalize(schemaObject.className());
        var builder = FieldSpec.builder(Object.class, schemaObject.className());
        builder.initializer("$L.builder().build()", name);

        BuilderMaker.BuilderMakerBuilder builderMakerBuilder = BuilderMaker.builder()
                .withDir("build/generated")
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName(name);

        schemaObject.fields().forEach(builderMakerBuilder::withField);

        BuilderMaker builderMaker = builderMakerBuilder.build();
        try {
            builderMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder;
    }
}
