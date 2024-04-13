package com.ruppyrup.javapoet.maker.factories;

import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.makers.ClassMaker;
import com.ruppyrup.javapoet.maker.makers.LombokClassMaker;
import com.ruppyrup.javapoet.maker.makers.StandardClassMaker;

public class ClassMakerFactory {

    public static ClassMaker getClassMakerOfType(String classMakerType, ClassGenerationBuilder builder) {
        if ("standard".equals(classMakerType)) {
            return new StandardClassMaker(builder);
        } else if ("lombok".equals(classMakerType)) {
            return new LombokClassMaker(builder);
        } else {
            throw new IllegalArgumentException("Unknown class maker type: " + classMakerType);
        }
    }
}
