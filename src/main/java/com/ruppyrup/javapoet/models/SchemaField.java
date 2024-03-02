package com.ruppyrup.javapoet.models;

public record SchemaField<T> (String name, Class<T> clazz, T initialValue) {}
