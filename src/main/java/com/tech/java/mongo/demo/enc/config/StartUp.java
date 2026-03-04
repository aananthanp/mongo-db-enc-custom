package com.tech.java.mongo.demo.enc.config;

import com.tech.java.mongo.demo.enc.model.FailedMessageDoc;
import com.tech.java.mongo.demo.enc.repository.FailedMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.Date;


public class StartUp implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private FailedMessageRepository failedMessageRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("StartUp...");
        FailedMessageDoc messageDoc = FailedMessageDoc.builder()
                .id(null)
                .logTime(new Date())
                .payload("sample payload")
                .countryCode("test")
                .messageType("test")
                .status("INSERTED")
                .build();
        System.out.println("Insert status :" + failedMessageRepository.insert(messageDoc));

    }
}
