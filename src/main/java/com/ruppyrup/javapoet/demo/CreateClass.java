package com.ruppyrup.javapoet.demo;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CreateClass {

    public static void create(String dir) throws IOException {
        FieldSpec name = FieldSpec
                .builder(String.class, "name")
                .addModifiers(Modifier.PRIVATE)
                .build();

        ParameterSpec strings = ParameterSpec
                .builder(
                        ParameterizedTypeName.get(ClassName.get(List.class), TypeName.get(String.class)),
                        "strings")
                .build();

        CodeBlock sumOfTenImpl = CodeBlock
                .builder()
                .addStatement("int sum = 0")
                .beginControlFlow("for (int i = 0; i <= 10; i++)")
                .addStatement("sum += i")
                .endControlFlow()
                .build();


        MethodSpec sumOfTen = MethodSpec
                .methodBuilder("sumOfTen")
                .addParameter(int.class, "number")
                .addParameter(strings)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addCode(sumOfTenImpl)
                .build();

        TypeSpec person = TypeSpec
                .classBuilder("Person")
                .addModifiers(Modifier.PUBLIC)
                .addField(name)
                .addMethod(MethodSpec
                        .methodBuilder("getName")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return this.name")
                        .build())
                .addMethod(MethodSpec
                        .methodBuilder("setName")
                        .addParameter(String.class, "name")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .addStatement("this.name = name")
                        .build())
                .addMethod(sumOfTen)
                .build();

        JavaFile file = JavaFile.builder("com.ruppyrup.javapoet.generated", person).build();

        file.writeTo(System.out);

        file.writeTo(new File(dir));

    }

}
