package com.ruppyrup.javapoet;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
        createJavaFile(getClassType(classNameType, getFieldSpecBuilderList()));
    }

    private List<FieldSpec.Builder> getFieldSpecBuilderList() {
        return fields.stream()
                .map(fieldSpecFactory::creatFieldSpec)
                .map(builder -> builder.addModifiers(Modifier.PUBLIC))
                .toList();
    }

    private TypeSpec getClassType(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders) {
        TypeName builderTypeName = ClassName.get("", className + "Builder");
        String builderMethodName = className.toLowerCase() + "Builder";

        TypeSpec.Builder classTypeSpecBuilder = TypeSpec
                .classBuilder(className)
                .addType(getBuilder(classNameType, fieldSpecBuilders, builderTypeName))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(createConstructor(builderTypeName, builderMethodName))
                .addMethod(createStaticBuilder(builderTypeName, builderMethodName));

        fieldSpecBuilders.forEach(fsb -> classTypeSpecBuilder.addField(fsb.build()));
        return classTypeSpecBuilder.build();
    }

    private TypeSpec getBuilder(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders, TypeName builderTypeName) {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className + "Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addMethod(createBuildMethod(classNameType));

        fields.forEach(field -> typeSpecBuilder.addMethod(getFieldSetterForField(field, builderTypeName)));
        fieldSpecBuilders.forEach(fsb -> typeSpecBuilder.addField(fsb.build()));

        return typeSpecBuilder.build();
    }

    private MethodSpec getFieldSetterForField(SchemaField<?> schemaField, TypeName builderTypeName) {
        return MethodSpec.methodBuilder("with" + StringUtils.capitalize(schemaField.name()))
                .addParameter(schemaField.clazz(), schemaField.name())
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$N = $N", schemaField.name(), schemaField.name())
                .addStatement("return this")
                .returns(builderTypeName)
                .build();
    }

    private void createJavaFile(TypeSpec classTypeSpec) throws IOException {
        JavaFile file = JavaFile.builder(packageName, classTypeSpec).build();

        file.writeTo(System.out);
        file.writeTo(new File("build/generated"));
    }

    private MethodSpec createConstructor(TypeName builderTypeName, String builderMethodName) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addParameter(builderTypeName, builderMethodName)
                .addModifiers(Modifier.PRIVATE);

        fields.forEach(field -> constructorBuilder.addStatement("this.$N = $N.$N", field.name(), builderMethodName, field.name()));
        return constructorBuilder.build();
    }

    private MethodSpec createStaticBuilder(TypeName builderTypeName, String builderMethodName) {
        return MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .addStatement("return new $T()", builderTypeName)
                .returns(builderTypeName)
                .build();
    }

    private MethodSpec createBuildMethod(TypeName classNameType) {
        return MethodSpec.methodBuilder("build")
                .addModifiers( Modifier.PUBLIC)
                .addStatement("return new $T(this)", classNameType)
                .returns(classNameType)
                .build();
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
