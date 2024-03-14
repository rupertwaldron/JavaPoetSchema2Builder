package com.ruppyrup.javapoet.parser;

import java.util.LinkedList;

public class TreeNode {
    public final DummySchemaField<?> dummySchemaField;
    public final LinkedList<TreeNode> children = new LinkedList<>();

    public TreeNode(DummySchemaField<?> dummySchemaField) {
        this.dummySchemaField = dummySchemaField;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }
}
