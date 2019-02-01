package com.example;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class SneakyHelloWorldTest {

    @Test
    void throwsException() {
        // given
        val sneaky = new SneakyHelloWorld();

        // when + then
        assertThatExceptionOfType(UnsupportedEncodingException.class)
            .isThrownBy(() -> sneaky.throwingStuff());
    }
}
