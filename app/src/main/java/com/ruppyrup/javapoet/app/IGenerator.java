package com.ruppyrup.javapoet.app;

import java.io.IOException;

public interface IGenerator {
    void generate(PoetNode poetNode, String outputDirectory, String packageName, String classMakerType);
}
