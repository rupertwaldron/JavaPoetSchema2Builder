package com.ruppyrup.javapoet.app;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
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
        File dir= new File(directory);

        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                run(directory + "/" + file.getName());
            } else {
                JsonNode parse = poetParser.parse(directory + "/" + file.getName());
                PoetNode treeNode = dataTree.buildFromNode(parse, file.getName().split("\\.")[0]);
                String[] split = directory.split("/");
                String packageName = "com.ruppyrup.javapoet." + split[split.length - 1];
                generator.generate(treeNode, "generated", packageName);
            }
        }
    }
}
