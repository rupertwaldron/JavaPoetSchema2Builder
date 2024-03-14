package com.ruppyrup.javapoet.parser;

public record DummySchemaField<T> (String name, Class<T> clazz, T initialValue) {}
