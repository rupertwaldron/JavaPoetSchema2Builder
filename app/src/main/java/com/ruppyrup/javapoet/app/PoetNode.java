package com.ruppyrup.javapoet.app;

import com.ruppyrup.javapoet.model.SchemaField;

import java.util.LinkedList;

public interface PoetNode {
    void addChild(PoetNode child);
    SchemaField<?> getSchemaField();
    LinkedList<PoetNode> getChildren();
    SchemaField<?> traverse(String fieldName);

    void print();
}
