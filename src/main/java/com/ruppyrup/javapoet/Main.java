package com.ruppyrup.javapoet;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        BuilderMaker builderMaker = BuilderMaker.builder()
//                .withPackageName("com.ruppyrup.javapoet.generated")
//                .withClassName("Address")
//                .withField("streetName", String.class)
//                .build();
//        builderMaker.makeBuilder();
        CreateClass.create();
    }
}
