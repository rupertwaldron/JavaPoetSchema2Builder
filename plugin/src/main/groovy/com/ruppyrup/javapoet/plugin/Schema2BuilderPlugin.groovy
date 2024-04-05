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
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

class Schema2BuilderPlugin implements Plugin<Project> {
    void apply(Project rootProject) {
        def extension = rootProject.extensions.create('poetBuilder', PoetBuilderExtension)
        extension.getDefaultKey().convention("sample")

        rootProject.getTasks().register('generateBuilders', task ->  {
            task.doLast {

                println "Schema directory is -> ${extension.schemaDir.get()}"

                PoetParser poetParser = new FileParser()
                IDataTree dataTree = new DataTree()
                IGenerator generator = new GeneratorImpl()
                App app = new App(poetParser, dataTree, generator)
                println "Directory is :: " + System.getProperty("user.dir") + extension.schemaDir.get()

                app.run(System.getProperty("user.dir") + "/" + extension.schemaDir.get(), extension.outputDir.get(), extension.defaultKey.get())
            }
        })

        rootProject.afterEvaluate { ignored ->
            configureJava(rootProject, extension)
        }
    }

    private void configureJava(Project rootProject, PoetBuilderExtension extension) {
        for (Project project : rootProject.getAllprojects()) {
            if (project.getPlugins().hasPlugin(JavaPlugin.class)) {
                createLocalTestSourceSet(project, extension)
                setDependencies(project)
            } else {
                project.getPlugins().whenPluginAdded(plugin -> {
                    if (plugin instanceof JavaPlugin) {
                        createLocalTestSourceSet(project, extension);
                    }
                });
            }
        }
    }

    private void setDependencies(Project project) {
        project.afterEvaluate(ignored -> {
            def task = project.tasks.generateBuilders
            task.dependsOn(project.tasks.processResources)
            project.tasks.compileJava.dependsOn(task)
        })
    }


    private void createLocalTestSourceSet(Project project, PoetBuilderExtension extension) {
        project.afterEvaluate(ignored -> {
            JavaPluginExtension javaPlugin = project.getExtensions().getByType(JavaPluginExtension.class)
//            ConfigurationContainer configurations = project.getConfigurations()
            DependencyHandler dependencies = project.getDependencies()

            javaPlugin.getSourceSets().stream()
//                    .filter(sourceSet -> sourceSet.getName().toLowerCase().contains("test"))
                    .forEach(sourceSet -> {

                        sourceSet.java {
                            println "Source directory added -> " + extension.outputDir.get()
                            srcDirs += extension.outputDir.get()
                        }

//                      SourceSet integrationTestSourceSet = javaPlugin.getSourceSets().getByName("integrationTest");
                        String implementation = sourceSet.getImplementationConfigurationName()

                        dependencies.add(implementation, "com.ruppyrup.javapoet:app:1.0-SNAPSHOT")
                        dependencies.add(implementation, "com.ruppyrup.javapoet:maker:1.0-SNAPSHOT")
                        dependencies.add(implementation, "com.ruppyrup.javapoet:parser:1.0-SNAPSHOT")
                        dependencies.add(implementation, "com.ruppyrup.javapoet:schema:1.0-SNAPSHOT")

                        System.out.println("---------------------- " + sourceSet.getName() + " Source Sets -------------------------");
                        sourceSet.getOutput().forEach(System.out::println);
                        System.out.println("---------------------- " + sourceSet.getName() + " Output dir -------------------------");
                        sourceSet.getJava().getSrcDirs().forEach(System.out::println);

                    })
        })
    }
}
