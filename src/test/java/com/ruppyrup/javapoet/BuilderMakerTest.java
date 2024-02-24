package com.ruppyrup.javapoet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BuilderMakerTest {

    @TempDir
    File tempdir;

    @Test
    void useBuilderToMakeBuilder() throws IOException {
        BuilderMaker builderMaker = BuilderMaker.builder()
                .withDir(tempdir.getPath())
                .withPackageName("com.ruppyrup.javapoet.generated")
                .withClassName("Address")
                .withField(new SchemaField<>("streetName", String.class, "Rances Lane"))
                .withField(new SchemaField<>("name", String.class, null))
                .withField(new SchemaField<>("houseNumber", Integer.class, 63))
                .withField(new SchemaField<>("family", String[].class, new String[]{"Ben", "Sam", "Joe"}))
                .build();
        builderMaker.makeBuilder();

        File input = new File(tempdir + "/com/ruppyrup/javapoet/generated/" + "Address.java");
        Assertions.assertTrue(input.exists());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjectsFromFiles(List.of(input));

        compiler.getTask(null, fileManager, null, null, null, files).call();

        fileManager.close();

        try(URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{tempdir.toURI().toURL()})) {
            Class<?> cls = urlClassLoader.loadClass("com.ruppyrup.javapoet.generated.Address$AddressBuilder");
            Object builder = cls.getConstructor().newInstance();
            Object createdObject = cls.getMethod("build").invoke(builder);
            assertThatFieldIsSet(createdObject, "streetName", "Rances Lane");
            assertThatFieldIsSet(createdObject, "houseNumber", 63);
            assertThatFieldIsSet(createdObject, "family", new String[]{"Ben", "Sam", "Joe"});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertThatFieldIsSet(Object createdObject, String fieldName, String expectedResult) throws IllegalAccessException, NoSuchFieldException {
        assertThat(createdObject.getClass().getField(fieldName).get(createdObject)).isEqualTo(expectedResult);
    }

    private static void assertThatFieldIsSet(Object createdObject, String fieldName, Integer expectedResult) throws IllegalAccessException, NoSuchFieldException {
        assertThat(createdObject.getClass().getField(fieldName).get(createdObject)).isEqualTo(expectedResult);
    }

    private static void assertThatFieldIsSet(Object createdObject, String fieldName, String[] expectedResult) throws IllegalAccessException, NoSuchFieldException {
        assertThat(createdObject.getClass().getField(fieldName).get(createdObject)).isEqualTo(expectedResult);
    }
}