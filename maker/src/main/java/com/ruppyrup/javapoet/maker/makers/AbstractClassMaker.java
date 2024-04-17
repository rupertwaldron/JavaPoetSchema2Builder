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

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClassMaker implements ClassMaker {
    protected final String dir;
    protected final String packageName;
    protected final String className;
    protected final List<SchemaField<?>> fields;
    protected final ChildObjectMaker childObjectMaker;
    List<FieldSpec.Builder> fieldSpecBuilders = new ArrayList<>();

    public AbstractClassMaker(ClassGenerationBuilder classGenerationBuilder) {
        this.dir = classGenerationBuilder.getDir();
        this.packageName = classGenerationBuilder.getPackageName();
        this.className = classGenerationBuilder.getClassName();
        this.fields = classGenerationBuilder.getFields();
        this.childObjectMaker = new ChildObjectMaker();
    }

    @Override
    public void makeBuilder() throws IOException {
        TypeName generatedClass = ClassName.get("", className);
        fieldBuilders();
        generateChildObjects();
        createJavaFile(generatedClassSpec(generatedClass, fieldSpecBuilders));
    }

    protected abstract void generateChildObjects();

    protected abstract void fieldBuilders();

    protected abstract TypeSpec generatedClassSpec(TypeName classNameType, List<FieldSpec.Builder> fieldSpecBuilders);

    protected void createJavaFile(TypeSpec classTypeSpec) throws IOException {
        JavaFile file = JavaFile.builder(packageName, classTypeSpec).build();

        file.writeTo(System.out);
        file.writeTo(new File(dir));
    }
}
