package com.ruppyrup.javapoet;

import java.util.List;

public record SchemaObject (String className, List<SchemaField<?>> fields) {}
