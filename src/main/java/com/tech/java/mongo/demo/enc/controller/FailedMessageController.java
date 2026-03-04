package com.tech.java.mongo.demo.enc.controller;

import com.tech.java.mongo.demo.enc.model.FailedMessageDoc;
import com.tech.java.mongo.demo.enc.service.FailedMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class FailedMessageController {

    @Autowired
    private FailedMessageService service;

    @PostMapping("/writeMessages")
    public ResponseEntity<?> save(@RequestParam String countryCode, @RequestParam String messageType, @RequestBody String payload) {
        return ResponseEntity.ok( service.saveMessage(FailedMessageDoc.builder()
                .countryCode(countryCode)
                .messageType(messageType)
                .payload(payload)
                .logTime(new Date())
                .status("INSERTED")
                .build()));
    }

    @GetMapping("/getData/{id}")
    public FailedMessageDoc get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot with Swagger!";
    }

    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name) {
        return "Hello " + name + "!";
    }
}
