package com.ruppyrup.javapoet.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class Schema2BuilderPlugin implements Plugin<Project> {
    void apply(Project target) {
        // Register a task
        target.getTasks().register("greeting", task -> {
            task.doLast(s -> System.out.println("Hello from plugin 'com.ruppyrup.javapoet.plugin.greeting'"));
        });

        def extension = target.extensions.create('poetBuilder', PoetBuilderExtension)

        target.getTasks().register('generateBuilders') {
            doLast {
                println "Schema directory is -> ${extension.schemaDir.get()}"
            }
        }

    }
}
