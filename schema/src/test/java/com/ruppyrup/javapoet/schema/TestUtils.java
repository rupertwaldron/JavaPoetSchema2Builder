package com.ruppyrup.javapoet.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public final class TestUtils {

    private final static ObjectMapper mapper = new ObjectMapper();

    private TestUtils() {
    }

    public static JsonNode getJsonNode() {
        JsonNode root;

        File projectDir = new File(System.getProperty("user.dir"));

        try (FileInputStream fisTargetFile = new FileInputStream(projectDir.getParent() + "/testdata/nested_schema.json")) {
            String targetFileStr = IOUtils.toString(fisTargetFile, StandardCharsets.UTF_8);
            root = mapper.readTree(targetFileStr);
        } catch (IOException iox) {
            throw new RuntimeException("Input stream failure whilst parsing: " + iox.getMessage());
        }
        return root;
    }

    public static void assertField(PoetNode poetNode, SchemaField<?> expected) {
        SchemaField<?> field = poetNode.traverse(expected.name());
        assertThat(field).isEqualTo(expected);
    }

    public static <T> void assertInitialValue(PoetNode poetNode, String nodeName, T expected) {
        SchemaField<?> schema = poetNode.traverse(nodeName);
        assertThat(schema.initialValue()).isEqualTo(expected);
    }

    @SafeVarargs
    public static <T> void  assertArrayField(PoetNode poetNode, String fieldName, T... values) {
        SchemaField<?> field = poetNode.traverse(fieldName);
        assertThat((T[])field.initialValue).containsExactly(values);
    }
}
