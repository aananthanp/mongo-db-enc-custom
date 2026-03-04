package com.tech.java.mongo.demo.enc.repository;


import com.tech.java.mongo.demo.enc.model.FailedMessageDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FailedMessageRepository extends MongoRepository<FailedMessageDoc, String> {
}
