package com.ltech.test.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return  name + ", this is a test api";
    }

}
