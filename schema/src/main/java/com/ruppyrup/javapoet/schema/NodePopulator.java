package com.ruppyrup.javapoet.schema;

import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;

import java.util.List;
import java.util.stream.Collectors;

public class NodePopulator {

    static void populate(PoetNode poetNode) {
        for (PoetNode node : poetNode.getChildren()) {
            if (node.getSchemaField().clazz().getName().equals("java.lang.Object") && !node.getChildren().isEmpty()) {
                List<? extends SchemaField<?>> schemaFields = node.getChildren().stream()
                        .map(PoetNode::getSchemaField)
                        .collect(Collectors.toList());
                ((SchemaField<Object>)node.getSchemaField()).initialValue(schemaFields);
            }
            populate(node);
        }
    }
}
