package com.microservice.users.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
//@PreAuthorize("denyAll()") // por defecto, nadie accede
public class TestAuthController {

    @GetMapping("/get")
    public String helloGet(){
        return "Hello world - GET (READ permitido)";
    }

    @PostMapping("/post")
    public String helloPost(){
        return "Hello world - POST (CREATE permitido)";
    }

    @PutMapping("/put")
    public String helloPut(){
        return "Hello world - PUT (UPDATE permitido)";
    }

    @DeleteMapping("/delete")
    public String helloDelete(){
        return "Hello world - DELETE (DELETE permitido)";
    }

    @PatchMapping("/patch")
    public String helloPatch(){
        return "Hello world - PATCH (UPDATE o READ permitido)";
    }
}
