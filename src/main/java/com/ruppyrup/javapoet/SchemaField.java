package com.ruppyrup.javapoet;

public record SchemaField<T> (String name, Class<T> clazz, T initialValue) {}
