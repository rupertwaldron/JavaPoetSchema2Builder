package com.ruppyrup.javapoet.maker.builders;

import com.ruppyrup.javapoet.maker.models.SchemaField;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ClassGenerationBuilder {
    private final String dir;
    private final String packageName;
    private final String className;
    private final List<SchemaField<?>> fields;

    private ClassGenerationBuilder(GenerationBuilder generationBuilder) {
        this.className = generationBuilder.className;
        this.fields = generationBuilder.fields;
        this.packageName = generationBuilder.packageName;
        this.dir = generationBuilder.dir;
    }

    public static GenerationBuilder builder() {
        return new GenerationBuilder();
    }

    public static class GenerationBuilder {
        private String dir;
        private String packageName;
        private String className;
        private final List<SchemaField<?>> fields = new ArrayList<>();

        public ClassGenerationBuilder build() {
            return new ClassGenerationBuilder(this);
        }

        public GenerationBuilder withPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public GenerationBuilder withClassName(String className) {
            this.className = className;
            return this;
        }

        public GenerationBuilder withField(SchemaField<?> schemaField) {
            this.fields.add(schemaField);
            return this;
        }

        public GenerationBuilder withDir(String dir) {
            this.dir = dir;
            return this;
        }
    }
}