package com.ruppyrup.javapoet;

import com.squareup.javapoet.FieldSpec;

public class FieldSpecFactory {

    public FieldSpec.Builder creatFieldSpec(SchemaField<?> schemaField) {
        var builder = FieldSpec.builder(schemaField.clazz(), schemaField.name());
        if (schemaField.clazz().getName().contains("String")) {
            builder.initializer("$S", schemaField.initialValue());
        } else if (schemaField.clazz().getName().contains("Integer")) {
            builder.initializer("$L", schemaField.initialValue());
        } else {
            throw new IncompatibleClassChangeError("Type found = " + schemaField.clazz());
        }
        return builder;
    }
}
