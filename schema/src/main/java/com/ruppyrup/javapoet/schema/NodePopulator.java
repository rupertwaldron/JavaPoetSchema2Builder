package com.ruppyrup.javapoet.schema;

import com.ruppyrup.javapoet.app.FieldType;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;

import java.util.List;
import java.util.stream.Collectors;

public class NodePopulator {
    static void populate(PoetNode poetNode) {
        for (PoetNode node : poetNode.getChildren()) {
            if ((node.getSchemaField().clazz().getName().equals(FieldType.OBJECT.typeIdentifier) && !node.getChildren().isEmpty()) ||
            (node.getSchemaField().clazz().getName().equals(FieldType.OBJECT_ARRAY.typeIdentifier) && !node.getChildren().isEmpty())) {
                List<? extends SchemaField<?>> schemaFields = node.getChildren().stream()
                        .map(PoetNode::getSchemaField)
                        .collect(Collectors.toList());
                ((SchemaField<Object>)node.getSchemaField()).initialValue(schemaFields);
            }
            populate(node);
        }
    }
}
