package com.tech.java.mongo.demo.enc.config;


import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.internal.MongoClientImpl;
import com.tech.java.mongo.demo.enc.repository.FailedMessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMongoRepositories(basePackages = "com.tech.java.mongo.demo.enc.*")
public class MongoConfig {

    @Value("${app.data.mongodb.uri}")
    private String URLString;

    @Value("${app.data.mongodb.tls.enabled:false}")
    private boolean isTlsEnabled;

    @Value("${app.data.mongodb.keystore.cert:NONE}")
    private String keyStoreCert;
    @Value("${app.data.mongodb.keystore.type:}")
    private String keyStoreType;
    @Value("${app.data.mongodb.keystore.password:}")
    private String keyStorePassword;
    @Value("${app.data.mongodb.truststore.cert:NONE}")
    private String trustStoreCert;
    @Value("${app.data.mongodb.truststore.type:}")
    private String trustStoreType;
    @Value("${app.data.mongodb.truststore.password:}")
    private String trustStorePassword;
    @Value("${app.data.mongodb.database}")
    private String database;
    @Bean
    public MongoClient mongoClient() throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        MongoClientSettings settings = addMongoCertificate(MongoClientSettings.builder())
                .applyConnectionString(new ConnectionString((URLString)))
                .applyToClusterSettings( builder -> {
                    builder.applyConnectionString((new ConnectionString((URLString))));
                    builder.serverSelectionTimeout(30000, TimeUnit.MILLISECONDS);
                    builder.localThreshold(15, TimeUnit.MILLISECONDS);
                })
                .readPreference(ReadPreference.primary())
                .readConcern(new ReadConcern((ReadConcernLevel.MAJORITY)))
                .writeConcern(WriteConcern.MAJORITY
                        .withWTimeout(30000, TimeUnit.MILLISECONDS)
                        .withJournal(false))
                .retryReads(true)
                .retryWrites(true)
                .applyToConnectionPoolSettings(pool ->{
                    pool.minSize(1).maxSize(5).build();
                })
                .applyToServerSettings(setting -> {
                    setting.minHeartbeatFrequency(500, TimeUnit.MILLISECONDS);
                    setting.heartbeatFrequency(10000, TimeUnit.MILLISECONDS);
                }).build();
        var mongoDriverInformation = MongoDriverInformation.builder().build();
        /*return MongoClients.create("mongodb://localhost:27017");*/
        return new MongoClientImpl(settings, mongoDriverInformation);
    }

    private MongoClientSettings.Builder addMongoCertificate(MongoClientSettings.Builder mongoBuilder) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        if(isTlsEnabled) {
            var sslContext = SSLContext.getInstance("TLSv1.2");
            KeyManager[] keyManager = null;

            var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStoreLocal;
            if(keyStoreType.isEmpty()) {
                keyStoreLocal = KeyStore.getInstance(keyStoreType);
            } else {
                keyStoreLocal = KeyStore.getInstance(KeyStore.getDefaultType());
            }
            if(StringUtils.hasText(keyStoreCert) && !"NONE".equalsIgnoreCase(keyStoreCert) ) {
                InputStream inputStream = null;
                if (keyStoreCert.startsWith("classpath:")) {
                    inputStream = this.getClass().getClassLoader().getResourceAsStream(keyStoreCert);
                } else {
                    inputStream = new FileInputStream(keyStoreCert);
                }
                keyStoreLocal.load(inputStream, keyStorePassword.toCharArray());
                keyManagerFactory.init(keyStoreLocal, keyStorePassword.toCharArray());
                keyManager = keyManagerFactory.getKeyManagers();
            }

            TrustManager[] trustManagers = null;
            var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore trustStoreLocal;
            if(trustStoreType.isEmpty()) {
                trustStoreLocal = KeyStore.getInstance(trustStoreType);
            } else {
                trustStoreLocal = KeyStore.getInstance(KeyStore.getDefaultType());
            }
            if(StringUtils.hasText(trustStoreCert) && !"NONE".equalsIgnoreCase(trustStoreCert) ) {
                InputStream inputStream = null;
                if (trustStoreCert.startsWith("classpath:")) {
                    inputStream = this.getClass().getClassLoader().getResourceAsStream(trustStoreCert);
                } else {
                    inputStream = new FileInputStream(trustStoreCert);
                }
                trustStoreLocal.load(inputStream, trustStorePassword.toCharArray());
                trustManagerFactory.init(trustStoreLocal);
                trustManagers = trustManagerFactory.getTrustManagers();
            }
            sslContext.init(keyManager, trustManagers, null);
            mongoBuilder.applyToSslSettings( builder -> {
                builder.enabled(true);
                builder.context(sslContext);
            });
        }
        return mongoBuilder;
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, database);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory,
            MongoMappingContext context) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);

        MappingMongoConverter converter =
                new MappingMongoConverter(dbRefResolver, context);

        return converter;
    }

    @Bean
    public MongoTemplate mongoTemplate(
            MongoClient client,
            MappingMongoConverter converter) {
        return new MongoTemplate(client, database);
    }

    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

}