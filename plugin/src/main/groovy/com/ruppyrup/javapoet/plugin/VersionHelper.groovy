package com.ruppyrup.javapoet.plugin

class VersionHelper {

    private VersionHelper() {
        throw new IllegalStateException("Cannot instantiate")
    }

    static String getVersion() {
        Properties properties = new Properties()
        try {
            println 'Location of VersionHelper :: ' + VersionHelper.location
            def stream = VersionHelper.class.getResourceAsStream("/META-INF/plugin.properties")
            //Todo Fix this version code
            if (stream == null) {
                println 'Default version is 1.2-SNAPSHOT'
                return '1.2-SNAPSHOT'
            } else {
                properties.load(stream)
                return properties.get("Implementation-Version")
            }
        } catch (IOException e) {
            throw new IllegalStateException("Java Poet Plugin version not found")
        }
    }
}
