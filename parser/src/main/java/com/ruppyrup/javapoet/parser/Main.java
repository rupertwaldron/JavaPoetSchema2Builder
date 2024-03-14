package com.ruppyrup.javapoet.parser;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {


    static TreeNode treeNode;

    public static void main(String[] args) throws IOException {
        FileInputStream fisTargetFile = new FileInputStream("parser/src/main/resources/nested_schema.json");

        String targetFileStr = IOUtils.toString(fisTargetFile, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(targetFileStr);

        treeNode = new TreeNode(SchemaFieldFactory.createSchemaField(Map.entry(root.path("title").asText(), root)));
        myFillMap(root, treeNode);

    }

    private static void myFillMap(JsonNode rootNode, TreeNode root) {
        JsonNode propertiesNode = rootNode.path("properties");
        Iterator<Map.Entry<String, JsonNode>> fields = propertiesNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> next = fields.next();
            if (next.getValue().path("type").asText().equals("object")) {
                DummySchemaField<?> schemaField = SchemaFieldFactory.createSchemaField(next);
                TreeNode nextNode = new TreeNode(schemaField);
                root.addChild(nextNode);
                myFillMap(next.getValue(), nextNode);
            } else {
                DummySchemaField<?> schemaField = SchemaFieldFactory.createSchemaField(next);
                TreeNode nextNode = new TreeNode(schemaField);
                root.addChild(nextNode);
            }
        }
    }
}