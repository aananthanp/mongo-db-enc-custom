package com.tech.java.mongo.demo.enc.config;

import com.tech.java.mongo.demo.enc.model.FailedMessageDoc;
import com.tech.java.mongo.demo.enc.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

@Configuration
public class MongoEncryptionListener extends AbstractMongoEventListener<FailedMessageDoc> {

    @Value("${app.encryption.secret}")
    private String secret;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<FailedMessageDoc> event) {
        FailedMessageDoc doc = event.getSource();
        if (doc.getPayload() != null) {
            doc.setPayload(CryptoUtil.encrypt(doc.getPayload(), secret));
        }
    }

    @Override
    public void onAfterConvert(AfterConvertEvent<FailedMessageDoc> event) {
        FailedMessageDoc doc = event.getSource();
        if (doc.getPayload() != null) {
            doc.setPayload(CryptoUtil.decrypt(doc.getPayload(), secret));
        }
    }
}