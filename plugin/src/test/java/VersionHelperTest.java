import com.ruppyrup.javapoet.plugin.VersionHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionHelperTest {

    @Test
    void getVersionReturnsCorrectVersion() {
        Assertions.assertThat(VersionHelper.getVersion()).isEqualTo("1.1-SNAPPY");
    }
}