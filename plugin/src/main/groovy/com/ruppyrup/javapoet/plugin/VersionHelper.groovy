package com.ruppyrup.javapoet.plugin

class VersionHelper {

    private VersionHelper() {
        throw new IllegalStateException("Cannot instantiate")
    }

    static String getVersion() {
        Properties properties = new Properties()
        try {
            println 'Location of VersionHelper :: ' + VersionHelper.location
            properties.load(VersionHelper.class.getResourceAsStream("/META-INF/plugin.properties"))
            return properties.get("Implementation-Version")
        } catch (IOException e) {
            throw new IllegalStateException("Java Poet Plugin version not found")
        }
    }
}
