package com.ruppyrup.javapoet.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(fluent = true)
public class SchemaField<T> {
    public String name;
    public Class<T> clazz;
    public T initialValue;
}
