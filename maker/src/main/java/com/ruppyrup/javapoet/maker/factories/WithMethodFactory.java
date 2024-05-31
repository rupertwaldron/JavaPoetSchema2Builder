package com.ruppyrup.javapoet.maker.factories;

import com.ruppyrup.javapoet.app.FieldType;
import com.ruppyrup.javapoet.app.SchemaField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.List;

public class WithMethodFactory {
    public static MethodSpec.Builder getWithMethod(SchemaField<?> schemaField) {
        String className = StringUtils.capitalize(schemaField.name());
        var builder = MethodSpec.methodBuilder("with" + className)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$N = $N", schemaField.name(), schemaField.name())
                .addStatement("return this");

        if (schemaField.clazz().getName().equals(FieldType.OBJECT.typeIdentifier)) {
            TypeName childTypeName = ClassName.get("", className);
            builder.addParameter(childTypeName, schemaField.name());
        } else if (schemaField.clazz().getName().equals(FieldType.OBJECT_ARRAY.typeIdentifier)) {
            TypeName childTypeName = ClassName.get("", StringUtils.chop(className));
            ClassName list = ClassName.get(List.class);
            ParameterizedTypeName listOfItems = ParameterizedTypeName.get(list, childTypeName);
            builder.addParameter(listOfItems, schemaField.name());
        } else {
            builder.addParameter(schemaField.clazz(), schemaField.name());
        }
        return builder;
    }
}
