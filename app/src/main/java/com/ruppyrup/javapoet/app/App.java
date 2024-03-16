package com.ruppyrup.javapoet.app;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class App {
    public static void run(PoetParser poetParser, IDataTree dataTree, IGenerator generator) {
        JsonNode parse = poetParser.parse();
        PoetNode treeNode = dataTree.buildFromNode(parse);
        generator.generate(treeNode);
    }
}
