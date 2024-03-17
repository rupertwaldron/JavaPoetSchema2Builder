package com.ruppyrup.javapoet.app;

import java.util.List;

public interface PoetNode {
    void addChild(PoetNode child);
    SchemaField<?> getSchemaField();
    List<PoetNode> getChildren();
    SchemaField<?> traverse(String fieldName);
    void print();
}
