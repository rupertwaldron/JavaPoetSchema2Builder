package com.ruppyrup.javapoet;

import com.ruppyrup.javapoet.demo.CreateClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;


public class CreateClassTest {

    @TempDir
    File tempdir;

    @Test
    void canFindClass() throws IOException {
        CreateClass.create(tempdir.getPath());
        File input = new File(tempdir + "/com/ruppyrup/javapoet/generated/" + "Person.java");
        Assertions.assertTrue(input.exists());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjectsFromFiles(List.of(input));

        compiler.getTask(null, fileManager, null, null, null, files).call();

        fileManager.close();

        try(URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{tempdir.toURI().toURL()})) {
            Class<?> cls = urlClassLoader.loadClass("com.ruppyrup.javapoet.generated.Person");
            Object o = cls.getConstructor().newInstance();
            Method setName = cls.getMethod("setName", String.class);
            setName.invoke(o, "Rupert");

            Method getName = cls.getMethod("getName");

            org.assertj.core.api.Assertions.assertThat(getName.invoke(o)).isEqualTo("Rupert");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
