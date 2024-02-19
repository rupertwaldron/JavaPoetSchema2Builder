package com.ruppyrup.javapoet;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

public class FieldSpecFactory {

    public FieldSpec.Builder creatFieldSpec(SchemaField<?> schemaField) {
        var builder = FieldSpec.builder(schemaField.clazz(), schemaField.name());
        if (schemaField.clazz().getName().equals("java.lang.String")) {
            builder.initializer("$S", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals("java.lang.Integer")) {
            builder.initializer("$L", schemaField.initialValue());
        } else if (schemaField.clazz().getName().equals("[Ljava.lang.String;")) {
            String[] testArr = new String[] { "1", "2" };
            String literal = "{\"" + String.join("\",\"", testArr) + "\"}";
            ArrayTypeName stringArray = ArrayTypeName.of(String.class);
            CodeBlock block = CodeBlock.builder().add("new $1T $2L", stringArray, literal).build();
            builder.initializer(block);
        } else {
            throw new IncompatibleClassChangeError("Type found = " + schemaField.clazz());
        }
        return builder;
    }
}
