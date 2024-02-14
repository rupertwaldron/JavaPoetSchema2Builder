package com.ruppyrup.javapoet;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;


public class CreateBuilder {

    public static void create() throws IOException {

        FieldSpec number = FieldSpec
                .builder(int.class, "houseNumber")
//                .initializer("$L", 63)
                .addModifiers(Modifier.PRIVATE)
                .build();

        FieldSpec streetName = FieldSpec
                .builder(String.class, "streetName")
//                .initializer("$S", "Rances Lane")
                .addModifiers(Modifier.PRIVATE)
                .build();

        TypeSpec builder = TypeSpec.classBuilder("AddressBuilder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addField(number)
                .addField(streetName)
                .build();

        TypeName addressBuilder = ClassName.get("", "AddressBuilder");

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(addressBuilder, "addressBuilder")
                .addStatement("this.$N = addressBuilder.$N", "houseNumber", "houseNumber")
                .addStatement("this.$N = addressBuilder.$N", "streetName", "streetName")
//                .addParameter(ClassName.get(builder.getClass()), "addressBuilder")
//                .addParameter(int.class, "houseNumber")
//                .addParameter(String.class, "streetName")
                .addModifiers(Modifier.PRIVATE)
                .build();


        TypeSpec address = TypeSpec
                .classBuilder("Address")
                .addType(builder)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor)
                .addField(number)
                .addField(streetName)
                .build();

        JavaFile file = JavaFile.builder("com.ruppyrup.javapoent.generated", address).build();

        file.writeTo(System.out);

        file.writeTo(new File("build/generated"));
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
