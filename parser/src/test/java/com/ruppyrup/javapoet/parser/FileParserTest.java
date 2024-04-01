package com.ruppyrup.javapoet.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


class FileParserTest {

    @Test
    void parse() throws IOException {
        FileParser fileParser = new FileParser();
        File projectDir = new File(System.getProperty("user.dir"));
        JsonNode jsonNode = fileParser.parse(projectDir.getParent() + "/testdata/nested_schema.json");

        FileInputStream expectedIS = new FileInputStream("src/test/resources/expected_output.json");
        String expectedString = IOUtils.toString(expectedIS, StandardCharsets.UTF_8);

        assertThat(jsonNode.toString()).isEqualTo(expectedString);
    }
}