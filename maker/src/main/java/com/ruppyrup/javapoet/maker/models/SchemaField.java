package com.ruppyrup.javapoet.maker.models;

public record SchemaField<T> (String name, Class<T> clazz, T initialValue) {}
