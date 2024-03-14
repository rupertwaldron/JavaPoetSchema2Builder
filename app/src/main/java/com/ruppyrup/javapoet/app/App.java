package com.ruppyrup.javapoet.app;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class App {

    public static PoetNode run(PoetParser poetParser, IDataTree dataTree) throws IOException {
        JsonNode parse = poetParser.parse("app/src/main/resources/nested_schema.json");
        PoetNode treeNode = dataTree.buildFromNode(parse);
        return treeNode;
    }
    public static void main(String[] args) throws IOException {
//        System.out.println("Hello from Java Poet");
//        PoetParser fileParser = new FileParser();
//        JsonNode rootNode = fileParser.parse("resources/nested_schema.json");
//        IDataTree IDataTree = new DataTree();
//        TreeNode treeNode = IDataTree.buildFromNode(rootNode);
//        System.out.println("Got tree: " + treeNode.toString());
    }
}
