package com.ruppyrup.javapoet.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {

    @Test
    void parse() throws IOException {
        FileParser fileParser = new FileParser();
        JsonNode jsonNode = fileParser.parse("parser/src/test/resources/nested_schema.json");
        assertNotNull(jsonNode);
//        jsonNode.path()
    }
}