package com.ruppyrup.javapoet.plugin

import com.ruppyrup.javapoet.app.App
import com.ruppyrup.javapoet.app.IDataTree
import com.ruppyrup.javapoet.app.IGenerator
import com.ruppyrup.javapoet.app.PoetParser
import com.ruppyrup.javapoet.maker.GeneratorImpl
import com.ruppyrup.javapoet.parser.FileParser
import com.ruppyrup.javapoet.schema.DataTree
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

                PoetParser poetParser = new FileParser()
                IDataTree dataTree = new DataTree()
                IGenerator generator = new GeneratorImpl()
                App app = new App(poetParser, dataTree, generator)
                app.run(System.getProperty("user.dir") + extension.schemaDir.get(), extension.outputDir.get())
            }
        }

    }
}
