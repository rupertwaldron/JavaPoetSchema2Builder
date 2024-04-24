/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package com.ruppyrup.javapoet.plugin

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

import java.nio.charset.Charset;


/**
 * A simple functional test for the 'tasktimer.greeting' plugin.
 */
class ClassPathTest extends Specification {
    @TempDir
    private File projectDir

    private getBuildFile() {
        new File(projectDir, "build.gradle")
    }

    private getSettingsFile() {
        new File(projectDir, "settings.gradle")
    }

    private getPropertiesFile() {
        File propsFile = new File(projectDir, '/META-INF/plugin.properties')
        FileUtils.copyFile(new File("src/functionalTest/resources/META-INF/plugin.properties"), propsFile)
    }

    def "can run task"() {
        given:
//        getPropertiesFile()
        settingsFile << ""
        buildFile << """
plugins {
    id 'java-library'
    id 'com.ruppyrup.javapoet.plugin.poetBuilder'
}


println System.getProperty("user.dir")


poetBuilder {
    schemaDir = 'src/functionalTest/resources/schemas'
    outputDir = "${projectDir.getPath()}"
    defaultKey = "sample"
}
"""

        when:
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("generateBuilders")
        runner.withProjectDir(projectDir)
        def result = runner.build()

        def classpath = runner.pluginClasspath.toString()

        def values = runner.metaPropertyValues

        then:
        classpath.contains("app-1.2-SNAPSHOT.jar")
        classpath.contains("parser-1.2-SNAPSHOT.jar")
        classpath.contains("schema-1.2-SNAPSHOT.jar")
        classpath.contains("maker-1.2-SNAPSHOT.jar")
    }
}
