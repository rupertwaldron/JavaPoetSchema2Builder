package com.ruppyrup.javapoet.parser;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {

    static Map<String, DummySchemaField<?>> elementsMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        FileInputStream fisTargetFile = new FileInputStream("parser/src/main/resources/nested_schema.json");

        String targetFileStr = IOUtils.toString(fisTargetFile, StandardCharsets.UTF_8);


        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(targetFileStr);
//        JsonNode rootNode = root.path("properties");
//        FillTheElementMap(rootNode);
        myFillMap(root);
        System.out.println(elementsMap);

    }

    private static void myFillMap(JsonNode rootNode) {
        JsonNode propertiesNode = rootNode.path("properties");
        Iterator<Map.Entry<String, JsonNode>> fields = propertiesNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> next = fields.next();
            if (next.getValue().path("type").asText().equals("object")) {
                myFillMap(next.getValue());
            } else {
                DummySchemaField<?> schemaField = SchemaFieldFactory.createSchemaField(next);
                elementsMap.put(schemaField.name(), schemaField);
            }
        }
    }

//    private static void FillTheElementMap(JsonNode rootNode) {
//        for (JsonNode cNode : rootNode){
//            if(cNode.path("type").toString().toLowerCase().contains("array")){
//                for(JsonNode ccNode : cNode.path("items")){
//                    FillTheElementMap(ccNode);
//                }
//            }
//            else if(cNode.path("type").toString().toLowerCase().contains("object")){
//                FillTheElementMap(cNode.path("properties"));
//            }
//            else{
//                elementsMap.put(cNode.path("id").asText(), cNode);
//            }
//        }
//    }
}