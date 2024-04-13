package com.ruppyrup.javapoet.maker.makers;

import com.ruppyrup.javapoet.app.SchemaField;
import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.factories.FieldSpecFactory;
import com.ruppyrup.javapoet.maker.factories.GetterMethodFactory;
import com.ruppyrup.javapoet.maker.factories.WithMethodFactory;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Builder;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StandardClassMaker implements ClassMaker {
    private final String dir;
    private final String packageName;
    private final String className;
    private final List<SchemaField<?>> fields;
    private final FieldSpecFactory fieldSpecFactory;
    private final GetterMethodFactory getterMethodFactory;
    private final ChildObjectMaker childObjectMaker;
    private final WithMethodFactory withMethodFactory;
    List<FieldSpec.Builder> fieldSpecBuilders = new ArrayList<>();

    public StandardClassMaker(ClassGenerationBuilder classGenerationBuilder) {
        this.className = classGenerationBuilder.getClassName();
        this.fields = classGenerationBuilder.getFields();
        this.packageName = classGenerationBuilder.getPackageName();
        this.dir = classGenerationBuilder.getDir();
        this.childObjectMaker = new ChildObjectMaker();
        this.fieldSpecFactory = new FieldSpecFactory();
        this.getterMethodFactory = new GetterMethodFactory();
        this.withMethodFactory = new WithMethodFactory();
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
                .forEach(schemaField -> childObjectMaker.makeChild(schemaField, dir, packageName));
    }

    private void fieldBuilders() {
        fields.stream()
                .map(fieldSpecFactory::creatFieldSpec)
                .map(builder -> builder.addModifiers(Modifier.PRIVATE))
                .forEach(fieldSpecBuilders::add);
    }

    private TypeSpec generatedClassSpec(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders) {
        TypeName builderTypeName = ClassName.get("", className + "Builder");
        String builderMethodName = className.toLowerCase() + "Builder";

        TypeSpec.Builder classTypeSpecBuilder = TypeSpec
                .classBuilder(className)
                .addType(builderForGeneratedClass(classNameType, fieldSpecBuilders, builderTypeName))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(createConstructor(builderTypeName, builderMethodName))
                .addMethod(createStaticBuilder(builderTypeName));

        fieldSpecBuilders.forEach(fsb -> classTypeSpecBuilder.addField(fsb.build()));
        fields.forEach(field -> classTypeSpecBuilder.addMethod(createGetterFor(field)));
        return classTypeSpecBuilder.build();
    }

    private TypeSpec builderForGeneratedClass(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders, TypeName builderTypeName) {
        TypeSpec.Builder builderType = TypeSpec.classBuilder(className + "Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addMethod(createBuildMethod(classNameType));

        fields.forEach(field -> builderType.addMethod(buildersWithMethods(field, builderTypeName)));
        fieldSpecBuilders.forEach(fsb -> builderType.addField(fsb.build()));

        return builderType.build();
    }

    private MethodSpec buildersWithMethods(SchemaField<?> schemaField, TypeName builderTypeName) {
        return withMethodFactory.getWithMethod(schemaField)
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
        return getterMethodFactory.getGetterMethod(field).build();
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
                .addStatement("return new $T(this)", classNameType)
                .returns(classNameType)
                .build();
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
