package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.app.SchemaField;
import com.ruppyrup.javapoet.maker.builders.ClassGenerationBuilder;
import com.ruppyrup.javapoet.maker.makers.ClassMaker;
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

class ClassMakerTest {

    private static final String PACKAGE_NAME = "com.ruppyrup.javapoet.generated";
    @TempDir
    File tempdir;

    @Test
    void checkGettersReturnCorrectFieldValues() throws IOException {

        List<SchemaField<?>> postCode = List.of(
                new SchemaField<>("firstPart", String.class, "RG40"),
                new SchemaField<>("secondPart", String.class, "2LG")
        );

        List<SchemaField<?>> countyFields = List.of(
                new SchemaField<>("countyName", String.class, "Berks"),
                new SchemaField<>("postCode", Object.class, postCode)
        );

        ClassGenerationBuilder classGenerationBuilder = ClassGenerationBuilder.builder()
                .withDir(tempdir.getPath())
                .withPackageName(PACKAGE_NAME)
                .withClassName("Address")
                .withField(new SchemaField<>("streetName", String.class, "Rances Lane"))
                .withField(new SchemaField<>("name", String.class, null))
                .withField(new SchemaField<>("yearsInHouse", Number.class, 16.9))
                .withField(new SchemaField<>("meterReadings", Number[].class, new Number[]{16.9, 120.9, 200.64}))
                .withField(new SchemaField<>("houseNumber", Integer.class, 63))
                .withField(new SchemaField<>("family", String[].class, new String[]{"Ben", "Sam", "Joe"}))
                .withField(new SchemaField<>("county", Object.class, countyFields))
                .build();

        ClassMaker classMaker = new ClassMaker(classGenerationBuilder);

        classMaker.makeBuilder();

        File input1 = new File(tempdir + "/com/ruppyrup/javapoet/generated/" + "Address.java");
        File input2 = new File(tempdir + "/com/ruppyrup/javapoet/generated/" + "County.java");
        File input3 = new File(tempdir + "/com/ruppyrup/javapoet/generated/" + "PostCode.java");

        List<File> javaFiles = List.of(input1, input2, input3);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjectsFromFiles(javaFiles);

        compiler.getTask(null, fileManager, null, null, null, files).call();

        fileManager.close();

        try(URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{tempdir.toURI().toURL()})) {
            Class<?> cls = urlClassLoader.loadClass(PACKAGE_NAME + ".Address$AddressBuilder");
            Object builder = cls.getConstructor().newInstance();
            Object createdObject = cls.getMethod("build").invoke(builder);

            assertThatMethodReturns(createdObject, "getHouseNumber", 63);
            assertThatMethodReturns(createdObject, "getName", null);
            assertThatMethodReturns(createdObject, "getStreetName", "Rances Lane");
            assertThatMethodReturns(createdObject, "getYearsInHouse", 16.9);
            assertThatGetterReturnsCorrectType(createdObject, "getCounty", "county");
            assertThatMethodReturnsArray(createdObject, "getFamily", "Ben", "Sam", "Joe");
            assertThatMethodReturnsArray(createdObject, "getMeterReadings", 16.9, 120.9, 200.64);

            Object countyObject = createdObject.getClass().getMethod("getCounty").invoke(createdObject);

            assertThat(countyObject.getClass().getName()).contains("County");

            assertThatMethodReturns(countyObject, "getCountyName", "Berks");

            Object postCodeObject = countyObject.getClass().getMethod("getPostCode").invoke(countyObject);
            assertThat(postCodeObject.getClass().getName()).contains("PostCode");

            assertThatMethodReturns(postCodeObject, "getFirstPart", "RG40");
            assertThatMethodReturns(postCodeObject, "getSecondPart", "2LG");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertThatMethodReturnsArray(Object createdObject, String methodName, Number... expectedResults) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(((Number[]) createdObject.getClass().getMethod(methodName).invoke(createdObject))).contains(expectedResults);
    }
    private static void assertThatMethodReturnsArray(Object createdObject, String methodName, String... expectedResults) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(((String[]) createdObject.getClass().getMethod(methodName).invoke(createdObject))).contains(expectedResults);
    }

    private static void assertThatMethodReturns(Object createdObject, String methodName, Object expectedResult) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(createdObject.getClass().getMethod(methodName).invoke(createdObject)).isEqualTo(expectedResult);
    }

    private static void assertThatGetterReturnsCorrectType(Object createdObject, String methodName, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(createdObject.getClass().getMethod(methodName).invoke(createdObject).getClass().getName().toLowerCase()).isEqualTo(PACKAGE_NAME + "." + type);
    }
}