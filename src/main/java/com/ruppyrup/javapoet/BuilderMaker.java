package com.ruppyrup.javapoet;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class BuilderMaker {

    private final String packageName;
    private final String className;
    private final List<SchemaField<?>> fields;

    private BuilderMaker(BuilderMakerBuilder builderMakerBuilder) {
        this.className = builderMakerBuilder.className;
        this.fields = builderMakerBuilder.fields;
        this.packageName = builderMakerBuilder.packageName;
    }

    public static BuilderMakerBuilder builder() {
        return new BuilderMakerBuilder();
    }

    public void makeBuilder() throws IOException {

        List<FieldSpec.Builder> fieldSpecBuilders = fields.stream()
                .map(sf -> {
                    var builder = FieldSpec.builder(sf.clazz(), sf.name());
                    builder.initializer("$S", sf.clazz().cast(sf.initialValue()));
                    return builder;
                })
                .map(builder -> builder.addModifiers(Modifier.PUBLIC))
                .toList();


        FieldSpec number = FieldSpec
                .builder(int.class, "houseNumber")
//                .initializer("$L", 63)
                .addModifiers(Modifier.PUBLIC)
                .build();

//        FieldSpec streetName = FieldSpec
//                .builder(, "streetName")
////                .initializer("$S", "Rances Lane")
//                .addModifiers(Modifier.PUBLIC)
//                .build();

        FieldSpec numberDefault = FieldSpec
                .builder(int.class, "houseNumber")
                .initializer("$L", 63)
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec streetNameDefault = FieldSpec
                .builder(String.class, "streetName")
                .initializer("$S", "Rances Lane")
                .addModifiers(Modifier.PRIVATE)
                .build();

        TypeName classNameType = ClassName.get("", className);

        MethodSpec buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers( Modifier.PUBLIC)
                .addStatement("return new $T(this)", classNameType)
                .returns(classNameType)
                .build();

        TypeSpec builder = TypeSpec.classBuilder(className + "Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addMethod(buildMethod)
                .addField(numberDefault)
                .addField(fieldSpecBuilders.get(0).build())
                .build();



        TypeName builderTypeName = ClassName.get("", className + "Builder");

        String builderMethodName = className.toLowerCase() + "Builder";

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(builderTypeName, builderMethodName)
                .addStatement("this.$N = $N.$N", "houseNumber", builderMethodName, "houseNumber")
                .addStatement("this.$N = $N.$N", "streetName", builderMethodName, "streetName")
//                .addParameter(ClassName.get(builder.getClass()), "builderTypeName")
//                .addParameter(int.class, "houseNumber")
//                .addParameter(String.class, "streetName")
                .addModifiers(Modifier.PRIVATE)
                .build();

        MethodSpec staticBuilder = MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addStatement("return new $T()", builderTypeName)
                .returns(builderTypeName)
                .build();


        TypeSpec classTypeSpec = TypeSpec
                .classBuilder(className)
                .addType(builder)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor)
                .addMethod(staticBuilder)
                .addField(number)
                .addField(fieldSpecBuilders.get(0).build())
                .build();

        JavaFile file = JavaFile.builder(packageName, classTypeSpec).build();

        file.writeTo(System.out);

        file.writeTo(new File("build/generated"));
    }

    public static class BuilderMakerBuilder {
        private String packageName;
        private String className;
        private final List<SchemaField<?>> fields = new ArrayList<>();

        public BuilderMaker build() {
            return new BuilderMaker(this);
        }

        public BuilderMakerBuilder withPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public BuilderMakerBuilder withClassName(String className) {
            this.className = className;
            return this;
        }

        public BuilderMakerBuilder withField(SchemaField<?> schemaField) {
            this.fields.add(schemaField);
            return this;
        }
    }
}


//public class Address {
//    int houseNumber = 63;
//
//    String streetName = "Rances Lane";
//
//    Address(int houseNumber, String streetName) {
//        this.houseNumber = houseNumber;
//        this.streetName = streetName;
//    }
//
//    public static AddressBuilder builder() {
//        return new AddressBuilder();
//    }
//
//    public static class AddressBuilder {
//        private int houseNumber;
//        private String streetName;
//
//        AddressBuilder() {
//        }
//
//        public AddressBuilder houseNumber(int houseNumber) {
//            this.houseNumber = houseNumber;
//            return this;
//        }
//
//        public AddressBuilder streetName(String streetName) {
//            this.streetName = streetName;
//            return this;
//        }
//
//        public Address build() {
//            return new Address(this.houseNumber, this.streetName);
//        }
//
//        public String toString() {
//            return "Address.AddressBuilder(houseNumber=" + this.houseNumber + ", streetName=" + this.streetName + ")";
//        }
//    }
//}
