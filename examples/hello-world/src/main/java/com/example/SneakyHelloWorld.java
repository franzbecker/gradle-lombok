package com.example;

import lombok.SneakyThrows;

public class SneakyHelloWorld {

    @SneakyThrows()
    public byte[] throwingStuff() {
        return "test".getBytes("unsupported");
    }

}
