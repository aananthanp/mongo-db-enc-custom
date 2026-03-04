package com.tech.java.mongo.demo.enc.controller;

import com.tech.java.mongo.demo.enc.model.FailedMessageDoc;
import com.tech.java.mongo.demo.enc.service.FailedMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class FailedMessageController {
    private final FailedMessageService service;

    public FailedMessageController(FailedMessageService service) {
        this.service = service;
    }
    @PostMapping("/messages")
    public ResponseEntity<?> save(@RequestBody String payload) {
        return ResponseEntity.ok( service.saveMessage(FailedMessageDoc.builder()
                .countryCode("test")
                .messageType("test")
                .payload(payload)
                .logTime(new Date())
                .build()));
    }

    @GetMapping("/{id}")
    public FailedMessageDoc get(@PathVariable String id) {
        return service.getMessage(id);
    }

}
