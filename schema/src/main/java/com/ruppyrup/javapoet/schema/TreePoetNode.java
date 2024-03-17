package com.ruppyrup.javapoet.schema;


import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;

import java.util.LinkedList;

import java.util.List;
import java.util.Queue;


public class TreePoetNode implements PoetNode {
    private final SchemaField<?> schemaField;
    private final List<PoetNode> children = new LinkedList<>();

    public TreePoetNode(SchemaField<?> schemaField) {
        this.schemaField = schemaField;
    }

    @Override
    public void addChild(PoetNode child) {
        children.add(child);
    }

    @Override
    public SchemaField<?> traverse(String fieldName) {
        if (schemaField == null) return null;
        if (fieldName.equals(schemaField.name())) return schemaField;

        Queue<PoetNode> fieldsToVisit = new LinkedList<>(children);

       while (!fieldsToVisit.isEmpty()) {
           PoetNode node = fieldsToVisit.poll();
           if (fieldName.equals(node.getSchemaField().name())) return node.getSchemaField();
           if (!node.getChildren().isEmpty()) fieldsToVisit.addAll(node.getChildren());
           System.out.println("Field: " + node.getSchemaField());
       }

       return null;
    }


    @Override
    public SchemaField<?> getSchemaField() {
        return schemaField;
    }

    @Override
    public List<PoetNode> getChildren() {
        return children;
    }

    @Override
    public void print() {
        traverse("");
    }
}
