package com.gb02.syumsvc.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class UserController{
    @GetMapping("/user")
    public String postUser() {
        return "OMG HI!";
    }

    @GetMapping("/user/{username}")
    public String patchUser() {
        return "/user/{username} (PATCH)";
    }
    
}