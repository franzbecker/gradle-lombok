package com.example;

import lombok.val;

public class HelloWorld {

    public static void main(String[] args) {
        val greeting = new Greeting();
        greeting.setMessage("Hello world!");
        System.out.println(greeting.getMessage());
    }

}
