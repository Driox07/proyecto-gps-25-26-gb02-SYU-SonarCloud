package com.gb02.syumsvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gb02.syumsvc.utils.Response;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<Map<String, Object>> handleNotFound(NoResourceFoundException ex) {
                return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.getErrorResponse(404, "Endpoint not found."));
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<Map<String, String>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
                return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of(
                        "code", "405",
                        "message", "The HTTP method used is not supported for this endpoint."
                ));
        }

        @ExceptionHandler(ServletRequestBindingException.class)
                public ResponseEntity<Map<String, Object>> handleMissingCookie(ServletRequestBindingException e) {
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.getOnlyMessage("Oversound auth token required"));
        }
}