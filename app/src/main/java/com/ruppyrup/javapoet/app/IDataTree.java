package com.ruppyrup.javapoet.app;

import com.fasterxml.jackson.databind.JsonNode;

public interface IDataTree {
    PoetNode buildFromNode(JsonNode root, String className, String sampleKey);
}
