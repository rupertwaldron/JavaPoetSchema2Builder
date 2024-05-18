package com.ruppyrup.javapoet.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.util.ArrayList;

import static com.ruppyrup.javapoet.schema.TestUtils.assertArrayField;
import static com.ruppyrup.javapoet.schema.TestUtils.assertField;
import static com.ruppyrup.javapoet.schema.TestUtils.getJsonNode;
import static org.assertj.core.api.Assertions.assertThat;

class DataTreeTest {
    private final static ObjectMapper mapper = new ObjectMapper();

    @ParameterizedTest
    @CsvSource({"sample, nested_schema.json", "default, nested_schema_default.json"})
    void canBuildDataTreeFromJsonNode(String defaultKey, String filename) {
        System.out.println(new File(".").getAbsolutePath());

        JsonNode root = getJsonNode(filename);

        DataTree dataTree = new DataTree();
        PoetNode poetNode = dataTree.buildFromNode(root, "Person", defaultKey);

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

        ArrayList<SchemaField<?>> listOfObjects = (ArrayList<SchemaField<?>>) (poetNode.traverse("keepsakes")).initialValue;

        assertThat(listOfObjects).hasSize(2);

        Object[] array = listOfObjects.stream()
                .map(SchemaField::name)
                .toArray();

        assertThat(array).contains("jewelery", "book");
    }
}