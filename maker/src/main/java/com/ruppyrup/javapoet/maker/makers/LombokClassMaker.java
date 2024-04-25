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
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


public class LombokClassMaker extends AbstractClassMaker {

    public LombokClassMaker(ClassGenerationBuilder classGenerationBuilder) {
        super(classGenerationBuilder);
    }

    @Override
    protected void fieldBuilders() {
        fields.stream()
                .map(LombokFieldSpecFactory::creatFieldSpec)
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
        TypeName builderTypeName = ClassName.get("", "Builder");

        AnnotationSpec builderAnnotation = AnnotationSpec.builder(Builder.class)
                .addMember("setterPrefix", "$S", "with")
                .addMember("builderClassName", "$S", "Builder")
                .addMember("buildMethodName", "$S", "build0")
                .addMember("toBuilder", "$L", Boolean.TRUE)
                .build();

        AnnotationSpec fluentAnnotation = AnnotationSpec.builder(Accessors.class)
                .addMember("fluent", "$L", Boolean.TRUE)
                .build();

        AnnotationSpec jacksonAutoInclude = AnnotationSpec.builder(JsonAutoDetect.class)
                .addMember("fieldVisibility", "$T.ANY", JsonAutoDetect.Visibility.ANY.getClass())
                .build();

        TypeSpec.Builder classTypeSpecBuilder = TypeSpec
                .classBuilder(className)
                //todo Add Jackson Annotations
//                .addAnnotation(jacksonAutoInclude)
                .addAnnotation(builderAnnotation)
                .addAnnotation(fluentAnnotation)
                .addAnnotation(Value.class)
                .addType(builderForGeneratedClass(classNameType, fieldSpecBuilders, builderTypeName))
                .addModifiers(Modifier.PUBLIC);

        fieldSpecBuilders.forEach(fsb -> classTypeSpecBuilder.addField(fsb.build()));

        return classTypeSpecBuilder.build();
    }

    private TypeSpec builderForGeneratedClass(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders, TypeName builderTypeName) {
        TypeSpec.Builder builderType = TypeSpec.classBuilder("Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addMethod(createBuildMethod(classNameType));


        fields.stream()
                .filter(schemaField -> schemaField.clazz().getName().equals("java.lang.Object"))
                .map(schemaField -> {
                    String className = StringUtils.capitalize(schemaField.name());
                    TypeName builderTN = ClassName.get("", className + ".Builder");
                    TypeName classTypeName = ClassName.get("", className);

                    CodeBlock initBlock = CodeBlock.builder().add("$1T.builder()", classTypeName).build();
                    return FieldSpec.builder(builderTN, schemaField.name() + "Builder")
                            .initializer(initBlock).build();
                })
                .forEach(builderType::addField);

        fields.stream()
                .filter(schemaField -> schemaField.clazz().getName().equals("java.lang.Object"))
                .forEach(schemaField -> {
                    String className = StringUtils.capitalize(schemaField.name());
                    TypeName childBuilder = ClassName.get("", className + ".Builder");
                    TypeName classTypeName = ClassName.get("", className);
                    builderType.addMethod(buildersWithMethods(schemaField, builderTypeName, childBuilder));
                });

        return builderType.build();
    }

    private MethodSpec buildersWithMethods(SchemaField<?> fieldSpec, TypeName builderTypeName, TypeName childBuilder) {
        ClassName consumer = ClassName.get(Consumer.class);
        ParameterizedTypeName consumerArg = ParameterizedTypeName.get(consumer, childBuilder);
        return MethodSpec.methodBuilder(fieldSpec.name)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(consumerArg, fieldSpec.name + "Action")
                .addStatement("$1NAction.accept($2N)", fieldSpec.name, fieldSpec.name + "Builder")
                .addStatement("return this")
                .returns(builderTypeName)
                .build();
    }

    private MethodSpec createBuildMethod(TypeName classNameType) {
        CodeBlock.Builder builderMethodBlock = CodeBlock.builder()
                .add("return this\n");

        fields.stream()
                .filter(schemaField -> schemaField.clazz().getName().equals("java.lang.Object"))
                .map(schemaField -> {
                    String className = StringUtils.capitalize(schemaField.name());
                    TypeName builderTypeName = ClassName.get("", className + ".Builder");
                    TypeName classTypeName = ClassName.get("", className);

                    return CodeBlock.builder().add(".with$1T($2LBuilder.build())", classTypeName, schemaField.name)
                            .add("\n")
                            .build();
                })
                .forEach(builderMethodBlock::add);

                builderMethodBlock.add(".build0()");
        return MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addStatement(builderMethodBlock.build())
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