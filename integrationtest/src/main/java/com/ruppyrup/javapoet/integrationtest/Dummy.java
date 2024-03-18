package com.ruppyrup.javapoet.integrationtest;

import com.ruppyrup.javapoet.app.App;
import com.ruppyrup.javapoet.app.IDataTree;
import com.ruppyrup.javapoet.app.IGenerator;
import com.ruppyrup.javapoet.app.PoetParser;
import com.ruppyrup.javapoet.maker.GeneratorImpl;
import com.ruppyrup.javapoet.parser.FileParser;
import com.ruppyrup.javapoet.schema.DataTree;

import java.io.File;

public class Dummy {
    public static void run(String filePath) {
        System.out.println(new File(".").getAbsolutePath());
        System.out.println("Hello from Java Poet" + System.getProperty("user.dir"));
        PoetParser poetParser = new FileParser();
        IDataTree dataTree = new DataTree();
        IGenerator generator = new GeneratorImpl();
        App app = new App(poetParser, dataTree, generator);
        app.run(filePath);
    }

    public static void main(String[] args) {
        Dummy.run("integrationtest/src/test/resources/schema1/nested_schema1.json");
    }
}