package com.ruppyrup.javapoet.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruppyrup.javapoet.app.IDataTree;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.model.SchemaField;

import java.util.Iterator;
import java.util.Map;


public class DataTree implements IDataTree {
    @Override
    public PoetNode buildFromNode(JsonNode root) {
        PoetNode poetNode = new TreePoetNode(SchemaFieldFactory.createSchemaField(Map.entry(root.path("title").asText(), root)));
        myFillMap(root, poetNode);
        NodePopulator.populate(poetNode);
        return poetNode;
    }

    private void myFillMap(JsonNode rootNode, PoetNode root) {
        JsonNode propertiesNode = rootNode.path("properties");
        Iterator<Map.Entry<String, JsonNode>> fields = propertiesNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> next = fields.next();
            if (next.getValue().path("type").asText().equals("object")) {
                SchemaField<?> schemaField = SchemaFieldFactory.createSchemaField(next);
                PoetNode nextPoetNode = new TreePoetNode(schemaField);
                root.addChild(nextPoetNode);
                myFillMap(next.getValue(), nextPoetNode);
            } else {
                SchemaField<?> schemaField = SchemaFieldFactory.createSchemaField(next);
                PoetNode nextPoetNode = new TreePoetNode(schemaField);
                root.addChild(nextPoetNode);
            }
        }
    }
}