import com.ruppyrup.javapoet.app.App;
import com.ruppyrup.javapoet.app.IDataTree;
import com.ruppyrup.javapoet.app.IGenerator;
import com.ruppyrup.javapoet.app.PoetParser;
import com.ruppyrup.javapoet.maker.GeneratorImpl;
import com.ruppyrup.javapoet.parser.FileParser;
import com.ruppyrup.javapoet.schema.DataTree;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppTest {

    @ParameterizedTest
    @CsvSource({"standard", "lombok"})
    void integrationTestForStandardBuilder(String builderType) {
        PoetParser poetParser = new FileParser();
        IDataTree dataTree = new DataTree();
        IGenerator generator = new GeneratorImpl();
        App app = new App(poetParser, dataTree, generator);

        String inputDir = System.getProperty("user.dir") + "/" + "src/test/resources/schemas";
        app.run(inputDir, "build/generated/test" + builderType, "sample", builderType);

        assertThat(new File("build/generated/test" + builderType + "/com/ruppyrup/javapoet/schemas").list())
                .containsExactlyInAnyOrder("Country.java", "Address.java", "TopObject.java", "PostalCode.java", "Book.java");
    }
}
