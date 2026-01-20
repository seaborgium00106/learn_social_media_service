package com.example.helloworld.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Greeting response model")
public class Greeting {

    @Schema(description = "The greeting message", example = "Hello, World!")
    private String message;

    public Greeting() {
    }

    public Greeting(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
