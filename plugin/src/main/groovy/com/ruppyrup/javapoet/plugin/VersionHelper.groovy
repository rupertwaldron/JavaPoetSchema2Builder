package com.ruppyrup.javapoet.plugin

import org.apache.commons.io.FileUtils

import java.nio.charset.Charset


class VersionHelper {
    static String getVersion() {
        File projectDir = new File(System.getProperty("user.dir"))
        def generated = projectDir.toPath().resolve("../gradle.properties").toFile()
        def first = FileUtils.readLines(generated, Charset.defaultCharset()).stream()
                .filter {
                    it.contains('projVersion')
                }
                .findFirst()
        .orElse('projVersion=null')
        return first.split('=')[1]
    }
}
