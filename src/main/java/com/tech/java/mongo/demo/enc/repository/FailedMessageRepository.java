package com.tech.java.mongo.demo.enc.repository;


import com.tech.java.mongo.demo.enc.model.FailedMessageDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedMessageRepository extends MongoRepository<FailedMessageDoc, String> {
}
