package com.ruppyrup.javapoet.maker.factories;

import com.ruppyrup.javapoet.maker.models.SchemaField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;

public class WithMethodFactory {
    public MethodSpec.Builder getWithMethod(SchemaField<?> schemaField) {
        String className = StringUtils.capitalize(schemaField.name());
        var builder = MethodSpec.methodBuilder("with" + className)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$N = $N", schemaField.name(), schemaField.name())
                .addStatement("return this");

        if (schemaField.clazz().getName().equals("java.lang.Object")) {
            TypeName childTypeName = ClassName.get("", className);
            builder.addParameter(childTypeName, schemaField.name());
        } else {
            builder.addParameter(schemaField.clazz(), schemaField.name());
        }
        return builder;
    }
}
