package com.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GreetingTest {

    @Test
    void canSetMessage() {
        // given
        Greeting greeting = new Greeting();

        // when
        greeting.setMessage("test");

        // then
        assertThat(greeting.getMessage()).isEqualTo("test");
    }
}