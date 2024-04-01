package com.ruppyrup.javapoet.maker.factories;

public enum FieldType {
    STRING("java.lang.String"),
    STRING_ARRAY("[Ljava.lang.String;"),
    INTEGER("java.lang.Integer"),
    INTEGER_ARRAY("[Ljava.lang.Integer;"),

    NUMBER("java.lang.Number"),
    NUMBER_ARRAY("[Ljava.lang.Number;"),

    BOOLEAN("java.lang.Boolean"),
    OBJECT("java.lang.Object");

    public final String typeIdentifier;
    FieldType(String typeIdentifier) {
        this.typeIdentifier = typeIdentifier;
    }
}
