package com.ruppyrup.javapoet.model;

public record SchemaField<T> (String name, Class<T> clazz, T initialValue) {}
