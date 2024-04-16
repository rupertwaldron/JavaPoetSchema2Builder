package com.ruppyrup.javapoet.maker.makers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ruppyrup.javapoet.app.SchemaField;
import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.factories.FieldSpecFactory;
import com.ruppyrup.javapoet.maker.factories.GetterMethodFactory;
import com.ruppyrup.javapoet.maker.factories.LombokFieldSpecFactory;
import com.ruppyrup.javapoet.maker.factories.WithMethodFactory;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LombokClassMaker implements ClassMaker {
    private final String dir;
    private final String packageName;
    private final String className;
    private final List<SchemaField<?>> fields;
    private final ChildObjectMaker childObjectMaker;
    List<FieldSpec.Builder> fieldSpecBuilders = new ArrayList<>();

    public LombokClassMaker(ClassGenerationBuilder classGenerationBuilder) {
        this.className = classGenerationBuilder.getClassName();
        this.fields = classGenerationBuilder.getFields();
        this.packageName = classGenerationBuilder.getPackageName();
        this.dir = classGenerationBuilder.getDir();
        this.childObjectMaker = new ChildObjectMaker();
    }

    @Override
    public void makeBuilder() throws IOException {
        TypeName generatedClass = ClassName.get("", className);
        fieldBuilders();
        generateChildObjects();
        createJavaFile(generatedClassSpec(generatedClass, fieldSpecBuilders));
    }

    private void generateChildObjects() {
        fields.stream()
                .filter(schemaField -> schemaField.clazz().getName().equals("java.lang.Object"))
                .forEach(schemaField -> childObjectMaker.makeChild(schemaField, dir, packageName, "lombok"));
    }

    private void fieldBuilders() {
        fields.stream()
                .map(LombokFieldSpecFactory::creatFieldSpec)
//                .map(builder -> builder.addModifiers(Modifier.PRIVATE))
                .forEach(fieldSpecBuilders::add);
    }

    private TypeSpec generatedClassSpec(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders) {
//        TypeName builderTypeName = ClassName.get("", "Builder1");
        String builderMethodName = className.toLowerCase() + "Builder2";

        AnnotationSpec builderAnnotation = AnnotationSpec.builder(Builder.class)
                .addMember("setterPrefix", "$S","with")
                .addMember("builderClassName", "$S","Builder")
                .addMember("buildMethodName", "$S","build0")
                .addMember("toBuilder", "$L", true)
                .build();

        AnnotationSpec fluentAnnotation = AnnotationSpec.builder(Accessors.class)
                .addMember("fluent", "$L", true)
                .build();

        AnnotationSpec jacksonAutoInclude = AnnotationSpec.builder(JsonAutoDetect.class)
                .addMember("fieldVisibility", "$T.ANY", JsonAutoDetect.Visibility.ANY.getClass())
                .build();

        TypeSpec.Builder classTypeSpecBuilder = TypeSpec
                .classBuilder(className)
//                .addAnnotation(jacksonAutoInclude)
                .addAnnotation(builderAnnotation)
                .addAnnotation(fluentAnnotation)
                .addAnnotation(Value.class)
                .addType(builderForGeneratedClass(classNameType, fieldSpecBuilders))
                .addModifiers(Modifier.PUBLIC);
//                .addMethod(createConstructor(builderTypeName, builderMethodName))
//                .addMethod(createStaticBuilder(builderTypeName));

        fieldSpecBuilders.forEach(fsb -> classTypeSpecBuilder.addField(fsb.build()));
//        fields.forEach(field -> classTypeSpecBuilder.addMethod(createGetterFor(field)));
        return classTypeSpecBuilder.build();
    }

    private TypeSpec builderForGeneratedClass(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders) {
        TypeSpec.Builder builderType = TypeSpec.classBuilder("Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addMethod(createBuildMethod(classNameType));

//        fields.forEach(field -> builderType.addMethod(buildersWithMethods(field, builderTypeName)));
//        fieldSpecBuilders.forEach(fsb -> builderType.addField(fsb.build()));

        return builderType.build();
    }

    private MethodSpec buildersWithMethods(SchemaField<?> schemaField, TypeName builderTypeName) {
        return WithMethodFactory.getWithMethod(schemaField)
                .returns(builderTypeName)
                .build();
    }

    private void createJavaFile(TypeSpec classTypeSpec) throws IOException {
        JavaFile file = JavaFile.builder(packageName, classTypeSpec).build();

        file.writeTo(System.out);
        file.writeTo(new File(dir));
    }

    private MethodSpec createConstructor(TypeName builderTypeName, String builderMethodName) {
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addParameter(builderTypeName, builderMethodName)
                .addModifiers(Modifier.PRIVATE);

        fields.forEach(field -> constructor.addStatement("this.$N = $N.$N", field.name(), builderMethodName, field.name()));
        return constructor.build();
    }

    private MethodSpec createGetterFor(SchemaField<?> field) {
        return GetterMethodFactory.getGetterMethod(field).build();
    }

    private MethodSpec createStaticBuilder(TypeName builderTypeName) {
        return MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addStatement("return new $T()", builderTypeName)
                .returns(builderTypeName)
                .build();
    }

    private MethodSpec createBuildMethod(TypeName classNameType) {
        return MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.build0()")
                .returns(classNameType)
                .build();
    }
}

//JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Builder(setterPrefix = "with", builderClassName = "Builder", buildMethodName = "build0", toBuilder = true)
//@Value
////@Jacksonized
//@Accessors(fluent = true)
//public class Address {
//    @lombok.Builder.Default
//    int houseNumber = 63;
//    @lombok.Builder.Default
//    String roadName = "Rances Lane";
//    @lombok.Builder.Default
//    List<Number> meterReadings = Arrays.asList(16.9, 120.9, 200.64);
//    County county;
//
//    public static class Builder {
//        County.Builder countyBuilder = County.builder();
//
//        public Address build() {
//            return this
//                    .withCounty(countyBuilder.build())
//                    .build0();
//
//        }
//
//        public Address.Builder county(Consumer<County.Builder> countyAction) {
//            countyAction.accept(countyBuilder);
//            return this;
//        }
//    }
//}


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
