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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BuilderMakerTest {

    private static final String PACKAGE_NAME = "com.ruppyrup.javapoet.generated";
    @TempDir
    File tempdir;


    @Test
    void checkGettersReturnCorrectFieldValues() throws IOException {
        List<SchemaField<?>> countyFields = List.of(
                new SchemaField<>("countyName", String.class, "Berks"),
                new SchemaField<>("postCode", String.class, "RG40 2LG")
        );

        BuilderMaker builderMaker = BuilderMaker.builder()
                .withDir(tempdir.getPath())
                .withPackageName(PACKAGE_NAME)
                .withClassName("Address")
                .withField(new SchemaField<>("streetName", String.class, "Rances Lane"))
                .withField(new SchemaField<>("name", String.class, null))
                .withField(new SchemaField<>("houseNumber", Integer.class, 63))
                .withField(new SchemaField<>("family", String[].class, new String[]{"Ben", "Sam", "Joe"}))
                .withField(new SchemaField<>("county", Object.class, countyFields))
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
            Class<?> cls = urlClassLoader.loadClass(PACKAGE_NAME + ".Address$AddressBuilder");
            Object builder = cls.getConstructor().newInstance();
            Object createdObject = cls.getMethod("build").invoke(builder);

            assertThatMethodReturns(createdObject, "getHouseNumber", 63);
            assertThatMethodReturns(createdObject, "getName", null);
            assertThatMethodReturns(createdObject, "getStreetName", "Rances Lane");
            assertThatGetterReturnsCorrectType(createdObject, "getCounty", "county");

            Object childObject = createdObject.getClass().getMethod("getCounty").invoke(createdObject);

            assertThatMethodReturns(childObject, "getPostCode", "RG40 2LG");
            assertThatMethodReturns(childObject, "getCountyName", "Berks");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertThatMethodReturns(Object createdObject, String methodName, Object expectedResult) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(createdObject.getClass().getMethod(methodName).invoke(createdObject)).isEqualTo(expectedResult);
    }

    private static void assertThatGetterReturnsCorrectType(Object createdObject, String methodName, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(createdObject.getClass().getMethod(methodName).invoke(createdObject).getClass().getName().toLowerCase()).isEqualTo(PACKAGE_NAME + "." + type);
    }
}