package com.ruppyrup.javapoet.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.ruppyrup.javapoet.schema.TestUtils.assertArrayField;
import static com.ruppyrup.javapoet.schema.TestUtils.assertField;
import static com.ruppyrup.javapoet.schema.TestUtils.getJsonNode;

class DataTreeTest {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Test
    void canBuildDataTreeFromJsonNode() {
        System.out.println(new File(".").getAbsolutePath());

        JsonNode root = getJsonNode();

        DataTree dataTree = new DataTree();
        PoetNode poetNode = dataTree.buildFromNode(root, "Person");

        SchemaField<?> schemaField = new SchemaField<>("name", String.class, "Rupert");
        assertField(poetNode, schemaField);

        schemaField = new SchemaField<>("city", String.class, "Wokingham");
        assertField(poetNode, schemaField);

        schemaField = new SchemaField<>("age", Integer.class, 33);
        assertField(poetNode, schemaField);

        schemaField = new SchemaField<>("codePart2", String.class, "1LG");
        assertField(poetNode, schemaField);

        assertArrayField(poetNode, "coinToss", true, false, true);
        assertArrayField(poetNode, "emptyInts");
    }
}