package com.ruppyrup.javapoet.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.SchemaField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ruppyrup.javapoet.schema.TestUtils.assertField;
import static com.ruppyrup.javapoet.schema.TestUtils.assertInitialValue;
import static com.ruppyrup.javapoet.schema.TestUtils.getJsonNode;
import static org.assertj.core.api.Assertions.assertThat;

class NodePopulatorTest {

    @ParameterizedTest
    @CsvSource({"sample, nested_schema.json", "default, nested_schema_default.json"})
    void canPopulatePoetNode(String defaultKey, String filename) {
        System.out.println(new File(".").getAbsolutePath());

        JsonNode root = getJsonNode(filename);
        DataTree dataTree = new DataTree();
        PoetNode poetNode = dataTree.buildFromNode(root, "Person", defaultKey);

        NodePopulator.populate(poetNode);

        SchemaField<?> schemaField = new SchemaField<>("Person", Object.class, null);
        assertField(poetNode, schemaField);

        SchemaField<?> address = poetNode.traverse("address");
        assertThat(address.initialValue()).isInstanceOf(List.class);
        assertInitialValue(poetNode, "yearsInHouse", 16.9);
        assertInitialValue(poetNode, "codePart2", "1LG");
        assertInitialValue(poetNode, "male", true);
        SchemaField<?> meterReadings = poetNode.traverse("meterReadings");
        assertThat((Number[]) meterReadings.initialValue()).contains(16.9, 120.9, 200.64);
        SchemaField<?> emptyInts = poetNode.traverse("emptyInts");
        assertThat((Integer []) emptyInts.initialValue()).isEmpty();

        ArrayList<SchemaField<?>> listOfObjects = (ArrayList<SchemaField<?>>) (poetNode.traverse("books")).initialValue;

        assertThat(listOfObjects).hasSize(2);

        Object[] array = listOfObjects.stream()
                .map(SchemaField::name)
                .toArray();

        assertThat(array).contains("author", "title");
    }
}