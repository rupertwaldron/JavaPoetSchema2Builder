package com.ruppyrup.javapoet.maker.factories;

import com.ruppyrup.javapoet.maker.models.SchemaField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;

public class GetterMethodFactory {
    public MethodSpec.Builder getGetterMethod(SchemaField<?> schemaField) {
        String className = StringUtils.capitalize(schemaField.name());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + className)
                .addModifiers(Modifier.PUBLIC);

        if (schemaField.clazz().getName().equals("java.lang.Object")) {
            TypeName childTypeName = ClassName.get("", className);
            builder.addStatement("return this.$N", schemaField.name())
                    .returns(childTypeName);
        } else {
            builder.addStatement("return this.$N", schemaField.name())
                    .returns(schemaField.clazz());
        }
        return builder;
    }
}
