package com.tech.java.mongo.demo.enc.service;


import com.tech.java.mongo.demo.enc.model.FailedMessageDoc;
import com.tech.java.mongo.demo.enc.repository.FailedMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FailedMessageService {

    private final FailedMessageRepository repository;

    public FailedMessageService(FailedMessageRepository repository) {
        this.repository = repository;
    }

    public FailedMessageDoc saveMessage(FailedMessageDoc doc) {
        return repository.save(doc);
    }

    public List<FailedMessageDoc> getAll() {
        return repository.findAll();
    }

    public FailedMessageDoc getById(String id) {
        return repository.findById(id).orElse(null);
    }
}
