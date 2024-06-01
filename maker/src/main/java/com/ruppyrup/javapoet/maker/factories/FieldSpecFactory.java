package com.ruppyrup.javapoet.maker.factories;

import com.ruppyrup.javapoet.app.SchemaField;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Parameterizable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.ruppyrup.javapoet.app.FieldType.BOOLEAN_ARRAY;
import static com.ruppyrup.javapoet.app.FieldType.INTEGER;
import static com.ruppyrup.javapoet.app.FieldType.INTEGER_ARRAY;
import static com.ruppyrup.javapoet.app.FieldType.NUMBER;
import static com.ruppyrup.javapoet.app.FieldType.NUMBER_ARRAY;
import static com.ruppyrup.javapoet.app.FieldType.OBJECT;
import static com.ruppyrup.javapoet.app.FieldType.OBJECT_ARRAY;
import static com.ruppyrup.javapoet.app.FieldType.STRING;
import static com.ruppyrup.javapoet.app.FieldType.STRING_ARRAY;
import static com.ruppyrup.javapoet.app.FieldType.BOOLEAN;

public class FieldSpecFactory {

    public static FieldSpec.Builder creatFieldSpec(SchemaField<?> schemaField) {
        var builder = FieldSpec.builder(schemaField.clazz(), schemaField.name());
        if (schemaField.clazz().getName().equals(STRING.typeIdentifier)) {
            builder.initializer("$S", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(INTEGER.typeIdentifier)) {
            builder.initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(NUMBER.typeIdentifier)) {
            builder.initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(BOOLEAN.typeIdentifier)) {
            builder.initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(STRING_ARRAY.typeIdentifier)) {
            setUpStringArray(schemaField, builder);
        } else if (schemaField.clazz().getName().equals(INTEGER_ARRAY.typeIdentifier)) {
            setUpArray(schemaField, builder, Integer.class);
        } else if (schemaField.clazz().getName().equals(NUMBER_ARRAY.typeIdentifier)) {
            setUpArray(schemaField, builder, Number.class);
        } else if (schemaField.clazz().getName().equals(BOOLEAN_ARRAY.typeIdentifier)) {
                setUpArray(schemaField, builder, Boolean.class);
        } else if (schemaField.clazz().getName().equals(OBJECT.typeIdentifier)) {
            String name = StringUtils.capitalize(schemaField.name());
            TypeName childTypeName = ClassName.get("", name);
            builder = FieldSpec.builder(childTypeName, schemaField.name());
            builder.initializer("$L.builder().build()", name);
        } else if (schemaField.clazz().getName().equals(OBJECT_ARRAY.typeIdentifier)) {
            String capitalize = StringUtils.capitalize(schemaField.name());
            String name = StringUtils.chop(capitalize);
            TypeName childTypeName = ClassName.get("", name);
            ClassName list = ClassName.get(List.class);
            ParameterizedTypeName listOfItems = ParameterizedTypeName.get(list, childTypeName);
            builder = FieldSpec.builder(listOfItems, schemaField.name());
            setUpObjectArray(schemaField, builder, childTypeName);
        } else {
            throw new IncompatibleClassChangeError("Type found = " + schemaField.clazz());
        }
        return builder;
    }

    private static void setUpStringArray(SchemaField<?> schemaField, FieldSpec.Builder builder) {
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
    }

    private static <T> void setUpArray(SchemaField<?> schemaField, FieldSpec.Builder builder, Class<T> type) {
        try {
            Field f = schemaField.getClass().getDeclaredField("initialValue");
            f.setAccessible(true);
            T[] initialValue = (T[])f.get(schemaField);
            StringBuilder sb = new StringBuilder("{");
            if(initialValue != null) {
                Arrays.stream(initialValue)
                        .filter(Objects::nonNull)
                        .forEach(i -> sb.append(i).append(","));
                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
            sb.append("}");

            ArrayTypeName numberArray = ArrayTypeName.of(type);
            System.out.println("To sb for array = " + sb);
            CodeBlock block = CodeBlock.builder().add("new $1T $2L", numberArray, sb.toString()).build();
            builder.initializer(block);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void setUpObjectArray(SchemaField<?> schemaField, FieldSpec.Builder builder, TypeName type) {
        try {
            Field f = schemaField.getClass().getDeclaredField("initialValue");
            f.setAccessible(true);
//            Object[] initialValue = (Object[])f.get(schemaField);
            List<SchemaField<?>> initialValue = ((ArrayList<SchemaField<?>>)f.get(schemaField));
            StringBuilder sb = new StringBuilder("{");
            if(initialValue != null) {
//                Arrays.stream(initialValue)
                initialValue.stream()
                        .filter(Objects::nonNull)
                        .map(iv -> ((SchemaField<?>) iv).name)
                        .map(name -> StringUtils.capitalize(name) + ".builder().build()")
                        .forEach(i -> sb.append(i).append(","));
                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
            sb.append("}");

            ArrayTypeName numberArray = ArrayTypeName.of(type);
            System.out.println("To sb for array = " + sb);
            CodeBlock block = CodeBlock.builder().add("List.of($T.builder().build())", type).build();
            builder.initializer(block);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
