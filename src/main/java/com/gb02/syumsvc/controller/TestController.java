package com.gb02.syumsvc.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class TestController{
    @GetMapping("/")
    public String home() {
        return "OMG HI!";
    }
    
}