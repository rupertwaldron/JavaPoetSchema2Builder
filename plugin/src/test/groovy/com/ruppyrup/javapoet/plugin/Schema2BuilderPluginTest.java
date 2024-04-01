package com.ruppyrup.javapoet.plugin;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Schema2BuilderPluginTest {
    @Test
    void pluginRegistersATask() {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("com.ruppyrup.javapoet.plugin.poetBuilder");

        // Verify the result
        assertNotNull(project.getTasks().findByName("generateBuilders"));
    }
}