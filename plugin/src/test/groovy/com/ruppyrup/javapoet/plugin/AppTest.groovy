package com.ruppyrup.javapoet.plugin

import com.ruppyrup.javapoet.app.App
import com.ruppyrup.javapoet.app.IDataTree
import com.ruppyrup.javapoet.app.IGenerator
import com.ruppyrup.javapoet.app.PoetParser
import com.ruppyrup.javapoet.maker.GeneratorImpl
import com.ruppyrup.javapoet.parser.FileParser
import com.ruppyrup.javapoet.schema.DataTree
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertNotNull

class AppTest {

    @Test
    void integrationTest() {
        PoetParser poetParser = new FileParser();
        IDataTree dataTree = new DataTree();
        IGenerator generator = new GeneratorImpl();
        App app = new App(poetParser, dataTree, generator);

        String inputDir = System.getProperty('user.dir') + '/' + 'src/test/resources/schemas'
        app.run(inputDir, "src/test/resources", "sample", "standard")

//        app.run('src/test/resources/schemas', 'src/test/resources', 'sample' 'standard')
        assertNotNull(app);
    }
}
