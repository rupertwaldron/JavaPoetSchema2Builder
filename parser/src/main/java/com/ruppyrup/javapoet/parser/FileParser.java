package com.ruppyrup.javapoet.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruppyrup.javapoet.app.PoetParser;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileParser implements PoetParser {

    private final String dirOrFileForNow;
    private final static ObjectMapper mapper = new ObjectMapper();


    public FileParser(String dirOrFileForNow) {
        this.dirOrFileForNow = dirOrFileForNow;
    }

    public JsonNode parse() {
        System.out.println(new File(".").getAbsolutePath());

        String targetFileStr;
        try (FileInputStream fisTargetFile = new FileInputStream(dirOrFileForNow)) {
            targetFileStr = IOUtils.toString(fisTargetFile, StandardCharsets.UTF_8);
            return mapper.readTree(targetFileStr);
        } catch (IOException iox) {
            throw new RuntimeException("Input stream failure whilst parsing: " + iox.getMessage());
        }
    }
}
