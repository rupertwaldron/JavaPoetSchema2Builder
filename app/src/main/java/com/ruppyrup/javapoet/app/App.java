package com.ruppyrup.javapoet.app;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class App {

    public static PoetNode run(PoetParser poetParser, IDataTree dataTree) {
        JsonNode parse = poetParser.parse();
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
