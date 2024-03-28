package com.ruppyrup.javapoet.maker.factories;

import com.ruppyrup.javapoet.app.SchemaField;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

import static com.ruppyrup.javapoet.maker.factories.FieldType.*;

public class FieldSpecFactory {

    public FieldSpec.Builder creatFieldSpec(SchemaField<?> schemaField) {
        var builder = FieldSpec.builder(schemaField.clazz(), schemaField.name());
        if (schemaField.clazz().getName().equals(STRING.typeIdentifier)) {
            builder.initializer("$S", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(INTEGER.typeIdentifier)) {
            builder.initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(NUMBER.typeIdentifier)) {
            builder.initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals(STRING_ARRAY.typeIdentifier)) {
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
        } else if (schemaField.clazz().getName().equals(INTEGER_ARRAY.typeIdentifier)) {
            try {
                Field f = schemaField.getClass().getDeclaredField("initialValue");
                f.setAccessible(true);
                Integer[] initialValue = (Integer[]) f.get(schemaField);
                StringBuilder sb = new StringBuilder("{");
                Arrays.stream(initialValue)
                        .forEach(i -> sb.append(i).append(","));
                sb.deleteCharAt(sb.length() - 1);
                sb.append("}");

                ArrayTypeName integerArray = ArrayTypeName.of(Integer.class);
                CodeBlock block = CodeBlock.builder().add("new $1T $2L", integerArray, sb.toString()).build();
                builder.initializer(block);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (schemaField.clazz().getName().equals(OBJECT.typeIdentifier)) {
            String name = StringUtils.capitalize(schemaField.name());
            TypeName childTypeName = ClassName.get("", name);
            builder = FieldSpec.builder(childTypeName, schemaField.name());
            builder.initializer("$L.builder().build()", name);
        } else {
            throw new IncompatibleClassChangeError("Type found = " + schemaField.clazz());
        }
        return builder;
    }
}
