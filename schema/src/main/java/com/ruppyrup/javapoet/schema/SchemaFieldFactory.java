package com.ruppyrup.javapoet.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruppyrup.javapoet.app.SchemaField;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SchemaFieldFactory {

    public static SchemaField<?> createSchemaField(Map.Entry<String, JsonNode> next) {
        if (next.getValue().path("type").asText().equals("object")) {
            return new SchemaField<>(next.getKey(), Object.class, null);
        } else if (next.getValue().path("type").asText().equals("string")) {
            return new SchemaField<>(next.getKey(), String.class, next.getValue().path("sample").textValue());
        } else if (next.getValue().path("type").asText().equals("integer")) {
            return new SchemaField<>(next.getKey(), Integer.class, next.getValue().path("sample").intValue());
        } else if (next.getValue().path("type").asText().equals("number")) {
            return new SchemaField<>(next.getKey(), Number.class, next.getValue().path("sample").asDouble());
        } else if (next.getValue().path("type").asText().equals("array")) {
            Iterator<JsonNode> elements = next.getValue().path("items").path("sample").elements();
            if (next.getValue().path("items").path("type").asText().equals("string")) {
                List<String> elementsList = new ArrayList<>();
                elements.forEachRemaining(el -> elementsList.add(el.asText()));
                return new SchemaField<>(next.getKey(), String[].class, elementsList.toArray(new String[0]));
            } else if (next.getValue().path("items").path("type").asText().equals("integer")) {
                List<Integer> elementsList = new ArrayList<>();
                elements.forEachRemaining(el -> elementsList.add(el.intValue()));
                return new SchemaField<>(next.getKey(), Integer[].class, elementsList.toArray(new Integer[0]));
            } else if (next.getValue().path("items").path("type").asText().equals("number")) {
                List<Number> elementsList = new ArrayList<>();
                elements.forEachRemaining(el -> elementsList.add(el.doubleValue()));
                return new SchemaField<>(next.getKey(), Number[].class, elementsList.toArray(new Number[0]));
            } else {
                throw new InvalidParameterException(next.getKey() + " is not a valid schema field");
            }
        } else {
            throw new InvalidParameterException(next.getKey() + " is not a valid schema field");
        }
    }
}
