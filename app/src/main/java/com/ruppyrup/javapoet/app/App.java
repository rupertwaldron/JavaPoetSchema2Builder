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

    public void run(String schemaDir, String outputDir, String sampleKey) {
        File dir= new File(schemaDir);

        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                run(schemaDir + "/" + file.getName(), outputDir, sampleKey);
            } else {
                System.out.println("Parsing => " + schemaDir + "/" + file.getName());
                JsonNode parse = poetParser.parse(schemaDir + "/" + file.getName());
                PoetNode treeNode = dataTree.buildFromNode(parse, file.getName().split("\\.")[0], sampleKey);
                String[] split = schemaDir.split("/");
                String packageName = "com.ruppyrup.javapoet." + split[split.length - 1];
                generator.generate(treeNode, outputDir, packageName);
            }
        }
    }
}
