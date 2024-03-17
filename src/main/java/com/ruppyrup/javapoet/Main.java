package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.app.App;
import com.ruppyrup.javapoet.app.IDataTree;
import com.ruppyrup.javapoet.app.IGenerator;
import com.ruppyrup.javapoet.app.PoetParser;
import com.ruppyrup.javapoet.maker.GeneratorImpl;
import com.ruppyrup.javapoet.parser.FileParser;
import com.ruppyrup.javapoet.schema.DataTree;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Java Poet");
        PoetParser poetParser = new FileParser("src/main/resources/nested_schema2.json");
        IDataTree dataTree = new DataTree();
        IGenerator generator = new GeneratorImpl();
        App.run(poetParser, dataTree, generator);
    }
}
