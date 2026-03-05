package com.tech.java.mongo.demo.enc.config;

import com.tech.java.mongo.demo.enc.annotation.EncryptedField;
import com.tech.java.mongo.demo.enc.model.FailedMessageDoc;
import com.tech.java.mongo.demo.enc.util.GenericCryptoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;

@Configuration
public class MongoEncryptionListener extends AbstractMongoEventListener<FailedMessageDoc> {

    @Value("${app.encryption.secret}")
    private String secret;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<FailedMessageDoc> event) {
        System.out.println("Encryption listener triggered");
        processFields(event.getSource(), true);
    }

    @Override
    public void onAfterConvert(AfterConvertEvent<FailedMessageDoc> event) {
        System.out.println("Decrypt listener triggered");
        processFields(event.getSource(), false);
    }

    private void processFields(Object source, boolean encrypt) {

        ReflectionUtils.doWithFields(source.getClass(), field -> {

            if (field.isAnnotationPresent(EncryptedField.class)) {

                field.setAccessible(true);
                Object value = field.get(source);

                if (value instanceof String strValue) {

                    String processed = encrypt
                            ? GenericCryptoUtil.encrypt(strValue, secret)
                            : GenericCryptoUtil.decrypt(strValue, secret);

                    field.set(source, processed);
                }
            }

        });
    }
}