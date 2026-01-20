package com.example.helloworld.controller;

import com.example.helloworld.model.Greeting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Greeting", description = "Greeting API endpoints")
public class GreetingController {

    @GetMapping("/hello")
    @Operation(summary = "Get greeting message", description = "Returns a greeting message")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Greeting.class)))
    public Greeting hello(
            @RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting("Hello, " + name + "!");
    }

    @GetMapping("/greeting")
    @Operation(summary = "Get custom greeting", description = "Returns a custom greeting message for a person")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Greeting.class)))
    public Greeting greeting(
            @RequestParam(value = "firstName", defaultValue = "World") String firstName) {
        return new Greeting("Greetings, " + firstName + "!");
    }
}
