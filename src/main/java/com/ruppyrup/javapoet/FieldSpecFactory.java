package com.ruppyrup.javapoet;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class FieldSpecFactory {

    public FieldSpec.Builder creatFieldSpec(SchemaField<?> schemaField) {
        var builder = FieldSpec.builder(schemaField.clazz(), schemaField.name());
        if (schemaField.clazz().getName().equals("java.lang.String")) {
            builder.initializer("$S", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals("java.lang.Integer")) {
            builder.initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals("[Ljava.lang.String;")) {
            try {
                Field f = schemaField.getClass().getDeclaredField("initialValue");
                f.setAccessible(true);
                String[] initialValue = (String[]) f.get(schemaField);
                String literal = "{\"" + String.join("\",\"", initialValue) + "\"}";
                ArrayTypeName stringArray = ArrayTypeName.of(String.class);
                CodeBlock block = CodeBlock.builder().add("new $1T $2L", stringArray, literal).build();
                builder.initializer(block);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (schemaField.clazz().getName().equals("java.lang.Object")) {
            BuilderMaker builderMaker = BuilderMaker.builder()
                    .withDir("build/generated")
                    .withPackageName("com.ruppyrup.javapoet.generated")
                    .withClassName((String) schemaField.initialValue())
                    .build();
            try {
                builderMaker.makeBuilder();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            try {
//                builder = FieldSpec.builder(Class.forName(capitalize(schemaField.name())), schemaField.name());
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
            builder.initializer("$L.builder().build()", schemaField.initialValue());

        } else {
            throw new IncompatibleClassChangeError("Type found = " + schemaField.clazz());
        }
        return builder;
    }
}
