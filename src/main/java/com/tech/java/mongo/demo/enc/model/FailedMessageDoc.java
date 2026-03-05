package com.tech.java.mongo.demo.enc.model;

import com.tech.java.mongo.demo.enc.annotation.EncryptedField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "failed_message")
public class FailedMessageDoc {

    @Id
    private String id;
    private String messageType;
    private String countryCode;
    @EncryptedField
    private String payload;  // will be encrypted/decrypted automatically
    private String status;
    private Date logTime;

}