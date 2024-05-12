package com.ruppyrup.javapoet.maker.factories;

import com.ruppyrup.javapoet.app.SchemaField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ruppyrup.javapoet.app.FieldType.BOOLEAN;
import static com.ruppyrup.javapoet.app.FieldType.BOOLEAN_ARRAY;
import static com.ruppyrup.javapoet.app.FieldType.INTEGER;
import static com.ruppyrup.javapoet.app.FieldType.INTEGER_ARRAY;
import static com.ruppyrup.javapoet.app.FieldType.NUMBER;
import static com.ruppyrup.javapoet.app.FieldType.NUMBER_ARRAY;
import static com.ruppyrup.javapoet.app.FieldType.OBJECT;
import static com.ruppyrup.javapoet.app.FieldType.STRING;
import static com.ruppyrup.javapoet.app.FieldType.STRING_ARRAY;

public class LombokFieldSpecFactory {

    public static FieldSpec.Builder creatFieldSpec(SchemaField<?> schemaField) {
        if (schemaField.clazz().getName().equals(STRING.typeIdentifier)) {
            return FieldSpec.builder(schemaField.clazz(), schemaField.name())
                    .addAnnotation(lombok.Builder.Default.class)
                    .initializer("$S", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(INTEGER.typeIdentifier)) {
            return FieldSpec.builder(schemaField.clazz(), schemaField.name())
                    .addAnnotation(lombok.Builder.Default.class)
                    .initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(NUMBER.typeIdentifier)) {
            return FieldSpec.builder(schemaField.clazz(), schemaField.name())
                    .addAnnotation(lombok.Builder.Default.class)
                    .initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(BOOLEAN.typeIdentifier)) {
            return FieldSpec.builder(schemaField.clazz(), schemaField.name())
                    .addAnnotation(lombok.Builder.Default.class)
                    .initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(STRING_ARRAY.typeIdentifier)) {
            return setUpStringList(schemaField);
        } else if (schemaField.clazz().getName().equals(INTEGER_ARRAY.typeIdentifier)) {
            return setUpList(schemaField, Integer.class);
        } else if (schemaField.clazz().getName().equals(NUMBER_ARRAY.typeIdentifier)) {
            return setUpList(schemaField, Number.class);
        } else if (schemaField.clazz().getName().equals(BOOLEAN_ARRAY.typeIdentifier)) {
            return setUpList(schemaField, Boolean.class);
        } else if (schemaField.clazz().getName().equals(OBJECT.typeIdentifier)) {
            String name = StringUtils.capitalize(schemaField.name());
            TypeName childTypeName = ClassName.get("", name);
            return FieldSpec.builder(childTypeName, schemaField.name())
                    .addAnnotation(lombok.Builder.Default.class)
                    .initializer("$L", "null");
        } else {
            throw new IncompatibleClassChangeError("Type found = " + schemaField.clazz());
        }
    }

    private static FieldSpec.Builder setUpStringList(SchemaField<?> schemaField) {
        try {
            var builder = FieldSpec.builder(ParameterizedTypeName.get(List.class, String.class), schemaField.name());
            Field f = schemaField.getClass().getDeclaredField("initialValue");
            f.setAccessible(true);
            String[] initialValue = (String[]) f.get(schemaField);
            String literal = "\"" + String.join("\",\"", initialValue) + "\"";
            CodeBlock block = CodeBlock.builder().add("$1T.asList($2L)", Arrays.class, literal).build();
            builder.addAnnotation(lombok.Builder.Default.class);
            builder.initializer(block);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> FieldSpec.Builder setUpList(SchemaField<?> schemaField, Class<T> type) {
        try {
            var builder = FieldSpec.builder(ParameterizedTypeName.get(List.class, type), schemaField.name());
            Field f = schemaField.getClass().getDeclaredField("initialValue");
            f.setAccessible(true);
            T[] initialValue = (T[]) f.get(schemaField);
            StringBuilder sb = new StringBuilder();
            if (initialValue != null) {
                Arrays.stream(initialValue)
                        .filter(Objects::nonNull)
                        .forEach(i -> sb.append(i).append(","));
                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            }

            System.out.println("To sb for array = " + sb);
            CodeBlock block = CodeBlock.builder().add("$1T.asList($2L)", Arrays.class, sb.toString()).build();
            builder.addAnnotation(lombok.Builder.Default.class);
            builder.initializer(block);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
