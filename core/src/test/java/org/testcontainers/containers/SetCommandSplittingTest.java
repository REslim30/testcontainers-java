package org.testcontainers.containers;

import org.junit.Test;
import org.testcontainers.TestImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetCommandSplittingTest {
    GenericContainer container = new GenericContainer(TestImages.TINY_IMAGE);
    @Test
    public void splitsEmptyCommandline() {
        container.setCommand("");
        assertThat(container.getCommandParts()).isEmpty();
    }
    @Test
    public void splitsWithSpaces() {
        container.setCommand("echo hello world");
        assertThat(container.getCommandParts()).containsExactly("echo", "hello", "world");
    }

    @Test
    public void splitsWithDoubleQuotes() {
        container.setCommand("echo \"hello world\"");
        assertThat(container.getCommandParts()).containsExactly("echo", "hello world");
    }

    @Test
    public void throwsIllegalArgumentExceptionIfDoubleQuoteIsUnmatched() {
        assertThatThrownBy(() -> container.setCommand("echo \"hello world"))
            .isInstanceOf(IllegalArgumentException.class);
    }
//
//    @Test
//    public void splitsWithSingleQuotes() {
//        container.setCommand("echo 'hello world'");
//        assertThat(container.getCommandParts()).containsExactly("echo", "hello world");
//    }
}
