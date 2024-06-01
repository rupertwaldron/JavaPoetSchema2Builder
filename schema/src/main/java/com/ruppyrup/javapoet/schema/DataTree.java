package com.ruppyrup.javapoet.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ruppyrup.javapoet.app.IDataTree;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;


public class DataTree implements IDataTree {
    @Override
    public PoetNode buildFromNode(JsonNode root, String className, String sampleKey) {
        PoetNode poetNode = new TreePoetNode(SchemaFieldFactory.createSchemaField(Map.entry(className, root), sampleKey));
        myFillMap(root, poetNode, sampleKey);
        NodePopulator.populate(poetNode);
        return poetNode;
    }

    private void myFillMap(JsonNode rootNode, PoetNode root, String sampleKey) {
        JsonNode propertiesNode;
        if (rootNode.path("items").isEmpty()) {
            propertiesNode = rootNode.path("properties");
        } else {
            propertiesNode = rootNode.path("items").path("properties");
        }
        Iterator<Map.Entry<String, JsonNode>> fields = propertiesNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> next = fields.next();
            if ((next.getValue().path("type").asText().equals("object")) ||
                    (next.getValue().path("type").asText().equals("array") && next.getValue().path("items").path("type").asText().equals("object"))) {
                SchemaField<?> schemaField = SchemaFieldFactory.createSchemaField(next, sampleKey);
                PoetNode nextPoetNode = new TreePoetNode(schemaField);
                root.addChild(nextPoetNode);
                myFillMap(next.getValue(), nextPoetNode, sampleKey);
            } else {
                SchemaField<?> schemaField = SchemaFieldFactory.createSchemaField(next, sampleKey);
                PoetNode nextPoetNode = new TreePoetNode(schemaField);
                root.addChild(nextPoetNode);
            }
        }
    }
}