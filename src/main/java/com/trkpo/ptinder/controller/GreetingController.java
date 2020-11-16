package com.trkpo.ptinder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("greeting")
public class GreetingController {

    List<String> messages = Arrays.asList("Hello, ", "everyone!", ":)");

    @GetMapping
    public List<String> list() {
        return messages;
    }
}
