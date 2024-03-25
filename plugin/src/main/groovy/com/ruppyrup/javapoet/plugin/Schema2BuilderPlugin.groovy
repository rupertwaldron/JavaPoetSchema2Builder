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
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

class Schema2BuilderPlugin implements Plugin<Project> {
    void apply(Project rootProject) {
        // Register a task
        rootProject.getTasks().register("greeting", task -> {
            task.doLast(s -> System.out.println("Hello from plugin 'com.ruppyrup.javapoet.plugin.greeting'"));
        });

        def extension = rootProject.extensions.create('poetBuilder', PoetBuilderExtension)

        rootProject.getTasks().register('generateBuilders') {
            doLast {
                println "Schema directory is -> ${extension.schemaDir.get()}"

                PoetParser poetParser = new FileParser()
                IDataTree dataTree = new DataTree()
                IGenerator generator = new GeneratorImpl()
                App app = new App(poetParser, dataTree, generator)
                app.run(System.getProperty("user.dir") + extension.schemaDir.get(), extension.outputDir.get())
            }
        }

        for (Project project : rootProject.getAllprojects()) {
            if (project.getPlugins().hasPlugin(JavaPlugin.class)) {
                createLocalTestSourceSet(project);
            } else {
                project.getPlugins().whenPluginAdded(plugin -> {
                    if (plugin instanceof JavaPlugin) {
                        createLocalTestSourceSet(project);
                    }
                });
            }
        }

    }


    private void createLocalTestSourceSet(Project project) {
        project.afterEvaluate(ignored -> {
            JavaPluginExtension javaPlugin = project.getExtensions().getByType(JavaPluginExtension.class);
//            ConfigurationContainer configurations = project.getConfigurations();
            DependencyHandler dependencies = project.getDependencies();

            javaPlugin.getSourceSets().stream()
//                    .filter(sourceSet -> sourceSet.getName().toLowerCase().contains("test"))
                    .forEach(sourceSet -> {
//                      SourceSet integrationTestSourceSet = javaPlugin.getSourceSets().getByName("integrationTest");
                        String implementation = sourceSet.getImplementationConfigurationName();

                        dependencies.add(implementation, "com.ruppyrup.javapoet:app:1.0-SNAPSHOT")
                        dependencies.add(implementation, "com.ruppyrup.javapoet:maker:1.0-SNAPSHOT")
                        dependencies.add(implementation, "com.ruppyrup.javapoet:parser:1.0-SNAPSHOT")
                        dependencies.add(implementation, "com.ruppyrup.javapoet:schema:1.0-SNAPSHOT")

                        System.out.println("---------------------- " + sourceSet.getName() + " Source Sets -------------------------");
                        sourceSet.getOutput().forEach(System.out::println);
                        System.out.println("---------------------- " + sourceSet.getName() + " Output dir -------------------------");
                        sourceSet.getJava().getSrcDirs().forEach(System.out::println);

                    });

        });
    }
}
