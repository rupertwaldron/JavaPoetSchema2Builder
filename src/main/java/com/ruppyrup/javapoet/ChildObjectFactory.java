package com.ruppyrup.javapoet;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;

public class ChildObjectFactory {
    public FieldSpec.Builder creatFieldSpec(String childClassName) {
        String name = StringUtils.capitalize(childClassName);
        var builder = FieldSpec.builder(Object.class, childClassName);
        builder.initializer("$L.builder().build()", name);

        BuilderMaker builderMaker = BuilderMaker.builder()
                .withDir("build/generated")
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName(name)
                .build();
        try {
            builderMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder;
    }
}
