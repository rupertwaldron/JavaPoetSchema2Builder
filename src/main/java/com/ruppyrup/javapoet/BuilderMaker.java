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
    private final FieldSpecFactory fieldSpecFactory;

    private BuilderMaker(BuilderMakerBuilder builderMakerBuilder) {
        this.className = builderMakerBuilder.className;
        this.fields = builderMakerBuilder.fields;
        this.packageName = builderMakerBuilder.packageName;
        this.fieldSpecFactory = new FieldSpecFactory();
    }

    public static BuilderMakerBuilder builder() {
        return new BuilderMakerBuilder();
    }

    public void makeBuilder() throws IOException {
        TypeName classNameType = ClassName.get("", className);

        MethodSpec buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers( Modifier.PUBLIC)
                .addStatement("return new $T(this)", classNameType)
                .returns(classNameType)
                .build();

        TypeName builderTypeName = ClassName.get("", className + "Builder");

        String builderMethodName = className.toLowerCase() + "Builder";


        List<FieldSpec.Builder> fieldSpecBuilders = fields.stream()
                .map(fieldSpecFactory::creatFieldSpec)
                .map(builder -> builder.addModifiers(Modifier.PUBLIC))
                .toList();




        TypeSpec builder = TypeSpec.classBuilder(className + "Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addMethod(buildMethod)
                .addField(fieldSpecBuilders.get(1).build())
                .addField(fieldSpecBuilders.get(0).build())
                .build();





        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(builderTypeName, builderMethodName)
                .addStatement("this.$N = $N.$N", fields.get(0).name(), builderMethodName, fields.get(0).name())
                .addStatement("this.$N = $N.$N", fields.get(1).name(), builderMethodName, fields.get(1).name())
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
                .addField(fieldSpecBuilders.get(1).build())
                .addField(fieldSpecBuilders.get(0).build())
                .build();

        createJavaFile(classTypeSpec);
    }

    private void createJavaFile(TypeSpec classTypeSpec) throws IOException {
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
