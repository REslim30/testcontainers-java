package org.testcontainers.containers;

import org.junit.Test;
import org.testcontainers.TestImages;
import static org.assertj.core.api.Assertions.assertThat;

public class SetCommandSplittingTest {
    @Test
    public void splitsWithSpaces() {
        GenericContainer genericContainer = new GenericContainer(TestImages.ALPINE_IMAGE);
        genericContainer.setCommand("echo hello world");
        assertThat(genericContainer.getCommandParts()).containsExactly("echo", "hello", "world");
    }

    @Test
    public void splitsWithDoubleQuotes() {
        GenericContainer genericContainer = new GenericContainer(TestImages.ALPINE_IMAGE);
        genericContainer.setCommand("echo \"hello world\"");
        assertThat(genericContainer.getCommandParts()).containsExactly("echo", "hello world");
    }

    @Test
    public void splitsWithSingleQuotes() {
        GenericContainer genericContainer = new GenericContainer(TestImages.ALPINE_IMAGE);
        genericContainer.setCommand("echo 'hello world'");
        assertThat(genericContainer.getCommandParts()).containsExactly("echo", "hello world");
    }
}
