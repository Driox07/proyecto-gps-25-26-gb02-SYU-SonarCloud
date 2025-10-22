package com.gb02.syumsvc.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class Controller {
    @GetMapping("/")
    public String home() {
        return "SYU MSVC - GB02";
    }
    
}
