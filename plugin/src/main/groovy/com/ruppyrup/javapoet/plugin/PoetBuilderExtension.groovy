package com.ruppyrup.javapoet.plugin

import org.gradle.api.provider.Property

interface PoetBuilderExtension {
    Property<String> getSchemaDir()
    Property<String> getOutputDir()
    Property<String> getDefaultKey()
}