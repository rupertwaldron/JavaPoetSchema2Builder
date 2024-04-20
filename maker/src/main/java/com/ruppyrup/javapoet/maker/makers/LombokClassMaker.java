package com.ruppyrup.javapoet.maker.makers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ruppyrup.javapoet.app.SchemaField;
import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.factories.LombokFieldSpecFactory;
import com.ruppyrup.javapoet.maker.factories.WithMethodFactory;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;


public class LombokClassMaker extends AbstractClassMaker {

    public LombokClassMaker(ClassGenerationBuilder classGenerationBuilder) {
        super(classGenerationBuilder);
    }

    @Override
    protected void fieldBuilders() {
        fields.stream()
                .map(LombokFieldSpecFactory::creatFieldSpec)
//                .map(builder -> builder.addModifiers(Modifier.PRIVATE))
                .forEach(fieldSpecBuilders::add);
    }

    @Override
    protected void generateChildObjects() {
        fields.stream()
                .filter(schemaField -> schemaField.clazz().getName().equals("java.lang.Object"))
                .forEach(schemaField -> childObjectMaker.makeChild(schemaField, dir, packageName, "lombok"));
    }

    @Override
    protected TypeSpec generatedClassSpec(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders) {
//        TypeName builderTypeName = ClassName.get("", "Builder1");
        String builderMethodName = className.toLowerCase() + "Builder2";

        AnnotationSpec builderAnnotation = AnnotationSpec.builder(Builder.class)
                .addMember("setterPrefix", "$S", "with")
                .addMember("builderClassName", "$S", "Builder")
                .addMember("buildMethodName", "$S", "build0")
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


        fields.stream()
                .filter(schemaField -> schemaField.clazz().getName().equals("java.lang.Object"))
                .map(schemaField -> {
                    String className = StringUtils.capitalize(schemaField.name());
                    TypeName builderTypeName = ClassName.get("", className + ".Builder");
                    TypeName classTypeName = ClassName.get("", className);

                    CodeBlock initBlock = CodeBlock.builder().add("$1T.builder()", classTypeName).build();
                    return FieldSpec.builder(builderTypeName, schemaField.name())
                            .initializer(initBlock).build();
                })
                .forEach(builderType::addField);

//        fieldSpecBuilders.forEach(fsb -> builderType.addField(fsb.build()));

        return builderType.build();
    }

    private MethodSpec buildersWithMethods(SchemaField<?> schemaField, TypeName builderTypeName) {
        return WithMethodFactory.getWithMethod(schemaField)
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
