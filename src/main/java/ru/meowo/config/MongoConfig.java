package ru.meowo.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.internal.MongoClientImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.prepost.PreAuthorize;

@Configuration
public class MongoConfig {
    @Value("${mongodb.url1}")
    private String url1Guest;

    @Value("${mongodb.url2}")
    private String url2User;

    @Value("${mongodb.url3}")
    private String url3Admin;

    @Bean(name = "mongoClientGuest")
    public MongoClient mongoClient1() {
        return MongoClients.create(url1Guest);
    }
    @Bean(name = "mongoClientUser")
    public MongoClient mongoClient2() {
        return MongoClients.create(url2User);
    }
    @Primary
    @Bean(name = "mongoClientAdmin")
    public MongoClient mongoClient3() {
        return MongoClients.create(url3Admin);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient){
        return new MongoTemplate(mongoClient, "meowo");
    }
}
