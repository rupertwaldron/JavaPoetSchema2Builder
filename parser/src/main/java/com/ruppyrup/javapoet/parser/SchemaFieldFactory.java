package com.ruppyrup.javapoet.parser;

import com.fasterxml.jackson.databind.JsonNode;

import javax.xml.validation.Schema;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SchemaFieldFactory {

    public static DummySchemaField<?> createSchemaField(Map.Entry<String, JsonNode> next) {
        if (next.getValue().path("type").asText().equals("object")) {
            return new DummySchemaField<>(next.getKey(), Object.class, null);
        } else if (next.getValue().path("type").asText().equals("string")) {
            return new DummySchemaField<>(next.getKey(), String.class, next.getValue().path("sample").textValue());
        } else if (next.getValue().path("type").asText().equals("integer")) {
            return new DummySchemaField<>(next.getKey(), Integer.class, next.getValue().path("sample").intValue());
        } else if (next.getValue().path("type").asText().equals("array")) {
            Iterator<JsonNode> elements = next.getValue().path("items").path("sample").elements();
            if (next.getValue().path("items").path("type").asText().equals("string")) {
                List<String> elementsList = new ArrayList<>();
                elements.forEachRemaining(el -> elementsList.add(el.asText()));
                return new DummySchemaField<>(next.getKey(), String[].class, elementsList.toArray(new String[0]));
            } else if (next.getValue().path("items").path("type").asText().equals("integer")) {
                List<Integer> elementsList = new ArrayList<>();
                elements.forEachRemaining(el -> elementsList.add(el.intValue()));
                return new DummySchemaField<>(next.getKey(), Integer[].class, elementsList.toArray(new Integer[0]));
            } else {
                throw new InvalidParameterException(next.getKey() + " is not a valid schema field");
            }
        } else {
            throw new InvalidParameterException(next.getKey() + " is not a valid schema field");
        }
    }
}
