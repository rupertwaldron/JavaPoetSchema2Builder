package com.ruppyrup.javapoet.plugin;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VersionHelperTest {

    @Test
    void getVersionReturnsCorrectVersion() {
        assertThat(VersionHelper.getVersion()).isEqualTo("1.0-SNAPSHOT");
    }
}