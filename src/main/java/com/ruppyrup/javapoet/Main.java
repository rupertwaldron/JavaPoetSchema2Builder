package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.app.App;
import com.ruppyrup.javapoet.app.IDataTree;
import com.ruppyrup.javapoet.app.IGenerator;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.app.PoetParser;
import com.ruppyrup.javapoet.app.Populator;
import com.ruppyrup.javapoet.maker.GeneratorImpl;
import com.ruppyrup.javapoet.parser.FileParser;
import com.ruppyrup.javapoet.schema.DataTree;
import com.ruppyrup.javapoet.schema.NodePopulator;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Java Poet");
        PoetParser poetParser = new FileParser("src/main/resources/nested_schema.json");
        IDataTree dataTree = new DataTree();
        Populator populator = new NodePopulator();
        IGenerator generator = new GeneratorImpl();
        App.run(poetParser, dataTree, generator, populator);
    }
}
