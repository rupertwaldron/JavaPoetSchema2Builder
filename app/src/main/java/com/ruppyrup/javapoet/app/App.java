package com.ruppyrup.javapoet.app;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class App {

    private final PoetParser poetParser;
    private final IDataTree dataTree;
    private final IGenerator generator;

    public App(PoetParser poetParser, IDataTree dataTree, IGenerator generator) {
        this.poetParser = poetParser;
        this.dataTree = dataTree;
        this.generator = generator;
    }

    public void run(String directory) {
        JsonNode parse = poetParser.parse(directory);
        PoetNode treeNode = dataTree.buildFromNode(parse);
        generator.generate(treeNode);
    }
}
