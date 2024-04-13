package com.ruppyrup.javapoet.maker;

import com.ruppyrup.javapoet.app.IGenerator;
import com.ruppyrup.javapoet.app.PoetNode;
import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.factories.ClassMakerFactory;
import com.ruppyrup.javapoet.maker.makers.ClassMaker;
import com.ruppyrup.javapoet.maker.makers.StandardClassMaker;

import java.io.IOException;

public class GeneratorImpl implements IGenerator {

    public void generate(PoetNode poetNode, String outputDirectory, String packageName, String classMakerType) {
        String className = poetNode.getSchemaField().name();
        ClassGenerationBuilder.GenerationBuilder classGenerationBuilder = ClassGenerationBuilder.builder()
                .withDir(outputDirectory)
                .withPackageName(packageName)
                .withClassName(className);

        poetNode.getChildren().forEach(child -> {
            classGenerationBuilder.withField(child.getSchemaField());
        });

        ClassMaker classMaker = ClassMakerFactory.getClassMakerOfType(classMakerType, classGenerationBuilder.build());

        try {
            classMaker.makeBuilder();
        } catch (IOException e) {
            throw new RuntimeException("Error making class " + e.getMessage());
        }
    }
}
