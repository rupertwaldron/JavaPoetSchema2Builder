package com.ruppyrup.javapoet.app;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface PoetParser {
    JsonNode parse();
}
