package com.ruppyrup.javapoet;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class FieldSpecFactory {

    private final ChildObjectFactory childObjectFactory;

    public FieldSpecFactory(ChildObjectFactory childObjectFactory) {
        this.childObjectFactory = childObjectFactory;
    }


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
            childObjectFactory.creatFieldSpec(schemaField, builder);

        } else {
            throw new IncompatibleClassChangeError("Type found = " + schemaField.clazz());
        }
        return builder;
    }
}
