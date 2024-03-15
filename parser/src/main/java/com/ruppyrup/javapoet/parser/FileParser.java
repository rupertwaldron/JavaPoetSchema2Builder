package com.ruppyrup.javapoet.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.javapoet.app.PoetParser;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileParser implements PoetParser {

    public JsonNode parse(String fileName) throws IOException {
        FileInputStream fisTargetFile = new FileInputStream("src/main/resources/nested_schema.json");

        String targetFileStr = IOUtils.toString(fisTargetFile, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(targetFileStr);
    }

}
