package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.app.App;
import com.ruppyrup.javapoet.app.IDataTree;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.PoetParser;
import com.ruppyrup.javapoet.parser.FileParser;
import com.ruppyrup.javapoet.schema.DataTree;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello from Java Poet");
        PoetParser poetParser = new FileParser("src/main/resources/nested_schema.json");
        IDataTree dataTree = new DataTree();
        PoetNode rootNode = App.run(poetParser, dataTree);
        rootNode.print();
    }
}
