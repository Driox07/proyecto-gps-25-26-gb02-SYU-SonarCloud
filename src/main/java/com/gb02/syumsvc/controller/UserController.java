package com.gb02.syumsvc.controller;

import org.springframework.web.bind.annotation.RestController;

import com.gb02.syumsvc.model.dao.postgresql.PostgresqlConnector;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class UserController{
    @GetMapping("/user")
    public String postUser() {
        return "OMG HI!";
    }

    @GetMapping("/user/{username}")
    public String patchUser() {
        PostgresqlConnector.connect();
        return "/user/{username} (PATCH)";
    }
    
}