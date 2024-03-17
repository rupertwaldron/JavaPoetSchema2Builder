package com.ruppyrup.javapoet.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class DataTreeTest {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Test
    void buildFromNode() {
        System.out.println(new File(".").getAbsolutePath());

        JsonNode root;

        try (FileInputStream fisTargetFile = new FileInputStream("src/test/resources/nested_schema.json")) {
            String targetFileStr = IOUtils.toString(fisTargetFile, StandardCharsets.UTF_8);
            root = mapper.readTree(targetFileStr);
        } catch (IOException iox) {
            throw new RuntimeException("Input stream failure whilst parsing: " + iox.getMessage());
        }

        DataTree dataTree = new DataTree();
        PoetNode poetNode = dataTree.buildFromNode(root);

        SchemaField<?> schemaField = new SchemaField<>("name", String.class, "Rupert");
        assertField(poetNode, schemaField);

        schemaField = new SchemaField<>("city", String.class, "Wokingham");
        assertField(poetNode, schemaField);

        schemaField = new SchemaField<>("age", Integer.class, 33);
        assertField(poetNode, schemaField);

    }

    private void assertField(PoetNode poetNode, SchemaField<?> expected) {
        SchemaField<?> field = poetNode.traverse(expected.name());
        assertThat(field).isEqualTo(expected);
    }
}