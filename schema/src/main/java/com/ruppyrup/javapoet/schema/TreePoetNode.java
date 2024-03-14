package com.ruppyrup.javapoet.schema;


import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.model.SchemaField;

import java.util.LinkedList;

public class TreePoetNode implements PoetNode {
    public final SchemaField<?> schemaField;
    public final LinkedList<PoetNode> children = new LinkedList<>();

    public TreePoetNode(SchemaField<?> schemaField) {
        this.schemaField = schemaField;
    }

    @Override
    public void addChild(PoetNode child) {
        children.add(child);
    }
}
